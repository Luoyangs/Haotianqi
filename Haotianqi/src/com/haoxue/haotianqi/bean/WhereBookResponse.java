package com.haoxue.haotianqi.bean;

/**
 * 说明: 
 * 作者:Luoyangs 
 * 时间:2015-9-19
 */
public class WhereBookResponse {

	private boolean ret;
	private int errcode;
	private String errmsg;
	private int ver;
	private WhereBookData data;

	public boolean isRet() {
		return ret;
	}

	public void setRet(boolean ret) {
		this.ret = ret;
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public int getVer() {
		return ver;
	}

	public void setVer(int ver) {
		this.ver = ver;
	}

	public WhereBookData getData() {
		return data;
	}

	public void setData(WhereBookData data) {
		this.data = data;
	}

}