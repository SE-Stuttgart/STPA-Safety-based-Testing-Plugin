package xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

public class TreeGraph extends JApplet {
	 private static final long serialVersionUID = 2202072534703043194L;
	    private static final Dimension DEFAULT_SIZE = new Dimension(730, 520);
	    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
	    private JGraphModelAdapter m_jgAdapter;
	    private mxGraph graph;
	    private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
	    Tree tree;

	    public TreeGraph() {

	    }

	    public mxGraph getGraph() {
	        return graph;
	    }

	    public TreeGraph(Tree tree) {
	        this.tree = tree;

	    }

	    public void createGraph(Tree tree) {
	        TreeGraph applet = new TreeGraph();

	        applet.tree = tree;
	        applet.init();

	        add(new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	    }

	    public boolean isInTree(String vertex, List<String> vertices) {
	        boolean found = false;

	        for (String s : vertices) {
	            if (s.equals(vertex)) {
	                found = true;
	                break;
	            }
	        }
	        return found;
	    }

	    public void init(Dimension d) {
	    	System.out.println("################### Drawing Tree #####################");
	    	// create a JGraphT graph
	        UndirectedGraph g = new SimpleGraph(DefaultWeightedEdge.class);
	        // create a visualization using JGraph, via an adapter

	        // Control-drag should clone selection
	        resize(d);

	        List<StateNode> tempList;

	        tempList = tree.getListOfState();
	        	        
	        StateNode start = tree.getRoot();
	        List<String> vertices = new ArrayList<String>();
	        g.addVertex(start.getName() + "\t");
	        vertices.add(start.getName() + "\t");
	        for (StateNode s : tempList) {

	            g.addVertex(s.getName() + "\t");

	            vertices.add(s.getName() + "\t");
	        }
	        for (StateNode tc : start.getChildren()) {

	            if (isInTree(start.getName() + "\t", vertices) && isInTree(tc.getName() + "\t", vertices)) {
	                g.addEdge(start.getName() + "\t", tc.getName() + "\t");
	            }
	        }
	        for (StateNode t : tempList) {

	            for (StateNode child : tempList) {
	                if (child.getParentID().equals(t.getId()) && t.isHasChildren()) {
	                    if (isInTree(t.getName() + "\t", vertices) && isInTree(child.getName() + "\t", vertices)) {
	                        g.addEdge(t.getName() + "\t", child.getName() + "\t");
	                    }
	                }

	            }
	        }

	        
	        // randomizeLocations(jgraph);
	        try {
	            graph = new JGraphXAdapter(g);

	            final mxGraphComponent graphComponent = new mxGraphComponent(graph);
	           
	            add(graphComponent, BorderLayout.CENTER);
	            graph.getModel().beginUpdate();

	            mxHierarchicalLayout layout = new mxHierarchicalLayout(graph, SwingConstants.WEST);
	            layout.setResizeParent(false);
	            layout.setMoveParent(false);
	            layout.setInterHierarchySpacing(10.0);
	            layout.setInterRankCellSpacing(50.0);
	            layout.setIntraCellSpacing(100.0);

	            layout.setParallelEdgeSpacing(100.0);
	            layout.setOrientation(mxConstants.DIRECTION_MASK_WEST);
	            graph.setVertexLabelsMovable(true);
	            layout.setFineTuning(false);

	            Map<String, Object> edgeStyle = new HashMap<String, Object>();
	           edgeStyle.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ORTHOGONAL);
	             edgeStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
	            edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
	            edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
	            edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
	            edgeStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#ffffff"); 

	            mxStylesheet stylesheet = new mxStylesheet();
	            stylesheet.setDefaultEdgeStyle(edgeStyle);
	            graph.setStylesheet(stylesheet);
	            
	            layout.execute(graph.getDefaultParent());

	            mxParallelEdgeLayout parallellayout = new mxParallelEdgeLayout(graph, SwingConstants.WEST);
	            parallellayout.execute(graph.getDefaultParent());
	            graph.setCellsEditable(false);
	            graph.setAllowDanglingEdges(false);
	            graph.setAllowLoops(false);
	            graph.setCellsDeletable(false);
	            graph.setCellsCloneable(false);
	            graph.setCellsDisconnectable(false);
	            graph.setDropEnabled(false);
	            graph.setSplitEnabled(false);
	            graph.setCellsBendable(false);
	            graph.setConnectableEdges(false);
	            graph.setGridEnabled(true);
	            graph.setEdgeLabelsMovable(false);
	        } finally {
	            graph.getModel().endUpdate();
	        }

	    }
}
