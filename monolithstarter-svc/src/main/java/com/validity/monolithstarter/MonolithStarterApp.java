package com.validity.monolithstarter;

import com.validity.monolithstarter.config.DefaultProfileUtil;
import io.github.jhipster.config.JHipsterConstants;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.env.Environment;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.HashSet;
import java.io.FileReader;
import org.apache.commons.codec.language.Metaphone;
import org.springframework.core.io.ClassPathResource;
import org.apache.commons.text.similarity.LevenshteinDistance;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MonolithStarterApp implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(MonolithStarterApp.class);

    private final Environment env;

    public MonolithStarterApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes MonolithStarter.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MonolithStarterApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);

        RecordStorage recStor = new RecordStorage();
        fileMaker(recStor);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}{}\n\t" +
                "External: \t{}://{}:{}{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles());
    }

    public static void fileMaker(RecordStorage recStor)
    {
        ClassPathResource resource = new ClassPathResource("normal.csv");
        BufferedReader buffReader = null;
        //cant read file in a jar file, so had to use an input streamer

        //Create variable to store one line as well as a HashSet to store all the lines in the file
        String oneLine = "";
        HashSet<Record> records = new HashSet<>();
        HashSet<Record> duplicates = new HashSet<>();

        //read in every line from the file
        try{
            InputStreamReader reader = new InputStreamReader(resource.getInputStream());
            buffReader = new BufferedReader(reader);

            //skip the first line
            buffReader.readLine();

            while ((oneLine = buffReader.readLine()) != null)
            {
                //split the line
                String[] tokens = oneLine.split(",");
                Record record = null;
                boolean addToSet = false;
                if(tokens.length >0)
                {
                    //Create new records with that line
                    //exception handling for if there is a blank int
                    try{
                        Integer.parseInt(tokens[7]);
                    }catch(NumberFormatException ex){
                        tokens[7] = "0";
                    }

                    record = new Record(Integer.parseInt(tokens[0]), tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], Integer.parseInt(tokens[7]), tokens[8], tokens[9], tokens[10], tokens[11]);

                    System.out.println(record.getEmail());

                    //perform checks to see if that record is too similar to those in the set
                    if(!checkDuplicates(records, record))
                    {
                        addToSet = true;
                    }

                    //perform checks to see if records are too similar by checking Lev dist
                    if(!levCheck(records,record))
                        addToSet = true;

                }
                if(addToSet == true) //add does not add duplicates
                {
                    records.add(record);
                    System.out.println(oneLine);
                }
                else //add the duplicate to the duplicate HashSet
                    duplicates.add(record);
            }
            System.out.println(records.size()); //TODO remove this
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(buffReader != null) {
                try {
                    buffReader.close(); //close the reader after you are done using it
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        recStor.setRecords(records);
        recStor.setDuplicates(duplicates);
    }

    /**
     *
     * @param values
     * @param record
     * @return true if duplicate is found, false if no duplicate
     */
    public static boolean checkDuplicates(HashSet<Record> values, Record record)
    {
        //check to see if the record is already in the set
        if(values.contains(record))
        {
            return true;
        }

        //Check for sound
        Metaphone metaphone = new Metaphone();
        for(Record s : values)
        {
            //check if first names sound the same & and have same last name
            if(metaphone.isMetaphoneEqual(s.getFirstName().trim(), record.getFirstName().trim()) && s.getLastName().trim().equals(record.getLastName().trim()))
            {
                return true; //Same first name is used with different spelling (but same sound)
            }
            //check if last names sound the same but have same first name
            else if(metaphone.isMetaphoneEqual(s.getLastName().trim(),record.getLastName().trim()) && s.getFirstName().trim().equals(record.getFirstName().trim()))
                return true;
        }

        //TODO: in future make one looping check rather than multiple if statements in many loops

        //check for duplicate email
        for(Record s : values)
        {
            if(s.getEmail().trim().equals(record.getEmail().trim()))
            {
              return true;
            }
        }

        //check for duplicate address
        for(Record s : values)
        {
            if(s.getAddress1().trim().equals(record.getAddress1().trim()))
            {
                return true;
            }
        }

        //check for duplicate phone
        for(Record s : values)
        {
            if(s.getPhone().trim().equals(record.getPhone().trim()))
            {
                return true;
            }
        }

        //check Levenshtein distance


        //passes all duplicate checks
        return false;
    }

    /**
     *
     * @return returns true if two words are too close
     */
    public static boolean levCheck(HashSet<Record> values, Record record)
    {
        //if the two values are only distant by two, then they must be duplicates
        //this value can be changed to be more or less strict on similarity
        int distanceThreshold = 2;

        LevenshteinDistance lev = new LevenshteinDistance();

        //calculate distance between the records for first name
        for(Record s : values)
        {
            int dist = lev.apply(s.getFirstName(), record.getFirstName());
            //distance between first names too close
            if(dist <= distanceThreshold)
            {
                return true;
            }

            dist = lev.apply(s.getLastName(), record.getLastName());
            //distance between last names too close
            if(dist <= distanceThreshold)
            {
                return true;
            }

            dist = lev.apply(s.getAddress1(), record.getAddress1());
            //distance between addresses too close
            if(dist <= distanceThreshold)
            {
                return true;
            }

            dist = lev.apply(s.getEmail(), record.getEmail());
            //distance between email too close
            if(dist <= distanceThreshold)
            {
                return true;
            }
        }
        return false;
    }
}
