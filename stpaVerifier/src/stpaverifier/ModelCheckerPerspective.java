package stpaverifier;

import org.apache.log4j.Logger;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import stpaverifier.ui.views.CounterexampleViewPart;
import stpaverifier.ui.views.FormularViewPart;
import stpaverifier.ui.views.ConfigurationViewPart;
import stpaverifier.ui.views.ResultsDiagViewPart;
import stpaverifier.ui.views.ResultsPieViewPart;
import stpaverifier.ui.views.ResultsViewPart;
import xstampp.ui.navigation.ProjectExplorer;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 * @see IPerspectiveFactory
 */
public class ModelCheckerPerspective implements IPerspectiveFactory {

	public static final String ID = "stpaVerifier.perspective";
	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);

		
		layout.addStandaloneView(ProjectExplorer.ID, true, IPageLayout.LEFT	, 0.2f	,IPageLayout.ID_EDITOR_AREA);
		layout.getViewLayout(ProjectExplorer.ID).setCloseable(false);
	
		IFolderLayout lFolder= layout.createFolder("consoleFolder", IPageLayout.BOTTOM, 0.7f,  IPageLayout.ID_EDITOR_AREA);
		lFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		layout.getViewLayout(IConsoleConstants.ID_CONSOLE_VIEW).setCloseable(false);
		lFolder.addView(ResultsViewPart.ID);
		layout.getViewLayout(ResultsViewPart.ID).setCloseable(false);
		lFolder.addView(CounterexampleViewPart.ID);
		layout.getViewLayout(CounterexampleViewPart.ID).setCloseable(false);
		
		lFolder.addView(ResultsDiagViewPart.ID);
		layout.getViewLayout(ResultsDiagViewPart.ID).setCloseable(false);

		layout.addStandaloneView(FormularViewPart.ID, true, IPageLayout.LEFT	, 0.5f	,IPageLayout.ID_EDITOR_AREA);
		layout.getViewLayout(FormularViewPart.ID).setCloseable(false);
		
		layout.addStandaloneView(ConfigurationViewPart.ID, true, IPageLayout.RIGHT	, 0.7f	, IPageLayout.ID_EDITOR_AREA);
		layout.getViewLayout(ConfigurationViewPart.ID).setCloseable(false);

		layout.addStandaloneView(ResultsPieViewPart.ID, true, IPageLayout.BOTTOM	, 0.7f	, ConfigurationViewPart.ID);
		layout.getViewLayout(ResultsPieViewPart.ID).setCloseable(false);
		

	}

}
