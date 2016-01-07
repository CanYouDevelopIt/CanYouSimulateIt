package simulation.shonenfight;

import java.util.ArrayList;
import java.util.List;

import simulation.Personnage;
import simulation.etat.EtatCombattant;

public class EquipeCombattant {
	
	private List<Combattant> equipe;
	private int nbCombattantVivant;
	
	public EquipeCombattant(){
		equipe = new ArrayList<Combattant>();
	}
	
	public void ajouterCombattant(Combattant p){
		equipe.add(p);
		nbCombattantVivant++;
	}
	
	public int getSize(){
		return equipe.size();
	}
	
	public boolean contientCombattant(Combattant p){
		return equipe.contains(p);
	}
	
	public int getNbCombattantVivant(){
		return nbCombattantVivant;
	}
	
	public List<Combattant> getEquipe(){
		return equipe;
	}
	
	public void aPerduCombattant(){
		nbCombattantVivant--;
	}
	
	public Combattant getCombattant(int i){
		return equipe.get(i);
	}
	
	public Combattant getCombattantFaible(){
		
		int indiceCombattantFaible = -1;
		
		for(int i = 0; i < equipe.size(); i++){
			
			if(indiceCombattantFaible == -1){
				if(equipe.get(i).getEtatPersonnage().getClass().getSimpleName().equals(EtatCombattant.class.getSimpleName()))
					indiceCombattantFaible = i;
			}else{
				if(equipe.get(i).getEtatPersonnage().getClass().getSimpleName().equals(EtatCombattant.class.getSimpleName()))
					if(equipe.get(indiceCombattantFaible).getPointDeVie() > equipe.get(i).getPointDeVie())
						indiceCombattantFaible = i;
			}
			
		}
		
		return equipe.get(indiceCombattantFaible);
		
	}

	public Combattant getProchainCombattant() {
		
		for(int i = 0; i < equipe.size(); i++){
			if(equipe.get(i).getAPasCombattu() && equipe.get(i).getEtatPersonnage().getClass().getSimpleName().equals(EtatCombattant.class.getSimpleName())){
				equipe.get(i).setAPasCombattu(false);
				return equipe.get(i);
			}
		}
		
		for(int i = 0; i < equipe.size(); i++){
			if(equipe.get(i).getEtatPersonnage().getClass().getSimpleName().equals(EtatCombattant.class.getSimpleName())){
				equipe.get(i).setAPasCombattu(true);
			}
		}
		
		return getProchainCombattant();
	}
		
}
