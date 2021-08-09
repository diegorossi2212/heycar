package com.heycar.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CarDTO {

	@NotEmpty
	private String code;

	@NotEmpty
	private String make;

	@NotEmpty
	private String model;

	@NotNull
	private Integer kW;

	@NotEmpty
	private String color;

	@NotNull
	private BigDecimal price;

	@NotNull
	private Integer year;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getkW() {
		return kW;
	}

	public void setkW(Integer kW) {
		this.kW = kW;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
