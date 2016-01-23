package simulation.shonenfight;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import simulation.Personnage;
import simulation.common.graph.Dijkstra;
import simulation.common.graph.Edge;
import simulation.common.graph.Graph;
import simulation.common.graph.Node;
import simulation.common.tools.ClosingTools;
import simulation.common.tools.XmlReader;
import simulation.etat.EtatInactif;
import simulation.factory.ImageFactory;
import simulation.view.MainApplicationView;

public class ShonenFightMap extends JPanel implements ActionListener {

	private static final Color BG_COLOR = new Color(98, 165, 199);

	private static final long serialVersionUID = 446565083035345353L;

	private static final String map = "resources/shonenfight/map/piratemap.xml";
	private static final String fichierPersonnages = "resources/shonenfight/listePersonnages.xml";

	MainApplicationView mainApplicationView;

	private File fichier;
	private Graph graph;
	private Node[][] nodes;
	private int nbligne;
	private int nbcol;

	private JPanel jpNord;
	private JPanel jpSud;
	private JPanel jpInfosCombat;
	private JPanel jpX;
	private JPanel jpY;
	private JButton buttonLancer;

	private DefaultListModel<String> informationsCombat = new DefaultListModel<String>();
	private JList<String> jInformationsCombat = new JList<String>(
			informationsCombat);
	private JScrollPane scrollInformationsCombat = new JScrollPane(
			jInformationsCombat);

	private boolean isNotDisabled = true;

	private List<Personnage> listePersonnages;
	private EquipeCombattant equipeA;
	private EquipeCombattant equipeB;

	public ShonenFightMap(MainApplicationView mainAppView) {
		mainApplicationView = mainAppView;

		buildPanel(); // Creation Panel
		loadPersonnages(); // Chargement de tous les personnages
		creerEquipes();

		initMap();
	}

	public void buildPanel() {

		jpNord = new JPanel();
		jpNord.setBackground(BG_COLOR);

		jpSud = new JPanel();
		jpInfosCombat = new JPanel();
		jpX = new JPanel();
		jpY = new JPanel();

		initJpSudComponents();

		add(jpNord, BorderLayout.NORTH);
		add(jpSud, BorderLayout.SOUTH);
		setBackground(BG_COLOR);
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
					jpNord.add(imageFactory.getImageLabel(node.getId(),
							node.getIdOrigine()));
				}
			}
		}
	}

	public void initJpSudComponents() {

		jpSud.setBackground(BG_COLOR);

		buttonLancer = new JButton("Fight!");
		buttonLancer.addActionListener(this);

		jpInfosCombat.add(buttonLancer, BorderLayout.NORTH);
		jpInfosCombat.add(scrollInformationsCombat, BorderLayout.SOUTH);

		jpX.setBackground(BG_COLOR.BLUE);
		jpY.setBackground(BG_COLOR.RED);

		jpSud.add(jpX, BorderLayout.EAST);
		jpSud.add(jpInfosCombat, BorderLayout.CENTER);
		jpSud.add(jpY, BorderLayout.WEST);
	}

	private void loadPersonnages() {
		File filePersonnages = new File(fichierPersonnages);
		listePersonnages = XmlReader.getPersonnages(filePersonnages);
	}

	public void creerEquipes() {

		Random rand = new Random();

		equipeA = new EquipeCombattant();
		equipeB = new EquipeCombattant();

		while (equipeA.getSize() < 5) {
			int chiffreAlea = rand.nextInt(listePersonnages.size() - 1);
			Combattant p = (Combattant) listePersonnages.get(chiffreAlea);

			if (!equipeA.contientCombattant(p)) {
				equipeA.ajouterCombattant(p);
				System.out.print(p.getNomPersonnage() + "|");
			}
		}
		System.out.println("");
		System.out.println("VS");
		while (equipeB.getSize() < 5) {
			int chiffreAlea = rand.nextInt(listePersonnages.size() - 1);
			Combattant p = (Combattant) listePersonnages.get(chiffreAlea);

			if (!equipeB.contientCombattant(p)
					&& !equipeA.contientCombattant(p)) {
				equipeB.ajouterCombattant(p);
				System.out.print(p.getNomPersonnage() + "|");
			}
		}

		System.out.println("");
	}

	private void initMap() {

		try {
			loadFichier();
		} catch (IOException e) {
		}

		setSize(720, 480);
		mainApplicationView.setResizable(false);
		mainApplicationView.setPreferredSize(new Dimension(getWidth(),
				getHeight() + 20));

		actualiserMap();
	}

	public void loadFichier() throws IOException {
		fichier = new File(map);

		graph = new Graph();

		if (fichier.exists()) {
			initTailleMap();
			initParcellesMap();
			initDistanceEntreParcelle();
		}
	}

	public void actualiserMap() {

		removeAll();

		jpNord = new JPanel();
		initJpNordComponents();

		add(jpNord, BorderLayout.NORTH);
		add(jpSud, BorderLayout.SOUTH);
		setBackground(BG_COLOR);

		buttonLancer.setEnabled(isNotDisabled);

		mainApplicationView.pack();
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

		int combattantEquipeA = 0;
		int combattantEquipeB = 0;

		try {
			br = getBufferReader();
			String ligne2;
			int cptligne = 0;
			while ((ligne2 = br.readLine()) != null) {
				String[] tab2 = ligne2.split("");
				for (int i = 1; i < tab2.length; i++) {
					if (tab2[i].equals(" ") || tab2[i].equals("F")
							|| tab2[i].equals("X") || tab2[i].equals("Y")
							|| tab2[i].equals("C")) {

						nodes[cptligne][i - 1] = new Node(tab2[i], i - 1,
								cptligne);

						if (tab2[i].equals("X")) {
							equipeA.getCombattant(combattantEquipeA)
									.setPosition(nodes[cptligne][i - 1]);
							equipeA.getCombattant(combattantEquipeA)
									.getPosition().setIdOrigine("X");
							combattantEquipeA++;
						}

						if (tab2[i].equals("Y")) {
							equipeB.getCombattant(combattantEquipeB)
									.setPosition(nodes[cptligne][i - 1]);
							equipeB.getCombattant(combattantEquipeB)
									.getPosition().setIdOrigine("Y");
							combattantEquipeB++;
						}
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

	private void initDistanceEntreParcelle() {
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				if (nodes[i][j] != null) {
					graph.registerNode(nodes[i][j]);

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

	private BufferedReader getBufferReader() throws Exception {
		BufferedReader br;
		String map = XmlReader.getMap(fichier);
		InputStream is = new ByteArrayInputStream(map.getBytes());
		br = new BufferedReader(new InputStreamReader(is));
		return br;
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
			new Thread(new Runnable() {
				public void run() {
					lancerCombat();
				}
			}).start();
		}
	}

	public void lancerCombat() {

		// System.out.println("POSITION");
		// for (Combattant c : equipeA.getEquipe()) {
		// System.out.println("Position de " + c.getNomPersonnage() + " : "
		// + c.getPosition().toString());
		// }
		//
		// for (Combattant c : equipeB.getEquipe()) {
		// System.out.println("Position de " + c.getNomPersonnage() + " : "
		// + c.getPosition().toString());
		// }

		informationsCombat.addElement("FIGHT !!!");

		while (equipeA.getNbCombattantVivant() > 0
				&& equipeB.getNbCombattantVivant() > 0) {

			lancerAttaque(equipeA, equipeB);

			if (equipeB.getNbCombattantVivant() > 0) {
				lancerAttaque(equipeB, equipeA);
			}

			// System.out.println("----------A------------");
			// for (int i = 0; i < equipeA.getSize(); i++) {
			// equipeA.getCombattant(i).afficherHP();
			// ;
			// }
			// System.out.println("----------B------------");
			// for (int i = 0; i < equipeB.getSize(); i++) {
			// equipeB.getCombattant(i).afficherHP();
			// ;
			// }
		}

		if (equipeA.getNbCombattantVivant() > 0) {
			informationsCombat.addElement("L'équipe A a gagné.");
		} else {
			informationsCombat.addElement("L'équipe B a gagné.");
		}

	}

	public void lancerAttaque(EquipeCombattant equipeAttaquant,
			EquipeCombattant equipeDefenseur) {

		Combattant attaquant = equipeAttaquant.getProchainCombattant();
		Combattant defenseur = equipeDefenseur.getCombattantFaible();

		attaquant.AttaqueCombattant(defenseur);
		deplacerCombattant(attaquant, defenseur);

		if (defenseur.getEtatPersonnage().getClass().getSimpleName()
				.equals(EtatInactif.class.getSimpleName())) {
			equipeDefenseur.aPerduCombattant();
		}

	}

	public void deplacerCombattant(Combattant attaquant, Combattant defenseur) {

		boolean defenseurAttaque = false;
		String idOrigineAttaquant = attaquant.getPosition().getIdOrigine();

		List<Node> cheminPlusCourt = new ArrayList<Node>();

		Node nodeDepart = graph.getNode(attaquant.getPosition().getX(),
				attaquant.getPosition().getY());

		while (!defenseurAttaque) {

			Dijkstra d = new Dijkstra(graph, attaquant.getPosition(),
					defenseur.getPosition(), null);

			cheminPlusCourt = d.cheminPlusCourtOptimiser();

			if (cheminPlusCourt.size() > 1) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Placer graphiquement le pirate
				graph.getNode(cheminPlusCourt.get(0).getX(),
						cheminPlusCourt.get(0).getY()).setId(" ");
				graph.getNode(cheminPlusCourt.get(1).getX(),
						cheminPlusCourt.get(1).getY()).setId(
						attaquant.getPosition().getIdOrigine());

				if (cheminPlusCourt.get(2).getIdOrigine()
						.equals(defenseur.getPosition().getIdOrigine()))
					defenseurAttaque = true;

				// Changer la postion du combattant
				attaquant.setPosition(cheminPlusCourt.get(1));
				attaquant.getPosition().setId(idOrigineAttaquant);

				// Actualiser la map
				this.actualiserMap();
				this.repaintFrame();

			}

		}

		try {
			informationsCombat.addElement(attaquant.getNomPersonnage()
					+ " attaque " + defenseur.getNomPersonnage());
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while (!nodeDepart.equals(attaquant.getPosition())) {

			Dijkstra d = new Dijkstra(graph, attaquant.getPosition(),
					nodeDepart, null);

			cheminPlusCourt = d.cheminPlusCourtOptimiser();

			if (cheminPlusCourt.size() > 1) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Placer graphiquement le pirate
				graph.getNode(cheminPlusCourt.get(0).getX(),
						cheminPlusCourt.get(0).getY()).setId(" ");
				graph.getNode(cheminPlusCourt.get(1).getX(),
						cheminPlusCourt.get(1).getY()).setId(
						attaquant.getPosition().getIdOrigine());

				// Changer la postion du combattant
				attaquant.setPosition(cheminPlusCourt.get(1));
				attaquant.getPosition().setId(idOrigineAttaquant);

				// Actualiser la map
				this.actualiserMap();
				this.repaintFrame();

			}

		}

	}
}
