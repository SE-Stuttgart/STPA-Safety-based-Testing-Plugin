package stpaverifier;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import stpaverifier.controller.IVerifierController;
import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.controller.model.STPAVerifierServiceFactory.STPAControllerService;
import stpaverifier.ui.wizards.STPAVerifierExportWizard;
import stpaverifier.util.ModelFileObserver;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "stpaVerifier"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private STPAVerifierController dataModel;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		dataModel = new STPAVerifierController();
		dataModel.addObserver(ModelFileObserver.getFileObserver());
		ModelFileObserver.getFileObserver().setController(dataModel);
		STPAControllerService service =(STPAControllerService) PlatformUI.getWorkbench().getService(IVerifierController.class);
		service.setController(dataModel);
		PlatformUI.getWorkbench().addWorkbenchListener(new IWorkbenchListener() {
			

			@Override
			public boolean preShutdown(IWorkbench workbench, boolean forced) {
//				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(editorListener);
				PlatformUI.getWorkbench().removeWorkbenchListener(this);
				return true;
			}
			
			@Override
			public void postShutdown(IWorkbench workbench) {
				// TODO Auto-generated method stub
				
			}
		});
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		ModelFileObserver.getFileObserver().setController(null);
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public void fetchWizardInfos(IWizard wizard){
		if(wizard instanceof STPAVerifierExportWizard){
			((STPAVerifierExportWizard) wizard).setController(dataModel);
		}
	}
	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				path);
	}
}
