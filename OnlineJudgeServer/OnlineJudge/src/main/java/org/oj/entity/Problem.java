package org.oj.entity;

public class Problem {

	private int pid;
	private String pname;
	private int pdifficulty;
	private int ppass;
	private int psubmit;
	private String pdscp;
	private String pinputs;
	private String poutputs;
	
	public Problem(int pid, String pname, int pdifficulty, int ppass, int psubmit, String pdscp, String pinputs,
			String poutputs) {
		this.pid = pid;
		this.pname = pname;
		this.pdifficulty = pdifficulty;
		this.ppass = ppass;
		this.psubmit = psubmit;
		this.pdscp = pdscp;
		this.pinputs = pinputs;
		this.poutputs = poutputs;
	}

	@Override
	public String toString() {
		return "Problem [pid=" + pid + ", pname=" + pname + ", pdifficulty=" + pdifficulty + ", ppass=" + ppass
				+ ", psubmit=" + psubmit + ", pdscp=" + pdscp + ", pinputs=" + pinputs + ", poutputs=" + poutputs + "]";
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

	public int getPdifficulty() {
		return pdifficulty;
	}

	public void setPdifficulty(int pdifficulty) {
		this.pdifficulty = pdifficulty;
	}

	public int getPpass() {
		return ppass;
	}

	public void setPpass(int ppass) {
		this.ppass = ppass;
	}

	public int getPsubmit() {
		return psubmit;
	}

	public void setPsubmit(int psubmit) {
		this.psubmit = psubmit;
	}

	public String getPdscp() {
		return pdscp;
	}

	public void setPdscp(String pdscp) {
		this.pdscp = pdscp;
	}

	public String getPinputs() {
		return pinputs;
	}

	public void setPinputs(String pinputs) {
		this.pinputs = pinputs;
	}

	public String getPoutputs() {
		return poutputs;
	}

	public void setPoutputs(String poutputs) {
		this.poutputs = poutputs;
	}
	
}
