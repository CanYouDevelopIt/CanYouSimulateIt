package simulation.ant;

import simulation.factory.Simulation;
import simulation.view.MainApplicationView;

public class AntSimulation implements Simulation {
	private MainApplicationView mav;
	private AntMap am;

	public void prepareSimulation(MainApplicationView mainApplicationView) {
		mav = mainApplicationView;
		mav.setSimulationEnCours(this);
		am = new AntMap(mav);
	}

	@Override
	public void showSimulation() {
		mav.addSimulationToView(am);
	}

}
