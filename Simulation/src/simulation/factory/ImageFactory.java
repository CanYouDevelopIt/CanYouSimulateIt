package simulation.factory;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import simulation.common.Images;

public class ImageFactory {

	public JLabel getImageLabel(String imageId, String imageIdOrigine) {

		JLabel picLabel = new JLabel();

		if (imageId == null) {
			return new JLabel(new ImageIcon(Images.mur));
		} else if (imageId.equals(" ")) {
			return new JLabel(new ImageIcon(Images.mer));
		} else if (imageId.equals("D")) {
			return new JLabel(new ImageIcon(Images.ile));
		} else if (imageId.equals("A")) {
			return new JLabel(new ImageIcon(Images.tresor));
		} else if (imageId.equals("G")) {
			return new JLabel(new ImageIcon(Images.algue));
		} else if (imageId.equals("F")) {
			return new JLabel(new ImageIcon(Images.shonenFlamme));
		} else if (imageId.equals("X")) {
			System.out.println("X");
			return new JLabel(new ImageIcon(Images.shonen));
		} else if (imageId.equals("Y")) {
			return new JLabel(new ImageIcon(Images.shonen));
		} else if (imageId.equals("S")) {
			if (imageIdOrigine.equals("G")) {
				return new JLabel(new ImageIcon(Images.pirateAlgue));
			} else {
				return new JLabel(new ImageIcon(Images.pirate));
			}
		}
		return picLabel;
	}
}
