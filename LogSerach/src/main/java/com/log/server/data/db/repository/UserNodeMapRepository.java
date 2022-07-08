package com.log.server.data.db.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.log.server.data.db.entity.Node;
import com.log.server.data.db.entity.UserNodeMap;
import com.log.server.data.db.entity.pk.NodeMappingPK;

@Repository
public interface UserNodeMapRepository extends JpaRepository<UserNodeMap, NodeMappingPK>, Serializable{
	
	@Query("SELECT map.user.userName FROM UserNodeMap map WHERE map.node.nodeName = :nodeName")
	public List<String> getAllUsersOnNode(@Param("nodeName") String nodeName);
	
	@Query("SELECT DISTINCT U.userName FROM UserCredential U LEFT JOIN UserNodeMap N " + "ON U.userName = N.user.userName "
		+ "WHERE U.userName NOT IN (SELECT DISTINCT map.user.userName FROM UserNodeMap map WHERE NODE_NAME= :nodeName)")
	public List<String> getNonMappedUsersForNode(@Param("nodeName") String nodeName);
	
	@Query("SELECT map.node FROM UserNodeMap map WHERE map.user.userName IN :userNameList")
	public List<Node> getMappedNodesForUserList(@Param("userNameList") List<String> userNameList);
	

}
