package com.haoxue.haotianqi.bean;

import java.util.List;

/** 
 *	说明：API返回的数据
 *	作者： Luoyangs
 *	时间： 2015年8月17日
 */
public class ResponseBean {
	
	private int error;//错误次数
	private String status;//返回结果状态信息
	private String date;//当前时间
	private List<WeatherBean> results;//天气预报信息
	
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<WeatherBean> getResults() {
		return results;
	}
	public void setResults(List<WeatherBean> results) {
		this.results = results;
	}
}
