package stpaverifier.ui.views.utils;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import stpaverifier.controller.model.STPAVerifierController;

public interface IDrawablePart {

	public void paint(GC gc, Rectangle rect, boolean paintControls);

	public void setdModel(STPAVerifierController controller);

}