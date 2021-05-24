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
	
	//Décalration des attributs de la classe
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
    
    //Fonction qui permet d'ajouter un label dans la liste des labels
    //On la re-défifnira pour A*
    protected Label NewLabel(Node node, Graph graph, ShortestPathData mode) {
    	Label newLab = new Label(node,false,Double.POSITIVE_INFINITY,null);
    	this.labels.add(newLab);
    	return newLab;
    	
    }
    

    @Override
    protected ShortestPathSolution doRun() {
    	System.out.println("Entrée dans shortestPathSolution2");
    	//On fait l'acquisition des datas
        final ShortestPathData data = getInputData();
        
        ShortestPathSolution solution = null;
        
        //On vérifie d'abord si l'origine est égale à la destination 
        if (origin.equals(destination)) {
    	   System.out.println("L'origine et la destination sont le même lieu");
    	   //on renvoie une solution nulle optimale
    	   return new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL);
        }
        
        
                
    
        //On initalise notre tabelau qui fera la correspondance node-label avec des valeurs nulles 
        List <Label> nodeToLabel = new ArrayList <Label>();
        for (int k=0 ; k<graph.size() ; k++) {
        	nodeToLabel.add(null);
        }
        //On ajoute l'origine dans ce tableau
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
        	//on parcourt l'ensemble des successeurs
        	for (Arc oneArc : Suc) {
        		//On regarde si nous pouvons accéder à cette arc
        		if (!data.isAllowed(oneArc)) {
                    continue;
                }
        		//On vérifie si c'est la première fois qu'on rencontre ce noeud
        		if (nodeToLabel.get(oneArc.getDestination().getId())==null) {
        			//On créé un nouveau label
        			labelModif=this.NewLabel(oneArc.getDestination(), this.graph, data);
        			nodeToLabel.set(oneArc.getDestination().getId(), labelModif);
        			//On l'insère dans le tas si c'est le cas
        			tas.insert(labelModif);
        		}else {
        			//sinon on prend juste le Label qui correspond au noeud
        			labelModif=nodeToLabel.get(oneArc.getDestination().getId());
        		}
        		
        		Double oldvalue=labelModif.getCost();
        		//On vérifie la condition de mise à jour d'un label de dijkstra
        		if (labelCourrant.getCost()+data.getCost(oneArc) < labelModif.getCost()){
        			tas.remove(labelModif);
        			labelModif.setCost(labelCourrant.getCost()+data.getCost(oneArc));
        			labelModif.pere=oneArc;
        			tas.insert(labelModif);
        		}
        		double newValue=labelModif.getCost();
        		//Pour savoir si un label a été parcouru on regarde si son ancienne valeure était infinie et sa nouvelle finie
        		if (Double.isInfinite(oldvalue)&&Double.isFinite(newValue)) {
        			notifyNodeReached(labelModif.sommet);
        		}
        	}
        	
        	labelCourrant.marque=true;
        	//Pour vérifier que le coût des labels marqués est croissant
        	//System.out.println(labelCourrant.getTotalCost());
        	notifyNodeMarked(labelCourrant.sommet);
        	
        	//Si notre label marqué correspond à la destination
            if (labelCourrant.sommet.equals(destination)) {
            	//On sort de la boucle while
            	trouve=true;
            	//on le notifie aux observateurs
            	notifyDestinationReached(destination);
            }
            //On regarde si le tas est valide à chaque fin d'itération
            //Normalement, ça devrait jamais arrivé car les insert et remove permettent d'avoir un tas valide
            if (!(tas.isValid())) {
            	System.out.println("LE TAS N'EST PAS VALIDE");
            }
            
           
        	
        	
        		
        }
        //System.out.println("Sortie algo");
        
        //Si on a eu un réssultat
        if (trouve) {
        	//On met en forme la solution
        	List <Arc> sol = new ArrayList <Arc>();
        	sol.add(labelCourrant.pere);
        	Node fatherNode = labelCourrant.pere.getOrigin();
        	//On créé une liste d'arc corresondant à la solution
        	while (!(fatherNode.equals(origin))) {
        		labelCourrant=nodeToLabel.get(fatherNode.getId());
        		sol.add(labelCourrant.pere);
        		fatherNode=labelCourrant.pere.getOrigin();
        	}
        	//On reverse notre tableau
        	Collections.reverse(sol);
        	Path pathSolution = new Path(graph, sol);
        	System.out.println("Resultat mis en place");
        	
        	solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, pathSolution);
        	
        }else {
        	//Si on a pas de résultat, on retourne une solution avec le statut infaisable
        	System.out.println("resultat pas trouvé");
        	solution = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        	
        	
        }
        
        
        
        
        
        return solution;
    }

}
