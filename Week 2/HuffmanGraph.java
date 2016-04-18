import java.util.*;
import java.math.*;
import java.io.*;

public class HuffmanGraph {

	public static int clustersize = 0;
	public static HashMap<String, Integer> strToIntMap;
	public static int binToInt(String bin){
		int total = 0;
		for(int i=0; i<bin.length(); i++){
			// if(bin.charAt(i) == '1')
			// 	total += (int)Math.pow(2, 23-i);
			if(bin.charAt(i) == '1')
				total += (int)Math.pow(2, 2-i);
		}
		return total;
	}

	public static void initMap(HashSet<String> InputList){
		int intg = 0;
		strToIntMap = new HashMap<String, Integer>();
		for(String i : InputList){
			strToIntMap.put(i, intg);
			intg++;
		}
		System.out.println("HashMap initialsed");
	}	

	static class Edge {
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
		
		public int compareTo(Edge other){
			if(this.edgeCost < other.edgeCost){
				return -1;
			}else if(this.edgeCost == other.edgeCost){
				return 0;
			}else{
				return 1;
			}
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

	public static Subset[] subsets;

	public static void initSubset(int size){
		subsets = new Subset[size];
		for(int i=0; i<size; i++){
				subsets[i] = new Subset(i, 0);
		}
	}

	public static int find(int vertice){
		if(subsets[vertice].parent != vertice){
			subsets[vertice].parent = find(subsets[vertice].parent);
		}
		return subsets[vertice].parent;
	}

		// Merges two nodes
	public static void union(int one, int two){
		int oneRoot = find(one);
		int twoRoot = find(two);
		if(subsets[oneRoot].rank > subsets[twoRoot].rank){
			subsets[twoRoot].parent = oneRoot;
		}else if(subsets[oneRoot].rank < subsets[twoRoot].rank){
			subsets[oneRoot].parent = twoRoot;
		}else{
			subsets[twoRoot].parent = oneRoot;
			subsets[oneRoot].rank++;
		}
	}

	public static ArrayList<String> oneBit(){
		ArrayList<String> oneBit = new ArrayList<String>();
		StringBuilder temp = new StringBuilder("000000000000000000000000");
		for(int i=0; i<24; i++){
			temp.setCharAt(i, '1');
			oneBit.add(temp.toString());
			temp.setCharAt(i,'0');
		}
		return oneBit;
	}

	public static ArrayList<String> twoBit(){
		ArrayList<String> twoBit = new ArrayList<String>();
		StringBuilder temp = new StringBuilder("000000000000000000000000");
		for(int i=0; i<23; i++){
			temp.setCharAt(i, '1');
			for(int j=i+1; j<24; j++){
				temp.setCharAt(j, '1');
				twoBit.add(temp.toString());
				temp.setCharAt(j, '0');
			}
			temp.setCharAt(i, '0');
		}
		return twoBit;
	}

	private static boolean bitOf(char in) {
    	return (in == '1');
	}

	private static char charOf(Boolean in) {
    	return (in) ? '1' : '0';
	}

	public static String XOR(String one, String two){
		StringBuilder sb = new StringBuilder();

	    for(int i = 0; i < one.length(); i++) {
	        sb.append(charOf(bitOf(one.charAt(i)) ^ bitOf(two.charAt(i))));
	    }
	    return sb.toString();
	}


	public static int getClusterCount(ArrayList<Edge> edges){

		for(Edge e : edges){
			int from = e.from;
			int to = e.to;
			if(find(from) != find(to)){
				union(from, to);
				clustersize--;
			}
		}
		return clustersize;
	} 


	public static void findEdges(HashSet<String> Inputlist){
		HashSet <Edge> set = new HashSet<Edge>();
		HashMap<String, Boolean> InputListMap = new HashMap<String, Boolean>();
		// for(String i : Inputlist)
		// 	InputListMap.put(i, true);
		HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
		ArrayList<String> oneBit = oneBit();
		ArrayList<String> twoBit = twoBit();
		ArrayList<Edge> modEdges = new ArrayList<Edge>();
		//processed = new HashMap<String, Boolean>();
		for(String i : oneBit){
			for(String j : Inputlist){
				//System.out.print(i + " xor " + j + " ");
				if(strToIntMap.containsKey(XOR(i,j))){
					String modOne = j + XOR(i,j);
					String modTwo = XOR(i,j) + j;
					if(visited.containsKey(modOne) || visited.containsKey(modTwo)){

					}else{
						visited.put(modOne, true);
						visited.put(modTwo, true);
						modEdges.add(new Edge(strToIntMap.get(j), strToIntMap.get(XOR(i,j)), 1));
					}
				}
			}
		}

		System.out.println("One bit lists traversed");

		for(String i : twoBit){
			for(String j : Inputlist){
				if(strToIntMap.containsKey(XOR(i,j))){
					String modOne = j + XOR(i,j);
					String modTwo = XOR(i,j) + j;
					if(visited.containsKey(modOne) || visited.containsKey(modTwo)){

					}else{
						visited.put(modOne, true);
						visited.put(modTwo, true);
						modEdges.add(new Edge(strToIntMap.get(j), strToIntMap.get(XOR(i,j)), 2));
					}
				}
			}
		}

		//System.out.println("Cluster count : " + getClusterCount(set));
		//System.out.println(set);
		System.out.println("Edges formed, getting cluster count");
		System.out.println("Cluster count : " + getClusterCount(modEdges));
		stop = System.currentTimeMillis();
		float ans = start - stop;
		System.out.println("Time req : " + ans + " ms"); 
	}


	public static float start;
	public static float stop;

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(new File("/home/mayur/Downloads/clustering_big.txt"));
		int numberOfNodes = in.nextInt();
		int numberOfBits = in.nextInt();
		start = System.currentTimeMillis();
		//String binString = in.nextLine();
		HashSet<String> InputList = new HashSet<String>();
		int numberOfPoints = 0;
		while(in.hasNext()){
			StringBuilder tempString = new StringBuilder();
			for(int i=0; i<24; i++){
				tempString.append(in.nextInt());
			}
			// for(int i=0; i<3; i++){
			// 	tempString.append(in.nextInt());
			// }
			InputList.add(tempString.toString());
			numberOfPoints++;
		}
		clustersize = InputList.size();
		// Collections.sort(list);
		// HuffManGraph g = getGraph(list);
		// System.out.println("Completed forming graph");
		// g.graphInfo();
		// g.initMap();
		// g.formClusters();
		initSubset(clustersize);
		//System.out.println(InputList);
		System.out.println("Size : " + numberOfPoints);
		System.out.println("Unique size : " + clustersize);
		initMap(InputList);
		//System.out.println(strToIntMap);
		findEdges(InputList);
	}
}