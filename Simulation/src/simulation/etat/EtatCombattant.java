package simulation.etat;

import simulation.Personnage;

public class EtatCombattant extends EtatPersonnage {

	@Override
	public void changerEtat(Personnage p) {
		p.setEtatPersonnage(this);
	}

}
