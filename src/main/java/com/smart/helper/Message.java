package com.smart.helper;

public class Message {
	private String content;
	private String type;
	
	//Generate getter setter method
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	//generate field constructor 
	
	public Message(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}
	
	//generate tostring method
	
	@Override
	public String toString() {
		return "Message [content=" + content + ", type=" + type + "]";
	}
	
	
	
	
	

}
