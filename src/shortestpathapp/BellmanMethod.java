package shortestpathapp;

import javax.swing.JOptionPane;

public class BellmanMethod {

    // Singleton variables used by current and parent class
    private static int distances[];
    private static int parents[];
    private static int numberOfVertices;
    public static int MAX_VALUE = 9999;
    public static int[][] adjacencyMatrix;
    public static boolean isNegativeCycle = false;

    // Main initialisation. Called from parent
    public BellmanMethod(int numberofvertices, int distancesIn[], int parentsIn[]) {
        this.numberOfVertices = numberofvertices;
        distances = distancesIn;
        parents = parentsIn;
    }

    // The main Bellman Loop. Calculation will perform BellmanCalculateDistance followed by BellmanCheckNegativeCycle 
    public void BellmanFordLoop(int source, int adjacencyMatrix[][]) {
        try {
            distances[source] = 0;
            BellmanCalculateDistance(adjacencyMatrix);
            BellmanCheckNegativeCycle(adjacencyMatrix);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    // Bellman relaxation cycles
    // Each node is relaxed for n times.
    private void BellmanCalculateDistance(int adjacencyMatrix[][]) {
        // repeat for number of nodes
        for (int repeat = 1; repeat <= numberOfVertices - 1; repeat++) {
            for (int startNode = 1; startNode <= numberOfVertices; startNode++) {
                for (int endNode = 1; endNode <= numberOfVertices; endNode++) {
                    if (adjacencyMatrix[startNode][endNode] != MAX_VALUE) {
                        if (distances[endNode] > distances[startNode] + adjacencyMatrix[startNode][endNode]) {
                            distances[endNode] = distances[startNode] + adjacencyMatrix[startNode][endNode];
                            parents[endNode] = startNode;
                        }
                    }
                }
            }
        }
    }

    // Check for negative matrix using additional relaxation and detection
    private void BellmanCheckNegativeCycle(int adjacencyMatrix[][]) {
        for (int startNode = 1; startNode <= numberOfVertices; startNode++) {
            for (int endNode = 1; endNode <= numberOfVertices; endNode++) {
                if (adjacencyMatrix[startNode][endNode] != MAX_VALUE) {
                    if (distances[endNode] > distances[startNode]
                            + adjacencyMatrix[startNode][endNode]) {
                        isNegativeCycle = true;
                    }
                }
            }
        }
    }

}
