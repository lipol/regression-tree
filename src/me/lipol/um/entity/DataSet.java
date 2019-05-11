package me.lipol.um.entity;

import java.util.List;

import me.lipol.um.App;

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
	
	
	public boolean isSameClass() {
		boolean allEqual = true;
		for (String[] s : this.samples) {
			if(!s[App.CLASS].equals(this.samples.get(0)[App.CLASS]))
				allEqual = false;
			}
		return allEqual;
	}
	
	public boolean isSameAttributes() {
		boolean allEqual = true;
		for(int i=0; i<10; i++) {
			for(String[] s : this.samples) {
				if(!s[i].equals(this.samples.get(0)[i])) {
					allEqual = false;
				}
			}
		}
		return allEqual;
	}
}
