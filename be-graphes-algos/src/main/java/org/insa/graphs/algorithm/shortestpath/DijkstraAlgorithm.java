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
        BinaryHeap <Label> tas = new BinaryHeap <Label> ();
        Label labelOrigin=null;
        
        
        //Initialisation des labels
        for (Node oneNode : graph.getNodes()) {
        	if (!(oneNode.equals(origin))) {
        		labels.add(new Label(oneNode,false,Float.POSITIVE_INFINITY,arc));
        	}else {
        		labelOrigin = new Label(oneNode,true,0.0f,arc);
        		labels.add(labelOrigin);
        		tas.insert(labelOrigin);
        	}
        }
        System.out.println("Node init");
        
        //On place tous les labels dans un tas binaire
        //Mieux vaut le faire dans l'algortihme directement parce que peut-être que certains noeuds ne seront pas utilisé ce qui diminue juste les performances
        //for (Label oneLabel : labels) {
        //	tas.insert(oneLabel);
        //}
        //On créé un tableau dans lequel chaque noeud correspond à un label
        List <Label> nodeToLabel = new ArrayList <Label>();
        for (int k=0 ; k<labels.size() ; k++) {
        	nodeToLabel.add(null);
        }
        for (Label oneLabel : labels) {
        	nodeToLabel.set(oneLabel.sommet.getId(), oneLabel);
        }
        System.out.println("NodeToLabel");
        
        //On commence l'algorithme 
        boolean trouve = false;
        List<Arc> Suc = new ArrayList<Arc>();
        Label labelCourrant=null;
        Label labelModif;
        
        while (!(trouve) && !(tas.isEmpty())) {
        	labelCourrant=tas.findMin();
        	tas.deleteMin();
        	Suc=labelCourrant.sommet.getSuccessors();
        	System.out.println("on viens de trouver tous les successeurs du label courant");
        	//on trouve le minimum parmis les successors
        	for (Arc oneArc : Suc) {
        		if (!data.isAllowed(oneArc)) {
                    continue;
                }
        		labelModif=nodeToLabel.get(oneArc.getDestination().getId());
        		//on insère labelModif dans le tas seulement si il n'a pas été ajouté avant
        		if (labelModif.getCost()==Float.POSITIVE_INFINITY) {
        			tas.insert(labelModif);
        		}
        		
        		System.out.println("on a inséré dans le tas le label qu'on modifie");
        		//Si aucun arc n'est autorisé alors on sort de la boucle 
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
           
        	
        	
        		
        }
        System.out.println("Sortie algo");
        if (trouve) {
        	
        	List <Arc> sol = new ArrayList <Arc>();
        	sol.add(labelCourrant.pere);
        	Node fatherNode = labelCourrant.pere.getOrigin();
        	while (!(fatherNode.equals(origin))) {
        		labelCourrant=nodeToLabel.get(fatherNode.getId());
        		sol.add(labelCourrant.pere);
        		fatherNode=labelCourrant.pere.getOrigin();
        	}
        	//On reverse notre tableau de node
        	Collections.reverse(sol);
        	Path pathSolution = new Path(graph, sol);
        	System.out.println("Resultat mis en place");
        	
        	solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, pathSolution);
        	
        }else {
        	System.out.println("resultat pas trouvé");
        	solution = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        	
        	
        }
        
        
        
        
        
        return solution;
    }

}
