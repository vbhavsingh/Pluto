package com.log.server.data.db.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONFIG")
public class ApplicationConfiguration implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4048713578050895296L;

	@Id
	@Column(name = "CFGID")
	private String configId;

	@Column(name = "VAL")
	private String vaule;

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getVaule() {
		return vaule;
	}

	public void setVaule(String vaule) {
		this.vaule = vaule;
	}

	@Override
	public int hashCode() {
		return Objects.hash(configId, vaule);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplicationConfiguration other = (ApplicationConfiguration) obj;
		return Objects.equals(configId, other.configId) && Objects.equals(vaule, other.vaule);
	}

}
