package com.log.server.data.db.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.log.server.data.db.entity.pk.SearchAssistantKey;

@Entity
@Table(name = "SEARCH_ASSISTANT")
@IdClass(SearchAssistantKey.class)
public class SearchAssistant implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2290442823260995590L;

	@Id
	@Column(name = "FIELD")
	private String field;
	
	@Id
	@Column(name = "KEYTEXT")
	private String keyText;
	
	@Version
	@Column(name = "FREQUENCY")
	private Integer frequency;
	
	@ManyToOne
	@JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME")
	private UserCredential user;
	
	@Column(name = "CREATED_TIME")
	private Timestamp createdTime;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getKeyText() {
		return keyText;
	}

	public void setKeyText(String keyText) {
		this.keyText = keyText;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public UserCredential getUser() {
		return user;
	}

	public void setUser(UserCredential user) {
		this.user = user;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdTime, field, frequency, keyText, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchAssistant other = (SearchAssistant) obj;
		return Objects.equals(createdTime, other.createdTime) && Objects.equals(field, other.field)
				&& Objects.equals(frequency, other.frequency) && Objects.equals(keyText, other.keyText)
				&& Objects.equals(user, other.user);
	}
	
	
}
