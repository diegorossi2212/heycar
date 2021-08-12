package com.heycar.search;

import java.math.BigDecimal;

public class CarSearch extends BaseSearch {

	private Long idDealer;
	private String code;
	private String make;
	private String model;
	private Integer kw;
	private String color;
	private BigDecimal price;
	private Integer year;

	public Long getIdDealer() {
		return idDealer;
	}

	public void setIdDealer(Long idDealer) {
		this.idDealer = idDealer;
	}
	
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

	public Integer getKw() {
		return kw;
	}

	public void setKw(Integer kw) {
		this.kw = kw;
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
