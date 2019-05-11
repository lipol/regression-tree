package me.lipol.um;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.lipol.um.entity.DataSet;
import me.lipol.um.entity.Info;
import me.lipol.um.entity.Node;

public class Tree {
	
	private DataSet dataSet;
	
	public Tree(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	
	public Node<Info> build() {
		
		double totalEntropy = calculateEntropy(dataSet);
		System.out.println("Total dataSet entropy: " + round(totalEntropy, 3));
		System.out.println("------------------------------------------");
		
		HashMap<Integer, HashMap<String, DataSet>> attributes = getAttributesData(dataSet);
		HashMap<Integer, Double> infGains = getGains(dataSet, attributes);

		// finds root's attribute with max gain
		Map.Entry<Integer, Double> maxEntry = null;
		for (Map.Entry<Integer, Double> entry : infGains.entrySet())
		{
		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
		    {
		        maxEntry = entry;
		    }
		}
		
		int rootAttr = maxEntry.getKey();
		Node<Info> root = new Node<>(new Info(null, rootAttr, dataSet));
		
		root = recursionBuild(root);
		
		try {
			   FileWriter fileWriter = new FileWriter("output.txt");
			   PrintWriter printWriter = new PrintWriter(fileWriter);
			   //printTree(root, pw);
			   printTree(root, printWriter);
			   printWriter.close();
		 }catch(IOException e) {
			   e.printStackTrace();
		 }
		
		return root;
	}
	
	private Node<Info> recursionBuild(Node<Info> node) {
		
		List<String> variants = getAttrVariants(node.getData().getDataSet(), node.getData().getAttribute());
						
		HashMap<Integer, HashMap<String, DataSet>> attributes = getAttributesData(node.getData().getDataSet());
		HashMap<Integer, HashMap<String, DataSet>> tempAttributes;
			
		HashMap<Integer, Double> infGains = getGains(node.getData().getDataSet(), attributes);
			
			
		for(String v: variants) {
				
			DataSet sub = attributes.get(node.getData().getAttribute()).get(v);
				
			tempAttributes = getAttributesData(sub);
			infGains = getGains(sub, tempAttributes);
				
			Map.Entry<Integer, Double> maxEntry = null;
			if(infGains != null) {
				for (Map.Entry<Integer, Double> entry : infGains.entrySet())
				{
					if(!isRepeat(entry.getKey(),node)) {
						if ((maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) >= 0))
						{
							maxEntry = entry;
						}
					} 
				}
			}
			
			if(sub.isSameAttributes() || (sub.getSize() >= 1 && sub.isSameClass())) {
				
				Info info = new Info (v, maxEntry.getKey(), sub);
				List<Integer> intList = new ArrayList<>();
				
				for(String[] s : sub.getSamples()) intList.add(Integer.valueOf(s[App.CLASS]));
				
				int value = mostCommon(intList);
				info.setValue(value);
				node.addChild(new Node<Info>(info));
				break;
				
				
			}else if ((sub.getSize() >=1 && !sub.isSameClass() && maxEntry != null) ) {

				Info info = new Info (v, maxEntry.getKey(), sub);
				List<Integer> intList = new ArrayList<>();
				
				for(String[] s : sub.getSamples()) intList.add(Integer.valueOf(s[App.CLASS]));
				
				int value = mostCommon(intList);
				info.setValue(value);
				recursionBuild(node.addChild(new Node<Info>(info)));
			}
				
		}
	return node.getRoot();
}

	// checks whether the attribute has already occurred with parents
	private boolean isRepeat(int key, Node<Info> node) {
		while(node != null) {
			if(key == node.getData().getAttribute()) return true;
			node = node.getParent();
		}
		return false;
	}

	private static void printTree(Node<Info> node, PrintWriter printWriter) {
		
		if(node.getLevel()==1) printWriter.println("|----------------------------------");
		String a = new String();
		if(node.getLevel()>1) {
			a = String.join("", Collections.nCopies(node.getLevel()-1,"|  "));
		}
		
		if(node.getParent() != null) {
			if(node.getData().getValue() != -1 && node.getChildren().isEmpty()) {
				printWriter.println(a + "[" + node.getParent().getData().getAttribute() + "]=" + node.getData().getBranch() + " ---> VAL=" + (int)node.getData().getValue());
			}else {
				printWriter.println(a + "[" + node.getParent().getData().getAttribute() + "]=" + node.getData().getBranch());
			}
		}

		node.getChildren().forEach(each ->  printTree(each, printWriter));
   
	}

	private double calculateEntropy(DataSet ds) {
		
		double entropy = 0;
		
		List<String> classes = getAttrVariants(ds, App.CLASS);
		
		int all = ds.getSize();
		HashMap<String, List<String[]>> subDataSets = new HashMap<>();
		
		List<String[]> list = new ArrayList<>();
		for (String c: classes) {
			list.clear();
			for(int i=0; i<all; i++) {
				if(ds.getSamples().get(i)[App.CLASS].equals(c)) {
					list.add(ds.getSamples().get(i)); 
				}
			}
			subDataSets.put(c, list);
			
			double temp = (list.size()/(double)all);
			entropy += temp*Math.log(temp)/Math.log(2);
		}
		return (-entropy);
	}
	
	// get all possible attribute attribute values from dataset
	private List<String> getAttrVariants(DataSet ds, int attr) {
		
		if(ds == null) return null;

		List<String[]> samples = ds.getSamples();
		List<String> classes = new ArrayList<>();
		int i = 0;
		while(i != ds.getSize()) {

			if(classes.isEmpty()) classes.add(samples.get(i)[attr]);
			if(!classes.contains(samples.get(i)[attr])) classes.add(samples.get(i)[attr]);
			i++;
		}
		return classes;
	}
	
	// get subsets for every attribute
	private HashMap<Integer, HashMap<String, DataSet>> getAttributesData(DataSet ds){

		if(ds == null) return null;

		HashMap<Integer, HashMap<String, DataSet>> attributes = new HashMap<>();
		
		int all = ds.getSize();
		for(int a=0; a<10; a++) {
			
			List<String> variants = getAttrVariants(ds, a);
			HashMap<String, DataSet> subDataSets = new HashMap<>();
			
			for (String v: variants) {
				
				List<String[]> samples = new ArrayList<>();
				DataSet subDataSet = new DataSet();
			
				for(int i=0; i<all; i++) {
					if(ds.getSamples().get(i)[a].equals(v)) {
						samples.add(ds.getSamples().get(i));
					}
				}
				subDataSet.setSamples(samples);;
				subDataSets.put(v, subDataSet);
				
			}
			attributes.put(a, subDataSets);
		}
		
		return attributes;
	}
	
	// get conditional entropy for every attribute
	private HashMap<Integer, Double> getGains(DataSet ds, HashMap<Integer, HashMap<String, DataSet>> attributes) {
		
		if(ds == null) return null;
		
		HashMap<Integer, Double> conEntropies = new HashMap<>();
		HashMap<Integer, Double> infGains = new HashMap<>();
		
		double temp;
		double gain;
		int all = ds.getSize();
		double totalEntropy = calculateEntropy(ds);
		
		for(int a=0; a<10; a++) {
			
			temp = 0;
			gain = 0;
			
			List<String> variants = getAttrVariants(ds, a);
			HashMap<String, DataSet> subDataSets = attributes.get(a);
			
			for (String v: variants) {
				double p = subDataSets.get(v).getSize()/(double)all;
				double entropy = calculateEntropy(subDataSets.get(v));
				temp += p*entropy;
			}
			
			conEntropies.put(a, temp);
			
			gain = totalEntropy - temp;
			infGains.put(a, gain);
			
		}
		return infGains;
	}
	
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	 
	    BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static <T> T mostCommon(List<T> list) {
	    Map<T, Integer> map = new HashMap<>();

	    for (T t : list) {
	        Integer val = map.get(t);
	        map.put(t, val == null ? 1 : val + 1);
	    }

	    Entry<T, Integer> max = null;

	    for (Entry<T, Integer> e : map.entrySet()) {
	        if (max == null || e.getValue() > max.getValue())
	            max = e;
	    }

	    return max.getKey();
	}
}
