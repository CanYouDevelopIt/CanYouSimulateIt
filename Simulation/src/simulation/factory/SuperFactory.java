package simulation.factory;

import simulation.ant.AntSimulation;
import simulation.pirate.PirateSimulation;
import simulation.shonenfight.ShonenFightSimulation;

public class SuperFactory {
	public Simulation getSimulation(String simulationType) {

		switch (simulationType) {
		case "pirate":
			return new PirateSimulation();
		case "ant":
			return new AntSimulation();
		case "shonenfight":
			return new ShonenFightSimulation();
		default:
			throw new IllegalArgumentException("Invalid simulation.");
		}
	}
}
