package com.log.server.data.db.entity.pk;

import java.io.Serializable;

public class SearchAssistantKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7423570269487622944L;
	
	private String field;
	
	private String keyText;

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
	
	

}
