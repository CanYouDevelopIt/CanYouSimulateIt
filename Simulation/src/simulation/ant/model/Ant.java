package simulation.ant.model;

import java.util.ArrayList;
import java.util.List;

import simulation.Personnage;
import simulation.common.graph.Node;

public class Ant extends Personnage {
	
	private int id;
	private Node nodeAnt;
	private List<Node> nodesDejaPasses;
	private List<Node> pommes;
	private boolean trouve;
	
	public Ant(int id_, Node n, List<Node> tousLesPommes) {
		id = id_;
		nodeAnt = n;
		pommes = tousLesPommes;
		nodesDejaPasses = new ArrayList<Node>();
	}
		
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public List<Node> getPommes() {
		return pommes;
	}


	public void setPommes(List<Node> pommes) {
		this.pommes = pommes;
	}


	public Node getNodeAnt() {
		return nodeAnt;
	}
	
	public void setNodeAnt(Node nodeAnt) {
		this.nodeAnt = nodeAnt;
	}
	
	public boolean isTrouve() {
		return trouve;
	}
	
	public void setTrouve(boolean trouve) {
		this.trouve = trouve;
	}
	
	public List<Node> getNodesDejaPasses() {
		return nodesDejaPasses;
	}

	public void setNodesDejaPasses(List<Node> nodesDejaPasses) {
		this.nodesDejaPasses = nodesDejaPasses;
	}

	public void ajouterNodeDejaPasse(Node n) {
		nodesDejaPasses.add(n);
	}
}
