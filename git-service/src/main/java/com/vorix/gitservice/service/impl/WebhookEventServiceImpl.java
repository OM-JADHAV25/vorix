package com.vorix.gitservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import com.vorix.gitservice.domain.enums.WebhookStatus;
import com.vorix.gitservice.domain.model.WebhookEvent;
import com.vorix.gitservice.domain.repository.WebhookEventRepository;
import com.vorix.gitservice.service.WebhookEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WebhookEventServiceImpl implements WebhookEventService {

    private final WebhookEventRepository webhookEventRepository;

    @Override
    public void saveWebhookEvent(String deliveryId, String eventType, String payload) {

        if (deliveryId == null || deliveryId.isBlank()) {
            throw new IllegalArgumentException("GitHub delivery ID is required");
        }

        if (webhookEventRepository.existsByDeliveryId(deliveryId)) {

            log.warn("Duplicate webhook ignored. DeliveryId={}", deliveryId);

            return;
        }

        log.info("Persisting webhook event. DeliveryId={}, EventType={}", deliveryId, eventType);

        WebhookEvent event = WebhookEvent.builder()
                                         .deliveryId(deliveryId)
                                         .eventType(eventType)
                                         .payload(payload)
                                         .status(WebhookStatus.PENDING)
                                         .receivedAt(LocalDateTime.now())
                                         .build();

        webhookEventRepository.save(event);
    }
}
