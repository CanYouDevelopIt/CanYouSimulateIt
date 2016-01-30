package simulation.pirate;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulation.Personnage;
import simulation.RunningSimulation;
import simulation.common.graph.Dijkstra;
import simulation.common.graph.Edge;
import simulation.common.graph.Graph;
import simulation.common.graph.Node;
import simulation.common.tools.ClosingTools;
import simulation.common.tools.XmlReader;
import simulation.factory.ImageFactory;
import simulation.pirate.model.Pirate;
import simulation.view.MainApplicationView;

public class PirateMap extends JPanel implements ActionListener {

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

	private JTextField nbPirateSorti = new JTextField("10");

	private List<Integer> listNbPirateSorti = new ArrayList<Integer>();
	private List<Node> listNodeDepart = new ArrayList<Node>();
	private List<Node> listTresors = new ArrayList<Node>();

	private int nbTour = 0;
	private int nbDeplacement = 0;
	private int nbPirateEnCours = 0;
	private int nbPirateArrivees = 0;
	private int nbPirateSorties = 0;
	private int vitesseDeplacement = 100;
	private boolean isNotDisabled = true;

	public PirateMap(MainApplicationView mainAppView) {
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
					jpNord.add(imageFactory.getImageLabel(null, null,
							mainApplicationView.getClass().getSimpleName()));
				} else {
					jpNord.add(imageFactory.getImageLabel(node.getId(), node
							.getIdOrigine(), mainApplicationView.getClass()
							.getSimpleName()));
				}
			}
		}
	}

	public void initJpSudComponents() {
		jpSud.setBackground(BG_COLOR);
		JLabel labelTour = new JLabel("Tour: " + nbTour);
		JLabel labelDeplacement = new JLabel("Déplacements: " + nbDeplacement);
		JLabel labelNbPirateEnCours = new JLabel("Pirates en déplacement: "
				+ nbPirateEnCours);
		JLabel labelNbPirateArrive = new JLabel("Pirates arrivées: "
				+ nbPirateArrivees);
		buttonLancer = new JButton("Lancer");
		buttonLoadFile = new JButton("Load File");
		buttonLancer.addActionListener(this);
		buttonLoadFile.addActionListener(this);

		jpSud.add(buttonLoadFile);
		jpSud.add(labelTour);
		jpSud.add(labelDeplacement);
		jpSud.add(labelNbPirateEnCours);
		jpSud.add(labelNbPirateArrive);
		JLabel labelPorte = new JLabel(" Pirates partant de l'Ile : ");
		jpSud.add(labelPorte);
		jpSud.add(nbPirateSorti);

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
			System.out
					.println("Erreur lors de l'initialisation de la taille du map: "
							+ e.getMessage());
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
					if (tab2[i].equals(" ") || tab2[i].equals("D")
							|| tab2[i].equals("A") || tab2[i].equals("G")) {
						nodes[cptligne][i - 1] = new Node(tab2[i], i - 1,
								cptligne);
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

					if (nodes[i][j].getId().equals("D")) {
						listNodeDepart.add(nodes[i][j]);
					} else if (nodes[i][j].getId().equals("A")) {
						listTresors.add(nodes[i][j]);
					}

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

	public void deplacerPirate() {

		// Démarrage de la simulation
		RunningSimulation.startSimulation();
		// -- -- -- -- -- -- -- -- -- -- --

		List<Personnage> listePersonnage = new ArrayList<Personnage>();

		for (int i = 0; i < listNbPirateSorti.size(); i++) {
			for (int j = 0; j < listNbPirateSorti.get(i); j++) {
				Node positionDeDepart = listNodeDepart.get(i);
				Personnage personnage = new Pirate(nbPirateSorties + 1);
				personnage.setPosition(positionDeDepart);
				listePersonnage.add(personnage);
				nbPirateSorties++;
			}
		}

		System.out.println("Nb pirate envoyees : " + nbPirateSorties);
		while (nbPirateSorties != nbPirateArrivees) {

			for (int i = 0; i < listePersonnage.size(); i++) {

				// Recherche du chemin le plus court vers un ou plusieurs
				// tresors

				Personnage personnage = listePersonnage.get(i);
				List<Node> cheminPlusCourt = new ArrayList<Node>();
				int distance = 0;
				for (int x = 0; x < listTresors.size(); x++) {
					Node nodeDepart = graph.getNode(personnage.getPosition()
							.getX(), personnage.getPosition().getY());
					Node nodeArriver = graph.getNode(listTresors.get(x).getX(),
							listTresors.get(x).getY());
					List<Node> nodeDejaPasser = null;

					Dijkstra d = new Dijkstra(graph, nodeDepart, nodeArriver,
							nodeDejaPasser);

					List<Node> nouveauCheminCourt = d
							.cheminPlusCourtOptimiser();
					int nouveauCheminCourtDistance = nouveauCheminCourt.size();
					if (distance == 0) {
						cheminPlusCourt = nouveauCheminCourt;
						distance = cheminPlusCourt.size();
					} else if (distance > nouveauCheminCourtDistance) {
						cheminPlusCourt = nouveauCheminCourt;
						distance = cheminPlusCourt.size();
					} else {
						// on garde la précedente distance
					}
				}

				if (cheminPlusCourt.size() > 1) {

					try {
						if (cheminPlusCourt.get(0).getIdOrigine().equals("G")) {
							Thread.sleep(vitesseDeplacement * 2);
						} else {
							Thread.sleep(vitesseDeplacement);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// Placer graphiquement le pirate
					graph.getNode(cheminPlusCourt.get(0).getX(),
							cheminPlusCourt.get(0).getY()).setId(
							cheminPlusCourt.get(0).getIdOrigine());
					graph.getNode(cheminPlusCourt.get(1).getX(),
							cheminPlusCourt.get(1).getY()).setId("S");

					// Changer la postion du pirate
					personnage.setPosition(cheminPlusCourt.get(1));
					// evite au pirate de faire des aller-retour sur la même
					// case s'il y a trop de monde sur la map
					// pirate.ajouterNodeDejaPasse(cheminPlusCourt.get(0));
					nbDeplacement++;

					// Si un pirate commence à se déplacer
					for (int j = 0; j < listNodeDepart.size(); j++) {
						if (cheminPlusCourt.get(0)
								.equals(listNodeDepart.get(j))
								&& !cheminPlusCourt.get(1).equals(
										listNodeDepart.get(j))) {
							nbPirateEnCours++;
						}
					}

					// Si un pirate est arrivée sur un trésor de la map
					for (int x = 0; x < listTresors.size(); x++) {
						if (personnage.getPosition().equals(listTresors.get(x))) {
							graph.getNode(personnage.getPosition().getX(),
									personnage.getPosition().getY()).setId(
									personnage.getPosition().getIdOrigine());

							nbPirateArrivees++;
							nbPirateEnCours--;
						}
					}

					// Actualiser la map
					this.actualiserMap();
					this.repaintFrame();
				}

				if (listePersonnage.size() == 1) {
					nbTour++;
				} else if (i == listePersonnage.size() - 1) {
					nbTour++;
				}

			}
		}

		isNotDisabled = true;
		buttonLancer.setEnabled(isNotDisabled);
		buttonLoadFile.setEnabled(isNotDisabled);

		// Arret de la simulation
		RunningSimulation.stopSimulation();
		// -- -- -- -- -- -- -- -- -- -- --
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(buttonLancer)) {
			isNotDisabled = false;
			buttonLancer.setEnabled(isNotDisabled);
			buttonLoadFile.setEnabled(isNotDisabled);
			nbTour = 0;
			nbDeplacement = 0;
			nbPirateEnCours = 0;
			nbPirateArrivees = 0;
			nbPirateSorties = 0;
			listNbPirateSorti.clear();

			new Thread(new Runnable() {
				public void run() {
					listNbPirateSorti.add(Integer.parseInt(nbPirateSorti
							.getText()));
					deplacerPirate();
				}
			}).start();
		}

		if (e.getSource().equals(buttonLoadFile)) {
			initMap();
		}
	}

	private void initMap() {

		listNbPirateSorti.clear();
		listNodeDepart.clear();
		listTresors.clear();

		nbTour = 0;
		nbDeplacement = 0;
		nbPirateEnCours = 0;
		nbPirateArrivees = 0;
		nbPirateSorties = 0;
		vitesseDeplacement = 100;

		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("resources/"));
		fc.setDialogTitle("Choisir Fichier de Simulation");
		fc.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

			try {
				loadFichier(fc.getSelectedFile().getAbsolutePath());
			} catch (IOException e) {
			}

			setSize(nbcol * 26, nbligne * 26 + 80);
			mainApplicationView.setPreferredSize(new Dimension(getWidth(),
					getHeight() + 20));

			actualiserMap();
		}
	}

}
