package xstampp.stpatcgenerator.model.generateTestCases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class TestGraph {
	private ArrayList<Vertex> vertices = new ArrayList<Vertex>();

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }

    public Stack<String> getPathVertices() {
        return pathVertices;
    }

    public void setPathVertices(Stack<String> pathVertices) {
        this.pathVertices = pathVertices;
    }

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    private int[][] adjMatrix;
    private Stack<Integer> path = new Stack<Integer>();   // the current path
    private Set<Integer> onPath = new HashSet<Integer>();
    private Stack<String> pathVertices = new Stack<String>();
    private String paths = "";

    public void addvertex(Vertex n, int i) {
        n.setIndex(i);
        vertices.add(i, n);
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public int[][] getAdjMatrix() {
        return adjMatrix;
    }
    public void printAdjMatrix ()
    {
        for (int i=0; i<adjMatrix.length; i++)
        {   System.out.print (i +" [");
            for (int j=0; j <adjMatrix.length; j++)
            {
                System.out.print(adjMatrix[i][j] +" ");
            }
             System.out.println("]");
        }
    }            
    public void setAdjMatrix(int[][] adjMatrix) {
        this.adjMatrix = adjMatrix;
    }

    public Stack<Integer> getPath() {
        return path;
    }

    public void setPath(Stack<Integer> path) {
        this.path = path;
    }

    public Set<Integer> getOnPath() {
        return onPath;
    }

    public void setOnPath(Set<Integer> onPath) {
        this.onPath = onPath;
    }

    public void initMatrix() {
        int size = vertices.size();
        adjMatrix = new int[size][size];
    }

    public String getVertexName(int i) {
        return vertices.get(i).getName();
    }

    public Vertex getVertex(String name) {
        Vertex v = null;
        for (Vertex vertex : vertices) {
            if (vertex.getName().equals(name)) {
                return v;

            }

        }
        return v;
    }

    public List<Integer> getAllVertexIndexes() {
        List<Integer> indexes = new ArrayList<Integer>();
        for (Vertex v : vertices) {
            indexes.add(v.getIndex());
        }

        return indexes;
    }

    public Vertex getVertexByIndex(int index) {
        for (Vertex v : vertices) {
            if (v.getIndex() == index) {
                return v;
            }
        }
        return null;
    }

    public int indexOf(Vertex v) {
        int index = -1;
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getName().equals(v.getName())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public int sizeOfEdges() {
        return edges.size();
    }

    public Edge getEdge(int start, int end) {
        for (Edge e : edges) {
            if (e.getFromIndex() == start && e.getToIndex() == end) {
                return e;

            }
        }
        return null;
    }

    public Map<String,String> getTransitionCondition(Vertex from, Vertex to) {
        String str="";
        String id="";
        for (Edge e : edges) {
            if (e.getFrom().getName().equals(from.getName()) && e.getTo().getData().getName().equals(to.getName())) {

                str= e.getTransitonsAction();
                id=e.getTranID();
                break;
            }
        }
        Map<String, String> tran;
        tran = new HashMap<String, String>();
        tran.put(id,str );
                
        return tran; 
    }

    public void addEdge(Edge edge) {
        if (adjMatrix == null) {
            System.out.println("Adjacency matrix not initialized");
            return;
        }
        int startIndex = indexOf(edge.getFrom());
        int endIndex = indexOf(edge.getTo());
       
        
        //    System.out.println(startIndex + "," + endIndex);
        adjMatrix[startIndex][endIndex] = 1;   // undirected graph, so edge
        //adjMatrix[endIndex][startIndex] = 1;   // added twice
        edge.setFromIndex(startIndex);
        edge.setToIndex(endIndex);
        edges.add(edge);
        
    }  // end of addEdge()

    public void removeEdge(Edge edge) {

        if (adjMatrix == null) {
            System.out.println("Adjacency matrix not initialized");
            return;
        }
        int startIndex = indexOf(edge.getFrom());
        int endIndex = indexOf(edge.getTo());

        adjMatrix[startIndex][endIndex] = 0;   // undirected graph, so edge
        adjMatrix[endIndex][startIndex] = 0;   // added twice
        int i = 0;
        for (Edge e : edges) {
            if (e.getFrom() == edge.getFrom() && e.getTo() == edge.getTo()) {
                edges.remove(i);
                break;
            }
            i++;
        }
    }

    private Vertex getUnvisitedAdjNode(Vertex n) // find a node adjacent to n that has not been visited
    {
        int index = indexOf(n);
        for (int j = 0; j < vertices.size(); j++) {   // scan the index row of adjMatrix[]
            if ((adjMatrix[index][j] == 1) && (!(vertices.get(j)).isVisited())) // j is adjacent and not yet visited
            {
                return vertices.get(j);
            }
        }
        return null;
    }  // end of getUnvisitedAdjNode()

    private void unvisitNodes() {
        for (Vertex n : vertices) {
            n.setVisit(false);
        }
    }  // end of unvisitNodes()

    public int size() {
        return this.vertices.size();
    }

    public List<Integer> getNeighbors(int v) {
        List<Integer> neighbors = new ArrayList<Integer>();
        for (int i = 0; i < adjMatrix.length; i++) {
            if (adjMatrix[v][i] == 1) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    public List<String> getNeighbors(String name) {
        Vertex v = getVertex(name);
        List<String> neighbors = new ArrayList<String>();
        System.out.println("index" + v.getName());
        int index = 0;
        if (v != null) {
            index = indexOf(v);
            System.out.println("index" + index);
            for (int i = 0; i < adjMatrix.length; i++) {
                if (adjMatrix[index][i] == 1) {
                    neighbors.add(vertices.get(i).getName());
                }
            }

        }
        return neighbors;
    }

    public String bfs(Vertex startNode) // breadth-first search of graph starting at startNode
    {
        LinkedList<Vertex> q = new LinkedList<Vertex>();  // acts as a queue
        String path = "";
        q.add(startNode);
        startNode.print();
        path += startNode.getName() + " ";
        startNode.setVisit(true);

        while (!q.isEmpty()) {
            Vertex n = q.remove();
            Vertex adj = null;
            while ((adj = getUnvisitedAdjNode(n)) != null) {
                adj.setVisit(true);
                adj.print();
                path += adj.getName() + " ";
                q.add(adj);
            }
        }
        unvisitNodes();
        System.out.println();
        return path;
    }  // end of bfs()

    public String dfs(Vertex startNode) // depth-first search of graph starting at startNode
    {
        Stack<Vertex> s = new Stack<Vertex>();  // uses a stack
        s.push(startNode);
        startNode.setVisit(true);
        String path = "";
        startNode.print();
        path += startNode.getName() + " ";
        while (!s.isEmpty()) {
            Vertex n = s.peek();
            Vertex adj = getUnvisitedAdjNode(n);
            if (adj != null) {
                adj.setVisit(true);
                adj.print();
                path += adj.getName() + " ";
                s.push(adj);
            } else {
                s.pop();
            }
        }
        unvisitNodes();
        System.out.println();
        return path;
    }  // end of dfs()

    public String getAllPathsAsString() {
        return paths;
    }

    // use DFS
    public void AllPathsByDFS(TestGraph G, int v, int t) {

        // add node v to current path from s
        path.push(v);
        onPath.add(v);
        pathVertices.push(getVertexName(v));
        // found path from s to t - currently prints in reverse order because of stack
        if (v == t) {
            //System.out.println(path);
          //  System.out.println(pathVertices);
            paths += pathVertices + " \n";

        } // consider all neighbors that would continue path with repeating a node
        else {
            for (int w : G.getNeighbors(v)) {

                if (!onPath.contains(w)) {
                    AllPathsByDFS(G, w, t);
                }
            }
        }

        // done exploring from v, so remove from path
        path.pop();
        pathVertices.pop();
        onPath.remove(v);
    }
}
