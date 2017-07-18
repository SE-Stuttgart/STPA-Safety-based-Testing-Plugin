package xstampp.stpatcgenerator.ui.editors;


import java.awt.GridBagLayout;
import java.util.List;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
//import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.StateNode;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.stpatcgenerator.util.jobs.GraphZoomJob;
import xstampp.stpatcgenerator.wizards.ConfigurationWizard;
/**
 * Class to illustrate the stateflow model in tree graph.
 * @author Ting Luk-He
 *
 */
public class StateflowTreeGraphEditor extends TCGeneratorAbstractEditor{

	public static final String ID = "xstampp.stpatcgenerator.editor.stateflowtreegraph";
//	private GraphViewer viewer;
	private Tree tree = STPATCGModelController.getConfWizard().getTree();
	private Graph graph;
	
	// EFSM fsm;
	// Composite parentFrame;
	private TCGeneratorEditorInput input;

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub
		if (!(input instanceof TCGeneratorEditorInput)) {
			throw new RuntimeException("Wrong input");
		}
		this.setInput((TCGeneratorEditorInput) input);
		setSite(site);
		setInput(input);
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
	public void createPartControl(Composite parent) {
		
//		Label zoomLbl = new Label(parent, SWT.NONE);
//		zoomLbl.setText("Zoom:");
//		final Combo scale = new Combo(parent, SWT.READ_ONLY);
//		String items[] = {"25%","50%", "100%", "150%", "200%"};
//		scale.setItems(items);
//		
//		scale.addSelectionListener(new SelectionListener(){
//
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				// TODO Auto-generated method stub				
//				String s = scale.getText();
//				GraphZoomJob job = new GraphZoomJob("Zooming Graph", graph, s);
//				job.schedule();
//			}
//
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
		// Graph will hold all other objects
	    graph = new Graph(parent, SWT.NONE);
	    TCGeneratorPluginUtils.clearGraph(graph);
//	    graph.setPreferredSize(600, 400);
	    
	    // now a few nodes
	    StateNode rootNode = tree.getRoot();
	    GraphNode graphRoot = new GraphNode(graph, SWT.NONE, rootNode.getName());
	    graphRoot.setBackgroundColor(new Color(null, 255, 153, 51));
	    traverseTree(rootNode, graph, graphRoot);
	    	    
	    graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);

	    graph.addSelectionListener(new SelectionAdapter() {
	      @Override
	      public void widgetSelected(SelectionEvent e) {
	        System.out.println(e);
	      }

	    });

	}


	/**
	 * Traverse Tree with breadth-first search 
	 * @param stateNode
	 * 				Parent node for tree traverse in type of <node>StateNode</node>
	 * @param graph
	 * 				Graph which contains all tree nodes and connections
	 * @param parent
	 * 				Parent node in type of <node>GraphNode</node>
	 */
	public void traverseTree(StateNode stateNode, Graph graph, GraphNode parent) {	
		  if(stateNode == null)
			  return;
		  if(stateNode.isHasChildren()){
			  List<StateNode> children = stateNode.getChildren();
			  for(StateNode s: children){
				  GraphNode child = new GraphNode(graph, SWT.NONE, s.getName());
				  GraphConnection graphConnect = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, parent,
					        child);
				  graphConnect.setText("(" + stateNode.getName() + ":" + s.getName() + ")");
				  graphConnect.setLineColor(new Color(null, 0, 0, 0));
				  traverseTree(s, graph, child);
			  }
		  }
	}
	

	public void setFocus() {
		// TODO Auto-generated method stub
//		viewer.getControl().setFocus();
	}

	public Graph getGraph(){
		return this.graph;
	}

	public TCGeneratorEditorInput getInput() {
		return input;
	}

	public void setInput(TCGeneratorEditorInput input) {
		this.input = input;
	}
	
	
}
