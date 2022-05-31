package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Graph;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    public LabelStar[] data(int nbNodes, Graph graph, ShortestPathData data) {
        LabelStar[] Label = new LabelStar[nbNodes];
        for(int i = 0;i<Label.length;i++) {
        	Label[i] = new LabelStar(graph.get(i),data.getDestination(),data.getMode(), graph);
        }
        return Label;
    }
   

}
