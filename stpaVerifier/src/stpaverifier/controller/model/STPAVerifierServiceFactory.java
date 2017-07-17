package stpaverifier.controller.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import stpaverifier.controller.IProperty;
import stpaverifier.controller.IVerificationResult;
import stpaverifier.controller.IVerifierController;
import stpaverifier.ui.views.utils.AModelContentView;
import xstampp.model.ISafetyDataModel;

public class STPAVerifierServiceFactory extends AbstractServiceFactory {
	private static STPAControllerService service;
	
	private STPAControllerService getService() {
		if(service == null){
			service = new STPAControllerService();
		}
		return service;
	}
	public STPAVerifierServiceFactory() {
	}

	@Override
	public Object create(@SuppressWarnings("rawtypes") Class serviceInterface, IServiceLocator parentLocator,
			IServiceLocator locator) {
		return getService();
	}

	public class STPAControllerService implements IVerifierController{
		private STPAVerifierController controller;
		private List<AModelContentView> views;
		/**
		 * @param controller the controller to set
		 */
		public void setController(STPAVerifierController controller) {
			this.controller = controller;
			if(views != null){
				for(AModelContentView view: views){
					view.setdModel(controller);
				}
			}
		}
		
		public void registerView(AModelContentView view){
			if(views == null){
				views = new ArrayList<>();
			}
			views.add(view);
			view.setdModel(controller);
		}
		
		@Override
		public boolean removeProperty(UUID id){
			if(controller != null){
				return controller.removeProperty(id);
			}
			return false;
		}
		
		@Override
		public boolean removeAllProperties(){
			if(controller != null){
				return controller.removeAllProperties();
			}
			return false;
		}

		@Override
		public boolean removeChecked() {
			if(controller != null){
				return controller.removeChecked();
			}
			return false;
		}

		@Override
		public boolean executeSTPAVerifier(boolean oneByOne) {
			if(controller != null){
				return controller.executeSTPAVerifier(oneByOne);
			}
			return false;
		}

		@Override
		public boolean checkSyntax() {
			if(controller != null){
				return controller.checkSyntax();
			}
			return false;
		}

		@Override
		public void setUseAllProperties(boolean shouldUse) {
			if(controller != null){
				controller.setUseAllProperties(shouldUse);
			}
		}

		@Override
		public void addLTLFormular(String sId, String formula) {
			if(controller != null){
				controller.addLTLFormular(sId,formula);
			}
		}

		@Override
		public void addCTLFormular(String sId, String formula) {
			if(controller != null){
				controller.addCTLFormular(sId,formula);
			}
		}

		@Override
		public boolean resetProperties() {
			if(controller != null){
				return controller.resetProperties();
			}
			return false;
		}

		@Override
		public boolean pauseVerification() {
			if(controller != null){
				return controller.pauseVerification();
			}
			return false;
		}

		@Override
		public boolean resumeVerification() {
			if(controller != null){
				return controller.resumeVerification();
			}
			return false;
		}
		@Override
		public boolean cancelVerification() {
			if(controller != null){
				return controller.cancelVerification();
			}
			return false;
		}
		
		@Override
		public IFile getLog() {
			if(controller != null){
				return controller.getLog();
			}
			return null;
		}

		@Override
		public boolean importData(ISafetyDataModel model) {
			if(controller != null){
				return controller.importData(model);
			}
			return false;
		}

		@Override
		public List<IProperty> getAllProperties() {
			if(controller != null){
				return controller.getAllProperties();
			}
			return null;
		}

		@Override
		public List<IVerificationResult> getResults() {
			if(controller != null){
				return controller.getResults();
			}
			return null;
		}

		@Override
		public void addObserver(Observer ob) {
			if(controller != null){
				controller.addObserver(ob);
			}
		}
		
		
	}
}
