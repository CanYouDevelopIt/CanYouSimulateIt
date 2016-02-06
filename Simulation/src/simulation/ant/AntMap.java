package simulation.ant;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
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
import simulation.view.JPanelSimulation;
import simulation.view.MainApplicationView;

public class AntMap extends JPanelSimulation {

	private static final long serialVersionUID = 224516038879363496L;

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

	private JTextField nbAntSorti = new JTextField();

	private List<Integer> listNbAntSorti = new ArrayList<Integer>();
	private List<Node> listNodeDepart = new ArrayList<Node>();
	private List<Node> listObjets = new ArrayList<Node>();

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
			nbAntSorti.setText(Integer.toString(listObjets.size()));
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
							mainApplicationView.getSimulationEnCours().getClass().getSimpleName()));
				} else {
					jpNord.add(imageFactory.getImageLabel(node.getId(), node.getIdOrigine(), mainApplicationView.getSimulationEnCours().getClass().getSimpleName()));
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

	public void initTailleMap() {
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

	public void initParcellesMap() {
		BufferedReader br = null;
		try {
			br = getBufferReader();
			String ligne2;
			int cptligne = 0;
			while ((ligne2 = br.readLine()) != null) {
				String[] tab2 = ligne2.split("");
				for (int i = 1; i < tab2.length; i++) {
					if (tab2[i].equals(" ") || tab2[i].equals("D")) {
						nodes[cptligne][i - 1] = new Node(tab2[i], i - 1, cptligne);
					} else if (tab2[i].equals("B")) {
						nodes[cptligne][i - 1] = new Node(tab2[i], i - 1, cptligne, 2);
					} else if (tab2[i].equals("P")) {
						nodes[cptligne][i - 1] = new Node(tab2[i], i - 1, cptligne, 3);
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

	public BufferedReader getBufferReader() throws Exception {
		BufferedReader br;
		String map = XmlReader.getMap(fichier);
		InputStream is = new ByteArrayInputStream(map.getBytes());
		br = new BufferedReader(new InputStreamReader(is));
		return br;
	}

	public void initDistanceEntreParcelle() {
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				if (nodes[i][j] != null) {
					graph.registerNode(nodes[i][j]);

					if (nodes[i][j].getId().equals("D")) {
						listNodeDepart.add(nodes[i][j]);
					} else if (nodes[i][j].getId().equals("B") || nodes[i][j].getId().equals("P")) {
						listObjets.add(nodes[i][j]);
					}

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

	public void repaintFrame() {
		this.validate();
		this.repaint();
	}

	public void deplacerAnt() {

		List<Ant> listeAnt = new ArrayList<Ant>();
		for (int i = 0; i < listNbAntSorti.size(); i++) {
			for (int j = 0; j < listNbAntSorti.get(i); j++) {
				Node positionDepart = listNodeDepart.get(i);
				Ant ant = new Ant(nbAntSorties + 1);
				ant.setPosition(positionDepart);
				listeAnt.add(ant);
				nbAntSorties++;
			}
		}

		System.out.println("Nb ant envoyees : " + nbAntSorties);
		while (nbAntSorties != nbAntArrivees) {
			for (int i = 0; i < listeAnt.size(); i++) {

				// Recherche du chemin le plus court vers un ou plusieurs
				Ant ant = listeAnt.get(i);
				List<Node> cheminPlusCourt = new ArrayList<Node>();
				if (!ant.isTrouve()) {
					if (!ant.isChoix()) {
						for (int x = 0; x < listObjets.size(); x++) {
							if ((listObjets.get(x).getAnt() == ant.getId() || listObjets.get(x).getAnt() == 0) && !ant.isChoix()) {
								Node nodeDepart = graph.getNode(ant.getPosition().getX(), ant.getPosition().getY());
								Node nodeArriver = graph.getNode(listObjets.get(x).getX(), listObjets.get(x).getY());

								Dijkstra d = new Dijkstra(graph, nodeDepart,nodeArriver, null);

								cheminPlusCourt = d.cheminPlusCourtOptimiser();
								listObjets.get(x).setAnt(ant.getId());
								ant.setChoix(true);
								ant.setNodeObj(listObjets.get(x));
							}
						}
					} else {
						for (int x = 0; x < listObjets.size(); x++) {
							if (listObjets.get(x).getAnt() == ant.getId()) {
								Node nodeDepart = graph.getNode(ant.getPosition().getX(), ant.getPosition().getY());
								Node nodeArriver = graph.getNode(listObjets.get(x).getX(), listObjets.get(x).getY());

								Dijkstra d = new Dijkstra(graph, nodeDepart, nodeArriver, null);

								cheminPlusCourt = d.cheminPlusCourtOptimiser();
							}
						}
					}
				} else {
					System.out.println("else");
					Node nodeDepart = graph.getNode(ant.getPosition().getX(), ant.getPosition().getY());
					Node nodeArriver = graph.getNode(listNodeDepart.get(0).getX(), listNodeDepart.get(0).getY());

					Dijkstra d = new Dijkstra(graph, nodeDepart, nodeArriver, null);

					cheminPlusCourt = d.cheminRetourOptimiser();
				}

				if (cheminPlusCourt.size() > 1) {

					try {
						Thread.sleep(vitesseDeplacement);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// Placer graphiquement le pirate
					graph.getNode(cheminPlusCourt.get(0).getX(), cheminPlusCourt.get(0).getY()).setId(cheminPlusCourt.get(0).getIdOrigine());
					graph.getNode(cheminPlusCourt.get(1).getX(), cheminPlusCourt.get(1).getY()).setId("S");

					// Changer la postion du pirate
					ant.setPosition(cheminPlusCourt.get(1));
					// evite au pirate de faire des aller-retour sur la même
					// case s'il y a trop de monde sur la map
					// pirate.ajouterNodeDejaPasse(cheminPlusCourt.get(0));
					nbDeplacement++;

					// Si un pirate commence à se déplacer
					for (int j = 0; j < listNodeDepart.size(); j++) {
						if (cheminPlusCourt.get(0).equals(listNodeDepart.get(j)) && !cheminPlusCourt.get(1).equals(listNodeDepart.get(j))) {
							nbAntEnCours++;
						}
					}

					if (!ant.isTrouve()) {
						// Si un pirate est arrivée sur un trésor de la map
						for (int x = 0; x < listObjets.size(); x++) {
							if (ant.getPosition().equals(listObjets.get(x))) {
								listObjets.get(x).setNb(listObjets.get(x).getNb() - 1);
								if(listObjets.get(x).getNb() == 0) {
									graph.getNode(listObjets.get(x).getX(), listObjets.get(x).getY()).setIdOrigine(" ");
									listObjets.remove(x);
								}
								ant.setTrouve(true);
							}
						}
					} else {
						if (ant.getPosition().equals(graph.getNode(listNodeDepart.get(0).getX(),listNodeDepart.get(0).getY()))) {
							System.out.println("ici");
							graph.getNode(ant.getPosition().getX(), ant.getPosition().getY()).setId(ant.getPosition().getIdOrigine());
							if(!listObjets.contains(ant.getNodeObj())){
								nbAntArrivees++;
								nbAntEnCours--;
							} else {
								ant.setTrouve(false);
							}
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
					listNbAntSorti.add(Integer.parseInt(nbAntSorti.getText()));
					deplacerAnt();
				}
			}).start();
		}

		if (e.getSource().equals(buttonLoadFile)) {
			initMap();
		}
	}

	public void initMap() {

		listNbAntSorti.clear();
		listNodeDepart.clear();
		listObjets.clear();

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
			actualiserMap();
		}
	}

}
