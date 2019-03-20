package shortestpathapp;

import com.mxgraph.swing.mxGraphComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.border.*;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;

public class ShortestPathApp {

    public static int MAX_VALUE = 9999;
    private static int distances[];
    private static int parents[];
    public static int[][] adjacencyMatrix;
    private static int numberOfVertices;
    public static JGraphXAdapter<String, GraphFactory.MyEdge> graph;
    public static ListenableGraph<String, GraphFactory.MyEdge> graphHandle = new ListenableDirectedWeightedGraph<String, GraphFactory.MyEdge>(GraphFactory.MyEdge.class);
    public static BellmanMethod Bellman;
    public static JPanel mainPanel;
    public static JPanel buttonPanel;
    public static JPanel userControlPanel;
    public static JPanel distancePanel;
    public static JPanel graphPanel;
    public static JPanel displayPanel;
    public static JPanel drawPanel;

    public static void main(String[] args) {

        JFrame frame = new JFrame("BellMan Ford Shortest Path");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createGuiLayout();
        GraphFactory graphGenMod = new GraphFactory();

        /* Populate button panel
         Purpose of button panel allow selection of problem instances
         */
        JRadioButton problem1 = new JRadioButton("4 pt Transport problem");
        JRadioButton problem2 = new JRadioButton("5 pt Transport problem");
        JRadioButton problem3 = new JRadioButton("9 pt Transport problem (from lecture)");
        JRadioButton problem4 = new JRadioButton("9 pt Transport problem alternate");
        JRadioButton draw = new JRadioButton("Draw your own graph");
        ButtonGroup group = new ButtonGroup();
        group.add(problem1);
        group.add(problem2);
        group.add(problem3);
        group.add(problem4);
        group.add(draw);
        buttonPanel.add(problem1);
        buttonPanel.add(problem2);
        buttonPanel.add(problem3);
        buttonPanel.add(problem4);
        buttonPanel.add(draw);

        /* Populate distance panel
         A panel allow selection of source node and run the bellman ford algorithm to obtain shortest path
         */
        distancePanel.add(new JLabel("Source: (Ex: x1, x2,...)"));
        JButton runFullReport = new JButton("Run full report   ");
        JButton drawShortestPath = new JButton("Draw shortest distance");
        JTextField source = new JTextField();
        source.setPreferredSize(new Dimension(20, 20));
        JTextField destination = new JTextField();
        destination.setPreferredSize(new Dimension(20, 20));
        distancePanel.add(source);
        distancePanel.add(runFullReport);
        distancePanel.add(new JLabel("Destination: (Ex: x1, x2,...)"));
        distancePanel.add(destination);
        distancePanel.add(drawShortestPath);

        /* Populate draw and graph panels
         These two panels are where the user can view and modify the graph networks
         */
        JButton addVertex = new JButton(" Add Vertex");
        addVertex.setEnabled(false);
        JTextField addEdge = new JTextField("x1,x2,weight");
        addEdge.setPreferredSize(new Dimension(100, 30));
        JButton addAnEdge = new JButton("Add Edge:");
        addAnEdge.setEnabled(false);
        JLabel shortestDist = new JLabel();
        JLabel shortestDistNodes = new JLabel();
        drawPanel.add(addVertex);
        drawPanel.add(addAnEdge);
        drawPanel.add(addEdge);
        graphPanel.add(shortestDist);
        graphPanel.add(shortestDistNodes);
        ActionListener drawGraph = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton aButton = (AbstractButton) actionEvent.getSource();
                String selectedRadioButton = aButton.getText();
                addAnEdge.setEnabled(false);
                addVertex.setEnabled(false);
                if (selectedRadioButton.contains("4")) {
                    graphHandle = graphGenMod.buildGraph4v();
                    graph = graphGenMod.GenerateGraphAdapter(graphHandle);
                    numberOfVertices = 4;

                } else if (selectedRadioButton.contains("5")) {

                    graphHandle = graphGenMod.buildGraph5v();
                    graph = graphGenMod.GenerateGraphAdapter(graphHandle);
                    numberOfVertices = 5;

                } else if (selectedRadioButton.contains("alternate")) {
                    graphHandle = graphGenMod.buildGraph9vAlternate();
                    graph = graphGenMod.GenerateGraphAdapter(graphHandle);
                    numberOfVertices = 9;

                } else if (selectedRadioButton.contains("9")) {
                    graphHandle = graphGenMod.buildGraph9v();
                    graph = graphGenMod.GenerateGraphAdapter(graphHandle);
                    numberOfVertices = 9;
                } else {
                    addAnEdge.setEnabled(true);
                    addVertex.setEnabled(true);
                    graphHandle = graphGenMod.buildEmptyGraph();
                    graph = graphGenMod.GenerateGraphAdapter(graphHandle);
                    numberOfVertices = 1;
                }
                
                graphPanel.removeAll();
                
                graphPanel.add(new mxGraphComponent(graph));
                graphPanel.setPreferredSize(new Dimension(700, 500));
                graphPanel.add(shortestDist);
                graphPanel.add(shortestDistNodes);
                frame.getContentPane().revalidate();
            }
        };
        ActionListener customizingGraph = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton aButton = (AbstractButton) actionEvent.getSource();
                String selectedFeature = aButton.getText();
                try {
                    if (selectedFeature.contains("Add Vertex")) {
                        numberOfVertices++;
                        graphHandle.addVertex("x" + numberOfVertices);
                        graph = graphGenMod.GenerateGraphAdapter(graphHandle);
                        graphPanel.removeAll();
                        graphPanel.add(new mxGraphComponent(graph));
                        frame.getContentPane().revalidate();
                    } else if (selectedFeature.contains("Add Edge")) {
                        String[] stringSplit = addEdge.getText().split(",");
                        if (stringSplit.length < 3) {
                            JOptionPane.showMessageDialog(null, "You must enter an edge with format: x1,x2,weight");
                            return;
                        }
                        for (int i = 0; i < stringSplit.length - 1; i++) {
                            if (!isVertextNameValid(stringSplit[i])) {
                                JOptionPane.showMessageDialog(null, "Vertex name is not in the right format. Ex: x1, x2");
                                return;
                            }
                        }
                        if (!isInteger(stringSplit[2].trim())) {
                            JOptionPane.showMessageDialog(null, "Weight must be a valid integer");
                            return;
                        }
                        String src = stringSplit[0].trim();
                        String dest = stringSplit[1].trim();
                        String weight = stringSplit[2].trim();

                        GraphFactory.MyEdge e = graphHandle.addEdge(src, dest);
                        ListenableDirectedWeightedGraph<String, GraphFactory.MyEdge> g = (ListenableDirectedWeightedGraph<String, GraphFactory.MyEdge>) graphHandle;
                        g.setEdgeWeight(e, Integer.parseInt(weight));
                        graph = graphGenMod.GenerateGraphAdapter(graphHandle);

                        graphGenMod.updateHashMap(src.substring(1) + "," + dest.substring(1), e);
                        graphPanel.removeAll();
                        graphPanel.add(new mxGraphComponent(graph));
                        frame.getContentPane().revalidate();
                    } else {

                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    e.printStackTrace();
                }

            }
        };
        ActionListener calcDistance = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (group.getSelection() == null) {
                        JOptionPane.showMessageDialog(null, "You must specify a graph for running the algorithm");
                        return;
                    }

                    if (!isVertextNameValid(source.getText())) {
                        JOptionPane.showMessageDialog(null, "You must specify a valid source vertext from given graph");
                        return;
                    }
                    if (!isVertextNameValid(destination.getText())) {
                        JOptionPane.showMessageDialog(null, "You must specify a valid destination vertext from given graph");
                        return;
                    }
                    String startPt = source.getText().trim();
                    int startInt = Integer.parseInt(startPt.substring(1));
                    String endPt = destination.getText().trim();
                    int endInt = Integer.parseInt(endPt.substring(1));
                    distances = new int[numberOfVertices + 1];
                    parents = new int[numberOfVertices + 1];
                    recalculateBF(startInt);
                    ArrayList journey = new ArrayList();
                    if (Bellman.isNegativeCycle) {
                        shortestDistNodes.setText("");
                        shortestDist.setText("Negative cycle detected. Path not available");
                    } else if (distances[endInt] != MAX_VALUE) {
                        int v = endInt;
                        List<Integer> nodes = new ArrayList<>();
                        nodes.add(v);
                        while (parents[v] != MAX_VALUE) {
                            journey.add(parents[v] + "," + v);
                            v = parents[v];
                            nodes.add(v);
                        }
                        graphGenMod.changeEdgeColor(journey, graph);
                        String shortestDistText = "Shortest distance from " + startPt + " to " + endPt + " is " + distances[endInt] + "                   ";
                        String shortestDistNodesTxt = "";
                        for (int i = nodes.size(); i > 0; i--) {
                            shortestDistNodesTxt += "x" + nodes.get(i - 1);
                            if (i != 1) {
                                shortestDistNodesTxt += " ==> ";
                            }
                        }

                        shortestDist.setText(shortestDistText);
                        shortestDistNodes.setText(shortestDistNodesTxt);
                    } else {
                        graphGenMod.setDefaultColor(graph);
                        shortestDist.setText("No path available         ");
                        shortestDistNodes.setText("");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error");
                    e.printStackTrace();
                }
                frame.getContentPane().revalidate();
            }
        };
        ActionListener runReport = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (group.getSelection() == null) {
                    JOptionPane.showMessageDialog(null, "You must specify a graph for running the algorithm");
                    return;
                }

                if (!isVertextNameValid(source.getText())) {
                    JOptionPane.showMessageDialog(null, "You must specify a valid source vertext from given graph");
                    return;
                }
                if (!isVertextNameValid(destination.getText())) {
                    JOptionPane.showMessageDialog(null, "You must specify a valid destination vertext from given graph");
                    return;
                }
                String startPt = source.getText().trim();
                int startInt = Integer.parseInt(startPt.substring(1));
                distances = new int[numberOfVertices + 1];
                parents = new int[numberOfVertices + 1];
                recalculateBF(startInt);
                String report = "";
                if (Bellman.isNegativeCycle == true) {
                    infoBox("Negative Cycle detected", "Full distance Report");
                } else {
                    for (int vertex = 1; vertex <= numberOfVertices; vertex++) {

                        report += ("distance of source  " + source.getText() + " to x" + vertex + " is " + distances[vertex] + "\n");
                    }
                }
                infoBox(report, "Full distance Report");
            }
        };

        // Bind components to their respective event handlers
        drawShortestPath.addActionListener(calcDistance);
        runFullReport.addActionListener(runReport);
        problem1.addActionListener(drawGraph);
        problem2.addActionListener(drawGraph);
        problem3.addActionListener(drawGraph);
        problem4.addActionListener(drawGraph);
        draw.addActionListener(drawGraph);
        addVertex.addActionListener(customizingGraph);
        addEdge.addActionListener(customizingGraph);
        addAnEdge.addActionListener(customizingGraph);

        // Form the main panels
        userControlPanel.add(buttonPanel);
        userControlPanel.add(distancePanel);
        displayPanel.add(graphPanel);
        displayPanel.add(drawPanel);
        mainPanel.add(userControlPanel);
        mainPanel.add(displayPanel);

        // Display
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    // Message Box
    public static void infoBox(String message, String titleBar) {
        JOptionPane.showMessageDialog(null, message, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    // Recalculate Bellman Ford
    public static void recalculateBF(int startInt) {
        adjacencyMatrix = GraphFactory.generateAdjacencyMatrix(numberOfVertices, graphHandle);
        Arrays.fill(distances, MAX_VALUE);
        Arrays.fill(parents, MAX_VALUE);
        Bellman = new BellmanMethod(numberOfVertices, distances, parents);
        Bellman.BellmanFordLoop(startInt, adjacencyMatrix);
    }

    public static boolean isVertextNameValid(String label) {
        if (label == null || label.trim().length() <= 1 || !label.trim().startsWith("x")) {
            return false;
        }
        char[] toCharArray = label.trim().toCharArray();
        for (int i = 1; i < toCharArray.length; i++) {
            if (!Character.isDigit(toCharArray[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.digit(s.charAt(i), radix) < 0) {
                return false;
            }
        }
        return true;
    }

    // Create default GUI. Lenghty but important for user experience
    public static void createGuiLayout() {
        // Declare panels
        mainPanel = new JPanel();
        userControlPanel = new JPanel();
        buttonPanel = new JPanel();
        distancePanel = new JPanel();
        graphPanel = new JPanel();
        displayPanel = new JPanel();
        drawPanel = new JPanel();
        // Set Layout and preference
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        userControlPanel.setLayout(new BoxLayout(userControlPanel, BoxLayout.Y_AXIS));
        distancePanel.setLayout(new BoxLayout(distancePanel, BoxLayout.Y_AXIS));
        graphPanel.setLayout(new FlowLayout());
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        drawPanel.setLayout(new FlowLayout());
        buttonPanel.setLayout(new GridLayout(0, 1));

        buttonPanel.setPreferredSize(new Dimension(300, 300));
        distancePanel.setPreferredSize(new Dimension(100, 50));
        graphPanel.setPreferredSize(new Dimension(800, 600));
        drawPanel.setPreferredSize(new Dimension(100, 100));

        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, "Default Networks");
        TitledBorder titleForDistance = BorderFactory.createTitledBorder(lowerEtched, "Travel from");
        TitledBorder titleForGraph = BorderFactory.createTitledBorder(lowerEtched, "Weighted Graph representation");
        TitledBorder titleForDraw = BorderFactory.createTitledBorder(lowerEtched, "Draw your graph");
        buttonPanel.setBorder(title);
        distancePanel.setBorder(titleForDistance);
        graphPanel.setBorder(titleForGraph);
        drawPanel.setBorder(titleForDraw);

    }
}
