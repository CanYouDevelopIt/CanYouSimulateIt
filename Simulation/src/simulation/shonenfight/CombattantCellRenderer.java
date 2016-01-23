package simulation.shonenfight;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class CombattantCellRenderer extends JLabel implements
		ListCellRenderer<Combattant> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CombattantCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends Combattant> list, Combattant c, int index,
			boolean isSelected, boolean cellHasFocus) {

		setText(c.getNomPersonnage() + " : " + c.getPointDeVie() + " HP.");

		return this;
	}
}
