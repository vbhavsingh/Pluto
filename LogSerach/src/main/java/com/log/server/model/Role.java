/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author Vaibhav Singh
 */
public class Role {

    private String rolename;
    private String description;
    /*default visibility of a Role is true*/
    private boolean visible = true;
    

    public Role() {
		super();
	}

	public Role(String rolename, String tooltip) {
        this.rolename = rolename;
        this.description = tooltip;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    

}
