package com.log.server.data.db.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CREDENTIALS_ROLE")
public class UserRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5720064068432053132L;

	@Id
	@Column(name = "ROLENAME")
	private String roleName;

	@Column(name = "DESCRIPTION")
	private String desciption;

	@Column(name = "VISIBLE")
	private Boolean visbible = true;

	public UserRole() {
		super();
	}

	public UserRole(String roleName) {
		super();
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	public Boolean getVisbible() {
		return visbible;
	}

	public void setVisbible(Boolean visbible) {
		this.visbible = visbible;
	}

	@Override
	public int hashCode() {
		return Objects.hash(desciption, roleName, visbible);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRole other = (UserRole) obj;
		return Objects.equals(desciption, other.desciption) && Objects.equals(roleName, other.roleName)
				&& Objects.equals(visbible, other.visbible);
	}

}
