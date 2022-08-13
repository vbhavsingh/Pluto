package com.log.server.data.db.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.log.server.data.db.entity.UserRoleMap;

@Repository
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMap, String>, Serializable{
	
	@Query("SELECT map FROM UserRoleMap map WHERE map.user.userName = :userName")
	public List<UserRoleMap> getRolesOfAnUser(@Param("userName") String userName);
	
}
