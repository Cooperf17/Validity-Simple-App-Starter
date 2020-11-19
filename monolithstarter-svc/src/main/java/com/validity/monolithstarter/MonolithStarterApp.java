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
        fileMaker();
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

    private static void fileMaker()
    {
        ClassPathResource resource = new ClassPathResource("test-files/normal.csv");
        BufferedReader buffReader = null;

        //cant read file in a jar file, so had to use an input streamer

        //Create variable to store one line as well as a HashSet to store all the lines in the file
        String oneLine = "";
        HashSet<Record> records = new HashSet<>();

        //read in every line from the file
        try{
            InputStreamReader reader = new InputStreamReader(resource.getInputStream());
            buffReader = new BufferedReader(reader);
            while ((oneLine = buffReader.readLine()) != null)
            {
                //split the line
                String[] tokens = oneLine.split(",");
                Record record = null;
                if(tokens.length >0)
                {
                    //Create new records with that line
                    record = new Record(Integer.parseInt(tokens[0]), tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], Integer.parseInt(tokens[7]), tokens[8], tokens[9], tokens[10], tokens[11]);

                    //perform checks to see if that record is too similar to those in the set
                    checkDuplicates(records, record);

                }
                if(records.add(record)) //add does not add duplicates
                {
                    System.out.println(oneLine);
                    log.info(oneLine);
                }
            }
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
    }

    /**
     *
     * @param values
     * @param record
     * @return true if duplicate is found, false if no duplicate
     */
    private static boolean checkDuplicates(HashSet<Record> values, Record record)
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
            if(metaphone.isMetaphoneEqual(s.getFirstName(), record.getFirstName()) && s.getLastName() == record.getLastName())
            {
                return true; //Same first name is used with different spelling (but same sound)
            }
            //check if last names sound the same but have same first name
            else if(metaphone.isMetaphoneEqual(s.getLastName(),record.getLastName()) && s.getFirstName() == record.getFirstName())
                return true;
        }
        
        //passes all duplicate checks
        return false;
    }
}
