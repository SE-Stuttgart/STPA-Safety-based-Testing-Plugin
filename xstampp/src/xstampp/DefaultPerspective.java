package xstampp;

import org.apache.log4j.Logger;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * a simple perspective for displaying the projectExplorer and editor window
 * @author Lukas Balzer
 *
 */
public class DefaultPerspective implements IPerspectiveFactory {
	public static final String ID ="xstampp.defaultPerspective";
	private static final Logger LOGGER = Logger.getRootLogger();

	@Override
	public void createInitialLayout(IPageLayout layout) {
		DefaultPerspective.LOGGER.debug("Setup Default perspective"); //$NON-NLS-1$
		layout.setFixed(false);
		layout.setEditorAreaVisible(true);
		layout.addView(
				"astpa.explorer", IPageLayout.LEFT, 0.2f, layout.getEditorArea()); //$NON-NLS-1$
		
		layout.getViewLayout("astpa.explorer").setCloseable(false);
		
		
		IFolderLayout folder = layout.createFolder("buttomfolder", IPageLayout.BOTTOM, 0.65f, layout.getEditorArea());

		folder.addPlaceholder("A-CAST.view1");
		

	}
	
}
