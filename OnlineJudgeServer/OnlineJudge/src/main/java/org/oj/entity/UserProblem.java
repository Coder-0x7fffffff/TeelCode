package org.oj.entity;

public class UserProblem {

	private String uid;
	private int pid;
	private int pstate;
	
	public UserProblem(String uid, int pid, int pstate) {
		super();
		this.uid = uid;
		this.pid = pid;
		this.pstate = pstate;
	}
	
	@Override
	public String toString() {
		return "UserProblem [uid=" + uid + ", pid=" + pid + ", pstate=" + pstate + "]";
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

	public int getPstate() {
		return pstate;
	}

	public void setPstate(int pstate) {
		this.pstate = pstate;
	}
	
}
