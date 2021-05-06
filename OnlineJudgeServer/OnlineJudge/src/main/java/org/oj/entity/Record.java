package org.oj.entity;

public class Record {

	private int pid;
	private String uid;
	private int pstate;
	private java.sql.Timestamp rtime;
	private int rtimewaste;
	private int rmemory;
	private String rcode;
	private int rcodetype;
	private String rinfo;
	
	public Record(int pid, String uid, int pstate, java.sql.Timestamp rtime, int rtimewaste, int rmemory, String rcode, int rcodetype,
			String rinfo) {
		super();
		this.pid = pid;
		this.uid = uid;
		this.pstate = pstate;
		this.rtime = rtime;
		this.rtimewaste = rtimewaste;
		this.rmemory = rmemory;
		this.rcode = rcode;
		this.rcodetype = rcodetype;
		this.rinfo = rinfo;
	}
	
	@Override
	public String toString() {
		return "Record [pid=" + pid + ", uid=" + uid + ", pstate=" + pstate + ", rtime=" + rtime + ", rtimewaste="
				+ rtimewaste + ", rmemory=" + rmemory + ", rcode=" + rcode + ", rcodetype=" + rcodetype + ", rinfo="
				+ rinfo + "]";
	}
	
	public int getPid() {
		return pid;
	}
	
	public void setPid(int pid) {
		this.pid = pid;
	}
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public int getPstate() {
		return pstate;
	}
	
	public void setPstate(int pstate) {
		this.pstate = pstate;
	}
	
	public java.sql.Timestamp getRtime() {
		return rtime;
	}
	
	public void setRtime(java.sql.Timestamp rtime) {
		this.rtime = rtime;
	}
	
	public int getRtimewaste() {
		return rtimewaste;
	}
	
	public void setRtimewaste(int rtimewaste) {
		this.rtimewaste = rtimewaste;
	}
	
	public int getRmemory() {
		return rmemory;
	}
	
	public void setRmemory(int rmemory) {
		this.rmemory = rmemory;
	}
	
	public String getRcode() {
		return rcode;
	}
	
	public void setRcode(String rcode) {
		this.rcode = rcode;
	}
	
	public int getRcodetype() {
		return rcodetype;
	}
	
	public void setRcodetype(int rcodetype) {
		this.rcodetype = rcodetype;
	}
	
	public String getRinfo() {
		return rinfo;
	}
	
	public void setRinfo(String rinfo) {
		this.rinfo = rinfo;
	}
	
}
