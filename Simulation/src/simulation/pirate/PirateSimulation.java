package simulation.pirate;

import simulation.factory.Simulation;
import simulation.view.MainApplicationView;

public class PirateSimulation implements Simulation {
	private MainApplicationView mav;
	private PirateMap pm;

	public void prepareSimulation(MainApplicationView mainApplicationView) {
		mav = mainApplicationView;
		pm = new PirateMap(mav);
	}

	@Override
	public void launchSimulation() {
		mav.add(pm);
	}

}
