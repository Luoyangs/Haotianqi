package com.haoxue.haotianqi.bean;

/**
 * 说明: 
 * 作者:Luoyangs 
 * 时间:2015-9-19
 */
public class WhereBook {

	private String bookUrl;
	private String title;
	private String headImage;
	private String userName;
	private String userHeadImg;
	private String startTime;
	private String text;
	private int viewCount;
	private int likeCount;
	private int commentCount;

	public String getBookUrl() {
		return bookUrl;
	}

	public void setBookUrl(String bookUrl) {
		this.bookUrl = bookUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserHeadImg() {
		return userHeadImg;
	}

	public void setUserHeadImg(String userHeadImg) {
		this.userHeadImg = userHeadImg;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	@Override
	public String toString() {
		return "WhereBook [bookUrl=" + bookUrl + ", title=" + title
				+ ", headImage=" + headImage + ", userName=" + userName
				+ ", userHeadImg=" + userHeadImg + ", startTime=" + startTime
				+ ", text=" + text + ", viewCount=" + viewCount
				+ ", likeCount=" + likeCount + ", commentCount=" + commentCount
				+ "]";
	}
	
	
}
