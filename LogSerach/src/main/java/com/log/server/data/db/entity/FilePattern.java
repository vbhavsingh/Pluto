package com.log.server.data.db.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FILE_NAME_PATTERN")
public class FilePattern implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2493658547370726304L;

	@Id
	@ManyToOne
	@JoinColumn(name = "NODE_NAME", referencedColumnName = "NODE_NAME")
	private Node node;
	
	@Id
	@Column(name = "PATTERN")
	private String Pattern;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public String getPattern() {
		return Pattern;
	}

	public void setPattern(String pattern) {
		Pattern = pattern;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Pattern, node);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilePattern other = (FilePattern) obj;
		return Objects.equals(Pattern, other.Pattern) && Objects.equals(node, other.node);
	}
	
	
}
