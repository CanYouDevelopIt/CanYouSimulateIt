package simulation.common.graph;

public class LinkSimpleNode {

	Node node;
	LinkSimpleNode next;

	public LinkSimpleNode() {
	}

	public LinkSimpleNode(Node n) {
		this.node = n;
	}

	public Node getNode() {
		return this.node;
	}

	public void setNode(Node n) {
		this.node = n;
	}

	public LinkSimpleNode getNext() {
		return next;
	}

	public void setNext(LinkSimpleNode next) {
		this.next = next;
	}

}
