package com.validity.monolithstarter;

import java.util.HashSet;
import com.google.gson.Gson;

public class RecordStorage {
    HashSet<Record> records;
    HashSet<Record> duplicates;

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
