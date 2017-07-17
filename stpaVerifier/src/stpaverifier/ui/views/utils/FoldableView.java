/**
 * 
 */
package stpaverifier.ui.views.utils;

import org.eclipse.ui.IViewPart;

import stpaverifier.controller.model.STPAVerifierController;

/**
 * @author Lukas Balzer
 *
 */
public interface FoldableView extends IViewPart {

	void activate();
	
	/**
	 * @param dModel the dModel to set
	 */
	public void setdModel(STPAVerifierController dModel);
}
