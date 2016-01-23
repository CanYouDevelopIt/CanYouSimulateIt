package simulation.common.graph;

import java.util.ArrayList;
import java.util.List;

public class Dijkstra {
	
	private Graph graph;
	private LinkedPriorityQueueNode listePassage;
	private Node nodeDepart;
	private Node nodeArrive;
	private List<Node> nodesDejaPasses;
	
	public Dijkstra(Graph g, Node nd, Node na, List<Node> ln){
		
		graph = g;
		this.listePassage = new LinkedPriorityQueueNode();
		this.nodeDepart = nd;
		this.nodeArrive = na;
		this.nodesDejaPasses = ln;
		
		//REINITIALISER
		for(Node n: graph.getNodes()){
			n.setNodePrecedent(null);
			n.setMinDistance(999999);
		}
		
		nodeDepart.setNodePrecedent(null);
		nodeDepart.setMinDistance(0);
		
		nodeArrive.setNodePrecedent(null);
		nodeArrive.setMinDistance(999999);
		
		listePassage.add(nodeDepart);
		
	}
	
	public List<Node> cheminPlusCourtOptimiser(){
		
		List<Node> cheminPlusCourtInverse = new ArrayList<Node>();
		List<Node> cheminPlusCourt = new ArrayList<Node>();
		
		nodeDepart.setMinDistance(0);
		nodeDepart.setNodePrecedent(null);
		
		Node nodeActuel = null;
		Node nodeFils = null;
		boolean nodeInterdit;
		int distanceActuel;
		
		while(listePassage.size() > 0){
			
			nodeActuel = listePassage.poll();
			//System.out.println(nodeActuel.getEdges().size());
			
			for(Edge unEdge : nodeActuel.getEdges()){
				
				nodeFils = unEdge.getOther(nodeActuel);
				distanceActuel = unEdge.getDistance() + nodeActuel.getMinDistance();
				
				nodeInterdit = false;
				
				if(nodesDejaPasses != null){
					for(int i = 0; i < nodesDejaPasses.size(); i++){
						if(nodesDejaPasses.get(i).equals(nodeFils)){
							//System.out.println("INTERDIT !!!!!!" + nodesDejaPasses.get(i).getX() + ";" + nodesDejaPasses.get(i).getY());
							nodeInterdit = true;
						}
					}
				}
				
				if((nodeFils.getId().equals("S") && distanceActuel < 2) || nodeFils.getId().equals("D") || nodeInterdit){
				}
				else{	
					if(distanceActuel < nodeFils.getMinDistance()){
						
						//System.out.println(nodeFils.getX() + ";" + nodeFils.getY());
						
						nodeFils.setMinDistance(distanceActuel);
						nodeFils.setNodePrecedent(nodeActuel);
						listePassage.add(nodeFils);
					}
				}	
			}
		}
		
		for(Node n = nodeArrive; n != null; n = n.getNodePrecedent()){
			cheminPlusCourtInverse.add(n);
		}
		
		for(int i = cheminPlusCourtInverse.size() - 1; i >= 0; i--){
			cheminPlusCourt.add(cheminPlusCourtInverse.get(i));
		}
		
		return cheminPlusCourt;
	}
	
public List<Node> cheminRetourOptimiser(){
		
		List<Node> cheminPlusCourtInverse = new ArrayList<Node>();
		List<Node> cheminPlusCourt = new ArrayList<Node>();
		
		nodeDepart.setMinDistance(0);
		nodeDepart.setNodePrecedent(null);
		
		Node nodeActuel = null;
		Node nodeFils = null;
		boolean nodeInterdit;
		int distanceActuel;
		
		while(listePassage.size() > 0){
			
			nodeActuel = listePassage.poll();
			//System.out.println(nodeActuel.getEdges().size());
			
			for(Edge unEdge : nodeActuel.getEdges()){
				
				nodeFils = unEdge.getOther(nodeActuel);
				distanceActuel = unEdge.getDistance() + nodeActuel.getMinDistance();
				
				nodeInterdit = false;
				
				if(nodesDejaPasses != null){
					for(int i = 0; i < nodesDejaPasses.size(); i++){
						if(nodesDejaPasses.get(i).equals(nodeFils)){
							//System.out.println("INTERDIT !!!!!!" + nodesDejaPasses.get(i).getX() + ";" + nodesDejaPasses.get(i).getY());
							nodeInterdit = true;
						}
					}
				}
				
				if((nodeFils.getId().equals("S") && distanceActuel < 2) || nodeFils.getId().equals("A") || nodeInterdit){
				}
				else{	
					if(distanceActuel < nodeFils.getMinDistance()){
						
						//System.out.println(nodeFils.getX() + ";" + nodeFils.getY());
						
						nodeFils.setMinDistance(distanceActuel);
						nodeFils.setNodePrecedent(nodeActuel);
						listePassage.add(nodeFils);
					}
				}	
			}
		}
		
		for(Node n = nodeArrive; n != null; n = n.getNodePrecedent()){
			cheminPlusCourtInverse.add(n);
		}
		
		for(int i = cheminPlusCourtInverse.size() - 1; i >= 0; i--){
			cheminPlusCourt.add(cheminPlusCourtInverse.get(i));
		}
		
		return cheminPlusCourt;
	}
	
	public Node getNodeArrive() {
		return nodeArrive;
	}

	public void setNodeArrive(Node nodeArrive) {
		this.nodeArrive = nodeArrive;
	}

	public Node getNodeDepart() {
		return nodeDepart;
	}

	public void setNodeDepart(Node nodeDepart_) {
		this.nodeDepart = nodeDepart_;
		listePassage = new LinkedPriorityQueueNode();
		listePassage.add(nodeDepart);
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
}
