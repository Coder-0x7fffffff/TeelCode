package org.oj.entity;

public class Login {

	private String uid;
	private String upwd; // length=16
	private String uproblem;
	private String uanswer;
	
	public Login(String uid, String upwd, String uproblem, String uanswer) {
		super();
		this.uid = uid;
		this.upwd = upwd;
		this.uproblem = uproblem;
		this.uanswer = uanswer;
	}

	@Override
	public String toString() {
		return "Login [uid=" + uid + ", upwd=" + upwd + ", uproblem=" + uproblem + ", uanswer=" + uanswer + "]";
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUpwd() {
		return upwd;
	}

	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}

	public String getUproblem() {
		return uproblem;
	}

	public void setUproblem(String uproblem) {
		this.uproblem = uproblem;
	}

	public String getUanswer() {
		return uanswer;
	}

	public void setUanswer(String uanswer) {
		this.uanswer = uanswer;
	}
	
}
