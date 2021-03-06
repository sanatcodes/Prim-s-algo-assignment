// Kruskal's Minimum Spanning Tree Algorithm
// Union-find implemented using disjoint set trees without compression

import java.io.*;    
 
class Edge {
    public int u, v, wgt;

    public Edge() {
        u = 0;
        v = 0;
        wgt = 0;
    }

    public Edge( int u, int v, int wgt) {
        this.u = u;
        this.v = v;
        this.wgt = wgt;
    }
    
    public void show() {
        System.out.print("Edge " + toChar(u) + "--" + wgt + "--" + toChar(v) + "\n") ;
    }
    
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
}


class Heap
{
	private int[] h;    //heap array 
    int N, Nmax;
    Edge[] edge;    //array  of edges


    // Bottom up heap construc
    public Heap(int _N, Edge[] _edge) {
        int i;
        Nmax = N = _N;
        h = new int[N+1];
        edge = _edge;
       
        // initially just fill heap array with 
        // indices of edge[] array.
        for (i=0; i <= N; ++i) 
            h[i] = i;
           
        // Then convert h[] into a heap
        // from the bottom up.
        for(i = N/2; i > 0; --i)
            siftDown(i);
    }


    private void siftDown( int k) {
        // int e, j;

        // e = h[k];
        while (k <= N/2)
        {
            int leftchild = k * 2;
            int rightchild = (k * 2) + 1;
            int smallerchild;

            //determine smaller child
            if ((rightchild < (N)) && (edge[h[leftchild]].wgt > edge[h[rightchild]].wgt))
            {
                smallerchild = rightchild;
            }
            else
            {
                smallerchild = leftchild;
            }

            //the indices in the heap represent indices to edge array. The top of 
            //the heap will be the indice of the edge with the lowest weight.
            //comparing parent with smallest we swap if greater

            if (edge[h[k]].wgt >= edge[h[smallerchild]].wgt)
            {   //swap indices
                int temp = h[smallerchild];
                h[smallerchild] = h[k];
                h[k] = temp;
            }
            k = smallerchild;
        }
        // h[k] = e;
    }


    public int remove() {
        h[0] = h[1];
        h[1] = h[N--];
        siftDown(1);
        return h[0];
    }
}

/****************************************************
*
*       UnionFind partition to support union-find operations
*       Implemented simply using Discrete Set Trees
*
*****************************************************/

class UnionFindSets
{
    private int[] treeParent;
    private int N;
    
    public UnionFindSets( int V)
    {
        N = V;
        treeParent = new int[V+1];
        // missing lines
        //initialising each set as a parent of itself
        for(int i = 0; i <= V; i++ )
        {
            treeParent[i] = i;
        }
    }

    public int findSet( int vertex)
    {   
        while ( vertex != treeParent[vertex])
        {
            vertex = treeParent[vertex];
        }
        
        return vertex;
        
    }
    
    public void union( int set1, int set2)
    {
        treeParent[findSet(set2)] = findSet(treeParent[set1]);
    }
    
    public void showTrees()
    {
        int i;
        for(i=1; i<=N; ++i)
            System.out.print(toChar(i) + "->" + toChar(treeParent[i]) + "  " );
        System.out.print("\n");
    }
    
    public void showSets()
    {
        int u, root;
        int[] shown = new int[N+1];
        for (u=1; u<=N; ++u)
        {   
            root = findSet(u);
            if(shown[root] != 1) {
                showSet(root);
                shown[root] = 1;
            }            
        }   
        System.out.print("\n");
    }

    private void showSet(int root)
    {
        int v;
        System.out.print("Set{");
        for(v=1; v<=N; ++v)
            if(findSet(v) == root)
                System.out.print(toChar(v) + " ");
        System.out.print("}  ");
    
    }
    
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
}

class Graph 
{ 
    private int V, E;
    private Edge[] edge;
    private Edge[] mst;        

    public Graph(String graphFile) throws IOException
    {
        int u, v;
        int w, e;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create edge array
        edge = new Edge[E+1];   
        
       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            w = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + w + ")--" + toChar(v));                         
            
            //making edge object
            edge[e] = new Edge(u,v,w);
        }
        System.out.println("");
    }


/**********************************************************
*
*       Kruskal's minimum spanning tree algorithm
*
**********************************************************/
public Edge[] MST_Kruskal() 
{
    int ei;
    Edge e;
    int uSet, vSet;
    UnionFindSets partition;
    
    // create edge array to store MST
    // Initially it has no edges.
    mst = new Edge[V-1];

    // priority queue for indices of array of edges
    Heap heap = new Heap(E, edge);

    // create partition of singleton sets for the vertices
    partition = new UnionFindSets(V);

    //initial sets
    partition.showSets();
    System.out.println("");
    
    for(int i = 0; i < V-1; i++)
    {
        uSet = edge[heap.h[1].u];
        vSet = edge[heap.h[1].v];

        //if uSet and Vset are not part of the same set, then call union to combine 
        if(partition.findSet(uSet) != partition.findSet(vSet))
        {
            partition.union(uSet, vSet);

            //remove edge from the heap
            ei = heap.remove();

            //add edge to mst 
            mst[i] = edge[ei];

            //print set
            partition.showSets();
            System.out.println("");


        }
        //remove from the heap, if both belong to the same set
        else{
            heap.remove();
        }
    }
    
    
    return mst;
}


    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }

    public void showMST()
    {
        System.out.print("\nMinimum spanning tree build from following edges:\n");
        for(int e = 0; e < V-1; ++e) {
            mst[e].show(); 
        }
        System.out.println();
       
    }

} // end of Graph class
    
    // test code
class KruskalTrees {
    public static void main(String[] args) throws IOException
    {
        String fname = "wGraph3.txt";
        //System.out.print("\nInput name of file with graph definition: ");
        //fname = Console.ReadLine();

        Graph g = new Graph(fname);

        g.MST_Kruskal();

        g.showMST();
        
    }
}    


