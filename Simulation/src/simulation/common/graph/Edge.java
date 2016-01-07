package simulation.common.graph;

public class Edge {

	private Node nodeUn;
	private Node nodeDeux;
	private int distance;

	public int getDistance() {
		return distance;
	}

	public Node getOther(Node n) {

		if (n.equals(nodeUn)) {
			return nodeDeux;
		}
		if (n.equals(nodeDeux)) {
			return nodeUn;
		}
		return null;
	}

	public void setDistance(int uneDistance) {
		distance = uneDistance;
	}

	public Edge(Node nodeUn_, Node nodeDeux_, int uneDistance) {
		nodeUn = nodeUn_;
		nodeDeux = nodeDeux_;
		distance = uneDistance;
		nodeUn.getEdges().add(this);
		nodeDeux.getEdges().add(this);
	}

}
