package com.xuan.en;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MaxCountRecord")
public class MaxCountRecord {

	@Id
	@Column(name = "inserttime")
	String inserttime;
	
    @Column(name = "count1")
	private int count1 = 0;
	
    @Column(name = "count2")
	private int count2 = 0;
	
    @Column(name = "count3")
	private int count3 = 0;
	
    @Column(name = "count4")
	private int count4 = 0;
	
    @Column(name = "count5")
	private int count5 = 0;
	
    @Column(name = "count6")
	private int count6 = 0;
	
    @Column(name = "count7")
	private int count7 = 0;
	
    @Column(name = "count8")
	private int count8 = 0;
	
    @Column(name = "count9")
	private int count9 = 0;
	
    @Column(name = "count10")
	private int count10 = 0;
	
    @Column(name = "count11")
	private int count11 = 0;	
    
    @Column(name = "count12")
	private int count12 = 0;

	public String getInserttime() {
		return inserttime;
	}

	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	public int getCount1() {
		return count1;
	}

	public void setCount1(int count1) {
		this.count1 = count1;
	}

	public int getCount2() {
		return count2;
	}

	public void setCount2(int count2) {
		this.count2 = count2;
	}

	public int getCount3() {
		return count3;
	}

	public void setCount3(int count3) {
		this.count3 = count3;
	}

	public int getCount4() {
		return count4;
	}

	public void setCount4(int count4) {
		this.count4 = count4;
	}

	public int getCount5() {
		return count5;
	}

	public void setCount5(int count5) {
		this.count5 = count5;
	}

	public int getCount6() {
		return count6;
	}

	public void setCount6(int count6) {
		this.count6 = count6;
	}

	public int getCount7() {
		return count7;
	}

	public void setCount7(int count7) {
		this.count7 = count7;
	}

	public int getCount8() {
		return count8;
	}

	public void setCount8(int count8) {
		this.count8 = count8;
	}

	public int getCount9() {
		return count9;
	}

	public void setCount9(int count9) {
		this.count9 = count9;
	}

	public int getCount10() {
		return count10;
	}

	public void setCount10(int count10) {
		this.count10 = count10;
	}

	public int getCount11() {
		return count11;
	}

	public void setCount11(int count11) {
		this.count11 = count11;
	}

	public int getCount12() {
		return count12;
	}

	public void setCount12(int count12) {
		this.count12 = count12;
	}
}
