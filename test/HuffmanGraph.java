import java.util.*;
import java.math.*;
import java.io.*;

public class HuffmanGraph extends UnionFind{

	public static int binToInt(String bin){
		int total = 0;
		for(int i=0; i<bin.length(); i++){
			if(bin.charAt(i) == '1')
				total += (int)Math.pow(2, 23-i);
		}
		return total;
	}

	public static int hammingDistance(int one, int two){
		String sequenceX = modToBinString(one);
		String sequenceY = modToBinString(two);
		int a = 0;
		
		for (int x = 0; x < sequenceX.length(); x++) {
    		if (sequenceX.charAt(x) != sequenceY.charAt(x)) {
        		a += 1;
    		}
		}
		return a;
	}

	public static String modToBinString(int intg){
		String str = Integer.toBinaryString(intg);
		int padding=0;
		String x = "";
		if(str.length() == 24){
			return str;
		}else{
			padding = 24 - str.length();
			
			for(int i=0; i<padding; i++)
				x += "0";
			x += str;
			return x;
		}
	}

	public static Graph getGraph(ArrayList<Integer> list){
		Graph g = new Graph(list.size());
		Iterator<Integer> it = list.iterator();
		int first = it.next(); 
		int one = 0;
		int two = 1;
		while(it.hasNext()){
			int sec = it.next();
			g.addEdge(one, two, hammingDistance(first, sec), first);
			one++;
			two++;
			first = sec;
		}
		return g;
	}

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(new File("/home/mayur/Downloads/clustering_big.txt"));
		int numberOfNodes = in.nextInt();
		int numberOfBits = in.nextInt();
		String binString = in.nextLine();
		ArrayList<Integer> list = new ArrayList<Integer>();
		int numberOfPoints = 0;
		while(numberOfPoints<1000){
			StringBuilder tempString = new StringBuilder();
			for(int i=0; i<24; i++){
				tempString.append(in.nextInt());
			}
			list.add(Integer.parseInt(tempString.toString(), 2));
			numberOfPoints++;
		}
		Collections.sort(list);
		Graph g = getGraph(list);
		System.out.println("Completed forming graph");
		g.graphInfo();
		g.formClusters();
	}
}