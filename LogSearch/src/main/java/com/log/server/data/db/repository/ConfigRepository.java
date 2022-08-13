package com.log.server.data.db.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.log.server.data.db.entity.Config;

public interface ConfigRepository extends JpaRepository<Config, String>, Serializable {

}
