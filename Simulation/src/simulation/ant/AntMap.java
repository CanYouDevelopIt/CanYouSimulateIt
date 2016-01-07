package simulation.ant;

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

import simulation.ant.model.Ant;
import simulation.common.graph.Dijkstra;
import simulation.common.graph.Edge;
import simulation.common.graph.Graph;
import simulation.common.graph.Node;
import simulation.common.tools.ClosingTools;
import simulation.common.tools.XmlReader;
import simulation.factory.ImageFactory;
import simulation.view.MainApplicationView;

public class AntMap extends JPanel implements ActionListener {

	private static final Color BG_COLOR = new Color(98, 165, 199);

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

	private List<JTextField> listFieldNbAntSorti = new ArrayList<JTextField>();
	private List<Integer> listNbAntSorti = new ArrayList<Integer>();
	private List<Node> listNodeDepart = new ArrayList<Node>();
	private List<Node> listPommes = new ArrayList<Node>();

	private JTextField fieldVitesse;

	private int nbPorte = 0;
	private int nbTour = 0;
	private int nbDeplacement = 0;
	private int nbAntEnCours = 0;
	private int nbAntArrivees = 0;
	private int nbAntSorties = 0;
	private int vitesseDeplacement = 100;
	private boolean isNotDisabled = true;

	public AntMap(MainApplicationView mainAppView) {
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
		JLabel labelTour = new JLabel("Tour: " + nbTour);
		JLabel labelDeplacement = new JLabel("Déplacements: " + nbDeplacement);
		JLabel labelNbAntEnCours = new JLabel("Ant en déplacement: " + nbAntEnCours);
		JLabel labelNbAntArrive = new JLabel("Ant arrivées: " + nbAntArrivees);
		JLabel labelVitesse = new JLabel("Vitesse: ");
		fieldVitesse = new JTextField();
		fieldVitesse.setPreferredSize(new Dimension(30, 20));
		fieldVitesse.setText(Integer.toString(vitesseDeplacement));
		buttonLancer = new JButton("Lancer");
		buttonLoadFile = new JButton("Load File");
		buttonLancer.addActionListener(this);
		buttonLoadFile.addActionListener(this);

		jpSud.add(buttonLoadFile);
		jpSud.add(labelTour);
		jpSud.add(labelDeplacement);
		jpSud.add(labelNbAntEnCours);
		jpSud.add(labelNbAntArrive);
		for (int i = 0; i < nbPorte; i++) {
			JLabel labelPorte = new JLabel("Porte " + (i + 1) + " :");
			jpSud.add(labelPorte);
			jpSud.add(listFieldNbAntSorti.get(i));
		}
		jpSud.add(labelVitesse);
		jpSud.add(fieldVitesse);
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
					if (tab2[i].equals(" ") || tab2[i].equals("D") || tab2[i].equals("A") || tab2[i].equals("G")) {
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

					if (nodes[i][j].getId().equals("D")) {
						listNodeDepart.add(nodes[i][j]);
						++nbPorte;
					} else if (nodes[i][j].getId().equals("A")) {
						listPommes.add(nodes[i][j]);
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

	public void deplacerAnt() {

		List<Ant> listeAnt = new ArrayList<Ant>();

		for (int i = 0; i < listNbAntSorti.size(); i++) {
			for (int j = 0; j < listNbAntSorti.get(i); j++) {
				Node nodeAnt = listNodeDepart.get(i);
				listeAnt.add(new Ant(nbAntSorties, nodeAnt, listPommes));
				nbAntSorties++;
			}
		}

		System.out.println("Nb ant envoyees : " + nbAntSorties);

		vitesseDeplacement = Integer.parseInt(fieldVitesse.getText());
		System.out.println("Vitesse : " + vitesseDeplacement);

		while (nbAntSorties != nbAntArrivees) {

			for (int i = 0; i < listeAnt.size(); i++) {

				// Recherche du chemin le plus court vers un ou plusieurs
				// tresors
				Ant ant = listeAnt.get(i);
				List<Node> cheminPlusCourt = new ArrayList<Node>();
				int distance = 0;
				for (int x = 0; x < listPommes.size(); x++) {
					Node nodeDepart = graph.getNode(ant.getNodeAnt().getX(), ant.getNodeAnt().getY());
					Node nodeArriver = graph.getNode(listPommes.get(x).getX(), listPommes.get(x).getY());
					List<Node> nodeDejaPasser = ant.getNodesDejaPasses();

					Dijkstra d = new Dijkstra(graph, nodeDepart, nodeArriver, nodeDejaPasser);

					List<Node> nouveauCheminCourt = d.cheminPlusCourtOptimiser();
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
					graph.getNode(cheminPlusCourt.get(0).getX(), cheminPlusCourt.get(0).getY())
							.setId(cheminPlusCourt.get(0).getIdOrigine());
					graph.getNode(cheminPlusCourt.get(1).getX(), cheminPlusCourt.get(1).getY()).setId("S");

					// Changer la postion du pirate
					ant.setNodeAnt(cheminPlusCourt.get(1));
					// evite au pirate de faire des aller-retour sur la même
					// case s'il y a trop de monde sur la map
					// pirate.ajouterNodeDejaPasse(cheminPlusCourt.get(0));
					nbDeplacement++;

					// Si un pirate commence à se déplacer
					for (int j = 0; j < listNodeDepart.size(); j++) {
						if (cheminPlusCourt.get(0).equals(listNodeDepart.get(j))
								&& !cheminPlusCourt.get(1).equals(listNodeDepart.get(j))) {
							nbAntEnCours++;
						}
					}

					// Si un pirate est arrivée sur un trésor de la map
					for (int x = 0; x < listPommes.size(); x++) {
						if (ant.getNodeAnt().equals(listPommes.get(x))) {
							graph.getNode(ant.getNodeAnt().getX(), ant.getNodeAnt().getY())
									.setId(ant.getNodeAnt().getIdOrigine());

							nbAntArrivees++;
							nbAntEnCours--;
						}
					}

					// Actualiser la map
					this.actualiserMap();
					this.repaintFrame();
				}

				if (listeAnt.size() == 1) {
					nbTour++;
				} else if (i == listeAnt.size() - 1) {
					nbTour++;
				}

			}
		}

		isNotDisabled = true;
		buttonLancer.setEnabled(isNotDisabled);
		buttonLoadFile.setEnabled(isNotDisabled);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(buttonLancer)) {
			isNotDisabled = false;
			buttonLancer.setEnabled(isNotDisabled);
			buttonLoadFile.setEnabled(isNotDisabled);
			nbTour = 0;
			nbDeplacement = 0;
			nbAntEnCours = 0;
			nbAntArrivees = 0;
			nbAntSorties = 0;
			listNbAntSorti.clear();

			new Thread(new Runnable() {
				public void run() {
					for (int i = 0; i < nbPorte; i++) {
						listNbAntSorti.add(Integer.parseInt(listFieldNbAntSorti.get(i).getText()));
					}
					deplacerAnt();
				}
			}).start();
		}

		if (e.getSource().equals(buttonLoadFile)) {
			initMap();
		}
	}

	private void initMap() {

		listFieldNbAntSorti.clear();
		listNbAntSorti.clear();
		listNodeDepart.clear();
		listPommes.clear();

		fieldVitesse.setText("100");

		nbPorte = 0;
		nbTour = 0;
		nbDeplacement = 0;
		nbAntEnCours = 0;
		nbAntArrivees = 0;
		nbAntSorties = 0;
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
			mainApplicationView.setPreferredSize(new Dimension(getWidth(), getHeight() + 20));

			for (int i = 0; i < nbPorte; i++) {
				JTextField fieldNbAntSorti = new JTextField();
				fieldNbAntSorti.setPreferredSize(new Dimension(30, 20));
				fieldNbAntSorti.setText(Integer.toString(1));
				listFieldNbAntSorti.add(fieldNbAntSorti);
			}

			actualiserMap();
		}
	}

}