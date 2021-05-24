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
	//Getter pour avoir le coup
	public double getCost() {
		return this.cost;
	}
	//Ce getter sera utile pour le A*
	public double getTotalCost() {
		return this.cost;
	}
	
	public void setCost(double newCost) {
		this.cost=newCost;
	}
	
	public int compareTo(Label other) {
		//Si les coût totaux sont égaux deux cas sont possibles...
		if (Double.compare(this.getTotalCost(),other.getTotalCost())==0){
			//On regarde si on est dans le cas dijkstra 
			if (Double.compare(this.getTotalCost(), this.getCost())==0 && Double.compare(other.getTotalCost(), other.getCost())==0 ) {
				return Double.compare(this.getCost(),other.getCost());
			}
			//ou le cas A*
			else {
				//On compare other<this (si c'est vrai ça veut dire que this à le coût à l'origine le plus grand donc le coût estimé le plus petit)
				return Double.compare(other.getCost(), this.getCost());
			}
			
		}else {
			return Double.compare(this.getTotalCost(),other.getTotalCost());
		}
		
	}

}
