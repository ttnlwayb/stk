package com.xuan.en;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Stock")
public class Stock {

    @Id
    @Column(name = "stkcode")
	private String stkCode;
    @Column(name = "name")
	private String name;
    @Column(name = "stkgroup")
	private String stkgroup;
    @Column(name = "type")
	private String type;
	public String getStkCode() {
		return stkCode;
	}
	public void setStkCode(String stkCode) {
		this.stkCode = stkCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStkgroup() {
		return stkgroup;
	}
	public void setStkgroup(String stkgroup) {
		this.stkgroup = stkgroup;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
