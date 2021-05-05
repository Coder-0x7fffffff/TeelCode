package org.oj.model;

public class UserRecordModel {

	private String uid;
	private String uname;
	private int pid;
	private String pname;
	private int pstate;
	
	public UserRecordModel(String uid, String uname, int pid, String pname, int pstate) {
		super();
		this.uid = uid;
		this.uname = uname;
		this.pid = pid;
		this.pname = pname;
		this.pstate = pstate;
	}

	@Override
	public String toString() {
		return "UserRecordModel [uid=" + uid + ", uname=" + uname + ", pid=" + pid + ", pname=" + pname + ", pstate="
				+ pstate + "]";
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public int getPstate() {
		return pstate;
	}

	public void setPstate(int pstate) {
		this.pstate = pstate;
	}
	
}
