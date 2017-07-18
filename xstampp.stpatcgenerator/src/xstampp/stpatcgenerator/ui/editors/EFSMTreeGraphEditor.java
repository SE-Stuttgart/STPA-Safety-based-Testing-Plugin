package xstampp.stpatcgenerator.ui.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import xstampp.stpatcgenerator.controller.STPATCGController;
import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.fsm.EFSM;
import xstampp.stpatcgenerator.model.stateflow.fsm.StateEFSM;
import xstampp.stpatcgenerator.ui.views.utils.IDrawablePart;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.stpatcgenerator.wizards.ConfigurationWizard;
/**
 * This class contains the editor for the extended finite state machine Tree Graph.
 * 
 * @author Ting Luk-He
 *
 */
public class EFSMTreeGraphEditor extends TCGeneratorAbstractEditor implements IDrawablePart{
	public static final String ID = "xstampp.stpatcgenerator.editor.efsmTreeGraph";
//	private EFSM fsm = STPATCGModel.getFsm();
	private EFSM fsm = STPATCGModelController.getSfmHandler().getFsm();
	private Graph graph;
	private Tree tree = STPATCGModelController.getConfWizard().getTree();
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		if (!(input instanceof TCGeneratorEditorInput)) {
			throw new RuntimeException("Wrong input");
		}
		this.setInput((TCGeneratorEditorInput) input);
		setSite(site);
		setInput(input);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		// Graph will hold all other objects
	    graph = new Graph(parent, SWT.NONE);
	    TCGeneratorPluginUtils.clearGraph(graph);
	    
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
	    startNode.setBackgroundColor(new Color(null, 255, 153, 51));
	    GraphNode initialNode = new GraphNode(graph, SWT.NONE, fsm.getInitialState().getName());
	    GraphConnection gConnection = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, startNode,
	    		initialNode);
	    gConnection.setText("("+ startNode.getText() + ":" + initialNode.getText() + ")");
	    gConnection.setLineColor(new Color(null, 0, 0, 0));
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
	    int i = 0;
	    Map<String,Integer> curveDepth = new HashMap<String, Integer>();
	    for (StateTransition t : newFSM.getfsmTruthTable()) {
	    	GraphNode source = nodeMap.get(tree.getNameOfstate(t.getSource()));
	    	GraphNode destination = nodeMap.get(tree.getNameOfstate(t.getDestination()));
	    	GraphConnection graphConnect = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, source,
					destination);
	    	if(source.equals(destination)){
	    		graphConnect.setCurveDepth(10);
	    	}else if(curveDepth.isEmpty()){
	    		graphConnect.setCurveDepth(0);
	    		curveDepth.put(source.getText() + "," + destination.getText(), 0);
	    	}else if(curveDepth.get(destination.getText() + "," + source.getText()) == null && curveDepth.get(source.getText() + "," + destination.getText()) == null){
	    		graphConnect.setCurveDepth(0);
	    		curveDepth.put(source.getText() + "," + destination.getText(), 0);
	    	}else {
	    		graphConnect.setCurveDepth(10);
	    	}	    	
	    	graphConnect.setText(t.getCondition());
	    	graphConnect.setLineColor(randomColor());
	    	i++;
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
		
	}
	
	private Color randomColor(){
		Random rand = new Random();
		int randomR = rand.nextInt((255 - 0) + 1) + 0;
		int randomG = rand.nextInt((255 - 0) + 1) + 0;
		int randomB = rand.nextInt((255 - 0) + 1) + 0;
		return new Color(null, randomR, randomG, randomB);
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	@Override
	public void paint(GC gc, Rectangle rect, boolean paintControls) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paint(GC gc, Rectangle rect, Image img) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setdModel(STPATCGController controller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
