package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.model.Path;


import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;

import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;




import java.util.Random;




public class DijkstraTest {
	
	//Les 4 graphes que nous utiliseront pour les tests 
	protected static Graph graphInsa = null ;
	protected static Graph graphJapon = null ;  
	protected static Graph graphCarre = null ;
	protected static Graph graphHauteGaronne = null ;
	
	
	
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
		
		DijkstraTest.graphInsa = readerInsa.read();
		DijkstraTest.graphJapon = readerJapon.read();
		DijkstraTest.graphCarre = readerCarre.read();
		DijkstraTest.graphHauteGaronne = readerHauteGaronne.read();
	}
	
	public ShortestPathSolution runAlgo (ShortestPathData data) {
		DijkstraAlgorithm Dijkstra = new DijkstraAlgorithm(data);
		ShortestPathSolution solution = Dijkstra.run();
		return solution;
	}
	
	//On regarde si un deux éléments connexes donnent un résultat valide (voir critère de validité d'un path)
	//On test sur les 4 graphes
	@Test
	public void testIsValid() {
		//Test si un chemin généré sur le carte insa est valide
		ShortestPathData dataInsa = new ShortestPathData(graphInsa,graphInsa.get(13), graphInsa.get(42), ArcInspectorFactory.getAllFilters().get(0) );
		ShortestPathSolution solutionInsa = this.runAlgo(dataInsa);
		assertTrue(solutionInsa.getPath().isValid());
		
		//Test si un chemin généré sur le carte du japon est valide
		ShortestPathData dataJapon = new ShortestPathData(graphJapon,graphJapon.get(1234), graphJapon.get(4223), ArcInspectorFactory.getAllFilters().get(0) );
		ShortestPathSolution solutionJapon = this.runAlgo(dataJapon);
		assertTrue(solutionJapon.getPath().isValid());
		
		//Test si un chemin généré sur le carte carré est valide
		ShortestPathData dataCarre = new ShortestPathData(graphCarre,graphCarre.get(13), graphCarre.get(1), ArcInspectorFactory.getAllFilters().get(0) );
		ShortestPathSolution solutionCarre = this.runAlgo(dataCarre);
		assertTrue(solutionCarre.getPath().isValid());
		
		//Test si un chemin généré sur le carte Haute Garonne est valide
		ShortestPathData dataHG = new ShortestPathData(graphHauteGaronne,graphHauteGaronne.get(121), graphHauteGaronne.get(318), ArcInspectorFactory.getAllFilters().get(0) );
		ShortestPathSolution solutionHG = this.runAlgo(dataHG);
		assertTrue(solutionHG.getPath().isValid());
	}
	
	//On test l'algo lorsque l'origine et la destination sont non-connexes
	//On utilise le graph du japon 
	@Test
	public void cheminInexistant() {
		ShortestPathData dataJapon = new ShortestPathData(graphJapon,graphJapon.get(3473935), graphJapon.get(3469904), ArcInspectorFactory.getAllFilters().get(0) );
		ShortestPathSolution solutionJapon = this.runAlgo(dataJapon);
		assertFalse(solutionJapon.isFeasible());
		
		dataJapon = new ShortestPathData(graphJapon,graphJapon.get(3509414), graphJapon.get(1731746), ArcInspectorFactory.getAllFilters().get(0) );
		solutionJapon = this.runAlgo(dataJapon);
		assertFalse(solutionJapon.isFeasible());
	}
	
	//On test l'algo lorsque l'origine et la destination sont confondus
	//On utilise le graph de l'insa et de la haute-garonne
	@Test
	public void cheminDeLongueurNulle() {
		ShortestPathData dataInsa = new ShortestPathData(graphInsa,graphInsa.get(13), graphInsa.get(13), ArcInspectorFactory.getAllFilters().get(0) );
		ShortestPathSolution solutionInsa = this.runAlgo(dataInsa);
		assertTrue(solutionInsa.getStatus()==AbstractSolution.Status.OPTIMAL);
		assertTrue(solutionInsa.getPath()==null);
				
		ShortestPathData dataHG = new ShortestPathData(graphHauteGaronne,graphHauteGaronne.get(121), graphHauteGaronne.get(121), ArcInspectorFactory.getAllFilters().get(0) );
		ShortestPathSolution solutionHG = this.runAlgo(dataHG);
		assertTrue(solutionHG.getStatus()==AbstractSolution.Status.OPTIMAL);
		assertTrue(solutionHG.getPath()==null);
		
	}
	
	
	//On fait une comparaison des résultats obtenus pas Bellman-Ford et l'algo dans différents cas
	//On fait le test sur le graph de l'insa pour 10 trajets aléatoires différents
	@Test
	public void PccComparaisonBellman() {
		//System.out.println("**********Dans comparaison bellmam**********");
		ShortestPathData data ;
		Random random=new Random();
		int origin=0;
		int dest=0;
		int max=graphInsa.size();
		int mode=-1;
		//On va dester pour 20 trajets différents 
		for (int i = 0 ; i<20 ; i++) {
			origin = random.nextInt(max);
			dest = random.nextInt(max);
			mode = random.nextInt(5);
			data=new ShortestPathData(graphInsa,graphInsa.get(origin), graphInsa.get(dest), ArcInspectorFactory.getAllFilters().get(mode) );
			BellmanFordAlgorithm Bellman = new BellmanFordAlgorithm(data);
			ShortestPathSolution solutionAlgo = this.runAlgo(data);
			ShortestPathSolution solutionBellman = Bellman.run();


			if (solutionAlgo.isFeasible() && solutionBellman.isFeasible()) {
				if (solutionBellman.getPath()==null) {
					assertTrue(solutionAlgo.getPath()==null);
				}else {
					assertEquals(solutionAlgo.getPath().getLength(), solutionBellman.getPath().getLength(),1e-6);
					assertEquals(solutionAlgo.getPath().getMinimumTravelTime(), solutionBellman.getPath().getMinimumTravelTime(),1e-6);	
				}	
			}else {
				assertFalse(solutionAlgo.isFeasible());
				assertFalse(solutionBellman.isFeasible());
			}

		}


	}
	
	//On fait une comparaison des résultats obtenus pas Bellman-Ford et l'algo dans le cadre d'un "Fastest path, all roads allowed"
	//On fait le test sur le graph de l'insa pour 10 trajets aléatoires différents
	
	//On fait une comparaison des résultats obtenus pas Bellman-Ford et l'algo dans le cadre d'un "Shortest path, only roads open for cars"
	//On fait le test sur le graph de l'insa pour 10 trajets aléatoires différents
	
	//On fait une comparaison des résultats obtenus pas Bellman-Ford et l'algo dans le cadre d'un "Fastest path, only roads open for cars"
	//On fait le test sur le graph de l'insa pour 10 trajets aléatoires différents
	
	//On fait une comparaison des résultats obtenus pas Bellman-Ford et l'algo dans le cadre d'un "Fastest path for pedestrian"
	//On fait le test sur le graph de l'insa pour 10 trajets aléatoires différents

	//On regarde si la solution obtenue avec notre algo donne bien un chemin optimal en distance à partir d'une liste de noeuds donnée.
	//On utilise le graph de l'insa et de la haute-garonne
	
	//On regarde si la liste de noeuds obtenus par notre algo correspond bien à la plus petite distance possible avec cette liste
	//On utilise le graph de l'insa et de la haute-garonne
	@Test
	public void CoutAlgoEgalCoutShortestPath() {
		ShortestPathData dataInsa = new ShortestPathData(graphInsa,graphInsa.get(13), graphInsa.get(42), ArcInspectorFactory.getAllFilters().get(0) );
		ShortestPathSolution solutionInsa = this.runAlgo(dataInsa);
		List <Node> nodes= new ArrayList <Node>();
		nodes.add(dataInsa.getOrigin());
		//On créé notre liste de noeuds 
		for (int i = 0; i<solutionInsa.getPath().getArcs().size();i++) {
			nodes.add(solutionInsa.getPath().getArcs().get(i).getDestination());
		}
		assertEquals(solutionInsa.getPath().getLength(), Path.createShortestPathFromNodes(graphInsa, nodes).getLength(),1e-16);
		assertEquals(solutionInsa.getPath().getMinimumTravelTime(), Path.createShortestPathFromNodes(graphInsa, nodes).getMinimumTravelTime(),1e-16);
		
		
		ShortestPathData dataHG = new ShortestPathData(graphHauteGaronne,graphHauteGaronne.get(93561), graphHauteGaronne.get(103536), ArcInspectorFactory.getAllFilters().get(0) );
		ShortestPathSolution solutionHG = this.runAlgo(dataHG);
		nodes= new ArrayList <Node>();
		nodes.add(dataHG.getOrigin());
		//On créé notre liste de noeuds 
		for (int i = 0; i<solutionHG.getPath().getArcs().size();i++) {
			nodes.add(solutionHG.getPath().getArcs().get(i).getDestination());
		}
		assertEquals(solutionHG.getPath().getLength(), Path.createShortestPathFromNodes(graphHauteGaronne, nodes).getLength(),1e-16);
		assertEquals(solutionHG.getPath().getMinimumTravelTime(), Path.createShortestPathFromNodes(graphInsa, nodes).getMinimumTravelTime(),1e-16);
		
	}
	
	//On regarde si la solution obtenue avec notre algo donne bien un chemin optimal en temps à partir d'une liste de noeuds donnée
	//On utilise le graph de l'insa et de la haute-garonne
	
	//On regarde si la liste de noeuds obtenus par notre algo correspond bien à la  plus rapide possible avec cette liste
	//On utilise le graph de l'insa et de la haute-garonne
	@Test
	public void CoutAlgoEgalCoutFastestPath() {
		ShortestPathData dataInsa = new ShortestPathData(graphInsa,graphInsa.get(344), graphInsa.get(216), ArcInspectorFactory.getAllFilters().get(2) );
		ShortestPathSolution solutionInsa = this.runAlgo(dataInsa);
		List <Node> nodes= new ArrayList <Node>();
		nodes.add(dataInsa.getOrigin());
		//On créé notre liste de noeuds 
		for (int i = 0; i<solutionInsa.getPath().getArcs().size();i++) {
			nodes.add(solutionInsa.getPath().getArcs().get(i).getDestination());
		}
		assertEquals(solutionInsa.getPath().getLength(), Path.createFastestPathFromNodes(graphInsa, nodes).getLength(),1e-16);
		assertEquals(solutionInsa.getPath().getMinimumTravelTime(), Path.createFastestPathFromNodes(graphInsa, nodes).getMinimumTravelTime(),1e-16);
		
		
		ShortestPathData dataHG = new ShortestPathData(graphHauteGaronne,graphHauteGaronne.get(93561), graphHauteGaronne.get(103536), ArcInspectorFactory.getAllFilters().get(2) );
		ShortestPathSolution solutionHG = this.runAlgo(dataHG);
		nodes= new ArrayList <Node>();
		nodes.add(dataHG.getOrigin());
		//On créé notre liste de noeuds 
		for (int i = 0; i<solutionHG.getPath().getArcs().size();i++) {
			nodes.add(solutionHG.getPath().getArcs().get(i).getDestination());
		}
		assertEquals(solutionHG.getPath().getLength(), Path.createFastestPathFromNodes(graphHauteGaronne, nodes).getLength(),1e-16);
		assertEquals(solutionHG.getPath().getMinimumTravelTime(), Path.createFastestPathFromNodes(graphInsa, nodes).getMinimumTravelTime(),1e-16);
		
	}
	
	
	
	//On regarde que l'exception soit bien levée lorsque l'origine ou la destination n'est pas un noeud existant dans le graph
	@Test(expected = IndexOutOfBoundsException.class)
	public void testNodeNotInGraph() {
		ShortestPathData data = new ShortestPathData(graphCarre,graphCarre.get(-21), graphCarre.get(42), ArcInspectorFactory.getAllFilters().get(0) );
		this.runAlgo(data);
	}
	
	
	
	
	
	
	
}
