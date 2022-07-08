package com.log.server.data.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.log.server.data.db.entity.pk.LabelKey;

@Entity
@Table(name = "LABELS")
public class Label implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8753555827747249625L;
	
	@EmbeddedId
	private LabelKey labelKey = new LabelKey();
	
	@ManyToOne
	@MapsId("nodeName")
	@JoinColumn(name = "NODE_NAME" )
	private  Node node;
	
	
	@Column(name = "LABEL_NAME", insertable = false, updatable = false)
	private String labelName;
	
	@Column(name = "NODE_NAME", insertable = false, updatable = false)
	private String nodeName;
	

	public Label() {
		super();
	}
	
	public Label(Node node, String labelName) {
		super();
		labelKey = new LabelKey();
		this.node = node;
		this.labelKey = new LabelKey(node.getNodeName(), labelName);
		this.labelName = labelName;
		this.nodeName = node.getNodeName();
	}


	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public LabelKey getLabelKey() {
		return labelKey;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	
}
