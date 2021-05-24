package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.insa.graphs.model.*;


import java.util.*;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.GraphStatistics;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.junit.Test;

public class AStarTest extends DijkstraTest {

	//Fonction pour lancer la recherche du PCC avec A* 
	@Override
	public ShortestPathSolution runAlgo (ShortestPathData data) {
		AStarAlgorithm Astar = new AStarAlgorithm(data);
		ShortestPathSolution solution = Astar.run();
		return solution;
	}


	//On regarde si des chemins obtenus avec Bellman sont les mêmes que ceux obtenus avec Dijkstra et A* 
	//On utilise le graph de l'insa
	@Test
	public void ComparaisonDijkstraAStarBellman() {
		ShortestPathData data ;
		Random random=new Random();
		int origin=0;
		int dest=0;
		int max=graphInsa.size();
		int mode = -1;
		//On va dester pour 10 trajets aléatoires différents 
		for (int i = 0 ; i<10 ; i++) {
			origin = random.nextInt(max);
			dest = random.nextInt(max);
			mode=random.nextInt(5);
			data=new ShortestPathData(graphInsa,graphInsa.get(origin), graphInsa.get(dest), ArcInspectorFactory.getAllFilters().get(mode) );
			DijkstraAlgorithm Dijkstra = new DijkstraAlgorithm(data);
			BellmanFordAlgorithm Bellman = new BellmanFordAlgorithm(data);
			ShortestPathSolution solutionAStar = this.runAlgo(data);
			ShortestPathSolution solutionDijkstra = Dijkstra.run();
			ShortestPathSolution solutionBellman = Bellman.run();
			//on regarde si les chemins sont bien faisables 
			if (solutionAStar.isFeasible() && solutionDijkstra.isFeasible() && solutionBellman.isFeasible()) {
				//Si oui on regarde si ils sont null 
				if (solutionBellman.getPath()==null) {
					assertTrue(solutionAStar.getPath()==null);
					assertTrue(solutionDijkstra.getPath()==null);
				}else { //Ou on regarde si les resultats renvoyés sont bien identiques
					assertEquals(solutionBellman.getPath().getLength(), solutionAStar.getPath().getLength(),1e-6);
					assertEquals(solutionBellman.getPath().getLength(), solutionDijkstra.getPath().getLength(),1e-6);
					assertEquals(solutionBellman.getPath().getMinimumTravelTime(), solutionAStar.getPath().getMinimumTravelTime(),1e-6);
					assertEquals(solutionBellman.getPath().getMinimumTravelTime(), solutionDijkstra.getPath().getMinimumTravelTime(),1e-6);
				}	
			}else {
				assertFalse(solutionAStar.isFeasible());
				assertFalse(solutionDijkstra.isFeasible());
				assertFalse(solutionBellman.isFeasible());
			}

		}

	}
	
	//Dans ce test on vérifie la propriété donnée dans le sujet : "Le coût estimé entre un sommet donné et la destination doit être une borne inférieure du coût réel pour garantir l'optimalité du résultat"
	//On test sur le graph du japon 
	@Test
	public void TestSansOracle() {
		//On fait le test pour un PCC en temps
		ShortestPathData data = new ShortestPathData(graphJapon,graphJapon.get(1375783), graphJapon.get(2029255), ArcInspectorFactory.getAllFilters().get(3));
		ShortestPathSolution solutionAStar = this.runAlgo(data);
				
		//On détermine la vitesse maximum qui nous servira à l'estimation en temps
        int maxSpeed=-1;
        
        //Si dans les données il est renseigné la vitesse maximum on choisit cette valeur
        if (data.getMaximumSpeed() != GraphStatistics.NO_MAXIMUM_SPEED) {
        	maxSpeed=data.getMaximumSpeed();
        }
        //Sinon on prend la valeur de vitesse maximale du graph si elle existe
        else if(data.getGraph().getGraphInformation().hasMaximumSpeed()) {
        	maxSpeed=data.getGraph().getGraphInformation().getMaximumSpeed();
        }
        
        //On regarde si pour chaque noeuds la propriété d'optimalité est vérifiée
        Point currentPoint = data.getOrigin().getPoint();
        Point pointDestination = data.getDestination().getPoint();
        double estimation = currentPoint.distanceTo(pointDestination)*3600.0/(maxSpeed*1000);
        assertTrue(estimation<=solutionAStar.getPath().getMinimumTravelTime());
		for (int i = 0; i<solutionAStar.getPath().getArcs().size();i++) {
			currentPoint=solutionAStar.getPath().getArcs().get(i).getDestination().getPoint();
			estimation = currentPoint.distanceTo(pointDestination)*3600.0/(maxSpeed*1000);
	        assertTrue(estimation<=solutionAStar.getPath().getMinimumTravelTime());
		}
		
		
		//On fait le test pour un PCC en distance
		data = new ShortestPathData(graphJapon,graphJapon.get(1375783), graphJapon.get(2029255), ArcInspectorFactory.getAllFilters().get(1));
		solutionAStar = this.runAlgo(data);
		currentPoint = data.getOrigin().getPoint();
		estimation=currentPoint.distanceTo(pointDestination);
		assertTrue(estimation<=solutionAStar.getPath().getLength());
		for (int i = 0; i<solutionAStar.getPath().getArcs().size();i++) {
			currentPoint=solutionAStar.getPath().getArcs().get(i).getDestination().getPoint();
			estimation = currentPoint.distanceTo(pointDestination);
	        assertTrue(estimation<=solutionAStar.getPath().getLength());
		}
		
	}


}
