package com.analyzer.posix.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.analyzer.commons.LocalConstants;

public class PosixSearchThreadKiller extends Thread {

	private final static Logger Log = LogManager.getLogger(PosixSearchThreadKiller.class);

	private static PosixSearchThreadKiller deathFactory;

	private static ArrayBlockingQueue<Process> deathQueue;

	private PosixSearchThreadKiller() {
		deathQueue = new ArrayBlockingQueue<Process>(2 * LocalConstants.MAX_FILES_TO_BE_SEARCHED);
		start();
	}

	public static BlockingQueue<Process> deathQueue() {
		if (deathFactory == null) {
			deathFactory = new PosixSearchThreadKiller();

		}
		return deathQueue;
	}

	public void run() {
		while (true) {
			try {
				Process p = deathQueue.take();
				p.destroy();
			} catch (InterruptedException e) {
				Log.error(e);
			}
		}
	}

}
