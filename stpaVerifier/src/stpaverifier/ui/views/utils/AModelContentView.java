package stpaverifier.ui.views.utils;

import java.util.Observer;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import stpaverifier.controller.IVerifierController;
import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.controller.model.STPAVerifierServiceFactory.STPAControllerService;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public abstract class AModelContentView extends ViewPart implements Observer {
	

	private STPAVerifierController dModel;
	private Control control;
	/**
	 * @return the dModel
	 */
	protected STPAVerifierController getdModel() {
		return dModel;
	}

	public AModelContentView() {
		STPAControllerService service =(STPAControllerService) PlatformUI.getWorkbench().getService(IVerifierController.class);
		service.registerView(this);
	}
	/**
	 * @param dModel the dModel to set
	 */
	public void setdModel(STPAVerifierController dModel) {
		this.dModel = dModel;
		this.dModel.addObserver(this);
	}

	@Override
	public void dispose() {
		if(this.dModel!= null){
			this.dModel.deleteObserver(this);
		}
		super.dispose();
	}

	/**
	 * @param control the control to set
	 */
	public void setControl(Control control) {
		this.control = control;
		this.control.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if(dModel!= null){
					dModel.deleteObserver(AModelContentView.this);
				}
			}
		});
	}


}
