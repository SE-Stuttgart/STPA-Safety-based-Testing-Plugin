package xstampp.stpatcgenerator.ui.views;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.StateNode;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;

public class StateFlowTreeGraphView extends ViewPart implements PaintListener, IZoomableWorkbenchPart{

	public static final String ID = "xstampp.stpatcgenerator.view.stateflowtreegraph";
	
	private Tree tree = STPATCGModelController.getConfWizard().getTree();
	private Graph graph;
//	private GraphViewer viewer;
	@Override
	public void createPartControl(Composite parent) {

		// Graph will hold all other objects
	    graph = new Graph(parent, SWT.NONE);
	    // now a few nodes
	    StateNode rootNode = tree.getRoot();
	    GraphNode graphRoot = new GraphNode(graph, SWT.NONE, rootNode.getName());
	    traverseTree(rootNode, graph, graphRoot);

	    graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);

	    graph.addSelectionListener(new SelectionAdapter() {
	      @Override
	      public void widgetSelected(SelectionEvent e) {
	        System.out.println(e);
	      }

	    });
	    
//	    viewer = new GraphViewer(parent, SWT.NONE);
//	    viewer.setControl(graph);
//	    viewer.getGraphControl().addMouseWheelListener( new MouseWheelListener() {
//            @Override
//            public void mouseScrolled(final MouseEvent ev) {
//                if (( ev.stateMask & SWT.CTRL ) == 0)
//                    return;
//                                
//                if (ev.count > 0) {
//                    viewer.getGraphControl().getZoomManager().zoomOut();
//                } else if (ev.count < 0) {
//                    viewer.getGraphControl().getZoomManager().zoomIn();
//                }
//            }
//        } );
	}

//	public void createPartControl(Composite parent) {
//		viewer = new GraphViewer(parent, SWT.BORDER);
//		viewer.setContentProvider(new ZestNodeContentProvider());
//		viewer.setLabelProvider(new ZestLabelProvider());
//		NodeModelContentProvider model = new NodeModelContentProvider();
//		viewer.setInput(model.getNodes());
//		LayoutAlgorithm layout = setLayout();
//		viewer.setLayoutAlgorithm(layout, true);
//		viewer.applyLayout();
//	}
	
	private LayoutAlgorithm setLayout() {
		LayoutAlgorithm layout;
		// layout = new
		// SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		layout = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		// layout = new
		// RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		return layout;

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
				  traverseTree(s, graph, child);
			  }
		  }
	  }
	
	public Graph getGraph(){
		return this.graph;
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

//	@Override
//	public void paint(GC gc, Rectangle rect, boolean paintControls) {
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
	
	public Image export(){
//		GC gc = new GC(viewer.getControl());
//		Rectangle bounds = viewer.getControl().getBounds();
//		Image img = new Image(viewer.getControl().getDisplay(), bounds);
		
//		GC gc = new GC(viewer.getGraphControl());
//		Rectangle bounds = viewer.getGraphControl().getBounds();
//		Image img = new Image(viewer.getGraphControl().getDisplay(), bounds);
		
		GC gc = new GC(graph);
		Rectangle bounds = graph.getBounds();
		Image img = new Image(graph.getDisplay(), bounds);
		try{
			gc.copyArea(img, 0, 0);
			ImageLoader imageLoader = new ImageLoader();

			imageLoader.data = new ImageData[]{img.getImageData()};
			imageLoader.save("/Users/Ting/NetBeansProjects/testIMG3.png", SWT.IMAGE_PNG);
		}finally {
		    img.dispose();
		    gc.dispose();
		}		
		
		return img;
	}

//	@Override
//	public void paint(GC gc, Rectangle rect, Image img) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		// TODO Auto-generated method stub
		return null;
	}

}
