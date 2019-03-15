/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shortestpathapp;

/**
 *
 * @author cheeyauk Graph Factory is a module to draw directed graphs coded
 * using the jgraph library. Most functions are catered for this application and
 * provide useful information for solving the Bellman Ford problem (adjacency
 * matrix). This is based on Factory design pattern where most customized
 * features are stored and changed in this factory isolated from the main
 * environment.
 */
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class GraphFactory {

    // Global Variables

    public static int MAX_VALUE = 9999;
    public DefaultWeightedEdge[] EdgeList;
    public Map x;

    // Transform graph into a type can be plotted irectly into swing UI
    public JGraphXAdapter<String, MyEdge> GenerateGraphAdapter(ListenableGraph<String, MyEdge> g) {
        JGraphXAdapter<String, MyEdge> graphAdapter
                = new JGraphXAdapter<String, MyEdge>(g);
        graphAdapter.setCellStyle("strokeColor=#000000", graphAdapter.getEdgeToCellMap().values().toArray());
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        return graphAdapter;
    }

    // Extended edge class
    public static class MyEdge extends DefaultWeightedEdge {
        @Override
        public String toString() {
 
            return String.valueOf(getWeight());
        }
    }

    // Update a hash map used to store all edge information
    public void updateHashMap(String vertexInfo, MyEdge e) {
        x.put(vertexInfo, e);
    }

    // Function to update color change to input edges for displaying shortest path
    public void changeEdgeColor(ArrayList al, JGraphXAdapter<String, MyEdge> graphAdapter) {
        Object[] edgeCellArray = new Object[al.size()];
        HashMap<MyEdge, com.mxgraph.model.mxICell> edgeToCellMap
                = graphAdapter.getEdgeToCellMap();
        for (int i = 0; i <= al.size() - 1; i++) {
            edgeCellArray[i] = edgeToCellMap.get(x.get(al.get(i)));

        }
        graphAdapter.setCellStyle("strokeColor=#000000", edgeToCellMap.values().toArray());
        graphAdapter.setCellStyle("strokeColor=#00FF00", edgeCellArray);
        //  mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        // layout.execute(graphAdapter.getDefaultParent());

    }

    // Default color for graph edges is black
    public void setDefaultColor(JGraphXAdapter<String, MyEdge> graphAdapter) {

        HashMap<MyEdge, com.mxgraph.model.mxICell> edgeToCellMap
                = graphAdapter.getEdgeToCellMap();

        graphAdapter.setCellStyle("strokeColor=#000000", edgeToCellMap.values().toArray());

    }

    // Function to create attach a new edge to the graph with specified weigth
    public void setEdge(ListenableDirectedWeightedGraph<String, MyEdge> g, String v1, String v2, int weight) {
        MyEdge e = g.addEdge(v1, v2);
        x.put(v1.substring(1) + "," + v2.substring(1), e);
        g.setEdgeWeight(e, weight);
    }

    // Generate adjacency matrix for solving Bellman Problem
    public static int[][] generateAdjacencyMatrix(int numberOfVertices, ListenableGraph<String, GraphFactory.MyEdge> listenableGraph) {
        int adjacencyMatrix[][] = new int[numberOfVertices + 1][numberOfVertices + 1];
        for (int startNode = 1; startNode <= numberOfVertices; startNode++) {
            for (int endNode = 1; endNode <= numberOfVertices; endNode++) {
                adjacencyMatrix[startNode][endNode] = MAX_VALUE;
            }
        }
        for (int i = 1; i <= numberOfVertices; i++) {
            Object[] x = listenableGraph.edgesOf("x" + i).toArray();
            for (Object edgeObj : x) {
                GraphFactory.MyEdge edge = (GraphFactory.MyEdge) edgeObj;
                int a = Integer.parseInt(listenableGraph.getEdgeTarget(edge).substring(1));
                if (a != i) {
                    int w = (int) listenableGraph.getEdgeWeight(edge);
                    adjacencyMatrix[i][a] = w;
                }
            }
        }
        return adjacencyMatrix;
    }

    // Creates an empty graph
    public ListenableGraph<String, MyEdge> buildEmptyGraph() {
        x = new HashMap();
        ListenableDirectedWeightedGraph<String, MyEdge> g
                = new ListenableDirectedWeightedGraph<String, MyEdge>(MyEdge.class);

        String x1 = "x1";

        g.addVertex(x1);

        return g;
    }

    // Creates a default graph with four vertices
    public ListenableGraph<String, MyEdge> buildGraph4v() {
        x = new HashMap();
        ListenableDirectedWeightedGraph<String, MyEdge> g
                = new ListenableDirectedWeightedGraph<String, MyEdge>(MyEdge.class);

        String x1 = "x1";
        String x2 = "x2";
        String x3 = "x3";
        String x4 = "x4";
        g.addVertex(x1);
        g.addVertex(x2);
        g.addVertex(x3);
        g.addVertex(x4);
        setEdge(g, x1, x2, 1);
        setEdge(g, x2, x1, 1);
        setEdge(g, x2, x3, -2);
        setEdge(g, x3, x1, -4);
        setEdge(g, x3, x4, 5);

        return g;
    }

      // Creates a default graph with five vertices
    public ListenableGraph<String, MyEdge> buildGraph5v() {
        ListenableDirectedWeightedGraph<String, MyEdge> g
                = new ListenableDirectedWeightedGraph<String, MyEdge>(MyEdge.class);
        x = new HashMap();
        String x1 = "x1";
        String x2 = "x2";
        String x3 = "x3";
        String x4 = "x4";
        String x5 = "x5";
        g.addVertex(x1);
        g.addVertex(x2);
        g.addVertex(x3);
        g.addVertex(x4);
        g.addVertex(x5);

        setEdge(g, x1, x2, 3);
        setEdge(g, x2, x3, 2);
        setEdge(g, x2, x4, 20);
        setEdge(g, x3, x1, 3);
        setEdge(g, x1, x4, 3);
        setEdge(g, x4, x5, 4);
        setEdge(g, x3, x5, 10);

        return g;
    }

      // Creates a default graph with nine vertices. This is taken from lecture directly
    public ListenableGraph<String, MyEdge> buildGraph9v() {
        int numberOfVertices = 9;
        x = new HashMap();
        ListenableDirectedWeightedGraph<String, MyEdge> g
                = new ListenableDirectedWeightedGraph<String, MyEdge>(MyEdge.class);
        EdgeList = new DefaultWeightedEdge[15];
        String x1 = "x1";
        String x2 = "x2";
        String x3 = "x3";
        String x4 = "x4";
        String x5 = "x5";
        String x6 = "x6";
        String x7 = "x7";
        String x8 = "x8";
        String x9 = "x9";

        g.addVertex(x1);
        g.addVertex(x2);
        g.addVertex(x3);
        g.addVertex(x4);
        g.addVertex(x5);
        g.addVertex(x6);
        g.addVertex(x7);
        g.addVertex(x8);
        g.addVertex(x9);

        setEdge(g, x2, x1, 4);
        setEdge(g, x1, x3, 3);
        setEdge(g, x3, x1, 6);
        setEdge(g, x3, x2, 3);
        setEdge(g, x2, x8, 2);
        setEdge(g, x1, x8, -4);
        setEdge(g, x4, x3, -1);
        setEdge(g, x7, x4, 2);
        setEdge(g, x2, x4, 1);
        setEdge(g, x7, x2, 3);
        setEdge(g, x8, x7, 5);
        setEdge(g, x4, x6, 7);
        setEdge(g, x5, x4, 3);
        setEdge(g, x6, x5, -4);
        setEdge(g, x6, x9, -3);
        setEdge(g, x9, x1, -5);
        return g;
    }

     // Creates a default graph with nine vertices. This is taken from youtube
    public ListenableGraph<String, MyEdge> buildGraph9vAlternate() {
        int numberOfVertices = 9;
        x = new HashMap();
        ListenableDirectedWeightedGraph<String, MyEdge> g
                = new ListenableDirectedWeightedGraph<String, MyEdge>(MyEdge.class);
        EdgeList = new DefaultWeightedEdge[15];
        String x1 = "x1";
        String x2 = "x2";
        String x3 = "x3";
        String x4 = "x4";
        String x5 = "x5";
        String x6 = "x6";
        String x7 = "x7";
        String x8 = "x8";
        String x9 = "x9";

        g.addVertex(x1);
        g.addVertex(x2);
        g.addVertex(x3);
        g.addVertex(x4);
        g.addVertex(x5);
        g.addVertex(x6);
        g.addVertex(x7);
        g.addVertex(x8);
        g.addVertex(x9);

        setEdge(g, x1, x2, 2);
        setEdge(g, x2, x6, -3);
        setEdge(g, x6, x3, 1);
        setEdge(g, x3, x4, 7);
        setEdge(g, x4, x7, -5);
        setEdge(g, x7, x3, 8);
        setEdge(g, x6, x8, 6);
        setEdge(g, x6, x9, -3);
        setEdge(g, x9, x8, 1);
        setEdge(g, x8, x5, 2);
        return g;
    }

}
