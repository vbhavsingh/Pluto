package com.log.analyzer.commons.model;

public class AgentCommands {
	
	private String fileIndexingCommand;
	
	private String archiveDateLineExtractorCommand;

	public String getFileIndexingCommand() {
		return fileIndexingCommand;
	}

	public void setFileIndexingCommand(String fileIndexingCommand) {
		this.fileIndexingCommand = fileIndexingCommand;
	}

	public String getArchiveDateLineExtractorCommand() {
		return archiveDateLineExtractorCommand;
	}

	public void setArchiveDateLineExtractorCommand(String archiveDateLineExtractorCommand) {
		this.archiveDateLineExtractorCommand = archiveDateLineExtractorCommand;
	}

	@Override
	public String toString() {
		return "AgentCommands [fileIndexingCommand=" + fileIndexingCommand + ", archiveDateLineExtractorCommand=" + archiveDateLineExtractorCommand + "]";
	}
	
	

}
