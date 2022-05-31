package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.Graph;

public class LabelStar extends Label {
	
	public Node Destination;
	public AbstractInputData.Mode mode;
	public Graph graph;
	
	public LabelStar(Node current, Node Destination, AbstractInputData.Mode mode, Graph graph) {
		super(current);
		this.Destination = Destination;
		this.mode = mode;
		this.graph = graph;
	}
	public Double get_cost_est() {
		if(this.mode == AbstractInputData.Mode.LENGTH ) {
			return this.current_node.getPoint().distanceTo(Destination.getPoint());
		}
		else {
			return (this.current_node.getPoint().distanceTo(Destination.getPoint())*3.6) /(this.graph.getGraphInformation().getMaximumSpeed());
		}
	}
	
	public double getTotalCost(){
		return this.cost + this.get_cost_est();
	}
	
	public int compareTo(LabelStar other) {
		if(this.getTotalCost() == other.getTotalCost()) {
			if(this.get_cost_est()<other.get_cost_est()) {
				return -1;
			}else {
				return 1;
			}
		}
		else if(this.getTotalCost() > other.getTotalCost()) {
			return 1;
		}
		else {
			return -1;
		}
	}
}