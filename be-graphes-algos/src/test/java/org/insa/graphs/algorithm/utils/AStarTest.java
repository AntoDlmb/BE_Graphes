package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.model.Path;


import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;

import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class AStarTest {
	//Les 4 graphes que nous utiliseront pour les tests 
		private static Graph graphInsa = null ;
		private static Graph graphJapon = null ;  
		private static Graph graphCarre = null ;
		private static Graph graphHauteGaronne = null ;
		
		
		
		//Initialisation des 4 graphes
		@BeforeClass
		public static void initAll() throws Exception {
			final String mapInsa = "/Users/Antonin/Documents/insa/3A/BE_graphes/maps/insa.mapgr";
			final String mapJapon = "/Users/Antonin/Documents/insa/3A/BE_graphes/maps/japan.mapgr";
			final String mapCarre = "/Users/Antonin/Documents/insa/3A/BE_graphes/maps/carre.mapgr";
			final String mapHauteGaronne = "/Users/Antonin/Documents/insa/3A/BE_graphes/maps/haute-garonne.mapgr";
			
			final GraphReader readerInsa = new BinaryGraphReader(
		            new DataInputStream(new BufferedInputStream(new FileInputStream(mapInsa))));
			final GraphReader readerJapon = new BinaryGraphReader(
		            new DataInputStream(new BufferedInputStream(new FileInputStream(mapJapon))));
			final GraphReader readerCarre = new BinaryGraphReader(
		            new DataInputStream(new BufferedInputStream(new FileInputStream(mapCarre))));
			final GraphReader readerHauteGaronne = new BinaryGraphReader(
		            new DataInputStream(new BufferedInputStream(new FileInputStream(mapHauteGaronne))));
			
			AStarTest.graphInsa = readerInsa.read();
			AStarTest.graphJapon = readerJapon.read();
			AStarTest.graphCarre = readerCarre.read();
			AStarTest.graphHauteGaronne = readerHauteGaronne.read();
		}
		
		//On regarde si un deux éléments connexes donnent un résultat valide (voir critère de validité d'un path)
		//On test sur les 4 graphes
		@Test
		public void testIsValid() {
			//Test si un chemin généré sur le carte insa est valide
			ShortestPathData dataInsa = new ShortestPathData(graphInsa,graphInsa.get(13), graphInsa.get(42), ArcInspectorFactory.getAllFilters().get(0) );
			AStarAlgorithm AStarInsa = new AStarAlgorithm(dataInsa);
			ShortestPathSolution solutionInsa = AStarInsa.run();
			assertTrue(solutionInsa.getPath().isValid());
			
			//Test si un chemin généré sur le carte du japon est valide
			ShortestPathData dataJapon = new ShortestPathData(graphJapon,graphJapon.get(1234), graphJapon.get(4223), ArcInspectorFactory.getAllFilters().get(0) );
			AStarAlgorithm AStarJapon = new AStarAlgorithm(dataJapon);
			ShortestPathSolution solutionJapon = AStarJapon.run();
			assertTrue(solutionJapon.getPath().isValid());
			
			//Test si un chemin généré sur le carte carré est valide
			ShortestPathData dataCarre = new ShortestPathData(graphCarre,graphCarre.get(13), graphCarre.get(1), ArcInspectorFactory.getAllFilters().get(0) );
			AStarAlgorithm AStarCarre = new AStarAlgorithm(dataCarre);
			ShortestPathSolution solutionCarre = AStarCarre.run();
			assertTrue(solutionCarre.getPath().isValid());
			
			//Test si un chemin généré sur le carte Haute Garonne est valide
			ShortestPathData dataHG = new ShortestPathData(graphHauteGaronne,graphHauteGaronne.get(121), graphHauteGaronne.get(318), ArcInspectorFactory.getAllFilters().get(0) );
			AStarAlgorithm AStarHG = new AStarAlgorithm(dataHG);
			ShortestPathSolution solutionHG = AStarHG.run();
			assertTrue(solutionHG.getPath().isValid());
		}
		
		//On test l'algo lorsque l'origine et la destination sont non-connexes
		//On utilise le graph du japon 
		@Test
		public void cheminInexistant() {
			ShortestPathData dataJapon = new ShortestPathData(graphJapon,graphJapon.get(3473935), graphJapon.get(3469904), ArcInspectorFactory.getAllFilters().get(0) );
			AStarAlgorithm AStarJapon = new AStarAlgorithm(dataJapon);
			ShortestPathSolution solutionJapon = AStarJapon.run();
			assertFalse(solutionJapon.isFeasible());
			
			dataJapon = new ShortestPathData(graphJapon,graphJapon.get(3509414), graphJapon.get(1731746), ArcInspectorFactory.getAllFilters().get(0) );
			AStarJapon = new AStarAlgorithm(dataJapon);
			solutionJapon = AStarJapon.run();
			assertFalse(solutionJapon.isFeasible());
		}
		
		//On test l'algo lorsque l'origine et la destination sont confondus
		//On utilise le graph de l'insa et de la haute-garonne
		@Test
		public void cheminDeLongueurNulle() {
			ShortestPathData dataInsa = new ShortestPathData(graphInsa,graphInsa.get(13), graphInsa.get(13), ArcInspectorFactory.getAllFilters().get(0) );
			AStarAlgorithm AStarInsa = new AStarAlgorithm(dataInsa);
			ShortestPathSolution solutionInsa = AStarInsa.run();
			assertFalse(solutionInsa.isFeasible());
					
			ShortestPathData dataHG = new ShortestPathData(graphHauteGaronne,graphHauteGaronne.get(121), graphHauteGaronne.get(121), ArcInspectorFactory.getAllFilters().get(0) );
			AStarAlgorithm AStarHG = new AStarAlgorithm(dataHG);
			ShortestPathSolution solutionHG = AStarHG.run();
			assertFalse(solutionHG.isFeasible());
			
		}
		
		
		//On fait une comparaison des résultats obtenus pas Bellman-Ford et l'algo dans le cadre d'un "Shortest path, all roads allowed"
		//On fait le test sur le graph de l'insa pour 10 trajets aléatoires différents
		@Test
		public void PccDistanceComparaisonBellman() {
			//System.out.println("*****************Dans comparaison bellmam");
			ShortestPathData data ;
			Random random=new Random();
			int origin=0;
			int dest=0;
			int max=graphInsa.size();
			//On va dester pour 10 trajets différents 
			for (int i = 0 ; i<10 ; i++) {
				origin = random.nextInt(max);
				dest = random.nextInt(max);
				data=new ShortestPathData(graphInsa,graphInsa.get(origin), graphInsa.get(dest), ArcInspectorFactory.getAllFilters().get(0) );
				AStarAlgorithm AStar = new AStarAlgorithm(data);
				BellmanFordAlgorithm Bellman = new BellmanFordAlgorithm(data);
				ShortestPathSolution solutionAStar = AStar.run();
				ShortestPathSolution solutionBellman = Bellman.run();
				
				
				if (solutionAStar.isFeasible() && solutionBellman.isFeasible()) {
					assertEquals(solutionAStar.getPath().getLength(), solutionBellman.getPath().getLength(),1e-6);
				}else {
					assertFalse(solutionAStar.isFeasible());
					assertFalse(solutionBellman.isFeasible());
				}
				
			}
			
			
		}
		
		//On fait une comparaison des résultats obtenus pas Bellman-Ford et l'algo dans le cadre d'un "Fastest path, all roads allowed"
		//On fait le test sur le graph de l'insa pour 10 trajets aléatoires différents
		@Test
		public void PccTempsComparaisonBellman() {
			ShortestPathData data ;
			Random random=new Random();
			int origin=0;
			int dest=0;
			int max=graphInsa.size();
			//On va dester pour 10 trajets différents 
			for (int i = 0 ; i<10 ; i++) {
				origin = random.nextInt(max);
				dest = random.nextInt(max);
				data=new ShortestPathData(graphInsa,graphInsa.get(origin), graphInsa.get(dest), ArcInspectorFactory.getAllFilters().get(2) );
				AStarAlgorithm AStar = new AStarAlgorithm(data);
				BellmanFordAlgorithm Bellman = new BellmanFordAlgorithm(data);
				ShortestPathSolution solutionAStar = AStar.run();
				ShortestPathSolution solutionBellman = Bellman.run();
				
				if (solutionAStar.isFeasible() && solutionBellman.isFeasible()) {
					assertEquals(solutionAStar.getPath().getMinimumTravelTime(), solutionBellman.getPath().getMinimumTravelTime(),1e-6);
				}else {
					assertFalse(solutionAStar.isFeasible());
					assertFalse(solutionBellman.isFeasible());
				}
				
			}
			
		}
		
		//On fait une comparaison des résultats obtenus pas Bellman-Ford et l'algo dans le cadre d'un "Shortest path, only roads open for cars"
		//On fait le test sur le graph de l'insa pour 10 trajets aléatoires différents
		@Test
		public void PccDistanceVoitureComparaisonBellman() {
			ShortestPathData data ;
			Random random=new Random();
			int origin=0;
			int dest=0;
			int max=graphInsa.size();
			//On va dester pour 10 trajets différents 
			for (int i = 0 ; i<10 ; i++) {
				origin = random.nextInt(max);
				dest = random.nextInt(max);
				data=new ShortestPathData(graphInsa,graphInsa.get(origin), graphInsa.get(dest), ArcInspectorFactory.getAllFilters().get(1) );
				AStarAlgorithm AStar = new AStarAlgorithm(data);
				BellmanFordAlgorithm Bellman = new BellmanFordAlgorithm(data);
				ShortestPathSolution solutionAStar = AStar.run();
				ShortestPathSolution solutionBellman = Bellman.run();
				
				if (solutionAStar.isFeasible() && solutionBellman.isFeasible()) {
					assertEquals(solutionAStar.getPath().getLength(), solutionBellman.getPath().getLength(),1e-6);
				}else {
					assertFalse(solutionAStar.isFeasible());
					assertFalse(solutionBellman.isFeasible());
				}
				
			}
			
		}
		
		//On fait une comparaison des résultats obtenus pas Bellman-Ford et l'algo dans le cadre d'un "Fastest path, only roads open for cars"
		//On fait le test sur le graph de l'insa pour 10 trajets aléatoires différents
		@Test
		public void PccTempsVoitureComparaisonBellman() {
			ShortestPathData data ;
			Random random=new Random();
			int origin=0;
			int dest=0;
			int max=graphInsa.size();
			//On va dester pour 10 trajets différents 
			for (int i = 0 ; i<10 ; i++) {
				origin = random.nextInt(max);
				dest = random.nextInt(max);
				data=new ShortestPathData(graphInsa,graphInsa.get(origin), graphInsa.get(dest), ArcInspectorFactory.getAllFilters().get(3) );
				AStarAlgorithm AStar = new AStarAlgorithm(data);
				BellmanFordAlgorithm Bellman = new BellmanFordAlgorithm(data);
				ShortestPathSolution solutionAStar = AStar.run();
				ShortestPathSolution solutionBellman = Bellman.run();
				
				if (solutionAStar.isFeasible() && solutionBellman.isFeasible()) {
					assertEquals(solutionAStar.getPath().getMinimumTravelTime(), solutionBellman.getPath().getMinimumTravelTime(),1e-6);
				}else {
					assertFalse(solutionAStar.isFeasible());
					assertFalse(solutionBellman.isFeasible());
				}
				
			}
			
		}
		
		//On fait une comparaison des résultats obtenus pas Bellman-Ford et l'algo dans le cadre d'un "Fastest path for pedestrian"
		//On fait le test sur le graph de l'insa pour 10 trajets aléatoires différents
		@Test
		public void PccTempsPietonComparaisonBellman() {
			ShortestPathData data ;
			Random random=new Random();
			int origin=0;
			int dest=0;
			int max=graphInsa.size();
			//On va dester pour 10 trajets différents 
			for (int i = 0 ; i<10 ; i++) {
				origin = random.nextInt(max);
				dest = random.nextInt(max);
				data=new ShortestPathData(graphInsa,graphInsa.get(origin), graphInsa.get(dest), ArcInspectorFactory.getAllFilters().get(4) );
				AStarAlgorithm AStar = new AStarAlgorithm(data);
				BellmanFordAlgorithm Bellman = new BellmanFordAlgorithm(data);
				ShortestPathSolution solutionAStar = AStar.run();
				ShortestPathSolution solutionBellman = Bellman.run();
				
				if (solutionAStar.isFeasible() && solutionBellman.isFeasible()) {
					assertEquals(solutionAStar.getPath().getMinimumTravelTime(), solutionBellman.getPath().getMinimumTravelTime(),1e-6);
				}else {
					assertFalse(solutionAStar.isFeasible());
					assertFalse(solutionBellman.isFeasible());
				}
				
			}
			
		}
		
		//On regarde si la liste de noeuds obtenus par notre algo correspond bien à la plus petite distance possible avec cette liste
		//On utilise le graph de l'insa et de la haute-garonne
		@Test
		public void CoutAStarEgalCoutShortestPath() {
			ShortestPathData dataInsa = new ShortestPathData(graphInsa,graphInsa.get(13), graphInsa.get(42), ArcInspectorFactory.getAllFilters().get(0) );
			AStarAlgorithm AStarInsa = new AStarAlgorithm(dataInsa);
			ShortestPathSolution solutionInsa = AStarInsa.run();
			List <Node> nodes= new ArrayList <Node>();
			nodes.add(dataInsa.getOrigin());
			//On créé notre liste de noeuds 
			for (int i = 0; i<solutionInsa.getPath().getArcs().size();i++) {
				nodes.add(solutionInsa.getPath().getArcs().get(i).getDestination());
			}
			assertEquals(solutionInsa.getPath().getLength(), Path.createShortestPathFromNodes(graphInsa, nodes).getLength(),1e-16);
			
			
			ShortestPathData dataHG = new ShortestPathData(graphHauteGaronne,graphHauteGaronne.get(93561), graphHauteGaronne.get(103536), ArcInspectorFactory.getAllFilters().get(0) );
			AStarAlgorithm AStarHG = new AStarAlgorithm(dataHG);
			ShortestPathSolution solutionHG = AStarHG.run();
			nodes= new ArrayList <Node>();
			nodes.add(dataHG.getOrigin());
			//On créé notre liste de noeuds 
			for (int i = 0; i<solutionHG.getPath().getArcs().size();i++) {
				nodes.add(solutionHG.getPath().getArcs().get(i).getDestination());
			}
			assertEquals(solutionHG.getPath().getLength(), Path.createShortestPathFromNodes(graphHauteGaronne, nodes).getLength(),1e-16);
			
		}
		
		//On regarde si la liste de noeuds obtenus par notre algo correspond bien à la  plus rapide possible avec cette liste
		//On utilise le graph de l'insa et de la haute-garonne
		@Test
		public void CoutAStarEgalCoutFastestPath() {
			ShortestPathData dataInsa = new ShortestPathData(graphInsa,graphInsa.get(344), graphInsa.get(216), ArcInspectorFactory.getAllFilters().get(2) );
			AStarAlgorithm AStarInsa = new AStarAlgorithm(dataInsa);
			ShortestPathSolution solutionInsa = AStarInsa.run();
			List <Node> nodes= new ArrayList <Node>();
			nodes.add(dataInsa.getOrigin());
			//On créé notre liste de noeuds 
			for (int i = 0; i<solutionInsa.getPath().getArcs().size();i++) {
				nodes.add(solutionInsa.getPath().getArcs().get(i).getDestination());
			}
			assertEquals(solutionInsa.getPath().getLength(), Path.createFastestPathFromNodes(graphInsa, nodes).getLength(),1e-16);
			
			
			ShortestPathData dataHG = new ShortestPathData(graphHauteGaronne,graphHauteGaronne.get(93561), graphHauteGaronne.get(103536), ArcInspectorFactory.getAllFilters().get(2) );
			AStarAlgorithm AStarHG = new AStarAlgorithm(dataHG);
			ShortestPathSolution solutionHG = AStarHG.run();
			nodes= new ArrayList <Node>();
			nodes.add(dataHG.getOrigin());
			//On créé notre liste de noeuds 
			for (int i = 0; i<solutionHG.getPath().getArcs().size();i++) {
				nodes.add(solutionHG.getPath().getArcs().get(i).getDestination());
			}
			assertEquals(solutionHG.getPath().getLength(), Path.createFastestPathFromNodes(graphHauteGaronne, nodes).getLength(),1e-16);
			
		}
		
		
		//On regarde si des chemins obtenus avec Dijkstra sont les mêmes que ceux obtenus avec A*
		//On utilise le graph de la haute-garonne
		@Test
		public void ComparaisonDijkstraAStar() {
			ShortestPathData data ;
			Random random=new Random();
			int origin=0;
			int dest=0;
			int max=graphHauteGaronne.size();
			int mode = -1;
			//On va dester pour 3 trajets aléatoires différents 
			for (int i = 0 ; i<3 ; i++) {
				origin = random.nextInt(max);
				dest = random.nextInt(max);
				mode=random.nextInt(4)+1;
				data=new ShortestPathData(graphHauteGaronne,graphHauteGaronne.get(origin), graphHauteGaronne.get(dest), ArcInspectorFactory.getAllFilters().get(mode) );
				AStarAlgorithm AStar = new AStarAlgorithm(data);
				DijkstraAlgorithm Dijkstra = new DijkstraAlgorithm(data);
				ShortestPathSolution solutionAStar = AStar.run();
				ShortestPathSolution solutionDijkstra = Dijkstra.run();
				
				if (solutionAStar.isFeasible() && solutionDijkstra.isFeasible()) {
					assertEquals(solutionAStar.getPath().getMinimumTravelTime(), solutionDijkstra.getPath().getMinimumTravelTime(),1e-6);
					assertEquals(solutionAStar.getPath().getLength(), solutionDijkstra.getPath().getLength(),1e-6);
				}else {
					assertFalse(solutionAStar.isFeasible());
					assertFalse(solutionDijkstra.isFeasible());
				}
				
			}
			
		}
		
		
		//On regarde que l'exception soit bien levée lorsque l'origine ou la destination n'est pas un noeud existant dans le graph
		@Test(expected = IndexOutOfBoundsException.class)
		public void testNodeNotInGraph() {
			ShortestPathData data = new ShortestPathData(graphCarre,graphCarre.get(-21), graphCarre.get(42), ArcInspectorFactory.getAllFilters().get(0) );
			AStarAlgorithm AStar = new AStarAlgorithm(data);
			AStar.run();
		}


}
