package com.dynamia.modules.entityfile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.dynamia.modules.entityfile.domain.EntityFile;
import com.dynamia.tools.io.FileInfo;

public class UploadedFileInfo {

	private String fullName;
	private String contentType;
	private InputStream inputStream;
	private EntityFile parent;

	public UploadedFileInfo() {
		// TODO Auto-generated constructor stub
	}

	public UploadedFileInfo(FileInfo info) throws FileNotFoundException {
		this.fullName = info.getName();
		this.inputStream = new FileInputStream(info.getFile());
	}

	public UploadedFileInfo(String fullName, InputStream inputStream) {
		super();
		this.fullName = fullName;
		this.inputStream = inputStream;
	}

	public UploadedFileInfo(String fullName, String contentType, InputStream inputStream) {
		super();
		this.fullName = fullName;
		this.contentType = contentType;
		this.inputStream = inputStream;
	}

	public EntityFile getParent() {
		return parent;
	}

	public void setParent(EntityFile parent) {
		this.parent = parent;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
