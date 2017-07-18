package xstampp.stpatcgenerator.util.handler;

import java.util.List;

import javax.swing.text.BadLocationException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.ProjectInformation;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.StateNode;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.fsm.EFSM;
import xstampp.stpatcgenerator.model.stateflow.fsm.EFSMGenerator;
import xstampp.stpatcgenerator.model.stateflow.fsm.StateEFSM;
import xstampp.stpatcgenerator.ui.editors.EFSMTreeGraphEditor;
import xstampp.stpatcgenerator.ui.editors.StateflowTreeGraphEditor;
import xstampp.stpatcgenerator.ui.editors.TCGeneratorEditorInput;
import xstampp.stpatcgenerator.ui.views.EFSMTreeGraphView;
import xstampp.stpatcgenerator.ui.views.EFSMTruthTableView;
import xstampp.stpatcgenerator.ui.views.LTLTableView;
import xstampp.stpatcgenerator.ui.views.LogErrorView;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.stpatcgenerator.wizards.ConfigurationWizard;

/**
 * The handler for building save test model.
 * @author Ting
 *
 */
public class BuildSafeTestModelHandler extends AbstractHandler{
	private EFSM fsm = new EFSM();
	private Tree tree = STPATCGModelController.getConfWizard().getTree();
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		if (tree != null && fsm != null) {
			
			EFSMGenerator fsmgenerator = new EFSMGenerator(tree);
			fsm = fsmgenerator.generateFSM();
			
			if (fsm != null) {
                if (fsm.getFSMErrors().length() > 0) {
                	String message = "[ERROR] " + TCGeneratorPluginUtils.getCurrentTime() + " STPATCG->" + fsm.getFSMErrors();
    				TCGeneratorPluginUtils.getErrorOS().println(message);
    				LogErrorView.writeError(message, true);
    				TCGeneratorPluginUtils.showView(LogErrorView.ID);
                } else {
                	String message = "[INFO] " + TCGeneratorPluginUtils.getCurrentTime() + " STPATCG-> The FSM Model has been successfully generated";
    				TCGeneratorPluginUtils.getInfoOS().println(message);
    				message = "[INFO] " + TCGeneratorPluginUtils.getCurrentTime() + fsmgenerator.printFSM(fsm) + fsmgenerator.getfsmTruthTable().size();
    				TCGeneratorPluginUtils.getInfoOS().println(message);
    				message = "[INFO] " + TCGeneratorPluginUtils.getCurrentTime() + "number of transition in truthtable " + fsmgenerator.getfsmTruthTable().size();
    				TCGeneratorPluginUtils.getInfoOS().println(message);
    				STPATCGModelController.setSfmHandler(this);

    				// open view of EFSM Truth Table
    				TCGeneratorPluginUtils.showView(EFSMTruthTableView.ID);
    				openEFSMTreeGraphEditor();

                }

	                
			} else {
				String message = "[ERROR] " + TCGeneratorPluginUtils.getCurrentTime() + "STPATCG-> The tool could not generated FSM Model from the stateflow model";
				TCGeneratorPluginUtils.getErrorOS().println(message);
				LogErrorView.writeError(message, false);
				TCGeneratorPluginUtils.showView(LogErrorView.ID);
            }
		}
//		System.out.println("BuildSafeTestModelHandler");
		return null;
	}

	public static void openEFSMTreeGraphEditor() {
		TCGeneratorEditorInput input = new TCGeneratorEditorInput(EFSMTreeGraphEditor.ID);
		input.runCommand("open efsm tree graph");
	}

	public EFSM getFsm() {
		return fsm;
	}

	public void setFsm(EFSM fsm) {
		this.fsm = fsm;
	}
	
	
}
