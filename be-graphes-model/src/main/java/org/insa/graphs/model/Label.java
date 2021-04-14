package org.insa.graphs.model;

public class Label implements Comparable<Label> {
	public Node sommet;
	public boolean marque;
	private float cost;
	public Arc pere;
	public Label(Node sommet, boolean marque, float cost, Arc pere ) {
		this.sommet=sommet;
		this.marque=marque;
		this.cost=cost;
		this.pere=pere;
		
	}
	public float getCost() {
		return this.cost;
	}
	public void setCost(float newCost) {
		this.cost=newCost;
	}
	
	public int compareTo(Label other) {
		return Float.compare(this.getCost(),other.getCost());
	}

}
