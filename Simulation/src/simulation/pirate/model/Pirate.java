package simulation.pirate.model;

import java.util.List;

import simulation.Personnage;
import simulation.common.graph.Node;
import simulation.etat.EtatPirate;

public class Pirate extends Personnage {

	private int id;
	private List<Node> nodesDejaPasses;

	public Pirate() {
		super("Mugiwara");
		setEtatPersonnage(new EtatPirate());
	}

	public Pirate(int id) {
		super("pirate " + id);
		setEtatPersonnage(new EtatPirate());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
