package org.wf.dp.dniprorada.model;

public class SmsTemplate {
	private String text;
	private String password;
	public String getText() {
		return text;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "{text:"+text+"}, {password:"+password+"}";
	}
}
