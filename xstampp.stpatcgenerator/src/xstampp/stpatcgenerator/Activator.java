package xstampp.stpatcgenerator;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import xstampp.DefaultPerspective;
import xstampp.stpatcgenerator.controller.STPATCGController;
import xstampp.stpatcgenerator.wizards.STPATCGExportWizard;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "xstampp.stpatcgenerator"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	private STPATCGController dataModel;
	
	private static final Logger LOGGER = Logger.getRootLogger();
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		setDefaultPerspective();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
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
		if(wizard instanceof STPATCGExportWizard){
			((STPATCGExportWizard) wizard).setController(dataModel);
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
	
	public void setDefaultPerspective() {
		Activator.LOGGER.debug("Setup Default perspective"); //$NON-NLS-1$
		IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench()
				.getPerspectiveRegistry()
				.findPerspectiveWithId("xstampp.defaultPerspective"); //$NON-NLS-1$
		if (descriptor != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().setPerspective(descriptor);
		}
	}

}
