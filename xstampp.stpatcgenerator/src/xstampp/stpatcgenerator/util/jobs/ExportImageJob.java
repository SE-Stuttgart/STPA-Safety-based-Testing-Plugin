package xstampp.stpatcgenerator.util.jobs;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import xstampp.stpatcgenerator.controller.STPATCGController;
import xstampp.stpatcgenerator.ui.editors.EFSMTreeGraphEditor;
import xstampp.stpatcgenerator.ui.editors.StateflowTreeGraphEditor;
import xstampp.stpatcgenerator.ui.views.TestCaseHistogrammView;
import xstampp.util.XstamppJob;

/**
 * This class controls the jobs for export images.
 * 
 * @author Ting Luk-He
 *
 */
public class ExportImageJob extends XstamppJob {

	private String id;
	private STPATCGController controller;
	private String path;
//	private IDrawablePart part;
	private boolean showPreview;
	private Class<?> clazz;
	private TestCaseHistogrammView view;
	private JFreeChart chart;
	private GraphViewer viewer;
	private IEditorReference[] editorReference;
	private StateflowTreeGraphEditor sbmEditor;
	private EFSMTreeGraphEditor efsmEditor;
	
	public ExportImageJob(String name, STPATCGController controller, String path, Class<?> clazz,String id) {
		super(name);
		this.clazz = clazz;
		this.id = id;
		setShowPreview(true);
		 
//		part = (IDrawablePart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(id);
		this.controller = controller;
		this.path = path;
		
		if(id.equals(StateflowTreeGraphEditor.ID)){
			this.editorReference = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditors(null, id, IWorkbenchPage.MATCH_ID);
			this.sbmEditor = (StateflowTreeGraphEditor) editorReference[0].getEditor(true);
		}else if(id.equals(EFSMTreeGraphEditor.ID)){
			this.editorReference = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditors(null, id, IWorkbenchPage.MATCH_ID);
			this.efsmEditor = (EFSMTreeGraphEditor) editorReference[0].getEditor(true);
		}
	}
	
	public ExportImageJob(String name, STPATCGController controller, String path, Class<?> clazz,String id, JFreeChart chart) {
		super(name);
		this.clazz = clazz;
		this.id = id;
		setShowPreview(true);
		 
//		part = (IDrawablePart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(id);
//		this.controller = controller;
		this.path = path;
		
		if(id.equals(TestCaseHistogrammView.ID) && chart != null){
			this.view = (TestCaseHistogrammView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(id);
			this.chart = chart;
		}
	}
	
	@Override
	protected Observable getModelObserver() {
		return controller;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
//		if(part == null){
//   			try {
//   				part = (IDrawablePart) clazz.newInstance();
//   			} catch (InstantiationException | IllegalAccessException e) {
//   				e.printStackTrace();
//   				return Status.CANCEL_STATUS;
//   			}
//   			part.setdModel(controller);
//   		}	
		if(editorReference != null){
			Display.getDefault().asyncExec(new Runnable() {			
				@Override
				public void run() {	

					GC gc = null;
					Rectangle bounds= null;
					Image img= null;
	           		if(id.equals(StateflowTreeGraphEditor.ID)) {
	           			gc = new GC(sbmEditor.getGraph());
	               		bounds = sbmEditor.getGraph().getBounds();
	               		img = new Image(sbmEditor.getGraph().getDisplay(), bounds);
	           		} else if(id.equals(EFSMTreeGraphEditor.ID)) {
	           			gc = new GC(efsmEditor.getGraph());
	               		bounds = efsmEditor.getGraph().getBounds();
	               		img = new Image(efsmEditor.getGraph().getDisplay(), bounds);
	           		}
					
	           		if(gc != null && img != null){
	           			ImageLoader loader = new ImageLoader();
	               		gc.copyArea(img, 0, 0);
	               		loader.data = new ImageData[]{img.getImageData()};
	               		if(path.toLowerCase().endsWith(".png")){
	               			loader.save(path, SWT.IMAGE_PNG);
	               		}else{
	               			loader.save(path, SWT.IMAGE_JPEG);
	               		}
	           		}         		
				}
			});
			File imageFile = new File(this.path);
	   		if (imageFile.exists() && this.isShowPreview()) {
	   			if (Desktop.isDesktopSupported()) {
	   				try {
	   					Desktop.getDesktop().open(imageFile);
	   				} catch (IOException e) {
	   					return Status.CANCEL_STATUS;
	   				}
	   			}
	   		}
		} else if (view != null && chart != null){
			Display.getDefault().asyncExec(new Runnable() {			
				@Override
				public void run() {	
					try {
						saveChart(path, chart, 850, 270);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});			
		}
		
		return Status.OK_STATUS;
	}
		
	public void saveChart(String filename, JFreeChart chart, int width, int height) throws Exception {
		if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg"))
			ChartUtilities.saveChartAsJPEG(new File(filename), chart, width, height);
		else
			ChartUtilities.saveChartAsPNG(new File(filename), chart, width, height);

	}
	
	/**
	 * @return the showPreview
	 */
	public boolean isShowPreview() {
		return this.showPreview;
	}

	/**
	 * @param showPreview the showPreview to set
	 */
	public void setShowPreview(boolean showPreview) {
		this.showPreview = showPreview;
	}

}
