package com.vorix.projectservice.controller;

import com.vorix.projectservice.dto.common.ApiResponse;
import com.vorix.projectservice.dto.request.CreateProjectRequest;
import com.vorix.projectservice.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<Map<String, String>> health() {

        return ApiResponse.success(
                "Service is running",
                Map.of(
                        "service",
                        "vorix-project-service"
                )
        );
    }

    @GetMapping("/error-test")
    public void errorTest() {

        throw new ResourceNotFoundException(
                "Project not found"
        );
    }

    @PostMapping("/validate-test")
    public ApiResponse<String> validateTest(
            @Valid
            @RequestBody
            CreateProjectRequest request
    ) {

        return ApiResponse.success(
                "Validation successful",
                request.projectName()
        );
    }
}