package me.lipol.um.entity;

public class Info {

	private String branch;
	private int attribute;
	private DataSet dataSet;
	private int value = -1;
	
	public Info() {}

	public Info(String branch, int attribute, DataSet dataSet) {
		this.branch = branch;
		this.attribute = attribute;
		this.dataSet = dataSet;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public int getAttribute() {
		return attribute;
	}

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	public DataSet getDataSet() {
		return dataSet;
	}

	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
