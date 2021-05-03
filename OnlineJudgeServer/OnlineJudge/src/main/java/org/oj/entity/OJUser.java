package org.oj.entity;

import java.sql.Blob;

public class OJUser {
	
	private String uid;
	private String upwd;
	private int usex;
	private String udscp;
	private Blob uimg;
	
	public OJUser(String uid, String upwd, int usex, String udscp, Blob uimg) {
		super();
		this.uid = uid;
		this.upwd = upwd;
		this.usex = usex;
		this.udscp = udscp;
		this.uimg = uimg;
	}
	
	@Override
	public String toString() {
		return "OJUser [uid=" + uid + ", upwd=" + upwd + ", usex=" + usex + ", udscp=" + udscp + ", uimg=" + uimg + "]";
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
	
	public Blob getUimg() {
		return uimg;
	}
	
	public void setUimg(Blob uimg) {
		this.uimg = uimg;
	}
	
}
