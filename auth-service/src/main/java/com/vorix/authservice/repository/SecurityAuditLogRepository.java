package com.vorix.authservice.repository;

import com.vorix.authservice.entity.SecurityAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SecurityAuditLogRepository extends JpaRepository<SecurityAuditLog, UUID> {
}