package org.insa.graphs.algorithm.shortestpath;


import org.insa.graphs.model.Label;
import org.insa.graphs.model.LabelStar;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        for (Node oneNode : graph.getNodes()) {
        	if (!(oneNode.equals(origin))) {
        		this.labels.add(new LabelStar(oneNode,false,Double.POSITIVE_INFINITY,null,destination));
        	}else {
        		this.labelOrigin = new LabelStar(oneNode,true,0.0,null,destination);
        		this.labels.add(labelOrigin);
        		this.tas.insert(labelOrigin);
        	}
        }
        
        
    }
    


}
