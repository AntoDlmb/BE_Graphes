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

	public double getTotalCost() {
		return this.cost;
	}
	
	public void setCost(double newCost) {
		this.cost=newCost;
	}
	
	public int compareTo(Label other) {
		if (Double.compare(this.getTotalCost(),other.getTotalCost())==0){
			//On regarde si on est dans le cas dijkstra 
			if (Double.compare(this.getTotalCost(), this.getCost())==0 && Double.compare(other.getTotalCost(), other.getCost())==0 ) {
				return Double.compare(this.getCost(),other.getCost());
			}
			//ou le cas A*
			else {
				return Double.compare(other.getCost(), this.getCost());
			}
			
		}else {
			return Double.compare(this.getTotalCost(),other.getTotalCost());
		}
		
	}

}
