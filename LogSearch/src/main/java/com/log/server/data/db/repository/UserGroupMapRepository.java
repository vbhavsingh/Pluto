package com.log.server.data.db.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.log.server.data.db.entity.UserGroupMap;
import com.log.server.data.db.entity.pk.GroupMappingPk;

@Repository
public interface UserGroupMapRepository extends JpaRepository<UserGroupMap, GroupMappingPk>, Serializable{
	
	@Modifying
	@Query("UPDATE UserGroupMap gp SET "
			+ "gp.id.groupName = :newGroupName "
			+ "WHERE gp.id.groupName = :oldGroupName")
	int updateUserGroupMapping(@Param("newGroupName") String newGroupName, @Param("oldGroupName") String oldGroupName);

}
