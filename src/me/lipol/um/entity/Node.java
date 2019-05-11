package me.lipol.um.entity;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {

	private T data = null;
	private List<Node<T>> children = new ArrayList<>();
	private Node<T> parent = null;
	
	public Node(T data) {
		this.data = data;
	}
	
	public Node<T> addChild(Node <T> child){
		child.setParent(this);
		this.children.add(child);
		return child;
	}
	
	public List<Node<T>> getChildren() {
		return children;
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public void setParent(Node<T> parent) {
		this.parent = parent;
	}
	
	public Node<T> getParent() {
		return parent;
	}
	
	public Node<T> getRoot() {
		if(parent == null){
			return this;
		}
		return parent.getRoot();
	}
	
	public int getLevel() {
		Node<T> temp = this;
		int level = 0;
		while(temp.getParent() != null) {
			temp = temp.getParent();
			level++;
		}
		return level;
	}
}
