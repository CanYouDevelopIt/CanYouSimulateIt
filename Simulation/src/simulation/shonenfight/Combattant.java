package simulation.shonenfight;

import simulation.Personnage;
import simulation.etat.EtatCombattant;
import simulation.etat.EtatInactif;
import simulation.etat.EtatPersonnage;

public class Combattant extends Personnage {

	private int pointAttaque;
	private int pointDeVie;
	private boolean aPasCombattu;

	public Combattant(String _nom, int _vie, int _attaque) {
		super(_nom);
		pointAttaque = _attaque;
		pointDeVie = _vie;
		aPasCombattu = true;
		
		EtatPersonnage etat = new EtatCombattant();
		etat.changerEtat(this);
	}

	public int getPointAttaque() {
		return pointAttaque;
	}

	public void setPointAttaque(int pointAttaque) {
		this.pointAttaque = pointAttaque;
	}

	public int getPointDeVie() {
		return pointDeVie;
	}

	public void setPointDeVie(int pointDeVie) {
		this.pointDeVie = pointDeVie;
	}
	
	public boolean getAPasCombattu() {
		return aPasCombattu;
	}

	public void setAPasCombattu(boolean val) {
		this.aPasCombattu = val;
	}

	public void AttaqueCombattant(Combattant adversaire){
		int pdvAdversaire = adversaire.getPointDeVie() - pointAttaque;
		adversaire.setPointDeVie(pdvAdversaire);
		
		System.out.println(this.getNomPersonnage() + " a attaqué " + adversaire.getNomPersonnage());
		System.out.println(adversaire.getNomPersonnage() + " a perdu " + pointAttaque + " HP. Il lui reste " + pdvAdversaire + "HP.");
		
		if(adversaire.getPointDeVie() < 1){
			EtatInactif etat = new EtatInactif();
			etat.changerEtat(adversaire);
			System.out.println(adversaire.getNomPersonnage() + " est KO.");
		}
	}
	
	public void afficherHP(){
		System.out.println(this.getNomPersonnage() + " a " + this.getPointDeVie());
	}
	
}
