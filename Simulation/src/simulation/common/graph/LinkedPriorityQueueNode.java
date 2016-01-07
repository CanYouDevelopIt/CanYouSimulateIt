package simulation.common.graph;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class LinkedPriorityQueueNode {

	private LinkSimpleNode linkSimple;

	private int listCount = 0;

	private Comparator<Node> comparator;

	public LinkedPriorityQueueNode() {
		this.setComparator(null);
	}

	public LinkedPriorityQueueNode(Comparator<Node> comp) {
		this.setComparator(comp);
	}

	public LinkedPriorityQueueNode(Comparator<Node> comp, LinkSimpleNode ls) {
		this.setComparator(comp);
		this.linkSimple = ls;
	}

	public void add(Node n) {
		// Valeur à ajouter
		LinkSimpleNode newValue = new LinkSimpleNode();
		newValue.setNode(n);
		newValue.setNext(null);

		if (linkSimple == null) {
			linkSimple = newValue;
		} else {
			LinkSimpleNode actual = linkSimple;

			while (actual.getNext() != null) {
				actual = (LinkSimpleNode) actual.getNext();
			}

			actual.setNext(newValue);
		}
		listCount++;
	}

	/**
	 * Remove throws NoSuchElementException
	 * */
	public Node remove() {
		if (linkSimple.getNext() == null || linkSimple == null) {
			linkSimple = null;
			throw new NoSuchElementException(
					"L'element a enlever est inexistant.");
		} else {
			linkSimple = (LinkSimpleNode) linkSimple.getNext();
			listCount--;
			return linkSimple.getNode();
		}
	}

	public Node peek() {
		return linkSimple.getNode();
	}

	public int size() {
		return getListCount();
	}

	public Node poll() {
		if (linkSimple == null) {
			return null;
		} else {
			Node oldHeadValue = linkSimple.getNode();
			linkSimple = (LinkSimpleNode) linkSimple.getNext();
			listCount--;
			return oldHeadValue;
		}
	}

	/**
	 * Element throws NoSuchElementException
	 * */
	public Node Element() {
		if (linkSimple.getNode() == null)
			throw new NoSuchElementException("Aucun element trouver.");
		return linkSimple.getNode();
	}

	/**
	 * Iterator
	 * */
	public boolean hasNext() {
		return (linkSimple.getNext() != null);
	}

	public Node next() {
		if (this.hasNext() != false)
			return linkSimple.getNext().getNode();
		return null;
	}

	/**
	 * Getters & Setters
	 * 
	 * */
	public LinkSimpleNode getLinkSimple() {
		return linkSimple;
	}

	public void setLinkSimple(LinkSimpleNode linkSimple) {
		this.linkSimple = linkSimple;
	}

	public int getListCount() {
		return listCount;
	}

	public void setListCount(int listCount) {
		this.listCount = listCount;
	}

	public Comparator<Node> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<Node> comparator) {
		this.comparator = comparator;
	}

}
