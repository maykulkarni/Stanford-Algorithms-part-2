import java.util.*;
import java.io.*;
public class UnionFind{

	static class AdjNode{
		int verticeNumber;
		AdjNode next;
		int cost;

		public AdjNode(int verticeNumber, int cost){
			this.verticeNumber = verticeNumber;
			this.cost = cost;
		} 
	}

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

	static class AdjList{
		AdjNode head;
		AdjNode tail;
		public void addEdge(int to, int cost){
			if(head == null){
				head = new AdjNode(to, cost);
				tail = head;
			}else{
				AdjNode temp = tail;
				temp.next = new AdjNode(to, cost);
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
				System.out.print(temp.verticeNumber + " > " + temp.cost + " | ");
				temp = temp.next;
			}
			System.out.println(temp.verticeNumber + " > " + temp.cost);
		}
	}

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
		private int nodesToBeProcessed;
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

		public void addEdge(int from, int to, int cost){
			//System.out.println("From " + from + " to " + to + " costs " + cost);
			list[from].addEdge(to, cost);
			edges.add(new Edge(from, to, cost));
		}

		public void printGraph(){
			for(int i=0; i<numberOfVertices; i++){
				System.out.print(i + " ");
				list[i].printList();
			}
		}

		public void sortEdges(){
			Collections.sort(edges, new EdgeComparator());
		}

		public void printEdges(){
			System.out.println(edges);
		}

		public int find(int vertice){

			if(subsets[vertice].parent != vertice){
				subsets[vertice].parent = find(subsets[vertice].parent);
			}

			return subsets[vertice].parent;
		}

		public void union(int one, int two){
			int oneRoot = this.find(one);
			int twoRoot = this.find(two);
			//System.out.println("Union " + one + " " + two);
			if(subsets[oneRoot].rank > subsets[twoRoot].rank){
				subsets[twoRoot].parent = oneRoot;
			}else if(subsets[oneRoot].rank < subsets[twoRoot].rank){
				subsets[oneRoot].parent = twoRoot;
			}else{
				subsets[twoRoot].parent = oneRoot;
				subsets[oneRoot].rank++;
			}
			//printSubsets();
			nodesToBeProcessed--;
		}

		public void findCycle(){
			Iterator<Edge> it = edges.iterator();
			while(it.hasNext()){
				Edge currentEdge = it.next();
				int src = currentEdge.from;
				int dest = currentEdge.to;

				int srcParent = this.find(src);
				int destParent = this.find(dest);

				if(srcParent == destParent){
					System.out.println("Cycle Present");
					return;
				}else{
					union(src, dest);
				}
			}
			System.out.println("Cycle not Present!");
		}

		public void printSubsets(){
			System.out.println(Arrays.toString(subsets));
		}

		public void formClusters(){
			visited = new ArrayList<Edge>();
			Iterator<Edge> it = edges.iterator();
			sortEdges();
			//this.printEdges();
			while(nodesToBeProcessed >= 5){
				Edge currentEdge = it.next();
				int src = currentEdge.from;
				int dest = currentEdge.to;
				//printSubsets();
				if(find(src)!=find(dest))
					union(src, dest);
				it.remove();
			}

			//System.out.println("Finished clustering");
			//System.out.println(edges);
			// Iterator<Edge> newit = visited.iterator();
			// int max = -999;

			// while(newit.hasNext()){
			// 	Edge curr = newit.next();
			// 	if(max < curr.edgeCost)
			// 		max = curr.edgeCost;
			// }
			// System.out.println("Max edge : " + max);
			Iterator<Edge> newIter = edges.iterator();
			while(newIter.hasNext()){
				Edge currentEdge = newIter.next();
				int parentOne = find(currentEdge.from);
				int parentTwo = find(currentEdge.to);
				if(parentOne != parentTwo){
					//printSubsets();
					System.out.println("Answer : " + currentEdge.edgeCost + " from " + currentEdge.from + 
										" to " + currentEdge.to);
					break;
				}
			}
		}

	}

	public static void main(String[] args) throws Exception{
		Scanner in = new Scanner(new File("/home/mayur/Downloads/clustering1.txt"));
		int numberOfVertices = in.nextInt();
		Graph g = new Graph(numberOfVertices);
		while(in.hasNext()){
			g.addEdge(in.nextInt() - 1, in.nextInt() - 1, in.nextInt());
		}
		//g.printGraph();
		//g.printEdges();
		System.out.println("Clustering graph with " + numberOfVertices + " nodes.");

		g.formClusters();
	}
}