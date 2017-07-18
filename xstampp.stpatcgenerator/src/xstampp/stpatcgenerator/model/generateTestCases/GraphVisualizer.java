package xstampp.stpatcgenerator.model.generateTestCases;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.*;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.*;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgrapht.*;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;

import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.fsm.EFSM;
import xstampp.stpatcgenerator.model.stateflow.fsm.StateEFSM;

public class GraphVisualizer extends JApplet{
	private static final long serialVersionUID = 2202072534703043194L;
    private static final Dimension DEFAULT_SIZE = new Dimension(730, 520);
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    private JGraphModelAdapter m_jgAdapter;
    private mxGraph graph;
    private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
    EFSM fsm;
    Tree tree;
    public GraphVisualizer() {

    }

    public mxGraph getGraph() {
        return graph;
    }

    public GraphVisualizer(EFSM fsm) {
        this.fsm = fsm;
        

    }
 public GraphVisualizer(EFSM fsm, Tree tree) {
        this.fsm = fsm;
        this.tree=tree;
        

    }
    public void createGraph(EFSM fsm) {
        GraphVisualizer applet = new GraphVisualizer();

        applet.fsm = fsm;
        applet.init();

        JScrollPane jsp = new JScrollPane(this);

    }

    /**
     * {@inheritDoc}
     */
    private void adjustDisplaySettings(JGraph jg) {
        jg.setPreferredSize(DEFAULT_SIZE);

        Color c = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter("bgcolor");
        } catch (Exception e) {
        }

        if (colorStr != null) {
            c = Color.decode(colorStr);
        }

        jg.setBackground(c);
    }

    public void randomizeLocationsVertices(JGraph jgraph) {
        HashSet<Point2D.Double> unique = new HashSet<Point2D.Double>();
        GraphLayoutCache cache = jgraph.getGraphLayoutCache();
        Random r = new Random(System.nanoTime());
        for (Object item : jgraph.getRoots()) {

            GraphCell cell = (GraphCell) item;
            CellView view = cache.getMapping(cell, true);
            Rectangle2D bounds = view.getBounds();
            int currentSize = unique.size();
            double x = 0;
            double y = 0;
            while (unique.size() == currentSize) {
                x = r.nextDouble() * 400;
                y = r.nextDouble() * 50;
                if (x < DEFAULT_SIZE.width && y < DEFAULT_SIZE.height) {
                    unique.add(new Point2D.Double(x, y));
                }
            }
            bounds.setRect(x, y, bounds.getWidth(), bounds.getHeight());
        }

    }

    public void randomizeLocationsVertex(JGraph jgraph) {

        GraphLayoutCache cache = jgraph.getGraphLayoutCache();

        Random r = new Random();
        for (Object item : jgraph.getRoots()) {

            GraphCell cell = (GraphCell) item;
            CellView view = cache.getMapping(cell, true);
            Rectangle2D bounds = view.getBounds();

            bounds.setRect(r.nextDouble() * 400, r.nextDouble() * 50,
                    bounds.getWidth(), bounds.getHeight());

        }

        cache.reload();

        jgraph.repaint();

    }

    @SuppressWarnings("unchecked") // FIXME hb 28-nov-05: See FIXME below
    private void positionVertexAt(Object vertex, int x, int y, int z, int w) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell(vertex);
        AttributeMap attr = cell.getAttributes();
        Rectangle2D bounds = GraphConstants.getBounds(attr);

        Rectangle2D newBounds
                = new Rectangle2D.Double(x, y, z, w);

        GraphConstants.setBounds(attr, newBounds);

        // TODO: Clean up generics once JGraph goes generic
        AttributeMap cellAttr = new AttributeMap();
        cellAttr.put(cell, attr);

        m_jgAdapter.edit(cellAttr, null, null, null);
    }

    /**
     * Example of how to randomize vertex locations
     */
    public void randomizeLocations(JGraph jgraph) {
        GraphLayoutCache cache = jgraph.getGraphLayoutCache();
        Random r = new Random();
        for (Object item : jgraph.getRoots()) {
            GraphCell cell = (GraphCell) item;
            CellView view = cache.getMapping(cell, true);
            Rectangle2D bounds = view.getBounds();
            bounds.setRect(r.nextDouble() * 200, r.nextDouble() * 200, bounds.getWidth(), bounds.getHeight());
        }
        cache.reload();
        jgraph.repaint();
    }

    /**
     * General graph settings.
     *
     * @param graph the graph to configure.
     */
    private void configureGraph(mxGraph graph) {
        graph.setEnabled(false);
        graph.setCellsResizable(true);
        graph.setConstrainChildren(true);
        graph.setExtendParents(true);
        graph.setExtendParentsOnAdd(true);
        graph.setDefaultOverlap(0);
    }

    /**
     * General graph component settings.
     *
     * @param graphComponent
     */
    private void configureGraphComponent(mxGraphComponent graphComponent) {
        graphComponent.getViewport().setOpaque(true);
        graphComponent.getViewport().setBackground(Color.WHITE);
        graphComponent.setConnectable(false);
    }

    /**
     * Create a new style for process vertices.
     *
     * @return the created style.
     */
    private Hashtable<String, Object> createProcessStyle() {
        Hashtable<String, Object> style = new Hashtable<String, Object>();
        style.put(mxConstants.STYLE_FILLCOLOR, mxUtils.getHexColorString(Color.WHITE));
        style.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        style.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(0, 0, 170)));
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style.put(mxConstants.STYLE_PERIMETER, mxConstants.PERIMETER_ELLIPSE);
        return style;
    }

    /**
     * Create a new style for object vertices.
     *
     * @return the created style.
     */
    private Map<String, Object> createObjectStyle() {
        Map<String, Object> style = new Hashtable<String, Object>();
        style.put(mxConstants.STYLE_FILLCOLOR, mxUtils.getHexColorString(Color.WHITE));
        style.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(0, 110, 0)));
        style.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        style.put(mxConstants.STYLE_PERIMETER, mxConstants.PERIMETER_RECTANGLE);
        return style;
    }

    /**
     * Create a new style for state vertices.
     *
     * @return the created style.
     */
    private Map<String, Object> createStateStyle() {
        Map<String, Object> style = new Hashtable<String, Object>();
        style.put(mxConstants.STYLE_FILLCOLOR, mxUtils.getHexColorString(Color.WHITE));
        style.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(91, 91, 0)));
        style.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        style.put(mxConstants.STYLE_PERIMETER, mxConstants.PERIMETER_RECTANGLE);
        style.put(mxConstants.STYLE_ROUNDED, true);
        return style;
    }

    /**
     * Create a new style for agent links.
     *
     * @return the created style.
     */
    /**
     * Execute the program.
     *
     * @param args ignored.
     */
    public boolean isInGraph(String vertex, List<String> vertices) {
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

// create a JGraphT graph
        ListenableGraph g = new ListenableDirectedGraph(DefaultEdge.class);

        // create a visualization using JGraph, via an adapter
     

        graph = new JGraphXAdapter(g);
        // Control-drag should clone selection
        resize(d);
  add( new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        List<StateEFSM> tempList;

        tempList = fsm.getFsmGstates();
        EFSM newFSM = new EFSM();
        newFSM.setDatavariables(fsm.getDatavariables());
        newFSM.setFsmTreestates(fsm.getFsmGstates());
        newFSM.setfsmTruthTable(fsm.getfsmTruthTable());
        newFSM.setStartState(fsm.getStartState());
        newFSM.setFsmstates(fsm.getFsmstates());
        newFSM.setCurrentState(fsm.getCurrentState());
        newFSM.setFSMErrors(fsm.getFSMErrors());
        newFSM.setFinalStates(fsm.getFinalStates());
        newFSM.setInitialState(fsm.getInitialState());

        StateEFSM start = new StateEFSM();
        start.setId("10000");
        start.setName("start");
        StateTransition startTransition = new StateTransition();
        startTransition.setSSID("100000");
        startTransition.setDefault(false);
        startTransition.setCondition("true");
        startTransition.setSource(start.getId());
       System.out.println (fsm.getInitialState().getName() + fsm.getInitialState().getId());
        startTransition.setDestination(fsm.getInitialState().getId());

        // fsm.addTransition(start
        List<String> vertices = new ArrayList<String>();

        vertices.add(start.getName());
        g.addVertex(start.getName());
        for (StateEFSM s : tempList) {

            g.addVertex(s.getName());
            vertices.add(s.getName());

        }
         
             
        if (isInGraph(start.getName(), vertices) && isInGraph( fsm.getInitialState().getName(), vertices)) {
            g.addEdge("start", fsm.getInitialState().getName());
        }
        
        int index = 0;
        for (StateTransition t : newFSM.getfsmTruthTable()) {

            if (t.getSource() != null && t.getDestination() != null) {
                StateEFSM source = newFSM.getstateFSM(t.getSource());

                StateEFSM destination = newFSM.getstateFSM(t.getDestination());
                if (source != null && destination != null) {
                    if (isInGraph(source.getName(), vertices) && isInGraph(destination.getName(), vertices)) {
                        g.addEdge(source.getName(), destination.getName(), "t" + index + "=" + t.getCondition());
                        index++;
                    }
                }
            }
        }
        // randomizeLocations(jgraph);
        try {

            final mxGraphComponent graphComponent = new mxGraphComponent(graph);
            configureGraphComponent(graphComponent);
            add(graphComponent, BorderLayout.CENTER);
            
            graph.getModel().beginUpdate();

            mxHierarchicalLayout layout = new mxHierarchicalLayout(graph, SwingConstants.WEST);
            layout.setResizeParent(false);
            layout.setMoveParent(false);
            layout.setInterHierarchySpacing(10.0);
            layout.setInterRankCellSpacing(50.0);
            layout.setIntraCellSpacing(200.0);

            layout.setParallelEdgeSpacing(500.0);
            layout.setOrientation(mxConstants.DIRECTION_MASK_WEST);

            layout.setFineTuning(true);

           /* Map<String, Object> edgeStyle = new HashMap<String, Object>();
            //edgeStyle.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ORTHOGONAL);
            edgeStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
            edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
            edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
            edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
            edgeStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#ffffff");

            mxStylesheet stylesheet = new mxStylesheet();
            stylesheet.setDefaultEdgeStyle(edgeStyle);
            graph.setStylesheet(stylesheet);*/

            Map<String, Object> EdgeStyle = graph.getStylesheet().getDefaultEdgeStyle();
            EdgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.OrthConnector);
            EdgeStyle.put(mxConstants.STYLE_STROKECOLOR, "red");
            EdgeStyle.put(mxConstants.STYLE_STROKEWIDTH, 1); 
            layout.setDisableEdgeStyle(true);
            layout.execute(graph.getDefaultParent());

            //mxParallelEdgeLayout parallellayout = new mxParallelEdgeLayout(graph, SwingConstants.WEST);

          //  parallellayout.execute(graph.getDefaultParent());

         /*   graph.setCellsEditable(false);
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
            graph.setEdgeLabelsMovable(false);*/

        } finally {
            graph.getModel().endUpdate();
        }

    }
}
