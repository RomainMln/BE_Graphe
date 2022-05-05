package org.insa.graphs.algorithm.shortestpath;

public class LabelStar extends Label {
	public LabelStar(int current) {
		super(current);
	}
	
	public Double get_cost_est() {
		return this.cost_est;
	}
}