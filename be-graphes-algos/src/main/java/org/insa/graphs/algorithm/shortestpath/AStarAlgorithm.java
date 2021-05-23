package org.insa.graphs.algorithm.shortestpath;


import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.*;


public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        
    }
    
    @Override
    protected Label NewLabel(Node node, Graph graph, ShortestPathData mode) {
    	//On détermine la vitesse maximum qui nous servira à l'estimation en temps
        int maxSpeed=-1;
        
        //Si dans les données il est renseigné la vitesse maximum on choisit cette valeur
        if (data.getMode()==AbstractInputData.Mode.TIME && data.getMaximumSpeed() != GraphStatistics.NO_MAXIMUM_SPEED) {
        	maxSpeed=data.getMaximumSpeed();
        }
        //Sinon on prend la valeur de vitesse maximale du graph si elle existe
        else if(data.getMode()==AbstractInputData.Mode.TIME && this.graph.getGraphInformation().hasMaximumSpeed()) {
        	maxSpeed=this.graph.getGraphInformation().getMaximumSpeed();
        }
        
        double estim = node.getPoint().distanceTo(this.destination.getPoint());
        
        if (data.getMode()==AbstractInputData.Mode.TIME) {
    		if (maxSpeed!=-1) {
    			//Pour avoir en km/h
    			estim=estim*3600.0/(maxSpeed*1000); 
    		}else {
    			estim=0;
    		}
    	}
        
        
    	Label newLab = new LabelStar(node,false,Double.POSITIVE_INFINITY,null,estim);
    	this.labels.add(newLab);
    	return newLab;
    	
    }
    


}
