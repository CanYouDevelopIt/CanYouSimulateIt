package simulation.view;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JPanel;

public abstract class JPanelSimulation extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	public abstract void buildMap();

	public abstract void initJpNordComponents();

	public abstract void initJpSudComponents();

	public abstract void initMap();

	public abstract void loadFichier(String f) throws IOException;

	public abstract void actualiserMap();

	public abstract void initTailleMap();

	public abstract void initParcellesMap();

	public abstract void initDistanceEntreParcelle();

	public abstract BufferedReader getBufferReader() throws Exception;

	public abstract void repaintFrame();

}
