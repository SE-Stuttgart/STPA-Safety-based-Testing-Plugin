package xstampp.stpatcgenerator.util.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.ProjectInformation;
import xstampp.stpatcgenerator.model.astpa.ParseSTPAMain;
import xstampp.stpatcgenerator.model.smv.DeleteAllSMVFiles;
import xstampp.stpatcgenerator.model.smv.GeneratorSMVFile;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.ui.editors.SMVTextEditor;
import xstampp.stpatcgenerator.ui.editors.StateflowTreeGraphEditor;
import xstampp.stpatcgenerator.ui.editors.TCGeneratorEditorInput;
import xstampp.stpatcgenerator.ui.views.LogErrorView;
import xstampp.stpatcgenerator.ui.views.StateFlowPropertiesView;
import xstampp.stpatcgenerator.ui.views.StateFlowTreeGraphView;
import xstampp.stpatcgenerator.ui.views.StateFlowTruthTableView;
import xstampp.stpatcgenerator.ui.views.TestCaseHistogrammView;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.stpatcgenerator.wizards.ConfigurationWizard;

/**
 * The handler for generating SMV file without STPA and open smv text editor.
 * @author Ting Luk-He
 *
 */
public class GenerateSMVWithoutSTPAHandler extends AbstractHandler{
	Tree tree;
	GeneratorSMVFile generatorSMV;
	ParseSTPAMain parseSTPA;
	
	public void init(){
		tree = STPATCGModelController.getConfWizard().getTree();
		//parseSTPA = STPATCGModelController.getConfWizard().getParseSTPA();
		ProjectInformation.setTypeOfUse(1);
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		init();
		generateSMVWithoutSTPA();
		
		openEditor(event, SMVTextEditor.ID);
		//TCGeneratorPluginUtils.hideView(StateFlowTruthTableView.ID);
		//TCGeneratorPluginUtils.hideView(StateFlowPropertiesView.ID);
		//TCGeneratorPluginUtils.hideView(TestCaseHistogrammView.ID);

		return null;
	}

	private void openEditor(ExecutionEvent event, String editorId) {
		// get the page
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        IWorkbenchPage page = window.getActivePage();
        IFile file = GeneratorSMVFile.getsmvIFile();
        try {
        	IDE.openEditor(page, file, editorId);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
	}
	
	public void generateSMVWithoutSTPA() {
		if(tree != null) {
			generatorSMV = new GeneratorSMVFile("SMVModel");
            generatorSMV.setUsedbySTPA(false);
            String message = "[INFO] " + TCGeneratorPluginUtils.getCurrentTime() + " STPATCG-> Start Creating a new SMV model file without STPA...";
            TCGeneratorPluginUtils.getInfoOS().println(message);
            generatorSMV.generateSMVModel(tree);
		}
	}
}
