package simulation.common.graph;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private String id;
	private String idOrigine;
	private int x;
	private int y;
	private List<Edge> mesEdges;
	private int minDistance = 999999;
	private Node nodePrecedent = null;
	private int nb;
	private int ant = 0;

	public Node(String unId, int x, int y) {
		id = unId;
		idOrigine = unId;
		this.x = x;
		this.y = y;
		mesEdges = new ArrayList<Edge>();
		nb = 0;
	}
	
	public Node(String unId, int x, int y, int nb) {
		id = unId;
		idOrigine = unId;
		this.x = x;
		this.y = y;
		mesEdges = new ArrayList<Edge>();
		this.nb = nb;
	}
	
	public int getNb() {
		return nb;
	}

	public void setNb(int nb) {
		this.nb = nb;
	}

	public int getAnt() {
		return ant;
	}

	public void setAnt(int ant) {
		this.ant = ant;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setX(int x_) {
		x = x_;
	}

	public void setY(int y_) {
		y = y_;
	}
	
	public List<Edge> getEdges() {
		return mesEdges;
	}

	public String getId() {
		return id;
	}

	public boolean equals(Node n) {
		if (n == null)
				return false;
		return this.x == n.getX() && this.y == n.getY();
	}

	public void setId(String unId) {
		this.id = unId;
	}

	public String getIdOrigine() {
		return idOrigine;
	}

	public void setIdOrigine(String idOrigine) {
		this.idOrigine = idOrigine;
	}
	
	public int getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(int minDistance) {
		this.minDistance = minDistance;
	}

	public Node getNodePrecedent() {
		return nodePrecedent;
	}
	
	public void setNodePrecedent(Node nodePrecedent) {
		this.nodePrecedent = nodePrecedent;
	}
	
	public String toString() {
		return "[" + this.x + "," + this.y + "]";
	}

}
