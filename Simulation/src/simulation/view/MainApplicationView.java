package simulation.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import simulation.factory.Simulation;

public class MainApplicationView extends JFrame {

	private static final long serialVersionUID = 270726019658012724L;

	private SimulationMenuBar simulationMenuBar;
	private Simulation simulationEnCours;

	public MainApplicationView() {
		guiFactory();
		setVisible(true);
	}

	private void guiFactory() {
		setTitle("Simulation");

		setResizable(true);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		setSize(d.width / 2, d.height / 2);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(new SimulationMenuBar(this));
	}

	public void addSimulationToView(JPanel simulationPanel) {
		Container c = getContentPane();
		c.removeAll();
		c.add(simulationPanel, BorderLayout.CENTER);
		revalidate();
		pack();

	}

	public SimulationMenuBar getSimulationMenuBar() {
		return simulationMenuBar;
	}

	public void setSimulationMenuBar(SimulationMenuBar simulationMenuBar) {
		this.simulationMenuBar = simulationMenuBar;
	}

	public Simulation getSimulationEnCours() {
		return simulationEnCours;
	}

	public void setSimulationEnCours(Simulation simulationEnCours) {
		this.simulationEnCours = simulationEnCours;
	}

}
