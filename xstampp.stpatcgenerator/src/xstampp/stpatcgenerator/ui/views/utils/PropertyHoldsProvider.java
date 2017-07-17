package xstampp.stpatcgenerator.ui.views.utils;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import xstampp.stpatcgenerator.Activator;
import xstampp.stpatcgenerator.controller.IProperty;
import xstampp.stpatcgenerator.util.ColorManager;

public class PropertyHoldsProvider extends ColumnLabelProvider{
	private static final Image successImage = Activator.getImageDescriptor("icons/CheckSuccess.png").createImage();
	private static final Image failImage = Activator.getImageDescriptor("icons/CheckFailed.png").createImage();
	private static final Image syntaxCorrectImage = Activator.getImageDescriptor("icons/SyntaxCorrectCheck.png").createImage();
	private static final Image syntaxErrImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
	private static final Image processingImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_STOP);
	private static final Image pauseImage = Activator.getImageDescriptor("icons/pause.gif").createImage();

	@Override
	public String getText(Object element) {
		int state = ((IProperty)element).getState();
		String label = null;
		if((state & IProperty.STATE_SUCCESS) != 0){
			label = "validated";
		}else if(state == IProperty.STATE_COUNTEREXAMPLE){
			label = "failed with Counterexample";
		}else if((state & IProperty.STATE_FAILED) != 0){
			label = "failed";
		}else if((state & IProperty.STATE_WAITING) != 0){
			label = "waiting";
		}else if((state & IProperty.STATE_CANCELED) != 0){
			label = "canceled";
		}else if((state & IProperty.STATE_VALIDATING) != 0){
			label = "processing...";
		}else if((state & IProperty.STATE_PAUSED) != 0){
			label = "paused";
		}
		
		if((state & IProperty.STATE_SYNTAX_ERROR) != 0){
			label = "syntax error!";
		}if((state & IProperty.STATE_SYNTAX_CORRECT) != 0){
			label = "syntax correct";
		}
		if(label == null){
			return "unchecked";
		}else{
			return label;
		}
		
	}
	
	@Override
	public Color getForeground(Object element) {
		int state = ((IProperty)element).getState();
		if((state & (IProperty.STATE_SUCCESS | IProperty.STATE_SYNTAX_CORRECT)) != 0){
			return ColorManager.COLOR_GREEN;
		}if((state & IProperty.STATE_FAILED) != 0){
			return ColorManager.COLOR_RED;
		}if((state & IProperty.STATE_SYNTAX_ERROR) != 0){
			return ColorManager.COLOR_YELLOW;
		}
		return null;
	}
	
	@Override
	public Image getImage(Object element) {
		
		int state = ((IProperty)element).getState();
		if((state & IProperty.STATE_SUCCESS) != 0){
			return successImage;
		}if((state & IProperty.STATE_FAILED) != 0){
			return failImage;
		}if((state & IProperty.STATE_SYNTAX_CORRECT) != 0){
			return syntaxCorrectImage;
		}if((state & IProperty.STATE_VALIDATING) != 0){
			return processingImage;
		}if((state & IProperty.STATE_SYNTAX_ERROR) != 0){
			return syntaxErrImage;
		}if((state & IProperty.STATE_PAUSED) != 0){
			return pauseImage;
		}
		return null;
	}
	
	@Override
	public String getToolTipText(Object element) {
		int state = ((IProperty)element).getState();
		if((state & IProperty.STATE_SYNTAX_ERROR) != 0){
			return ((IProperty)element).getMessage();
		}else if(state == IProperty.STATE_COUNTEREXAMPLE){
			return "click to open the counterexample";
		}
		return null;
	}
	
}
