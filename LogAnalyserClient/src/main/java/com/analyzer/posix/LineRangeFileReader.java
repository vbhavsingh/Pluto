package com.analyzer.posix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.analyzer.commons.LocalConstants;
import com.analyzer.commons.SystemStreamCapture;
import com.log.analyzer.commons.model.FileLineClientResultModel;
import com.log.analyzer.commons.model.FileLineRequestModel;

public class LineRangeFileReader {

	private final static Logger Log = LogManager.getLogger(LineRangeFileReader.class);

	public FileLineClientResultModel readLines(FileLineRequestModel request) throws IOException {
		String[] script = { "/bin/sh", "-c", request.getCommand() };
		Log.debug("executing line fetch command : " + request.getCommand());

		Process p = Runtime.getRuntime().exec(script);

		SystemStreamCapture errStream = new SystemStreamCapture(p.getErrorStream(), LocalConstants.GENERIC_TYPE);
		SystemStreamCapture outStream = new SystemStreamCapture(p.getInputStream(), LocalConstants.GENERIC_TYPE);

		errStream.start();
		outStream.start();
		
		FileLineClientResultModel model = getFileMetdata(request.getFileName());

		while (!outStream.isDone()) {

		}
		Log.debug("Read complete");
		model.setTextInRange(outStream.getData());
		return model;

	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public FileLineClientResultModel getFileMetdata(String fileName) {
		FileLineClientResultModel model = new FileLineClientResultModel();
		try {
			File file = new File(fileName);
			long size = file.length();

			model.setFileSize(size);

			Path path = Paths.get(fileName);
			BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

			model.setLastModifiedTime(attrs.lastModifiedTime().toMillis());
			model.setCreationTime(attrs.creationTime().toMillis());
		} catch (IOException e) {
			Log.error("error while reading file attributes for file: " + fileName, e);
		}
		return model;
	}

}
