package simulation.shonenfight;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulation.common.graph.Edge;
import simulation.common.graph.Graph;
import simulation.common.graph.Node;
import simulation.common.tools.ClosingTools;
import simulation.common.tools.XmlReader;
import simulation.factory.ImageFactory;
import simulation.view.MainApplicationView;

public class ShonenFightMap extends JPanel implements ActionListener {

	private static final Color BG_COLOR = new Color(98, 165, 199);

	private static final long serialVersionUID = 446565083035345353L;

	MainApplicationView mainApplicationView;

	private File fichier;
	private Graph graph;
	private Node[][] nodes;
	private int nbligne;
	private int nbcol;

	private JPanel jpNord;
	private JPanel jpSud;
	private JButton buttonLancer;
	private JButton buttonLoadFile;

	private int vitesseDeplacement = 100;
	private boolean isNotDisabled = true;

	public ShonenFightMap(MainApplicationView mainAppView) {
		mainApplicationView = mainAppView;
		buildMap();
	}

	public void loadFichier(String f) throws IOException {
		fichier = new File(f);
		graph = new Graph();

		if (fichier.exists()) {
			initTailleMap();
			initParcellesMap();
			initDistanceEntreParcelle();

		}
	}

	public void buildMap() {

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		if (nbligne == 0 & nbcol == 0) {
			setSize(d.width / 2, d.height / 2);
		} else {
			setSize(nbcol * 26, nbligne * 26 + 80);
		}

		jpNord = new JPanel();
		jpNord.setBackground(BG_COLOR);

		jpSud = new JPanel();

		initJpSudComponents();

		add(jpNord, BorderLayout.NORTH);
		add(jpSud, BorderLayout.SOUTH);
		setBackground(BG_COLOR);
	}

	public void actualiserMap() {

		removeAll();

		jpNord = new JPanel();
		initJpNordComponents();

		jpSud = new JPanel();
		initJpSudComponents();

		add(jpNord, BorderLayout.NORTH);
		add(jpSud, BorderLayout.SOUTH);
		setBackground(BG_COLOR);

		buttonLancer.setEnabled(isNotDisabled);
		buttonLoadFile.setEnabled(isNotDisabled);

		mainApplicationView.pack();
	}

	public void initJpNordComponents() {
		jpNord.setBackground(BG_COLOR);
		GridLayout g = new GridLayout(nbligne, nbcol);
		jpNord.setLayout(g);
		ImageFactory imageFactory = new ImageFactory();
		for (int i = 0; i < nbligne; i++) {
			for (int j = 0; j < nbcol; j++) {
				Node node = graph.getNode(j, i);
				if (node == null) {
					jpNord.add(imageFactory.getImageLabel(null, null));
				} else {
					jpNord.add(imageFactory.getImageLabel(node.getId(), node.getIdOrigine()));
				}
			}
		}
	}

	public void initJpSudComponents() {
		jpSud.setBackground(BG_COLOR);
		buttonLancer = new JButton("Lancer");
		buttonLoadFile = new JButton("Load File");
		buttonLancer.addActionListener(this);
		buttonLoadFile.addActionListener(this);

		jpSud.add(buttonLoadFile);
		jpSud.add(buttonLancer);
	}

	private void initTailleMap() {
		BufferedReader br = null;
		try {
			br = getBufferReader();
			String ligne;
			nbligne = 0;
			nbcol = 0;
			while ((ligne = br.readLine()) != null) {
				nbligne++;
				String[] tab = ligne.split("");
				nbcol = tab.length - 1;
			}
		} catch (Exception e) {
			System.out.println("Erreur lors de l'initialisation de la taille du map: " + e.getMessage());
		} finally {
			ClosingTools.closeQuietly(br);
		}
		nodes = new Node[nbligne][nbcol];
	}

	private void initParcellesMap() {
		BufferedReader br = null;
		try {
			br = getBufferReader();
			String ligne2;
			int cptligne = 0;
			while ((ligne2 = br.readLine()) != null) {
				String[] tab2 = ligne2.split("");
				for (int i = 1; i < tab2.length; i++) {
					if (tab2[i].equals(" ") || tab2[i].equals("D") || tab2[i].equals("A") || tab2[i].equals("G")
							|| tab2[i].equals("X") || tab2[i].equals("Y") || tab2[i].equals("S")) {
						nodes[cptligne][i - 1] = new Node(tab2[i], i - 1, cptligne);
					}
				}
				cptligne++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ClosingTools.closeQuietly(br);
		}
	}

	private BufferedReader getBufferReader() throws Exception {
		BufferedReader br;
		String map = XmlReader.getMap(fichier);
		InputStream is = new ByteArrayInputStream(map.getBytes());
		br = new BufferedReader(new InputStreamReader(is));
		return br;
	}

	private void initDistanceEntreParcelle() {
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				if (nodes[i][j] != null) {
					graph.registerNode(nodes[i][j]);

					if (nodes[i][j].getId().equals("G")) {
						if (nodes[i][j + 1] != null) {
							new Edge(nodes[i][j], nodes[i][j + 1], 2);
						}
						if (nodes[i + 1][j] != null) {
							new Edge(nodes[i][j], nodes[i + 1][j], 2);
						}
					} else {
						nodes[i][j].setMinDistance(1);
						if (nodes[i][j + 1] != null) {
							new Edge(nodes[i][j], nodes[i][j + 1], 1);
						}
						if (nodes[i + 1][j] != null) {
							new Edge(nodes[i][j], nodes[i + 1][j], 1);
						}
					}

				}
			}
		}
	}

	public void repaintFrame() {
		this.validate();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(buttonLancer)) {
			isNotDisabled = false;
			buttonLancer.setEnabled(isNotDisabled);
			buttonLoadFile.setEnabled(isNotDisabled);

		}

		if (e.getSource().equals(buttonLoadFile)) {
			initMap();
		}
	}

	private void initMap() {

		vitesseDeplacement = 100;

		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("resources/"));
		fc.setDialogTitle("Choisir fichier de Simulation");
		fc.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

			try {
				loadFichier(fc.getSelectedFile().getAbsolutePath());
			} catch (IOException e) {
			}

			setSize(nbcol * 26, nbligne * 26 + 80);
			mainApplicationView.setPreferredSize(new Dimension(getWidth(), getHeight() + 20));

			actualiserMap();
		}
	}

}
