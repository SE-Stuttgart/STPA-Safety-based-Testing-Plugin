package xstampp.stpatcgenerator.wizards;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.DefaultPerspective;
import xstampp.astpa.model.DataModelController;
import xstampp.model.IDataModel;
import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.ProjectInformation;
import xstampp.stpatcgenerator.model.astpa.ParseSTPAMain;
import xstampp.stpatcgenerator.model.astpa.STPADataModelController;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.TreeStateflowGenerator;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.ParseStateflowMain;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.properties.StateFlowProperties;
import xstampp.stpatcgenerator.ui.editors.StateflowTreeGraphEditor;
import xstampp.stpatcgenerator.ui.editors.TCGeneratorEditorInput;
import xstampp.stpatcgenerator.ui.perspective.STPATCGeneratorPerspective;
import xstampp.stpatcgenerator.ui.views.EFSMTreeGraphView;
import xstampp.stpatcgenerator.ui.views.EFSMTruthTableView;
import xstampp.stpatcgenerator.ui.views.LTLTableView;
import xstampp.stpatcgenerator.ui.views.LogErrorView;
import xstampp.stpatcgenerator.ui.views.StateFlowPropertiesView;
import xstampp.stpatcgenerator.ui.views.StateFlowTruthTableView;
import xstampp.stpatcgenerator.ui.views.TestCaseHistogrammView;
import xstampp.stpatcgenerator.ui.views.TraceabilityMatrixView;
import xstampp.stpatcgenerator.ui.views.ValidateSTPAandSBM;
import xstampp.stpatcgenerator.ui.views.StateFlowTreeGraphView;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.stpatcgenerator.wizards.pages.ConfigurationPage;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.ui.navigation.ProjectExplorer;
import xstampp.util.STPAPluginUtils;

/**
 * This class creates the start wizard for Generate Test Case as CSV.
 * 
 * @author Ting Luk-He
 *
 */
public class ConfigurationWizard extends Wizard {
	private static final Logger LOGGER = Logger.getRootLogger();
	private UUID projectID;
	private String pathPref;
	private String projectName;
	private String projectExt;
	private String projectPath;
	private ConfigurationPage page;
	private ParseStateflowMain parseStateflow;
	private TreeStateflowGenerator treeStateflow;
	private Tree tree;
	private ParseSTPAMain parseSTPA;
	private STPADataModelController stpaDataModel;
	private List<String> views;
	private StateFlowProperties stateFlowProperties;
	private static List<String> errorMsgs;
	private static List<String> warningMsgs;

	public ConfigurationWizard() {
		errorMsgs = new ArrayList<String>();
		warningMsgs = new ArrayList<String>();
		this.projectID = getProjectId();
		this.pathPref = Platform.getInstanceLocation().getURL().getPath();
		this.projectName = ProjectManager.getContainerInstance().getDataModel(getProjectId()).getProjectName();
		this.projectExt = ProjectManager.getContainerInstance().getDataModel(getProjectId()).getFileExtension();
		String tempPath = pathPref + projectName + "." + projectExt;
		File f = new File(tempPath);
		if (!f.exists()) {
			if (this.projectExt.equals("haz")) {
				this.projectExt = "hazx";
			} else if (this.projectExt.equals("hazx")) {
				this.projectExt = "hazx";
			}
		}
		this.projectPath = pathPref + projectName + "." + projectExt;
		page = new ConfigurationPage("GenerateTestCasePage", this.projectPath);
		this.addPage(this.page);
		STPATCGModelController.setConfWizard(this);
	}

	@Override
	public boolean performFinish() {
		if (page.getGenTCWichSTPA()) {
			ProjectInformation.setTypeOfUse(2);
		} else {
			ProjectInformation.setTypeOfUse(1);
		}
		ProjectInformation.setStateflow(page.getStateFlowPath());
		TCGeneratorPluginUtils.writeLocationToFile(TCGeneratorPluginUtils.STATEFLOW_LOCATION_FILE,
				page.getStateFlowPath());
		ProjectInformation.setSTPAPath(page.getProjectPath());
		parse();
		setSTPATCGeneratorPerspective();
		// TCGeneratorPluginUtils.hideView(EFSMTruthTableView.ID);
		//TCGeneratorPluginUtils.hideView(EFSMTreeGraphView.ID);
		//TCGeneratorPluginUtils.hideView(LTLTableView.ID);
		////TCGeneratorPluginUtils.hideView(TraceabilityMatrixView.ID);
		//TCGeneratorPluginUtils.hideView(TestCaseHistogrammView.ID);
		//TCGeneratorPluginUtils.hideView(ValidateSTPAandSBM.ID);
		TCGeneratorPluginUtils.showView(StateFlowPropertiesView.ID);
		TCGeneratorPluginUtils.showView(IConsoleConstants.ID_CONSOLE_VIEW);
		if (tree != null && page.getGenTCWichSTPA()) {
			TCGeneratorPluginUtils.showView(ValidateSTPAandSBM.ID);
		}
		TCGeneratorPluginUtils.showView(StateFlowTruthTableView.ID);
		openStateFlowTreeGraphEditor();
		LogErrorView.initErrorLog();
		// write message to error log
		for (int i = 0; i < errorMsgs.size(); i++) {
			if (i == 0) {
				LogErrorView.writeError(errorMsgs.get(i), true);
			} else {
				LogErrorView.writeError(errorMsgs.get(i), false);
			}
		}
		for (int i = 0; i < warningMsgs.size(); i++) {
			if (i == 0 && errorMsgs.size() == 0) {
				LogErrorView.writeWarning(warningMsgs.get(i), true);
			} else if (errorMsgs.size() > 0) {
				LogErrorView.writeWarning(warningMsgs.get(i), false);
			}
		}
		// show log error view if it contains message
		if (errorMsgs.size() > 0 || warningMsgs.size() > 0) {
			TCGeneratorPluginUtils.showView(LogErrorView.ID);
		}
		return true;

	}

	public void setSTPATCGeneratorPerspective() {
		ConfigurationWizard.LOGGER.debug("Setup STPATCGenerator Perspective"); //$NON-NLS-1$
		IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench().getPerspectiveRegistry()
				.findPerspectiveWithId(STPATCGeneratorPerspective.ID); // $NON-NLS-1$
		if (descriptor != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(descriptor);
		}
	}

	public UUID getProjectId() {
		IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ProjectExplorer.ID);
		if (part != null) {
			Object selection = part.getViewSite().getSelectionProvider().getSelection();

			if (selection instanceof IProjectSelection) {
				projectID = ((IProjectSelection) selection).getProjectId();
			}
		}
		return projectID;
	}

	public Tree getTree() {
		return tree;
	}

	public static void closeStateFlowTreeGraphEditor() {

	}

	public static void openStateFlowTreeGraphEditor() {
		TCGeneratorEditorInput input = new TCGeneratorEditorInput(StateflowTreeGraphEditor.ID);
		TCGeneratorPluginUtils.getInputMap().put(StateflowTreeGraphEditor.ID, input);
		input.runCommand("open state flow tree graph");
	}

	public void parse() {
		parseStateflow = new ParseStateflowMain(ProjectInformation.getStateflow());
		treeStateflow = new TreeStateflowGenerator(parseStateflow);
		if (ProjectInformation.getTypeOfUse() == 1 && ProjectInformation.getStateflow().length() > 0) {
			System.out.println("Type of use = 1");
			stateFlowProperties = parseStateflow.ParseStateflowXML();

			tree = treeStateflow.generateTree(parseStateflow.getdataStateflowModel());
			// hideView(CompareSTPAStateflowView.ID);
			if (tree != null) {
				String message = "[INFO] " + TCGeneratorPluginUtils.getCurrentTime()
						+ " STPATCG-> The stateflow model file has been succesfully parsed...";
				TCGeneratorPluginUtils.getInfoOS().println(message);
				message = "[INFO] " + TCGeneratorPluginUtils.getCurrentTime()
						+ " STPATCG-> The truthtable of stateflow model file is generated...";
				TCGeneratorPluginUtils.getInfoOS().println(message);
				List<StateTransition> truthtable = tree.generateTruthTable(tree.getRoot());
				message = tree.getTruthTableAsString(truthtable);
				TCGeneratorPluginUtils.getOutputStream().println(message);
			} else {
				String message = "[ERROR] " + TCGeneratorPluginUtils.getCurrentTime()
						+ " STPATCG-> The tool can not parse the input file " + ProjectInformation.getStateflow()
						+ " please check the path of input file...";
				TCGeneratorPluginUtils.getErrorOS().println(message);
				errorMsgs.add(message);
			}

		} else if (ProjectInformation.getTypeOfUse() == 2 && ProjectInformation.getSTPAPath().length() > 0) {
		// update the parse to be direct from XSTAMPP code 
			//Asim solved the parse bug.
			
			System.out.println("Type of use = 2");
			ProjectExplorer explorerView = (ProjectExplorer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ProjectExplorer.ID);
			UUID id = explorerView.getActiveSelection().getProjectId();
			IDataModel dataModel = ProjectManager.getContainerInstance().getDataModel(id);

			//parseSTPA.parseSTPAHazXML();
			
			stpaDataModel = new STPADataModelController (dataModel);
			stateFlowProperties = parseStateflow.ParseStateflowXML();
			parseSTPA = new ParseSTPAMain(ProjectInformation.getSTPAPath(), (DataModelController) dataModel);
            parseSTPA.setSTPAdataModel(stpaDataModel);
			tree = treeStateflow.generateTree(parseStateflow.getdataStateflowModel());

			if (tree != null) {
				String message = "[INFO] " + TCGeneratorPluginUtils.getCurrentTime()
						+ " STPATCG-> The stateflow model file has been succesfully parsed...";
				TCGeneratorPluginUtils.getInfoOS().println(message);
				message = "[INFO] " + TCGeneratorPluginUtils.getCurrentTime()
						+ " STPATCG-> The truthtable of stateflow model file is generated...";
				TCGeneratorPluginUtils.getInfoOS().println(message);
				List<StateTransition> truthtable = tree.generateTruthTable(tree.getRoot());
				message = tree.getTruthTableAsString(truthtable);
				TCGeneratorPluginUtils.getOutputStream().println(message);
				// String testMessage = "Test Error Message";
				// for(int i = 0; i < 2; i++){
				// errorMsgs.add("[Error] " +
				// TCGeneratorPluginUtils.getCurrentTime() + testMessage);
				// }
				//
				// String testMessage1 = "Test Warning Message";
				// for(int i = 0; i < 2; i++){
				// warningMsgs.add("[Warning] " +
				// TCGeneratorPluginUtils.getCurrentTime() + testMessage1);
				// }
			} else {
				String message = "[ERROR] " + TCGeneratorPluginUtils.getCurrentTime()
						+ " STPATCG-> The tool can not parse the input file " + ProjectInformation.getStateflow()
						+ " please check the path of input file...";
				TCGeneratorPluginUtils.getErrorOS().println(message);
				// LogErrorView.writeError(message, true);
				errorMsgs.add(message);
				message = "[ERROR] " + TCGeneratorPluginUtils.getCurrentTime()
						+ " STPATCG-> The tool can not parse the input file " + ProjectInformation.getSTPAPath()
						+ " please check the path of input file...";
				TCGeneratorPluginUtils.getErrorOS().println(message);
				// LogErrorView.writeError(message, false);
				errorMsgs.add(message);

			}
		}
	}

	public StateFlowProperties getStateFlowProperties() {
		return stateFlowProperties;
	}

	public STPADataModelController getStpaDataModel() {
		return stpaDataModel;
	}

	public ParseSTPAMain getParseSTPA() {
		return parseSTPA;
	}

	public void setParseSTPA(ParseSTPAMain parseSTPA) {
		this.parseSTPA = parseSTPA;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
