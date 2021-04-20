package org.oj.entity;

public class ProblemClassification {
	
	private int pid;
	private int cid;
	
	public ProblemClassification(int pid, int cid) {
		super();
		this.pid = pid;
		this.cid = cid;
	}

	@Override
	public String toString() {
		return "ProblemClassification [pid=" + pid + ", cid=" + cid + "]";
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}
	
}
