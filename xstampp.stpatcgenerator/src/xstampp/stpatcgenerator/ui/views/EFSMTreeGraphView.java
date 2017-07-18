package xstampp.stpatcgenerator.ui.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.fsm.EFSM;
import xstampp.stpatcgenerator.model.stateflow.fsm.StateEFSM;

/**
 * This class defines the view of safe test model tree graph.
 * @author Ting Luk-He
 *
 */
public class EFSMTreeGraphView extends ViewPart implements PaintListener, IZoomableWorkbenchPart{
	public static final String ID = "xstampp.stpatcgenerator.view.efsmTreeGraph";
//	private EFSM fsm = STPATCGModel.getFsm();
	private EFSM fsm = STPATCGModelController.getSfmHandler().getFsm();
	private Graph graph;
	private Tree tree = STPATCGModelController.getConfWizard().getTree();

	@Override
	public void createPartControl(Composite parent) {
		// Graph will hold all other objects
	    graph = new Graph(parent, SWT.NONE);
	    
	    // Start Node
	    StateEFSM start = new StateEFSM();
        start.setId("10000");
        start.setName("start");
        StateTransition startTransition = new StateTransition();
        startTransition.setSSID("100000");
        startTransition.setDefault(false);
        startTransition.setCondition("true");
        startTransition.setSource(start.getId());
        startTransition.setDestination(fsm.getInitialState().getId());
        
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
        
        Map<String, GraphNode> nodeMap = new HashMap<String, GraphNode>();
	    // add start Node and connection to the initial node
	    List<StateEFSM> tempList = fsm.getFsmGstates();	    
	    List<GraphNode> graphNodes = new ArrayList<GraphNode>();
	    GraphNode startNode = new GraphNode(graph, SWT.NONE, start.getName());
	    GraphNode initialNode = new GraphNode(graph, SWT.NONE, fsm.getInitialState().getName());
	    new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, startNode,
	    		initialNode);
	    nodeMap.put(startNode.getText(), startNode);
	    nodeMap.put(initialNode.getText(), initialNode);
	    // add graph nodes
	    for(StateEFSM s: tempList){
	    	if(!s.getName().equals(initialNode.getText())){
	    		GraphNode tmpNode = new GraphNode(graph, SWT.NONE, s.getName());
	    		graphNodes.add(tmpNode);
	    		nodeMap.put(tmpNode.getText(), tmpNode);
	    	}
	    }
	    // add connections
	    for (StateTransition t : newFSM.getfsmTruthTable()) {
	    	GraphNode source = nodeMap.get(tree.getNameOfstate(t.getSource()));
	    	GraphNode destination = nodeMap.get(tree.getNameOfstate(t.getDestination()));
//	    	System.out.println("source: " +  t.getSource());
//	    	System.out.println("destination: " +  t.getDestination());
	    	GraphConnection graphConnect = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, source,
					destination);
	    	graphConnect.setText(t.getCondition());
	    }
//	    for(int i = 0; i < tempList.size(); i++){
//	    	GraphNode source = null;
//	    	GraphNode destination = null;
//	    	for(GraphNode g: graphNodes){
//	    		if(g.getText().equals(tempList.get(i).getName()))
//	    			source = g;
//	    	}
//	    	for(StateNode sn: tempList.get(i).getLinkedStatesAsList()) {
//	    		for(int j = 0; j < graphNodes.size(); j++){
//	    			if (sn.getName().equals(graphNodes.get(j).getText())){
//	    				destination = graphNodes.get(j);
//	    				GraphConnection graphConnect = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, source,
//	    						destination);
//	    			}
//	    		}	    		
//	    	}
//	    	
//	    }
	    graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
//	    graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
	    // Selection listener on graphConnect or GraphNode is not supported
	    // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=236528
	    graph.addSelectionListener(new SelectionAdapter() {
	      @Override
	      public void widgetSelected(SelectionEvent e) {
	        System.out.println(e);
	      }

	    });
//	    traverseTree(rootNode, graph, graphRoot);
	}
	
	public void addGraphNodes(Graph graph){
		
	}
//	public void traverseTree(StateEFSM stateNode, Graph graph, GraphNode parent) {	
//		  if(stateNode == null)
//			  return;
//		  if(stateNode.isHasChildren()){
//			  List<StateNode> children = stateNode.getChildren();
//			  for(StateNode s: children){
//				  GraphNode child = new GraphNode(graph, SWT.NONE, s.getName());
//				  GraphConnection graphConnect = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, parent,
//					        child);
//				  graphConnect.setText("(" + stateNode.getName() + ":" + s.getName() + ")");
//				  traverseTree(s, graph, child);
//			  }
//		  }
//	  }
	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public void paint(GC gc, Rectangle rect, boolean paintControls) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void paint(GC gc, Rectangle rect, Image img) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void setdModel(STPATCGController controller) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void paintControl(PaintEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
