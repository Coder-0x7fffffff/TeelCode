package org.oj.entity;

public class OJUser {
	
	private String uid;
	private String upwd;
	private int usex;
	private String udscp;
	
	public OJUser(String uid, String upwd, int usex, String udscp) {
		this.uid = uid;
		this.upwd = upwd;
		this.usex = usex;
		this.udscp = udscp;
	}

	@Override
	public String toString() {
		return "OJUser [uid=" + uid + ", upwd=" + upwd + ", usex=" + usex + ", udscp=" + udscp + "]";
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

	public int getUsex() {
		return usex;
	}

	public void setUsex(int usex) {
		this.usex = usex;
	}

	public String getUdscp() {
		return udscp;
	}

	public void setUdscp(String udscp) {
		this.udscp = udscp;
	}
	
}
