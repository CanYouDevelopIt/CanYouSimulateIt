package simulation.pirate;

import simulation.factory.Simulation;
import simulation.view.MainApplicationView;

public class PirateSimulation implements Simulation {
	private MainApplicationView mav;
	private PirateMap pm;

	public void prepareSimulation(MainApplicationView mainApplicationView) {
		mav = mainApplicationView;
		mav.setSimulationEnCours(this);
		pm = new PirateMap(mav);
	}

	@Override
	public void showSimulation() {
		mav.addSimulationToView(pm);
	}

}
