package xstampp.stpatcgenerator.ui.views.utils;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import xstampp.stpatcgenerator.controller.STPATCGController;

public interface IDrawablePart {

	public void paint(GC gc, Rectangle rect, boolean paintControls);
	
	public void paint(GC gc, Rectangle rect, Image img);

	public void setdModel(STPATCGController controller);

}