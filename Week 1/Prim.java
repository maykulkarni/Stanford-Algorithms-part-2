import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Algorithms: Design and Analysis, Part 2
 * Programming Question - Week 1
 * Note : I'am not the original author of this code
 * the solution was inspired  by Felix Garcia Lainez
 * github profile : https://github.com/fgarcialainez
 */
public class Prim 
{
    private static int[][] graph = null;
    private static int numberOfNodes = 0;
    private static HashMap<Integer, ExpandedNode> spanningTree = new HashMap<Integer, ExpandedNode>();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        //READ THE GRAPH
        readGraphFromFile();
        
        System.out.println("*** Overall Cost => " + primMSTAlgorithm() + " ***");
    }
    
    /**
     * Running time O(mn)
     */
    private static int primMSTAlgorithm()
    {
	int overallCost = 0;
        int position = 0;
       
        spanningTree.put(0, new ExpandedNode(0, position));

	int minimumCost = Integer.MAX_VALUE;
		
        int minimumI = 0;
	int minimumJ = 0;
		
        while(!allNodesExpanded())
        {
            for(int i = 0; i < numberOfNodes; i++)
            {
		if(isNodeExpanded(i))
                {
                    for(int j = 0; j < numberOfNodes; j++)
                    {
                        if(!isNodeExpanded(j))
                        {
                            if(minimumCost > graph[i][j])
                            {
                                minimumCost = graph[i][j];
                                minimumI = i;
                                minimumJ = j;
                            }					
                        }
                    }
                }
            }
		
            spanningTree.put(minimumJ, new ExpandedNode(minimumJ, position));
            overallCost += graph[minimumI][minimumJ];
            minimumCost = Integer.MAX_VALUE;
	}

        return overallCost;
    }
    
    private static boolean allNodesExpanded(){
        return (spanningTree.size() == numberOfNodes);
    }
    
    private static boolean isNodeExpanded(int index){   
        return (spanningTree.get(index) != null);
    }
    
    /**
     * Reads the Job list 
     * @return A list of jobs 
     */
    private static void readGraphFromFile()
    {
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream("edges.txt");
            
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            String line = br.readLine();
            StringTokenizer tokens = new StringTokenizer(line);
            numberOfNodes = Integer.parseInt(tokens.nextToken());
             
            //INITIALIZE THE GRAPH
            graph = new int[numberOfNodes][numberOfNodes];
            for (int i = 0; i < numberOfNodes; i++){
		for(int j = 0; j < numberOfNodes; j++){
                    graph[i][j] = Integer.MAX_VALUE;
                }
            }
            
            while ((line = br.readLine()) != null)
            {
                // process the line
                tokens = new StringTokenizer(line);
                
                String firstToken = tokens.nextToken();
                String secondToken = tokens.nextToken();
                String thirdToken = tokens.nextToken();
                
                int i = Integer.parseInt(firstToken) - 1;
		int j = Integer.parseInt(secondToken) - 1;
		graph[i][j] = Integer.parseInt(thirdToken);
		graph[j][i] = Integer.parseInt(thirdToken);  
            }
            
            br.close();
        }catch (FileNotFoundException ex) {
            Logger.getLogger(Question1.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(Question1.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                fstream.close();
            } catch (IOException ex) {
                Logger.getLogger(Question1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

/**
 * This class represents a node
 */
class ExpandedNode
{
    private int nodeIndex; //the index of the node in the initial nodes array
    private int position; //the position in the spanning tree
    
    public ExpandedNode(int nodeIndex, int position){
        super();
        this.nodeIndex = nodeIndex;
        this.position = position;
    }
    
    public int getNodeIndex(){
        return nodeIndex;
    }
    
    public void setNodeIndex(int nodeIndex){
        this.nodeIndex = nodeIndex;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}