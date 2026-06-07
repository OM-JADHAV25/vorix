package com.vorix.projectservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorix.projectservice.dto.request.CreateProjectRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateProjectSuccessfully()
            throws Exception {

        CreateProjectRequest request =
                new CreateProjectRequest(
                        "Integration Test",
                        "Testing",
                        "https://github.com/test/integration"
                );

        mockMvc.perform(
                        post("/api/v1/projects")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("Project created successfully"))
                .andExpect(jsonPath("$.data.projectName")
                        .value("Integration Test"))
                .andExpect(jsonPath("$.data.githubUrl")
                        .value("https://github.com/test/integration"))
                .andExpect(jsonPath("$.data.status")
                        .value("ACTIVE"));
    }
}