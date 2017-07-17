package stpaverifier.ui.views.utils;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import stpaverifier.controller.IProperty;
import stpaverifier.controller.model.STPAVerifierController;

/**
 *  an object of PropertyEditingSupport contains an object of DataModelController
 *  and can edit/change property values contained in that controller object
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class PropertyEditingSupport extends EditingSupport {
	STPAVerifierController controller;
	
	public PropertyEditingSupport(ColumnViewer viewer) {
		super(viewer);
	}

	@Override
	protected void setValue(Object element, Object value) {
		this.controller.setProperty(((IProperty)element).getUUID(),(String) value);
	}
	
	@Override
	protected Object getValue(Object element) {
		return ((IProperty)element).getsFormular(this.controller.isUseSpin(), false);
	}
	
	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor((Composite) getViewer().getControl());
	}
	
	@Override
	protected boolean canEdit(Object element) {
		//returns true only if the information can be stored in a DataModel object
		return this.controller != null && !((IProperty)element).isLocked();
	}

	/**
	 * sets the dataModelController to which this edittingSupport writes,
	 * without a controller this editing support will always be disabled
	 * 
	 * @param controller an instance of a dataModelController which is than used to store the text changes 
	 */
	public void setController(STPAVerifierController controller) {
		this.controller = controller;
	}
}
