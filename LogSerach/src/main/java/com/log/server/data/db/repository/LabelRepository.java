package com.log.server.data.db.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.log.server.data.db.entity.Label;
import com.log.server.data.db.entity.pk.LabelKey;

@Repository
public interface LabelRepository extends JpaRepository<Label, LabelKey>, Serializable{
	
	@Query("SELECT l FROM Label l WHERE l.nodeName = :nodeName")
	public List<Label> getLabelsByNode(@Param("nodeName") String nodeName);

}
