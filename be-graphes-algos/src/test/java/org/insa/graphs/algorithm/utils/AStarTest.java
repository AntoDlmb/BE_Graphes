package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.*;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.junit.Test;

public class AStarTest extends DijkstraTest {

	@Override
	public ShortestPathSolution runAlgo (ShortestPathData data) {
		AStarAlgorithm Astar = new AStarAlgorithm(data);
		ShortestPathSolution solution = Astar.run();
		return solution;
	}


	//On regarde si des chemins obtenus avec Dijkstra sont les mêmes que ceux obtenus avec A*
	//On utilise le graph de la haute-garonne
	@Test
	public void ComparaisonDijkstraAStarBellman() {
		ShortestPathData data ;
		Random random=new Random();
		int origin=0;
		int dest=0;
		int max=graphInsa.size();
		int mode = -1;
		//On va dester pour 3 trajets aléatoires différents 
		for (int i = 0 ; i<3 ; i++) {
			origin = random.nextInt(max);
			dest = random.nextInt(max);
			mode=random.nextInt(5);
			data=new ShortestPathData(graphInsa,graphInsa.get(origin), graphInsa.get(dest), ArcInspectorFactory.getAllFilters().get(mode) );
			DijkstraAlgorithm Dijkstra = new DijkstraAlgorithm(data);
			BellmanFordAlgorithm Bellman = new BellmanFordAlgorithm(data);
			ShortestPathSolution solutionAStar = this.runAlgo(data);
			ShortestPathSolution solutionDijkstra = Dijkstra.run();
			ShortestPathSolution solutionBellman = Bellman.run();

			if (solutionAStar.isFeasible() && solutionDijkstra.isFeasible() && solutionBellman.isFeasible()) {

				if (solutionBellman.getPath()==null) {
					assertTrue(solutionAStar.getPath()==null);
					assertTrue(solutionDijkstra.getPath()==null);
				}else {
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


}
