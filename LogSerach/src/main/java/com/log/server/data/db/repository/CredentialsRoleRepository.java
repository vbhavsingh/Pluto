package com.log.server.data.db.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.log.server.data.db.entity.CredentialsRole;

public interface CredentialsRoleRepository extends JpaRepository<CredentialsRole, String>, Serializable {

}
