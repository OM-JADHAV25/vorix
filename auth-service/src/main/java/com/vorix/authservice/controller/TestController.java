package com.vorix.authservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/v1/test/user")
    @PreAuthorize("hasRole('USER')")
    public String userEndpoint() {

        return "USER ACCESS GRANTED";
    }

    @GetMapping("/api/v1/test/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {

        return "ADMIN ACCESS GRANTED";
    }
}