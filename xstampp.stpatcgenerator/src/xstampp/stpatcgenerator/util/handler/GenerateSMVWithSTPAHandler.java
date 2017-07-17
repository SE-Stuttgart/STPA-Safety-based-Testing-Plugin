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
import xstampp.stpatcgenerator.ui.views.LTLTableView;
import xstampp.stpatcgenerator.ui.views.StateFlowPropertiesView;
import xstampp.stpatcgenerator.ui.views.StateFlowTreeGraphView;
import xstampp.stpatcgenerator.ui.views.StateFlowTruthTableView;
import xstampp.stpatcgenerator.ui.views.TestCaseHistogrammView;
import xstampp.stpatcgenerator.ui.views.ValidateSTPAandSBM;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.stpatcgenerator.wizards.ConfigurationWizard;
/**
 * The handler for generating SMV file with STPA and open smv text editor.
 * @author Ting Luk-He
 *
 */
public class GenerateSMVWithSTPAHandler extends AbstractHandler{
	Tree tree;
	GeneratorSMVFile generatorSMV;
	ParseSTPAMain parseSTPA;
	
	public void init(){
		tree = STPATCGModelController.getConfWizard().getTree();
		parseSTPA = STPATCGModelController.getConfWizard().getParseSTPA();
		ProjectInformation.setTypeOfUse(2);
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		init();
		generateSMVWithSTPA();
//		IFile file = TCGeneratorPluginUtils.getSmvInput().get(SMVTextEditor.ID);
//		if(file != null){
//			openEditor(event, SMVTextEditor.ID, file);
//		} else {
//			file = GeneratorSMVFile.getsmvIFile();
//			TCGeneratorPluginUtils.getSmvInput().put(SMVTextEditor.ID, file);
//			openEditor(event, SMVTextEditor.ID, file);
//		}
		openEditor(event, SMVTextEditor.ID);
		TCGeneratorPluginUtils.showView(LTLTableView.ID);
		//TCGeneratorPluginUtils.hideView(StateFlowTruthTableView.ID);
		//TCGeneratorPluginUtils.hideView(StateFlowPropertiesView.ID);
//		TCGeneratorPluginUtils.hideView(ValidateSTPAandSBM.ID);
		//TCGeneratorPluginUtils.hideView(TestCaseHistogrammView.ID);
//		TCGeneratorPluginUtils.hideView(StateFlowTreeGraphView.ID);
//		TCGeneratorPluginUtils.hideView(StateflowTreeGraphEditor.ID);
		return null;
	}
	
	public void openEditor(ExecutionEvent event, String editorId) {
		// get the page
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        IWorkbenchPage page = window.getActivePage();
        IFile file = GeneratorSMVFile.getsmvIFile();
        try {
        	IDE.openEditor(page, file, editorId);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void generateSMVWithSTPA(){
		if(tree != null && parseSTPA != null) {
			generatorSMV = new GeneratorSMVFile("SMVModel");
	        generatorSMV.setUsedbySTPA(true);
	        new DeleteAllSMVFiles().deleteFile();   
	        String message = "[INFO] " + TCGeneratorPluginUtils.getCurrentTime() + " STPATCG-> Start Creating a new SMV model file with STPA...";
            TCGeneratorPluginUtils.getInfoOS().println(message);
	        generatorSMV.generateSMVModelWithSTPA(parseSTPA.getSTPAdataModel(), tree);
		}
		        
	}

}
