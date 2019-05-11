package me.lipol.um;

import java.io.IOException;

import me.lipol.um.entity.DataSet;

public class App {
	
	//================================================================================
    //	SAMPLE INFORMATION:
	//	attributes: 0-9
	//	classes: 10-12
    //================================================================================
	
	public static int CLASS = 10;

	public static void main(String[] args) throws IOException {
		
		String fileName = "flare.data2";
		Data data = new Data(fileName);
		DataSet dataSet = data.getDataSet();
		System.out.println("** DATASET \"" + fileName + "\" INFORMATION **");
		System.out.println("Dataset size: " + dataSet.getSize());
		System.out.println("Dataset attributes: " + dataSet.getCol());
		System.out.println("Class attribute number: " + CLASS);

		
		Tree tree = new Tree(dataSet);
		tree.build();
	}

}
