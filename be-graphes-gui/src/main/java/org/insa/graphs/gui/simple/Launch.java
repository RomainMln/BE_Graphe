package org.insa.graphs.gui.simple;

import java.util.List;
import java.util.Random;
import org.insa.graphs.model.io.BinaryPathReader;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.insa.graphs.algorithm.shortestpath.*;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

public class Launch {
	
	static int nb_test_valide = 0;
	static int nb_test = 500;
	static int nb_test_non_connexe = 0;
    public static Path GetShortestPath(Graph graph, Node origin, Node destination, String algo, int mode) {
    	List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
    	ArcInspector arcInspector = filters.get(mode);
    	ShortestPathData data = new ShortestPathData(graph, origin, destination, arcInspector);
    	ShortestPathSolution solution;
    	Path chemin = new Path(graph);
    	switch(algo){
    	case "DK":
    		DijkstraAlgorithm Algorithm = new DijkstraAlgorithm(data);
    		solution = Algorithm.doRun();
    		chemin = solution.getPath();
    		break;
    	case "AS":
    		AStarAlgorithm Algorithme = new AStarAlgorithm(data);
    		solution = Algorithme.doRun();
    		chemin = solution.getPath();
    		break;
    	case "BF":
    		BellmanFordAlgorithm Alg = new BellmanFordAlgorithm(data);
    		solution = Alg.doRun();
    		chemin = solution.getPath();
    		break;
    	default:
    		System.out.println("Erreur, algorithme non valide");
    		break;  		
    	}
    	return chemin;
    }
    /**
     * Create a new Drawing inside a JFrame an return it.
     * 
     * @return The created drawing.
     * 
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }
    

    public static void test(Path Dijkstra, Path Bellman, Path Astar, boolean isTime) {
    	if((Dijkstra== null) || (Bellman == null) || (Astar == null)) {
    		if((Dijkstra== null) && (Bellman == null) && (Astar == null)) {
    			nb_test_valide++;
    			nb_test_non_connexe++;
    			System.out.println("Test valide");
    		}
    		else {
    			System.out.println("Test invalide");
    		}
    	}
    	else if(isTime) {
        	Double TD = Dijkstra.getMinimumTravelTime();
        	Double TB = Bellman.getMinimumTravelTime();
        	Double TA = Astar.getMinimumTravelTime();
        	if((TD.compareTo(TA)==0) && (TD.compareTo(TB)==0)) {
        		System.out.println("Test valide");
        		nb_test_valide++;
        	}
        	else {
        		System.out.println("Test invalide");
        	}
        }
        else {
	        Float LD = Dijkstra.getLength();
	        Float LB = Bellman.getLength();
	        Float LA = Astar.getLength();
	        if((LD.compareTo(LA)==0) && (LD.compareTo(LB)== 0)) {
	        	System.out.println("Test valide");
	        	nb_test_valide++;
	        }
	        else {
        		System.out.println("Test invalide");
        	}
        }
    }
    
    public static void main(String[] args) throws Exception {
        // Visit these directory to see the list of available files on Commetud.
        final String mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/haute-garonne.mapgr";
        //final String pathName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";

        // Create a graph reader.
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        // TODO: Read the graph.
        final Graph graph = reader.read();
        
        int taille = graph.size();
        Random r = new Random();
        int nb1 = r.nextInt(taille);
        int nb2 = r.nextInt(taille);
        for(int i = 0; i<nb_test;i++) {
        	
        	nb1 = r.nextInt(taille);
        	nb2 = r.nextInt(taille);
            System.out.println("nb1 = " + nb1 + " nb2 = " + nb2);
            
            Path dijkstra = GetShortestPath(graph,graph.get(nb1), graph.get(nb2),"DK", 2);
            Path bellmanford = GetShortestPath(graph,graph.get(nb1), graph.get(nb2),"BF", 2);
            Path Astar = GetShortestPath(graph,graph.get(nb1), graph.get(nb2),"AS",2);
            
            
            test(dijkstra,bellmanford,Astar,false);
        }

        System.out.println("Nombre de test valide : " + nb_test_valide + " (sur "+ nb_test + " test.)");
        System.out.println("Le nombre de test n'ayant pas de solution étant : " + nb_test_non_connexe);
        // Create the drawing:
        //final Drawing drawing = createDrawing();

        // TODO: Draw the graph on the drawing.
        //drawing.drawGraph(graph);
        
        // TODO: Create a PathReader.
        //final PathReader pathReader = new BinaryPathReader(new DataInputStream(new FileInputStream(pathName)));

        // TODO: Read the path.
        //final Path path = pathReader.readPath(graph);
        
        
        
        //TODO: Draw the path.
        //drawing.drawPath(path);
    }

}
