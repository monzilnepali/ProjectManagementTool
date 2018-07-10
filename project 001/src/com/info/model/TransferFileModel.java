package com.info.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TransferFileModel implements Serializable {
	private static final long serialVersionUID = 5950169519310163575L;

	private String fileName;
	private long fileSize;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
}
