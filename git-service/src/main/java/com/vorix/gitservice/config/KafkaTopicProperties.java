package com.vorix.gitservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "vorix.kafka.topics")
public class KafkaTopicProperties {

    /**
     * Topic used to publish AI analysis requests.
     */
    private String analysisRequest;
}