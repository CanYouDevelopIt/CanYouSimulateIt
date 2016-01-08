package simulation.shonenfight;

import simulation.factory.Simulation;
import simulation.view.MainApplicationView;

public class ShonenFightSimulation implements Simulation {

	private MainApplicationView mav;
	private ShonenFightMap sfm;

	@Override
	public void showSimulation() {
		mav.addSimulationToView(sfm);
	}

	public void prepareSimulation(MainApplicationView mainApplicationView) {

		mav = mainApplicationView;
		sfm = new ShonenFightMap(mav);

	}

}
