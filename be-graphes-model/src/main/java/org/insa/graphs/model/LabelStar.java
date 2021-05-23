package org.insa.graphs.model;


public class LabelStar extends Label implements Comparable<Label> {
	private double estimatedCost;
	
	public LabelStar(Node sommet, boolean marque, double cost, Arc pere, Node Destination) {
		super(sommet,marque,cost,pere);
		this.estimatedCost=this.sommet.getPoint().distanceTo(Destination.getPoint());
	}
	
	public double getEstimatedCost() {
		return this.estimatedCost;
	}
	
	@Override
	public double getTotalCost() {
		return this.getEstimatedCost()+this.getCost();
	}
	
	
	

}
