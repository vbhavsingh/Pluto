package com.log.server.data.db.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.log.server.data.db.entity.UserGroup;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, String>, Serializable{
	
	@Modifying
	@Query("UPDATE UserGroup gp SET "
			+ "gp.groupName = :newGroupName, "
			+ "gp.description = :description, "
			+ "gp.modifiedBy = :modifiedBy "
			+ "WHERE gp.groupName = :oldGroupName")
	int updateGroup(@Param("newGroupName") String newGroupName,
			@Param("oldGroupName") String oldGroupName,
			@Param("description") String description,
			@Param("modifiedBy") String modifiedBy);

}
