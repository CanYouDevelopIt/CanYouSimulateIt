package simulation;

import simulation.common.graph.Node;
import simulation.etat.EtatPersonnage;

public class Personnage {

	private String nomPersonnage;
	private EtatPersonnage etatPersonnage;
	private Node position;

	public Personnage(String _nom) {
		nomPersonnage = _nom;
	}

	public Personnage() {
	}

	public String getNomPersonnage() {
		return nomPersonnage;
	}

	public void setNomPersonnage(String nomPersonnage) {
		this.nomPersonnage = nomPersonnage;
	}

	public EtatPersonnage getEtatPersonnage() {
		return etatPersonnage;
	}

	public void setEtatPersonnage(EtatPersonnage etatPersonnage) {
		this.etatPersonnage = etatPersonnage;
	}

	public Node getPosition() {
		return position;
	}

	public void setPosition(Node position) {
		this.position = position;
	}

}
