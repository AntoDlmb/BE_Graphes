package org.insa.graphs.algorithm.shortestpath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.print.attribute.standard.Destination;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Label;
import org.insa.graphs.algorithm.utils.BinaryHeap;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
    	System.out.println("Entrée dans shortestPathSolution2");
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        Node origin = data.getOrigin();
        Node destination = data.getDestination();
        Graph graph = data.getGraph();
        Arc arc=null;
        List<Label> labels = new ArrayList<Label>();
        
        
        //Initialisation des noeuds
        for (Node oneNode : graph.getNodes()) {
        	if (!(oneNode.equals(origin))) {
        		labels.add(new Label(oneNode,false,Float.POSITIVE_INFINITY,arc));
        	}else {
        		labels.add(new Label(oneNode,true,0.0f,arc));
        	}
        }
        System.out.println("Node init");
        
        //On place tous les labels dans un tas binaire
        BinaryHeap <Label> tas = new BinaryHeap <Label> ();
        for (Label oneLabel : labels) {
        	tas.insert(oneLabel);
        }
        //On créé un tableau dans lequel chaque noeud correspond à un label
        List <Label> nodeToLabel = new ArrayList <Label>();
        for (Label oneLabel : labels) {
        	nodeToLabel.set(oneLabel.sommet.getId(), oneLabel);
        }
        System.out.println("NodeToLabel");
        
        //On commence l'algorithme 
        boolean trouve = false;
        List<Arc> Suc = new ArrayList<Arc>();
        Label labelCourrant;
        Label labelModif;
        List <Label> labelMarques = new ArrayList <Label>();
        
        while (!(trouve) && !(tas.isEmpty())) {
        	labelCourrant=tas.findMin();
        	tas.deleteMin();
        	Suc=labelCourrant.sommet.getSuccessors();
        	//on trouve le minimum parmis les successors
        	for (Arc oneArc : Suc) {
        		labelModif=nodeToLabel.get(oneArc.getDestination().getId());
        		if (labelCourrant.getCost()+oneArc.getLength() < labelModif.getCost()){
        			tas.remove(labelModif);
        			labelModif.setCost(labelCourrant.getCost()+oneArc.getLength());
        			labelModif.pere=oneArc;
        			nodeToLabel.set(oneArc.getDestination().getId(), labelModif);
        			tas.insert(labelModif);
        		}
        	}
        	labelCourrant.marque=true;
        	if (labelCourrant.sommet.equals(destination)) {
        		trouve=true;
        	}
        	
        	//On ajout l'élément marqué dans notre tableau d'éléments 
        	labelMarques.set(labelCourrant.sommet.getId(),labelCourrant);
        		
        }
        System.out.println("Sortie algo");
        if (trouve) {
        	//on créé la liste des noeuds qui consitutent le chemin 
        	List <Arc> sol = new ArrayList<Arc> ();
        	sol.add(nodeToLabel.get(destination.getId()).pere);
        	Arc addedArc=nodeToLabel.get(destination.getId()).pere;
        	while (!(addedArc.getOrigin().equals(origin))) {
        		addedArc =nodeToLabel.get(addedArc.getOrigin().getId()).pere ;
        		sol.add(addedArc);
        	}
        	//On reverse notre tableau de node
        	List <Arc> solFinal = new ArrayList <Arc>();
        	for (int i = 0; i<sol.size() ; i++) {
        		solFinal.set(i, sol.get(sol.size()-1-i));
        	}
        	Path pathSolution = new Path(graph, solFinal);
        	System.out.println("Resultat mis en place");
        	
        	solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, pathSolution);
        	
        }else {
        	System.out.println("resultat pas trouvé");
        	solution = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        	
        	
        }
        
        
        
        
        
        return solution;
    }

}
