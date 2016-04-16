import java.util.*;
import java.io.*;

public class UnionFind{
	// A node in the Adjacency List
	static class AdjNode{
		int verticeNumber;
		int data;
		AdjNode next;
		int cost;

		public AdjNode(int verticeNumber, int cost, int data){
			this.verticeNumber = verticeNumber;
			this.cost = cost;
			this.data = data;
		} 
	}

	// Used by Union-Find
	static class Subset{
		int rank;
		int parent;

		public Subset(int parent, int rank){
			this.rank = rank;
			this.parent = parent;
		}

		public String toString(){
			return this.rank + " " + this.parent;
		}
	}

	// Adjacency List 
	static class AdjList{
		AdjNode head;
		AdjNode tail;
		public void addEdge(int to, int cost, int data){
			if(head == null){
				head = new AdjNode(to, cost, data);
				tail = head;
			}else{
				AdjNode temp = tail;
				temp.next = new AdjNode(to, cost, data);
				tail = tail.next;
			}
		}

		public void printList(){
			if(head == null){
				System.out.println("Empty List!");
				return;
			}
			AdjNode temp = head;
			while(temp.next!=null){
				System.out.print(Integer.toBinaryString(temp.data) + " > " + temp.cost + " | ");
				temp = temp.next;
			}
			System.out.println(Integer.toBinaryString(temp.data) + " | " + temp.data + " > " + temp.cost);
		}
	}

	// Used for sorting Edges
	static class EdgeComparator implements Comparator<Edge>{
		public int compare(Edge one, Edge two){
			if(one.edgeCost > two.edgeCost){
				return 1;
			}else if(one.edgeCost == two.edgeCost){
				return 0;
			}else{
				return -1;
			}
		}
	}

	// Used by Union-Find
	static class Edge{
		int from;
		int to;
		int edgeCost;

		public Edge(int from, int to, int edgeCost){
			this.from = from;
			this.to = to;
			this.edgeCost = edgeCost;
		}

		public String toString(){
			return " from " + from + " to " + to + " cost : " + edgeCost + "\n";
		}
			
	}

	static class Graph{
		int numberOfVertices;
		private AdjList[] list;
		private List<Edge> edges;
		private List<Edge> visited;
		private Subset[] subsets;
		private int nodesToBeProcessed;  // <-- (nodesToBeProcessed - 1) tells us the number of
										 // clusters formed

		public Graph(int numberOfVertices){
			this.numberOfVertices = numberOfVertices;
			list = new AdjList[numberOfVertices];

			for(int i=0; i<numberOfVertices; i++){
				list[i] = new AdjList();
			}

			edges = new ArrayList<Edge>();
			subsets = new Subset[numberOfVertices];

			for(int i=0; i<numberOfVertices; i++){
				subsets[i] = new Subset(i, 0);
			}
			nodesToBeProcessed = numberOfVertices;
		}

		public void addEdge(int from, int to, int cost, int data){
			list[from].addEdge(to, cost, data);
			edges.add(new Edge(from, to, cost));
		}

		public void printGraph(){
			for(int i=0; i<numberOfVertices; i++){
				list[i].printList();
			}
		}

		public void sortEdges(){
			Collections.sort(edges, new EdgeComparator());
		}

		public void printEdges(){
			System.out.println(edges);
		}

		// Find's parent of a node
		// Uses Path Compression
		public int find(int vertice){
			if(subsets[vertice].parent != vertice){
				subsets[vertice].parent = find(subsets[vertice].parent);
			}
			return subsets[vertice].parent;
		}

		// Merges two nodes
		public void union(int one, int two){
			int oneRoot = this.find(one);
			int twoRoot = this.find(two);
			if(subsets[oneRoot].rank > subsets[twoRoot].rank){
				subsets[twoRoot].parent = oneRoot;
			}else if(subsets[oneRoot].rank < subsets[twoRoot].rank){
				subsets[oneRoot].parent = twoRoot;
			}else{
				subsets[twoRoot].parent = oneRoot;
				subsets[oneRoot].rank++;
			}
			nodesToBeProcessed--;
		}

		public void printSubsets(){
			System.out.println(Arrays.toString(subsets));
		}

		// Prints the information of graph
		public void graphInfo(){
			int zeroCount = 0;
			int oneCount = 0;
			int twoCount = 0;

			for(Edge e : edges){
				switch(e.edgeCost){
					case 0 : zeroCount++;
							 break;
					case 1 : oneCount++;
							 break;
					case 2 : twoCount++;
							 break;
				}
			}
			System.out.println("Zero : " + zeroCount + " one ; " + oneCount + " two : " + twoCount);
		}

		public void formClusters(){
			visited = new ArrayList<Edge>();
			Iterator<Edge> it = edges.iterator();
			sortEdges();
			while(true){
				Edge currentEdge = it.next();
				if(currentEdge.edgeCost > 2){
					System.out.println("Clusters : " + nodesToBeProcessed);
					break;
				}
				int src = currentEdge.from;
				int dest = currentEdge.to;
				if(find(src)!=find(dest))
					union(src, dest);
				it.remove();
			}
		}

	}

	public static void main(String[] args) throws Exception{
		// Scanner in = new Scanner(new File("/home/mayur/Downloads/clustering1.txt"));
		// int numberOfVertices = in.nextInt();
		// Graph g = new Graph(numberOfVertices);
		// while(in.hasNext()){
		// 	g.addEdge(in.nextInt() - 1, in.nextInt() - 1, in.nextInt());
		// }
		// //g.printGraph();
		// //g.printEdges();
		// System.out.println("Clustering graph with " + numberOfVertices + " nodes.");

		// g.formClusters();
	}
}