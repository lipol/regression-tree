package me.lipol.um;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.lipol.um.entity.DataSet;

public class Data {
	
	private DataSet dataSet = new DataSet();
	
	public Data(String fileName) throws IOException {
		read(fileName);
	}
	
	private void read(String fileName) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader("resources/" + fileName));
		String line;
	    List<String[]> samples = new ArrayList<>();
	    String[] temp = null;

	    while((line = br.readLine()) != null)
	    {
	        temp = line.split(" ");
	        samples.add(temp);

	    }
	    dataSet.setCol(temp.length);
	    dataSet.setSamples(samples);
	    br.close();
	}
	
	public DataSet getDataSet() {
		return dataSet;
	}

}
