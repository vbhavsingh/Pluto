package com.log.server.data.db.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.log.server.data.db.entity.SearchAssistant;

@Repository
public interface SearchAssistantRepository extends JpaRepository<SearchAssistant, String>, Serializable{
	
	@Query("SELECT s.keyText FROM SearchAssistant s WHERE s.field = :fieldName")
	public List<String> getPreviousSearchCriterias(@Param("fieldName") String fieldName);
	
}
