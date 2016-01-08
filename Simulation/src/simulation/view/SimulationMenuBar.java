package simulation.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import simulation.RunningSimulation;
import simulation.ant.AntSimulation;
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
			// Check si une simulation est entrain de tourner avant d'afficher
			// la simulation
			if (RunningSimulation.getInstance() == null) {
				PirateSimulation ps = new PirateSimulation();
				ps.prepareSimulation(mainApplicationView);
				ps.showSimulation();
			} else {
				System.out.println("Une simulation est en cours.");
			}
		}

		if (e.getSource().equals(shonenFightSimulation)) {
			System.out.println("Shonen Fight");

			if (RunningSimulation.getInstance() == null) {

				ShonenFightSimulation sh = new ShonenFightSimulation();
				sh.prepareSimulation(mainApplicationView);
				sh.showSimulation();
			} else {
				System.out.println("Une simulation est en cours.");
			}
		}

		if (e.getSource().equals(antSimulation)) {
			System.out.println("Ant Simulation");

			if (RunningSimulation.getInstance() == null) {

				AntSimulation as = new AntSimulation();
				as.prepareSimulation(mainApplicationView);
				as.showSimulation();
			} else {
				System.out.println("Une simulation est en cours.");
			}
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
