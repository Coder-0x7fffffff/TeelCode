package org.oj.model;

import java.sql.Date;
import java.util.List;

public class CommentsModel {

	int comments_id;
	String username;
	String reply_username;
	String img;
	String detail;
	java.sql.Date time;
	List<CommentsModel> replies;
	
	public CommentsModel(int comments_id, String username, String reply_username, String img, String detail, Date time,
			List<CommentsModel> replies) {
		super();
		this.comments_id = comments_id;
		this.username = username;
		this.reply_username = reply_username;
		this.img = img;
		this.detail = detail;
		this.time = time;
		this.replies = replies;
	}

	@Override
	public String toString() {
		return "CommentsModel [comments_id=" + comments_id + ", username=" + username + ", reply_username="
				+ reply_username + ", img=" + img + ", detail=" + detail + ", time=" + time + ", replies=" + replies
				+ "]";
	}

	public int getComments_id() {
		return comments_id;
	}

	public void setComments_id(int comments_id) {
		this.comments_id = comments_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getReply_username() {
		return reply_username;
	}

	public void setReply_username(String reply_username) {
		this.reply_username = reply_username;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public java.sql.Date getTime() {
		return time;
	}

	public void setTime(java.sql.Date time) {
		this.time = time;
	}

	public List<CommentsModel> getReplies() {
		return replies;
	}

	public void setReplies(List<CommentsModel> replies) {
		this.replies = replies;
	}
	
}
