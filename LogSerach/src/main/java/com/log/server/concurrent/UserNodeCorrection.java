/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.log.server.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.log.server.biz.CommonServices;

/**
 *
 * @author Vaibhav Singh
 */
public class UserNodeCorrection implements Runnable {

    private Logger Log = LoggerFactory.getLogger(UserNodeCorrection.class);

    private String nodeName;
    private String userName;

    public UserNodeCorrection(String userName, String nodeName) {
        this.nodeName = nodeName;
        this.userName = userName;
    }

    @Override
    public void run() {
        Log.trace("deleting user role mapping for user: {}  & node: {}", userName, nodeName);
        int r = CommonServices.deleteUserNodeMapping(userName, nodeName);
        if (r == 1) {
            Log.info("deleted user role mapping for user: {}  & node: {}", userName, nodeName);
        }
    }

}
