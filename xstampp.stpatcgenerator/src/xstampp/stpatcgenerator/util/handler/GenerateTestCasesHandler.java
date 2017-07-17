package xstampp.stpatcgenerator.util.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.fsm.EFSM;
import xstampp.stpatcgenerator.ui.editors.GTCConfigEditor;
import xstampp.stpatcgenerator.ui.editors.StateflowTreeGraphEditor;
import xstampp.stpatcgenerator.ui.editors.TCGeneratorEditorInput;
import xstampp.stpatcgenerator.ui.views.EFSMTruthTableView;
import xstampp.stpatcgenerator.ui.views.LTLTableView;
import xstampp.stpatcgenerator.ui.views.TestCaseHistogrammView;
import xstampp.stpatcgenerator.ui.views.TraceabilityMatrixView;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;

/**
 * The handler for opening the configuration editor of generating test cases.
 * @author Ting Luk-He
 *
 */
public class GenerateTestCasesHandler extends AbstractHandler {
		
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		BuildSafeTestModelHandler fsmHandler = STPATCGModelController.getSfmHandler();
		if(fsmHandler == null) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openError(shell, "Error", "Please build safe test model at first and then click generate test cases.");
		} else {
			TCGeneratorEditorInput input = (TCGeneratorEditorInput) TCGeneratorPluginUtils.getInputMap().get(GTCConfigEditor.ID);
			if(input != null){
				TCGeneratorPluginUtils.openEditor(event, GTCConfigEditor.ID, input);
			} else {
				input = new TCGeneratorEditorInput(GTCConfigEditor.ID);
				TCGeneratorPluginUtils.getInputMap().put(GTCConfigEditor.ID, input);
				TCGeneratorPluginUtils.openEditor(event, GTCConfigEditor.ID, input);
			}
		//	TCGeneratorPluginUtils.hideView(TestCaseHistogrammView.ID);
		}
		
		return null;
	}

}
