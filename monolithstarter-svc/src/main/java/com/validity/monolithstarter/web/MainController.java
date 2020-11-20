/**
 * Copyright (c) 2019 Validity Inc.
 * All rights reserved.
 */

package com.validity.monolithstarter.web;

import com.validity.monolithstarter.MonolithStarterApp;
import com.validity.monolithstarter.RecordStorage;
import io.github.jhipster.config.JHipsterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.validity.monolithstarter.service.MainService;

import java.util.Arrays;
import java.util.Collection;

@Controller
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private MainService mainService;


    @GetMapping("/")
    public String main() {
        Collection<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)) {
            return "main_dev";
        }
        else {
            return "main";
        }
    }

    @GetMapping("/records")
    public String records()
    {
        return mainService.getRecords();
    }

    @GetMapping("/duplicates")
    public String duplicates()
    {
        return mainService.getDuplicates();
    }
}
