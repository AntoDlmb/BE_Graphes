package org.insa.graphs.model;

public class Label implements Comparable<Label> {
	public Node sommet;
	public boolean marque;
	private double cost;
	public Arc pere;
	public Label(Node sommet, boolean marque, double cost, Arc pere ) {
		this.sommet=sommet;
		this.marque=marque;
		this.cost=cost;
		this.pere=pere;
		
	}
	public double getCost() {
		return this.cost;
	}
	public void setCost(double newCost) {
		this.cost=newCost;
	}
	
	public int compareTo(Label other) {
		return Double.compare(this.getCost(),other.getCost());
	}

}
