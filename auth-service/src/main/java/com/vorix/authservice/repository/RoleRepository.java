package com.vorix.authservice.repository;

import com.vorix.authservice.entity.Role;
import com.vorix.authservice.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(RoleName name);
}