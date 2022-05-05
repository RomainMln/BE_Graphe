package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;

public class Label implements Comparable<Label>{
	public int current_node;
	public Double cost_est = 0.0;
	public boolean mark = false;
	public Double cost = 1.0/0.0;
	public Arc father = null;
	
	public Label(int current) {
		this.current_node = current;
	}
	
	public double getCost() {
		return this.cost;
	}
	
	public double getTotalCost() {
		return this.cost + this.cost_est;
	}
	
	public int compareTo(Label other) {
		if(this.getTotalCost() == other.getTotalCost()) {
			if(this.cost_est<other.cost_est) {
				return -1;
			}else {
				return 1;
			}
		}
		else if(this.getTotalCost() >= other.getTotalCost()) {
			return 1;
		}
		else {
			return -1;
		}
	}
}