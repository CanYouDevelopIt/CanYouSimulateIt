package simulation.shonenfight;

import java.util.List;

import javax.swing.AbstractListModel;

public class CombattantListModel extends AbstractListModel<Combattant> {

	private static final long serialVersionUID = 1L;
	private List<Combattant> combattants;

	public CombattantListModel(List<Combattant> _combattants) {
		combattants = _combattants;
	}

	@Override
	public Combattant getElementAt(int i) {
		return combattants.get(i);
	}

	@Override
	public int getSize() {
		return combattants.size();
	}

	public void addCombattant(Combattant c) {
		combattants.add(c);
		this.fireContentsChanged(c, 0, combattants.size());
	}

	public void clear() {
		combattants.clear();
	}

}
