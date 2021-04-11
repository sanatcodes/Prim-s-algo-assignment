//Sanat Thukral
//DT228 year 2

// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;

class GraphLists 
{
    class Node {
        public int vert;
        public int wgt;
        public Node next;

    }
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;
    
    // used for traversing graph
    private int[] visited;
    private int id;
    
    
    // default constructor
    public GraphLists(String graphFile)  throws IOException {
        int u, v;
        int e, wgt;
        Node t, t1;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create sentinel node
        z = new Node(); 
        z.next = z;
        
        // create adjacency lists, initialised to sentinel node z       
        adj = new Node[V+1];        
        for(v = 1; v <= V; ++v)
            adj[v] = z;   
                        
        
       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));    
            
            // write code to put edge into adjacency matrix     
            t = new Node();
            t.vert = v;
            t.wgt = wgt;
            t.next = adj[u];
            adj[u] = t;

            t1 = new Node();
            //covering both directions
            //inserting int adj[v]
            t1.vert = u;
            t1.wgt = wgt;
            t1.next = adj[v];
            adj[v] = t1; 
        }	       
    }// end constructor
   
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }

    //method to display the graph representation
    public void display() {
        int v;
        Node n;
        
        for(v=1; v<=V; ++v){
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");    
        }
        System.out.println("");
    }

    public void MST_Prim(int s)
    {
        int v, u;
        int wgt, wgt_sum = 0;
        int[]  dist, parent, hPos;
        Node t;

        //distance from each node
        dist = new int[V + 1];
        
        //parent node
        parent = new int[V + 1];

        //possition on the heap
        hPos = new int[V + 1];

        //initialising the above arrays 
        for(v=1; v <= V; v++){
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }

        dist[s] = 0;
        parent[s] = s;
        dist[0] = 0;


        Heap pq =  new Heap(V, dist, hPos);
        pq.insert(s);
        
        
        
        while (!(pq.isEmpty()))  
        {
            //adds v to the MST
            v = pq.remove();

            System.out.println("Adding Edge " + toChar(parent[v]) + "--(" + dist[v] + ")--" + toChar(v));

            //marks v as now in the MST
            dist[v] = -dist[v];

            for (t = adj[v]; t != z; t = t.next)
            {
                if(t.wgt < dist[t.vert] && dist[t.vert] > 0)
                {
                    dist[t.vert] = t.wgt;
                    parent[t.vert] = v;

                    //if the vertex is empty, insert next Vertex
                    if(hPos[t.vert] == 0)
                    {
                        pq.insert(t.vert);
                    }
                    else
                    {
                        pq.siftUp(hPos[t.vert]);
                    }
                }
            }
            
        }

        for(int i = 0; i < dist.length; i++) {
        	 wgt_sum += dist[i];
        }

        wgt_sum = wgt_sum * -1;

        System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");
        
        mst = parent;                      		
    }

    public void showMST()
    {
            System.out.print("\n\nMinimum Spanning tree parent array is:\n");
            for(int v = 1; v <= V; ++v)
                System.out.println(toChar(v) + " -> " + toChar(mst[v]));
            System.out.println("");
    }

    public void DFSutil( int s)
    {
        System.out.println("Starting Depth First Search ");
        id = 0;

        for(int i = 0; i < V; i++)
        {
            visited[i] = 0;
        }
        dfVisit(0,s);

    }

    private void dfVisit(int prev, int v)
    {
        visited[v] = ++id;

        System.out.println("visited Vertex" + toChar(v) + "along" + toChar(prev) + "----" + toChar(v));

        for(int u = 1; u <= v; u++)
        {
            if(visited[u] == 0 && adj[u].wgt != 0)
            {
                dfVisit(v,u);
            }
        }
    }

    
}// end of class 

class PrimLists {

    public static void main(String[] args) throws IOException
    {
        int s = 2;
        String fname = "wGraph1.txt";               

        GraphLists g = new GraphLists(fname);
        
        g.display();             
                
        g.DFSutil(s);
        
        // g.BF(s);
        
        g.MST_Prim(s);
        
        g.showMST();       
        
    }

}// end of main

/*

Heap Code for efficient implementationofPrim's Alg

*/

class Heap
{
    private int[] a;	   // heap array
    private int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    public boolean isEmpty() 
    {
        return N == 0;
    }


    public void siftUp( int k) 
    {
        int v = a[k];

 
        while (dist[v] < dist[a[k/2]])
        {
            hPos[a[k]] = k;
            a[k] = a[k/2];
            k = k / 2;
        }
        a[k] = v;
        hPos[v] = k;
        
    }


    public void siftDown( int k) 
    {
        int v, j;
       
        v = a[k];  
        
        // code yourself 
        // must use hPos[] and dist[] arrays
        while (k * 2 < N)
        {
            j = k * 2;
            if(j < N && dist[ a[j] ] > dist[ a[j+1] ])
            {
                j++;
            }

            if(dist[v] <= dist[ a[j]])
            {
                break;
            }

            hPos[ a[k] ] = j;

            a[k] = a[j];

            k = j;
        }

        a[k] = v;
        hPos[v] = k;
    }


    public void insert( int x) 
    {
        a[++N] = x;
        siftUp( N);
    }


    public int remove() 
    {   
        int v = a[1];
        hPos[v] = 0; // v is no longer in heap        
        
        a[1] = a[N--];
        siftDown(1);
        
        a[N+1] = 0;  // put null node into empty spot
        
        return v;
    }

    public void display() {
        System.out.println("Heap is: ");
        for(int i = 1; i < N; i++)
        {
            System.out.print(" " + a[i] + " -> ");
        }
    }

    

}// end main class 

//Queue class for implimenting BF traversal 

class Queue 
{

	private class Node 
	{
		int data;
		Node next;
	}

	Node z;
	Node head;
	Node tail;

	public Queue() 
	{
		z = new Node(); 
		z.next = z;
		head = z;  
		tail = null;
	}


	public void display() 
	{
		System.out.println("\nThe queue values are:\n");

		Node temp = head;
		while( temp != temp.next) 
		{
			System.out.print( temp.data + "  ");
			temp = temp.next;
		}
		System.out.println("\n");
	}


	public void enQueue( int x) 
	{
		Node temp;

		temp = new Node();
		temp.data = x;
		temp.next = z;

		if(head == z)    // case of empty list
		head = temp;
		else                // case of list not empty
		tail.next = temp;

		tail = temp;        // new node is now at the tail
	}


	// assume the queue is non-empty when this method is called
	public int deQueue() 
	{
		int data = head.data;
		head = head.next;

		return data;
	}


	public boolean isEmpty() 
	{
		if (head == null)
		return true;
		else
		return false;
	}
	
	public boolean isMember(int x)
	{
		Node d = head;
		
		while (d != tail)
		{
			if(d.data == x)
				return true;
			
			d = d.next;
		}
		
		return false;
	}
	
	public int size()
	{
		Node d = head;
		int x = 0;
		
		while (d != tail.next)
		{	
			d = d.next;
			x++;
		}
		
		return x;
	}
} // end of Queue class
