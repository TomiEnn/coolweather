package com.coolweather.app.model;

public class Province {
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String provinceQuName;
	private String provincePyName;

	public String getProvinceQuName() {
		return provinceQuName;
	}

	public void setProvinceQuName(String provinceQuName) {
		this.provinceQuName = provinceQuName;
	}

	public String getProvincePyName() {
		return provincePyName;
	}

	public void setProvincePyName(String provincePyName) {
		this.provincePyName = provincePyName;
	}

}
