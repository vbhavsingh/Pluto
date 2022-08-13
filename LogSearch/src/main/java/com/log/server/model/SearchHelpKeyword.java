package com.log.server.model;

import java.sql.Date;

public class SearchHelpKeyword {

	private String field;

	private String keyWord;

	private String user;

	private int frequency;

	private Date createDate;

	public SearchHelpKeyword() {
	}

	public SearchHelpKeyword(String field, String keyWord, String user) {
		super();
		this.field = field;
		this.keyWord = keyWord;
		this.user = user;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
