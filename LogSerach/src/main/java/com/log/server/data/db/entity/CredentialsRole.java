package com.log.server.data.db.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CREDENTIALS_ROLE")
public class CredentialsRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1784753809971096220L;

	@Id
	@Column(name = "ROLENAME")
	private String roleName;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "VISIBLE")
	private boolean visible;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, roleName, visible);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CredentialsRole other = (CredentialsRole) obj;
		return Objects.equals(description, other.description) && Objects.equals(roleName, other.roleName)
				&& visible == other.visible;
	}

}
