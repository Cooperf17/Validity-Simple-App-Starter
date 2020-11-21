package com.validity.monolithstarter.service;


import com.validity.monolithstarter.RecordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MainService {
//returns the records in json format
    public String getRecords()
    {
        return RecordStorage.recStore.recordJson();
    }
//returns duplicates in json format
    public String getDuplicates()
    {
        return RecordStorage.recStore.duplicateJson();
    }
}
