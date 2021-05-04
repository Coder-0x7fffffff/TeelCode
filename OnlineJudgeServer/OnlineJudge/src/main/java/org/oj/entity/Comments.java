package org.oj.entity;

public class Comments {
	private int cid;
	private String uid;
	private int pid;
	private int cfa;
	private String cfauid;
	private String cdetails;
	private java.sql.Timestamp ctime;
	
	public Comments(int cid, String uid, int pid, int cfa, String cfauid, String cdetails, java.sql.Timestamp ctime) {
		super();
		this.cid = cid;
		this.uid = uid;
		this.pid = pid;
		this.cfa = cfa;
		this.cfauid = cfauid;
		this.cdetails = cdetails;
		this.ctime = ctime;
	}

	@Override
	public String toString() {
		return "Comments [cid=" + cid + ", uid=" + uid + ", pid=" + pid + ", cfa=" + cfa + ", cfauid=" + cfauid
				+ ", cdetails=" + cdetails + ", ctime=" + ctime + "]";
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

	public String getCfauid() {
		return cfauid;
	}

	public void setCfauid(String cfauid) {
		this.cfauid = cfauid;
	}

	public String getCdetails() {
		return cdetails;
	}

	public void setCdetails(String cdetails) {
		this.cdetails = cdetails;
	}

	public java.sql.Timestamp getCtime() {
		return ctime;
	}

	public void setCtime(java.sql.Timestamp ctime) {
		this.ctime = ctime;
	}
	
}
