package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;

import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    public Label[] data(int nbNodes, Graph graph, ShortestPathData data) {
        Label[] Label = new Label[nbNodes];
        for(int i = 0;i<Label.length;i++) {
        	Label[i] = new Label(graph.get(i));
        }
        return Label;
    }
    @Override
    public ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        boolean set_cost;
        ShortestPathSolution solution = null;
        Graph graph = data.getGraph();
        // TODO:
        BinaryHeap<Label> tas = new BinaryHeap<Label>(); 
        final int nbNodes = graph.size();
        Label[] Label = data(nbNodes,graph,data);
        Arc[] predecessorArcs = new Arc[nbNodes];
        Label[data.getOrigin().getId()].cost = 0.;
        tas.insert(Label[data.getOrigin().getId()]);
        notifyOriginProcessed(data.getOrigin());
        boolean fini = false;
        while(!fini) { // boucle avec compteur de sommet marqués.
        	Label x = tas.deleteMin();
        	x.mark = true;
        	notifyNodeMarked(x.current_node);
        	
        	for(Arc successeur : x.current_node.getSuccessors()) {
                if (!data.isAllowed(successeur)) {
                    continue;
                }
        		int Id_Dest = successeur.getDestination().getId();
        		if(!(Label[Id_Dest].mark)) {
        			if(Label[Id_Dest].cost > x.cost + data.getCost(successeur)) {
        				if(Double.isInfinite(Label[Id_Dest].cost) && Double.isFinite(x.cost + data.getCost(successeur))) {
        					set_cost = true;
        					notifyNodeReached(successeur.getDestination());
        				}
        				else {
        					set_cost = false;
        				}
        				predecessorArcs[successeur.getDestination().getId()] = successeur;
        				if(set_cost) {
        					Label[Id_Dest].cost = x.cost + data.getCost(successeur);
            				Label[Id_Dest].father = successeur;
        					tas.insert(Label[Id_Dest]);
        				}
        				else {
        					tas.remove(Label[Id_Dest]);
        					Label[Id_Dest].cost = x.cost + data.getCost(successeur);
            				Label[Id_Dest].father = successeur;
        					tas.insert(Label[Id_Dest]);
        				}
        			}
        		}
        	}
        	
        	if(Label[data.getDestination().getId()].mark || tas.size() == 0) {
        		fini = true;
        	}
        }

        // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }

        return solution;
    }

}
