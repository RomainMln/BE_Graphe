package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Point;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        boolean set_cost;
        ShortestPathSolution solution = null;
        Graph graph = data.getGraph();
        // TODO:
        BinaryHeap<Label> tas = new BinaryHeap<Label>(); 
        final int nbNodes = graph.size();
        Point Destination = data.getDestination().getPoint();
        java.util.List<Node> Nod = graph.getNodes();
        LabelStar[] Label = new LabelStar[nbNodes];
        for(int i = 0;i<Label.length;i++) {
        	Label[i] = new LabelStar(i);
        }
        Arc[] predecessorArcs = new Arc[nbNodes];
        Label[data.getOrigin().getId()].cost = 0.;
        Label[data.getOrigin().getId()].cost_est = data.getOrigin().getPoint().distanceTo(Destination);
        tas.insert(Label[data.getOrigin().getId()]);
        notifyOriginProcessed(data.getOrigin());
        boolean fini = false;
        while(!fini) { // boucle avec compteur de sommet marqués.
        	Label x = tas.deleteMin();
        	x.mark = true;
        	notifyNodeMarked(Nod.get(x.current_node));
        	
        	for(Arc successeur : Nod.get(x.current_node).getSuccessors()) {
                if (!data.isAllowed(successeur)) {
                    continue;
                }
        		int Id_Dest = successeur.getDestination().getId();
        		if(!(Label[Id_Dest].mark)) {
        			if(Label[Id_Dest].getTotalCost() > x.getTotalCost() + data.getCost(successeur)) {
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
        					Label[Id_Dest].cost_est = successeur.getDestination().getPoint().distanceTo(Destination);
            				Label[Id_Dest].father = successeur;
        					tas.insert(Label[Id_Dest]);
        				}
        				else {
        					tas.remove(Label[Id_Dest]);
        					Label[Id_Dest].cost = x.cost + data.getCost(successeur);
        					Label[Id_Dest].cost_est = successeur.getDestination().getPoint().distanceTo(Destination);
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
