package com.log.server.model.graphics;

import java.io.Serializable;

public class GraphRowElement implements Serializable{

	private static final long serialVersionUID = 6641498267995595697L;

	private String label;
	
	private int count=0;
	
	public GraphRowElement() {

	}

	public GraphRowElement(String label) {
		super();
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public void increment() {
		this.count++;
	}
	
}
