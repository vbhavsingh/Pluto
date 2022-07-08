package com.log.server.data.db.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.log.server.data.db.entity.UserCredential;

@Repository
public interface UserRepository extends JpaRepository<UserCredential, String>, Serializable{

}
