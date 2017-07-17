package stpaverifier.util.jobs;

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
import org.eclipse.ui.PlatformUI;

import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.ui.views.ResultsPieViewPart;
import stpaverifier.ui.views.utils.IDrawablePart;
import xstampp.util.XstamppJob;

public class ExportPieJob extends XstamppJob {

	private STPAVerifierController controller;
	private String path;
	private IDrawablePart part;
	private boolean showPreview;
	private Class<?> clazz;

	public ExportPieJob(String name,STPAVerifierController controller, String path, Class<?> clazz,String id) {
		super(name);
		this.clazz = clazz;
		
		setShowPreview(false);
		 
		part = (IDrawablePart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(id);
		this.controller = controller;
		this.path = path;
		
	}

	@Override
	protected Observable getModelObserver() {
		return controller;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if(part == null){
			try {
				part = (IDrawablePart) clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return Status.CANCEL_STATUS;
			}
			part.setdModel(controller);
		}
		Image img =new Image(null,new Rectangle(0, 0, 1000, 300));
		GC gc = new GC(img);
		part.paint(gc,img.getBounds(), false);
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[]{img.getImageData()};
		if(path.toLowerCase().endsWith(".png")){
			loader.save(path, SWT.IMAGE_PNG);
		}else{
			loader.save(path, SWT.IMAGE_JPEG);
		}
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
		return Status.OK_STATUS;
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
