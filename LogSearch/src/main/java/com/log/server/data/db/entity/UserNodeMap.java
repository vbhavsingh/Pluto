package com.log.server.data.db.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.log.server.data.db.entity.pk.NodeMappingPK;

@Entity
@Table(name = "USER_NODE_MAP")
public class UserNodeMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7783575956199763044L;

	@EmbeddedId
	private NodeMappingPK nodeMappingPK;

//	@Id
//	@JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME")
//	@OneToOne
	@ManyToOne
	@MapsId("userName")
	@JoinColumn(name = "USERNAME")
	private UserCredential user;

//	@Id
//	@JoinColumn(name = "NODE_NAME", referencedColumnName = "NODE_NAME")
//	@OneToOne
	@ManyToOne
	@MapsId("nodeName")
	@JoinColumn(name = "NODE_NAME")
	private Node node;

	@JoinColumn(name = "CREATED_BY", referencedColumnName = "USERNAME")
	@OneToOne
	private UserCredential createdBy;

	@Column(name = "CREATED_TIME")
	private Timestamp createdTime;

	public UserNodeMap() {
		super();
	}

	public UserNodeMap(UserCredential user, Node node) {
		super();
		if (this.nodeMappingPK == null) {
			this.nodeMappingPK = new NodeMappingPK(user.getUserName(), node.getNodeName());
		}
		this.user = user;
		this.node = node;
	}

	public UserNodeMap(String userName, String nodeName) {
		super();
		if (this.nodeMappingPK == null) {
			this.nodeMappingPK = new NodeMappingPK(userName, nodeName);
		}
	}

	public UserCredential getUser() {
		return user;
	}

	public void setUser(UserCredential user) {
		this.user = user;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public UserCredential getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserCredential createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public NodeMappingPK getNodeMappingPK() {
		return nodeMappingPK;
	}

	public void setNodeMappingPK(NodeMappingPK nodeMappingPK) {
		this.nodeMappingPK = nodeMappingPK;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdBy, createdTime, node, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserNodeMap other = (UserNodeMap) obj;
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(createdTime, other.createdTime)
				&& Objects.equals(node, other.node) && Objects.equals(user, other.user);
	}

}
