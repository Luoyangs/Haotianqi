package com.haoxue.haotianqi.bean;

/**
 * 说明：列表显示的子项 作者： Luoyangs 时间： 2015年8月17日
 */
public class CityWetherBean {
	private String city;// 城市
	private String weatherimage;// 天气图片
	private String temp;// 温度
	private String weather;// 天气
	private String dec;// 描述
	private String pinyi;// 拼音

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getWeatherimage() {
		return weatherimage;
	}

	public void setWeatherimage(String weatherimage) {
		this.weatherimage = weatherimage;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}

	public String getPinyi() {
		return pinyi;
	}

	public void setPinyi(String pinyi) {
		this.pinyi = pinyi;
	}

	public CityWetherBean() {
		super();
	}

	public CityWetherBean(String name, String pinyi) {
		super();
		this.city = name;
		this.pinyi = pinyi;
		this.weather = "点击加载";
		this.weatherimage = "";
		this.temp = "0℃";
	}
}
