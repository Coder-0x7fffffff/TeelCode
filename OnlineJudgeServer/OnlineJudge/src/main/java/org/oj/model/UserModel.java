package org.oj.model;

public class UserModel {

	private String uid;
	private String token;
	private String name;
	private int sex;
	private String dscp;
	private String question;
	private String answer;
	private String img;
	private int isAdmin;
	
	public UserModel(String uid, String token, String name, int sex, String dscp, String question, String answer,
			String img, int isAdmin) {
		super();
		this.uid = uid;
		this.token = token;
		this.name = name;
		this.sex = sex;
		this.dscp = dscp;
		this.question = question;
		this.answer = answer;
		this.img = img;
		this.isAdmin = isAdmin;
	}
	
	@Override
	public String toString() {
		return "UserModel [uid=" + uid + ", token=" + token + ", name=" + name + ", sex=" + sex + ", dscp=" + dscp
				+ ", question=" + question + ", answer=" + answer + ", img=" + img + ", isAdmin=" + isAdmin + "]";
	}
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getSex() {
		return sex;
	}
	
	public void setSex(int sex) {
		this.sex = sex;
	}
	
	public String getDscp() {
		return dscp;
	}
	
	public void setDscp(String dscp) {
		this.dscp = dscp;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String getImg() {
		return img;
	}
	
	public void setImg(String img) {
		this.img = img;
	}

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}
	
}
