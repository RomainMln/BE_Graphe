package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;

public class Label implements Comparable<Label>{
	public Node current_node;

	public boolean mark = false;
	public Double cost = 1.0/0.0;
	public Arc father = null;
	
	public Label(Node current) {
		this.current_node = current;
	}
	
	public double getTotalCost() {
		return this.cost;
	}

	
	public int compareTo(Label other) {
		if(this.getTotalCost() == other.getTotalCost()) {
			return(0);
		}
		else if(this.getTotalCost() >= other.getTotalCost()) {
			return 1;
		}
		else {
			return -1;
		}
	}
}