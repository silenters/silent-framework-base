package com.silent.framework.base.utils.zip;

public class ZipUncompressBean {

	private String filename;
	private String filedirPath;
	private byte[] content;
	
	void setName(String name){
		name = name.replace("\\", "/");
		int index = name.lastIndexOf("/");
		this.filename = name.substring(index + 1);
		this.filedirPath = name.substring(0, index);
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getFiledirPath() {
		return filedirPath;
	}
}
