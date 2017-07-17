package xstampp.stpatcgenerator.util.jobs;

import java.util.Observable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;

import xstampp.util.XstamppJob;

/**
 * This class controls the zooming jobs for zest graph. (But has not been used in the project)
 * 
 * @author Ting Luk-He
 *
 */
public class GraphZoomJob extends XstamppJob{
	String scale;
	Graph graph;
	public GraphZoomJob(String name, Graph graph, String scale) {
		super(name);
		this.graph = graph;
		this.scale = scale;
	}

	@Override
	protected Observable getModelObserver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		if(graph != null && scale != null){
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					ZoomManager zoomManager = new ZoomManager(
				    	    graph.getRootLayer(), 
				    	    graph.getViewport() );
				    	zoomManager.setZoomAsText(scale);
				}
			});
		} else {
			return Status.CANCEL_STATUS;
		}
		
		return Status.OK_STATUS;
	}
}
