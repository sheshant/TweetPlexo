package org.sheshant.messenger.model;

public class MessageComment {
	private long id;
	private long messageid;
	private long commentid;
	public MessageComment(long id,long messageid,long commentid)
	{
		this.id = id;
		this.messageid = messageid;
		this.commentid = commentid;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMessageid() {
		return messageid;
	}
	public void setMessageid(long messageid) {
		this.messageid = messageid;
	}
	public long getCommentid() {
		return commentid;
	}
	public void setCommentid(long commentid) {
		this.commentid = commentid;
	}
}
