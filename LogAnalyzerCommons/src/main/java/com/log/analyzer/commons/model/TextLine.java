package com.log.analyzer.commons.model;

public class TextLine {
	
	private String text;
	
	private String lineNumber;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	@Override
	public String toString() {
		return "TextLine [text=" + text + ", lineNumber=" + lineNumber + "]";
	}

}
