package simulation.shonenfight;

import java.io.File;
import java.util.List;
import java.util.Random;

import simulation.Personnage;
import simulation.common.tools.XmlReader;
import simulation.etat.EtatInactif;
import simulation.factory.Simulation;
import simulation.view.MainApplicationView;

public class ShonenFightSimulation implements Simulation {

	public static final String fichierPersonnages = "resources/shonenfight/listePersonnages.xml";
	private List<Personnage> listePersonnages;
	private EquipeCombattant equipeA;
	private EquipeCombattant equipeB;
	private MainApplicationView mav;
	private ShonenFightMap sfm;
	
	@Override
	public void showSimulation() {

		File filePersonnages = new File(fichierPersonnages);
		listePersonnages = XmlReader.getPersonnages(filePersonnages);

		//créerEquipes();

		//lancerCombat();
		
		mav.addSimulationToView(sfm);
		
	}

	public void créerEquipes() {

		Random rand = new Random();

		equipeA = new EquipeCombattant();
		equipeB = new EquipeCombattant();

		while (equipeA.getSize() < 5) {
			int chiffreAlea = rand.nextInt(listePersonnages.size() - 1);
			Combattant p = (Combattant) listePersonnages.get(chiffreAlea);

			if (!equipeA.contientCombattant(p)) {
				equipeA.ajouterCombattant(p);
				System.out.print(p.getNomPersonnage() + "|");
			}
		}
		System.out.println("");
		System.out.println("VS");
		while (equipeB.getSize() < 5) {
			int chiffreAlea = rand.nextInt(listePersonnages.size() - 1);
			Combattant p = (Combattant) listePersonnages.get(chiffreAlea);

			if (!equipeB.contientCombattant(p) && !equipeA.contientCombattant(p)) {
				equipeB.ajouterCombattant(p);
				System.out.print(p.getNomPersonnage() + "|");
			}
		}

		System.out.println("");
	}

	public void lancerCombat() {

		System.out.println("FIGHT !!!");
		System.out.println("-----------------------");

		while (equipeA.getNbCombattantVivant() > 0 && equipeB.getNbCombattantVivant() > 0) {
			lancerAttaque(equipeA, equipeB);

			if (equipeB.getNbCombattantVivant() > 0) {
				lancerAttaque(equipeB, equipeA);
			}

			System.out.println("Fin de round");
			System.out.println("----------A------------");
			for (int i = 0; i < equipeA.getSize(); i++) {
				equipeA.getCombattant(i).afficherHP();
				;
			}
			System.out.println("----------B------------");
			for (int i = 0; i < equipeB.getSize(); i++) {
				equipeB.getCombattant(i).afficherHP();
				;
			}
			System.out.println("-----------------------");
		}

		if (equipeA.getNbCombattantVivant() > 0) {
			System.out.println("L'équipe A a gagné.");
		} else {
			System.out.println("L'équipe B a gagné.");
		}

	}

	public void lancerAttaque(EquipeCombattant equipeAttaquant, EquipeCombattant equipeDefenseur) {

		Combattant attaquant = equipeAttaquant.getProchainCombattant();
		Combattant defenseur = equipeDefenseur.getCombattantFaible();

		attaquant.AttaqueCombattant(defenseur);
		if (defenseur.getEtatPersonnage().getClass().getSimpleName().equals(EtatInactif.class.getSimpleName())) {
			equipeDefenseur.aPerduCombattant();
		}

	}

	public void prepareSimulation(MainApplicationView mainApplicationView) {
		
		mav = mainApplicationView;
		sfm = new ShonenFightMap(mav);
		
	}

}
