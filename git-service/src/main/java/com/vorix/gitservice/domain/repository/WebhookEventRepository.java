package com.vorix.gitservice.domain.repository;

import com.vorix.gitservice.domain.model.WebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WebhookEventRepository extends JpaRepository<WebhookEvent, UUID> {

    boolean existsByDeliveryId(String deliveryId);
}

