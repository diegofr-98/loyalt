package com.loyalt.loyalt.controller;

import com.loyalt.loyalt.model.entity.UserBusiness;
import com.loyalt.loyalt.repository.UserBusinessRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class DataBaseTestController {
    private final UserBusinessRepository userBusinessRepository;

    public DataBaseTestController(UserBusinessRepository userBusinessRepository){
        this.userBusinessRepository = userBusinessRepository;

    }

    @GetMapping("jpa-test")
    public String testDBConnection(){

        long totalRows = userBusinessRepository.count();

        return "user_business table has: " + totalRows + " rows";

    }

}
