package simulation;

public class RunningSimulation {

	private static RunningSimulation INSTANCE = null;

	private RunningSimulation() {

	}

	public static RunningSimulation getInstance() {
		return INSTANCE;
	}

	// Appelé quand on demarre une simulation
	public static void startSimulation() {
		INSTANCE = new RunningSimulation();
	}

	// Appelé quand on fini de tourner une simulation
	public static void stopSimulation() {
		INSTANCE = null;
	}
}
