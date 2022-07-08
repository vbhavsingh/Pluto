package com.log.server.data.db.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONFIG")
public class Config implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -715632159331884312L;

	@Id
	@Column(name = "CFGID")
	private String cfgId;

	@Column(name = "VAL")
	private String value;
	

	public Config() {
		super();
	}

	public Config(String cfgId, String value) {
		super();
		this.cfgId = cfgId;
		this.value = value;
	}

	public String getCfgId() {
		return cfgId;
	}

	public void setCfgId(String cfgId) {
		this.cfgId = cfgId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cfgId, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Config other = (Config) obj;
		return Objects.equals(cfgId, other.cfgId) && Objects.equals(value, other.value);
	}

}
