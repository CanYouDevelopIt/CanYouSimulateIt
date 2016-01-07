package simulation.common.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import simulation.Personnage;
import simulation.shonenfight.Combattant;

public abstract class XmlReader {

	public static String getMap(File xmlMapFile) {
		StringBuilder mapContent = new StringBuilder();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlMapFile);
			doc.getDocumentElement().normalize();
			if (doc.getDocumentElement().getNodeName().equals("map")) {
				NodeList nList = doc.getElementsByTagName("ligne");
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						mapContent.append(eElement.getAttribute("value") + "\n");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapContent.toString();
	}

	public static List<Personnage> getPersonnages(File xmlPersonnagesFile) {

		List<Personnage> listePersonnages = new ArrayList<Personnage>();

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlPersonnagesFile);
			doc.getDocumentElement().normalize();
			if (doc.getDocumentElement().getNodeName().equals("Personnages")) {
				NodeList nList = doc.getElementsByTagName("Personnage");
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						String nom = eElement.getAttribute("nom");
						int pointAttaque = Integer.parseInt(eElement.getAttribute("pointAttaque"));
						int pointVie = Integer.parseInt(eElement.getAttribute("pointVie"));

						Personnage p = new Combattant(nom, pointVie, pointAttaque);
						listePersonnages.add(p);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listePersonnages;
	}
}
