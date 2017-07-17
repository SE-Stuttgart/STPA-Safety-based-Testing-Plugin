package stpaverifier.ui.views;


import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import stpaverifier.controller.ObserverValues;
import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.ui.CompositeWithUDContent;
import stpaverifier.ui.views.utils.AModelContentView;
import stpaverifier.ui.views.utils.FoldableView;

/**
 *  The Main Configuration View which also holds the data model controller object and 
 *  controls therefore the stpa checkers life cycle 
 *    
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class ConfigurationViewPart extends AModelContentView{

	public static final String ID = "stpaVerifier.Mainview"; 
	private FoldableView modelView;
	private FoldableView modexView;
	private CompositeWithUDContent content;
	
	public ConfigurationViewPart() {
		super();
	}
	
	@Override
	public void createPartControl(Composite parent) {
		content = new CompositeWithUDContent(parent, SWT.NONE);
		content.setcTopContent(modelView);
		content.setcBottomContent(modexView);
		
	}
	@Override
	public void setdModel(STPAVerifierController dModel) {
		modelView = new ConfigureGeneralsFolder();
		modexView = new ConfigureModexFolder();
		modelView.setdModel(dModel);
		modexView.setdModel(dModel);
		super.setdModel(dModel);
	}
	/**
	 * this implementation of update handles changes of the model file
	 * if a file has been chosen it opens it in the accurate editor 
	 */
	@Override
	public void update(Observable o, Object arg) {
		ObserverValues value = (ObserverValues) arg;
		switch(value){
		case USE_SPIN:
			if(o instanceof STPAVerifierController){
				if(((STPAVerifierController) o).isUseSpin()){
					content.setTopTitle("Spin");
				}else{
					content.setTopTitle("NuSMV");
				}
			}
			break;
		
		default:
			break;
			
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}



}
