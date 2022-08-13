package com.log.server.data.db.entity.pk;

import java.io.Serializable;
import java.util.Objects;

import com.log.server.data.db.entity.UserCredential;
import com.log.server.data.db.entity.UserRole;

public class RoleMappingPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserCredential user;
	
	private UserRole role;

	public UserCredential getUser() {
		return user;
	}

	public void setUser(UserCredential user) {
		this.user = user;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(role, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleMappingPK other = (RoleMappingPK) obj;
		return Objects.equals(role, other.role) && Objects.equals(user, other.user);
	}
	
}
