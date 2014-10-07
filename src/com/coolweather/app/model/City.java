package com.coolweather.app.model;

public class City {

	private int id;
	private String cityName;
	private String cityPy;
	private int cityUrl;
	private String ProvincePy;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityPy() {
		return cityPy;
	}

	public void setCityPy(String cityPy) {
		this.cityPy = cityPy;
	}

	public int getCityUrl() {
		return cityUrl;
	}

	public void setCityUrl(int cityUrl) {
		this.cityUrl = cityUrl;
	}

	public String getProvincePy() {
		return ProvincePy;
	}

	public void setProvincePy(String provincePy) {
		ProvincePy = provincePy;
	}

}
