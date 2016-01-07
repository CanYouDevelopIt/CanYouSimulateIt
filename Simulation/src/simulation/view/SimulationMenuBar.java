package simulation.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import simulation.pirate.PirateSimulation;
import simulation.shonenfight.ShonenFightSimulation;

public class SimulationMenuBar extends JMenuBar implements MouseListener {

	private static final long serialVersionUID = -5077417245990897760L;

	private MainApplicationView mainApplicationView;

	private JMenu pirateSimulation = new JMenu("Pirate");

	private JMenu shonenFightSimulation = new JMenu("Shonen Fight");

	private JMenu antSimulation = new JMenu("Ant");

	public SimulationMenuBar(MainApplicationView appView) {
		setMainApplicationView(appView);

		pirateSimulation.addMouseListener(this);
		shonenFightSimulation.addMouseListener(this);
		antSimulation.addMouseListener(this);

		add(pirateSimulation);
		add(shonenFightSimulation);
		add(antSimulation);
	}

	public MainApplicationView getMainApplicationView() {
		return mainApplicationView;
	}

	public void setMainApplicationView(MainApplicationView mainApplicationView) {
		this.mainApplicationView = mainApplicationView;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(pirateSimulation)) {
			// modifie la fenetre principale avec la simulation de pirate
			System.out.println("Pirate");
			PirateSimulation ps = new PirateSimulation();
			ps.prepareSimulation(mainApplicationView);
			ps.launchSimulation();
		}

		if (e.getSource().equals(shonenFightSimulation)) {
			// modifie la fenetre principale avec la simulation de shonen Fight
			System.out.println("Shonen Fight");
			ShonenFightSimulation sh = new ShonenFightSimulation();
			sh.prepareSimulation(mainApplicationView);
			sh.launchSimulation();
		}

		if (e.getSource().equals(antSimulation)) {
			// modifie la fenetre principale avec la simulation des fourmis
			System.out.println("Ant Simulation");
			JPanel ant = new JPanel();
			ant.setBackground(Color.darkGray);
			mainApplicationView.addSimulation(ant);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
