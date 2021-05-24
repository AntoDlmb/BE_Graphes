package org.insa.graphs.model;




public class LabelStar extends Label implements Comparable<Label> {
	private double estimatedCost;
	
	public LabelStar(Node sommet, boolean marque, double cost, Arc pere, double estimatedCost) {
		super(sommet,marque,cost,pere);
		this.estimatedCost=estimatedCost;
	}
	
	public double getEstimatedCost() {
		return this.estimatedCost;
	}
	
	//Cette méthode nous permet de connaitre le coût total d'un labelStar
	@Override
	public double getTotalCost() {
		return this.getEstimatedCost()+this.getCost();
	}
	
	
	

}
