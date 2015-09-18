package com.haoxue.haotianqi.bean;

import java.util.List;

/**
 * 说明:笑话 
 * 作者:Luoyangs 
 * 时间:2015-9-18
 */
public class HappyText {
	private int ret_code;
	private int maxResult;
	private int currentPage;
	private int allNum;
	private int allPages;
	private List<HappyTextItem> contentlist;

	public int getRet_code() {
		return ret_code;
	}

	public void setRet_code(int ret_code) {
		this.ret_code = ret_code;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getAllNum() {
		return allNum;
	}

	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}

	public int getAllPages() {
		return allPages;
	}

	public void setAllPages(int allPages) {
		this.allPages = allPages;
	}

	public List<HappyTextItem> getContentlist() {
		return contentlist;
	}

	public void setContentlist(List<HappyTextItem> contentlist) {
		this.contentlist = contentlist;
	}
}
