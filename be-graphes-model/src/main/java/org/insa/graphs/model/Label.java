package org.insa.graphs.model;

public class Label{
	Node current_node;
	boolean mark = false;
	double cost = Double.MAX_VALUE;
	Arc father = null;
	
	public Label(Node current) {
		this.current_node = current;
	}
	
	public double getCost() {
		return this.cost;
	}
}
