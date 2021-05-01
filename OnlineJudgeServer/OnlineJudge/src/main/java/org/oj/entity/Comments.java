package org.oj.entity;

import java.sql.Date;

public class Comments {
	private int cid;
	private String uid;
	private int pid;
	private int cfa;
	private String cdetails;
	private java.sql.Date ctime;
	
	public Comments(int cid, String uid, int pid, int cfa, String cdetails, Date ctime) {
		super();
		this.cid = cid;
		this.uid = uid;
		this.pid = pid;
		this.cfa = cfa;
		this.cdetails = cdetails;
		this.ctime = ctime;
	}

	@Override
	public String toString() {
		return "Comments [cid=" + cid + ", uid=" + uid + ", pid=" + pid + ", cfa=" + cfa + ", cdetails=" + cdetails
				+ ", ctime=" + ctime + "]";
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getCfa() {
		return cfa;
	}

	public void setCfa(int cfa) {
		this.cfa = cfa;
	}

	public String getCdetails() {
		return cdetails;
	}

	public void setCdetails(String cdetails) {
		this.cdetails = cdetails;
	}

	public java.sql.Date getCtime() {
		return ctime;
	}

	public void setCtime(java.sql.Date ctime) {
		this.ctime = ctime;
	}

}
