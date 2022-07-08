package com.log.server.data.db.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.log.server.data.db.entity.Label;
import com.log.server.data.db.entity.Node;

@Repository
public interface NodeRepository extends JpaRepository<Node, String>, Serializable{

	@Modifying
	@Query("Update Node n SET n.lastHeartbeat = :time WHERE n.nodeName = :nodeName")
	public void recordHeartBeat(@Param("nodeName") String nodeName, @Param("time") Timestamp time);

	@Query("Select node FROM Node node WHERE node.labels = :labelList")
	public List<Node> getNodesByLabelNames (@Param("labelList") List<Label> labelList);
}
