package simulation;

import javax.swing.SwingUtilities;

import simulation.view.MainApplicationView;

public class Application {

	public static void main(String argv[]) {

		Runnable r = new Runnable() {
			public void run() {
				new MainApplicationView();
			}
		};
		SwingUtilities.invokeLater(r);
	}

}
