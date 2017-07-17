package xstampp.stpatcgenerator.ui.perspective;

import org.apache.log4j.Logger;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import xstampp.stpatcgenerator.model.ProjectInformation;
import xstampp.stpatcgenerator.ui.views.EFSMTreeGraphView;
import xstampp.stpatcgenerator.ui.views.EFSMTruthTableView;
import xstampp.stpatcgenerator.ui.views.LTLTableView;
import xstampp.stpatcgenerator.ui.views.LogErrorView;
import xstampp.stpatcgenerator.ui.views.TraceabilityMatrixView;
import xstampp.stpatcgenerator.ui.views.ValidateSTPAandSBM;
import xstampp.stpatcgenerator.ui.views.StateFlowPropertiesView;
import xstampp.stpatcgenerator.ui.views.StateFlowTreeGraphView;
import xstampp.stpatcgenerator.ui.views.StateFlowTruthTableView;
import xstampp.stpatcgenerator.ui.views.TestCaseHistogrammView;
import xstampp.ui.navigation.ProjectExplorer;
/**
 * This class defines the perspective of STPA TCGenerator
 * @author Ting Luk-He
 *
 */
public class STPATCGeneratorPerspective implements IPerspectiveFactory{
	public static final String ID ="xstampp.stpatcgenerator.perspective";
	private static final Logger LOGGER = Logger.getRootLogger();
	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		
		layout.addStandaloneView(ProjectExplorer.ID, true, IPageLayout.LEFT	, 0.2f	,IPageLayout.ID_EDITOR_AREA);
		layout.getViewLayout(ProjectExplorer.ID).setCloseable(false);
		
		IFolderLayout mFolder= layout.createFolder("middleFolder", IPageLayout.BOTTOM, 0.7f,  IPageLayout.ID_EDITOR_AREA);
		mFolder.addPlaceholder(TraceabilityMatrixView.ID);
		
		IFolderLayout bFolder= layout.createFolder("consoleFolder", IPageLayout.BOTTOM, 0.7f,  IPageLayout.ID_EDITOR_AREA);
		bFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		bFolder.addView(LogErrorView.ID);
		bFolder.addPlaceholder(StateFlowPropertiesView.ID);
		bFolder.addPlaceholder(StateFlowTruthTableView.ID);
		bFolder.addPlaceholder(ValidateSTPAandSBM.ID);
		bFolder.addPlaceholder(LTLTableView.ID);
		bFolder.addPlaceholder(EFSMTruthTableView.ID);		
		bFolder.addPlaceholder(TestCaseHistogrammView.ID);
		
//		IFolderLayout lFolder= layout.createFolder("graphFolder", IPageLayout.LEFT, 1.0f,  IPageLayout.ID_EDITOR_AREA);
//		lFolder.addPlaceholder(EFSMTreeGraphView.ID);
//		lFolder.addPlaceholder(StateFlowTreeGraphView.ID);
			
	}

}
