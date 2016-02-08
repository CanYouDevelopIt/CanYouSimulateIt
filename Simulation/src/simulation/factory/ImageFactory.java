package simulation.factory;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import simulation.ant.AntSimulation;
import simulation.common.Images;
import simulation.pirate.PirateSimulation;
import simulation.shonenfight.ShonenFightSimulation;

public class ImageFactory {

	public JLabel getImageLabel(String imageId, String imageIdOrigine, String simulation) {

		JLabel picLabel = new JLabel();

		if (imageId == null) {
			if (simulation.equals(ShonenFightSimulation.class.getSimpleName()))
				return new JLabel(new ImageIcon(Images.shonenMur));
			else if (simulation.equals(AntSimulation.class.getSimpleName()))
				return new JLabel(new ImageIcon(Images.buisson));
			else
				return new JLabel(new ImageIcon(Images.mur));
		} else if (imageId.equals(" ")) {
			if (simulation.equals(ShonenFightSimulation.class.getSimpleName()))
				return new JLabel(new ImageIcon(Images.shonenSol));
			else if (simulation.equals(AntSimulation.class.getSimpleName()))
				return new JLabel(new ImageIcon(Images.antSol));
			else
				return new JLabel(new ImageIcon(Images.mer));
		} else if (imageId.equals("D")) {
			if (simulation.equals(AntSimulation.class.getSimpleName()))
				return new JLabel(new ImageIcon(Images.anthill));
			else
				return new JLabel(new ImageIcon(Images.ile));
		} else if (imageId.equals("A") && simulation.equals(PirateSimulation.class.getSimpleName())) {
			return new JLabel(new ImageIcon(Images.tresor));
		} else if (imageId.equals("G") && simulation.equals(PirateSimulation.class.getSimpleName())) {
			return new JLabel(new ImageIcon(Images.algue));
		} else if (imageId.equals("F")) {
			return new JLabel(new ImageIcon(Images.shonenFlamme));
		} else if (imageId.equals("X")) {
			return new JLabel(new ImageIcon(Images.shonen));
		} else if (imageId.equals("Y")) {
			return new JLabel(new ImageIcon(Images.shonen));
		} else if (imageId.equals("M")) {
			return new JLabel(new ImageIcon(Images.shonenMort));
		} else if (imageId.equals("S")) {
			if (simulation.equals(AntSimulation.class.getSimpleName()))
				return new JLabel(new ImageIcon(Images.ant));
			else {
				if (imageIdOrigine.equals("G")) {
					return new JLabel(new ImageIcon(Images.pirateAlgue));
				} else {
					return new JLabel(new ImageIcon(Images.pirate));
				}
			}
		} else if (imageId.equals("B")) {
			return new JLabel(new ImageIcon(Images.banane));
		} else if (imageId.equals("P")) {
			return new JLabel(new ImageIcon(Images.pomme));
		}
		return picLabel;
	}

	public JLabel getImageCombattant(String nomPersonnage) {
		File imageFile = new File(Images.shonenPersonnage + nomPersonnage + ".png");
		if (imageFile.exists())
			return new JLabel(new ImageIcon(Images.shonenPersonnage + nomPersonnage + ".png"));
		else
			return new JLabel(new ImageIcon(Images.shonen));

	}
}
