package org.insa.graphs.algorithm.shortestpath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Label;
import org.insa.graphs.algorithm.utils.BinaryHeap;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	protected Graph graph;
	protected List <Label> labels ;
	protected Node origin;
	protected Label labelOrigin;
	protected Node destination;
	protected BinaryHeap <Label> tas ;

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        this.origin = data.getOrigin();
        this.destination = data.getDestination();
        this.graph = data.getGraph();
        this.labels = new ArrayList<Label>();
        this.tas = new BinaryHeap <Label> ();
        //Initialisation du label origin
        this.labelOrigin=new Label(this.origin,true,0.0,null);
        this.labels.add(labelOrigin);
        this.tas.insert(labelOrigin);
        
    }
    
    protected Label NewLabel(Node node, Graph graph, ShortestPathData mode) {
    	Label newLab = new Label(node,false,Double.POSITIVE_INFINITY,null);
    	this.labels.add(newLab);
    	return newLab;
    	
    }
    

    @Override
    protected ShortestPathSolution doRun() {
    	System.out.println("Entrée dans shortestPathSolution2");
        final ShortestPathData data = getInputData();
        
        ShortestPathSolution solution = null;
        
        //On vérifie d'abord si l'origine est égal à la destination 
        if (origin.equals(destination)) {
    	   System.out.println("L'origine et la destination sont le même lieu");
    	   return new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL);
        }
        
        
                
    
        //On créé un tableau dans lequel chaque noeud correspond à un label
        List <Label> nodeToLabel = new ArrayList <Label>();
        for (int k=0 ; k<graph.size() ; k++) {
        	nodeToLabel.add(null);
        }
        nodeToLabel.set(origin.getId(), labels.get(0));
        
        //On commence l'algorithme 
        boolean trouve = false;
        List<Arc> Suc = new ArrayList<Arc>();
        Label labelCourrant=null;
        Label labelModif=null;
        notifyOriginProcessed(this.origin);
        while (!(trouve) && !(tas.isEmpty())) {
        	labelCourrant=tas.deleteMin();
        	Suc=labelCourrant.sommet.getSuccessors();
        	//on trouve le minimum parmis les successors
        	for (Arc oneArc : Suc) {
        		if (!data.isAllowed(oneArc)) {
                    continue;
                }
        		
        		if (nodeToLabel.get(oneArc.getDestination().getId())==null) {
        			labelModif=this.NewLabel(oneArc.getDestination(), this.graph, data);
        			nodeToLabel.set(oneArc.getDestination().getId(), labelModif);
        			tas.insert(labelModif);
        		}else {
        			labelModif=nodeToLabel.get(oneArc.getDestination().getId());
        		}
        		
        		Double oldvalue=labelModif.getCost();
        		if (labelCourrant.getCost()+data.getCost(oneArc) < labelModif.getCost()){
        			tas.remove(labelModif);
        			labelModif.setCost(labelCourrant.getCost()+data.getCost(oneArc));
        			labelModif.pere=oneArc;
        			tas.insert(labelModif);
        		}
        		double newValue=labelModif.getCost();
        		if (Double.isInfinite(oldvalue)&&Double.isFinite(newValue)) {
        			notifyNodeReached(labelModif.sommet);
        		}
        	}
        	
        	labelCourrant.marque=true;
        	//System.out.println(labelCourrant.getTotalCost());
        	notifyNodeMarked(labelCourrant.sommet);
        	
            if (labelCourrant.sommet.equals(destination)) {
            	trouve=true;
            	notifyDestinationReached(destination);
            }
            if (!(tas.isValid())) {
            	System.out.println("LE TAS N'EST PAS VALIDE");
            }
            
           
        	
        	
        		
        }
        //System.out.println("Sortie algo");
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
