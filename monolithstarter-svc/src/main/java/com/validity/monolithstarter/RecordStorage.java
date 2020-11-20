package com.validity.monolithstarter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import com.google.gson.Gson;
import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.core.io.ClassPathResource;

public class RecordStorage {
    private static HashSet<Record> records;
    private static HashSet<Record> duplicates;

    public static RecordStorage recStore = new RecordStorage();

    public RecordStorage(HashSet<Record> records, HashSet<Record> duplicates) {
        this.records = records;
        this.duplicates = duplicates;
    }

    //default constructor
    public RecordStorage()
    {

    }

    public String recordJson()
    {
        Gson gson = new Gson();
        String recordJson = gson.toJson(records);
        return recordJson;
    }

    public String duplicateJson()
    {
        Gson gson = new Gson();
        String duplicateJson = gson.toJson(duplicates);
        return duplicateJson;
    }

    public HashSet<Record> getRecords() {
        return records;
    }

    public void setRecords(HashSet<Record> records) {
        this.records = records;
    }

    public HashSet<Record> getDuplicates() {
        return duplicates;
    }

    public void setDuplicates(HashSet<Record> duplicates) {
        this.duplicates = duplicates;
    }


}
