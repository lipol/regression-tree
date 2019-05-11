package me.lipol.um.entity;

import java.util.List;

public class DataSet {

	private List<String[]> samples;
	private int col;
	
	public DataSet() {}

	public List<String[]> getSamples() {
		return samples;
	}

	public void setSamples(List<String[]> samples) {
		this.samples = samples;
	}

	public int getSize() {
		return samples.size();
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
}
