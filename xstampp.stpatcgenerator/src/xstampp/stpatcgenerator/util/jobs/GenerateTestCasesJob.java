package xstampp.stpatcgenerator.util.jobs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.ProjectInformation;
import xstampp.stpatcgenerator.model.astpa.ParseSTPAMain;
import xstampp.stpatcgenerator.model.generateTestCases.CoverageCriteria;
import xstampp.stpatcgenerator.model.generateTestCases.CoverageCriteria.CoverageType;
import xstampp.stpatcgenerator.model.generateTestCases.TestCase;
import xstampp.stpatcgenerator.model.generateTestCases.TestCasesGenerator;
import xstampp.stpatcgenerator.model.generateTestCases.TestConfigurations;
import xstampp.stpatcgenerator.model.generateTestCases.TestGraph;
import xstampp.stpatcgenerator.model.generateTestCases.TestSuite;
import xstampp.stpatcgenerator.model.generateTestCases.TraceabilityMatrix;
import xstampp.stpatcgenerator.model.generateTestCases.Vertex;
import xstampp.stpatcgenerator.model.generateTestCases.buildGraph;
import xstampp.stpatcgenerator.model.generateTestCases.RuntimeExecution.SaveExcel;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.DataVariable;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.chart.parse.StateTransition;
import xstampp.stpatcgenerator.model.stateflow.fsm.EFSM;
import xstampp.stpatcgenerator.model.stateflow.fsm.StateEFSM;
import xstampp.stpatcgenerator.ui.editors.StateflowTreeGraphEditor;
import xstampp.stpatcgenerator.ui.editors.TCGeneratorEditorInput;
import xstampp.stpatcgenerator.ui.editors.TestCaseResultEditor;
import xstampp.stpatcgenerator.ui.views.LogErrorView;
import xstampp.stpatcgenerator.ui.views.TestCaseHistogrammView;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.util.XstamppJob;

/**
 * This class controls the generate test cases job.
 * 
 * @author Ting Luk-He
 *
 */
public class GenerateTestCasesJob extends XstamppJob {
	private EFSM fsm;
	private Tree tree;
	private ParseSTPAMain parseSTPA;
	private TestConfigurations configure;
	private JTable inputVariablesTable;
	private DefaultTableModel traceabilityMatrixTableModel;
	private String testStep;
	private boolean[] selectedAlg;
	private boolean[] selectedCoverage;
	private int stopCondition;
	private boolean isOneTestSuit;
	
	private CoverageCriteria stopCriteria;
	CoverageCriteria statesCriteria;
	CoverageCriteria transitionCriteria;
	CoverageCriteria ssrCriteria;
	
	int testsuite = 0;
	int testcases = 0;
	
	private DefaultTableModel infoMsgTableModel = new DefaultTableModel();
	private String[] infoMsgRowContent = new String[7];
	private String[] infoMsgTableColumns = {"Total No. Generated Test Suits", "Total No. Generated Test Cases", "Total No. Test Steps", "All States Coverage", "All Transition Coverage", "All STPA Sofetware Safety Requirements Coverage", "Time Taken"};
	public GenerateTestCasesJob(String name, EFSM fsm, Tree tree,
			ParseSTPAMain parseSTPA, TestConfigurations configure,
			JTable inputVariablesTable, DefaultTableModel traceabilityMatrixTableModel, String testStep, boolean[] selectedAlg,
			boolean[] selectedCoverage, int stopCondition, boolean isOneTestSuit) {
		super(name);
		this.fsm = fsm;
		this.tree = tree;
		this.parseSTPA = parseSTPA;
		this.configure = configure;
		this.inputVariablesTable = inputVariablesTable;
		this.traceabilityMatrixTableModel = traceabilityMatrixTableModel;
		this.testStep = testStep;
		this.selectedAlg = selectedAlg;
		this.selectedCoverage = selectedCoverage;
		this.stopCondition = stopCondition;
		this.isOneTestSuit = isOneTestSuit;
		statesCriteria = new CoverageCriteria(CoverageType.Statescoverage);
		transitionCriteria = new CoverageCriteria(CoverageType.TransitionsCoverage);
		ssrCriteria = new CoverageCriteria(CoverageType.ALLSTPARequirmentsCoverage);
				
		DefaultTableModel configTableModel = new DefaultTableModel();
		String[] colNames = {"No. Test Steps", "Test Algorithm", "Test Coverages", "Stop Condition"};
		configTableModel.setColumnIdentifiers(colNames);
		
		String selectedAlgText = "";
		if(selectedAlg[0]){
			selectedAlgText = "Depth First Search";
		}else if(selectedAlg[1]){
			selectedAlgText = "Breadth First Search";
		}else if(selectedAlg[2]){
			selectedAlgText = "Both";
		}
		
		String selectedCoverageText = "";
		if(selectedCoverage[0]){
			selectedCoverageText += "All States Coverage ";
		}
		if(selectedCoverage[1]){
			selectedCoverageText += "and All Transitions Coverage ";
		}
		if(selectedCoverage[2]){
			selectedCoverageText += "and STPA Safety Requirements Coverage";
		}
				
		String stopConditionText = "";
		if(stopCondition == 0){
			stopConditionText = "State Coverage";
		}else if(stopCondition == 1){
			stopConditionText = "Transition Coverage";
		}else if(stopCondition == 2){
			stopConditionText = "STPA Safety Requirements Coverage";
		}
		Object[] row = {testStep, selectedAlgText, selectedCoverageText, stopConditionText};
		configTableModel.addRow(row);
		STPATCGModelController.setGenTCConfigTableModel(configTableModel);
	}

	@Override
	protected Observable getModelObserver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		
		if (fsm != null && tree != null) {
			long time1 = System.currentTimeMillis();

			for (StateEFSM f : fsm.getFsmstates()) {
				if (f.getName().equals("start")) {
					fsm.getFsmstates().remove(f);
					break;
				}
			}
			for (StateTransition stf : fsm.getfsmTruthTable()) {
				if (stf.getSSID().equals("100000")) {
					fsm.getfsmTruthTable().remove(stf);
					break;
				}
			}

			updateDataVariables2(fsm);
			buildGraph testGraph = new buildGraph();
			testGraph.generateGraph(fsm);

			TestGraph graph = testGraph.getGraph();
			graph.printAdjMatrix();
			for (int i = 0; i < graph.getAdjMatrix().length; i++) {
				Vertex v = graph.getVertexByIndex(i);
				System.out.println(v.getName() + " index " + i);
			}
			if (parseSTPA == null) {
				// TODO maybe
				// jPanelTracebilityMatrix.setVisible(false);
				// jPanelTracebilityMatrix.setEnabled(false);
			}
			if (graph != null) {
				if (!selectedAlg[0] && !selectedAlg[1] && !selectedAlg[2]) {
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							TCGeneratorPluginUtils.showWarningDialog("Warning",
									"Please select at least one algorithm.");
						}
					});
				} else if (selectedAlg[0] || selectedAlg[1] || selectedAlg[2]) {

					// setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					// add the varaibles data of esfm into the configuration
					// test
					configure.setDataTestModel(fsm.getDatavariables());
					if (testStep != null) {
						configure.setStepsOfTest(Integer.parseInt(testStep));
					} else {
						configure.setStepsOfTest(30);
					}

					// get the traceabilityMatrix as the user enter in the Table
					// of Tracability Matrix
					List<TraceabilityMatrix> tracabilityMatrix = new ArrayList<TraceabilityMatrix>();

					try {
						for(int i = 0; i < traceabilityMatrixTableModel.getRowCount(); i++){
							if(traceabilityMatrixTableModel.getValueAt(i, 0) != null && !traceabilityMatrixTableModel.getValueAt(i, 0).equals("")){
								TraceabilityMatrix tm = new TraceabilityMatrix(traceabilityMatrixTableModel.getValueAt(i, 0).toString().trim(), traceabilityMatrixTableModel.getValueAt(i, 1).toString().trim());
								tracabilityMatrix.add(tm);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					// set Coverages
					List<CoverageCriteria> coverages = new ArrayList<CoverageCriteria>();
					for(int i = 0; i < selectedCoverage.length; i++){
						if(selectedCoverage[i])
							switch(i){
							case 0:
								coverages.add(this.statesCriteria);
								break;
							case 1:
								coverages.add(this.transitionCriteria);
								break;
							case 2:
								coverages.add(this.ssrCriteria);
								break;
							}
					}
					configure.setCoverages(coverages);
					
					// configure stop conditions
					switch (stopCondition) {
					case 0:
						stopCriteria = statesCriteria;
//						configure.setStopcondition(stopCriteria);
//						configure.getStopcondition().setValue(stopCondition);
//						configure.getStopcondition().setValue(0);
						break;
					case 1:
						stopCriteria = transitionCriteria;
//						configure.setStopcondition(stopCriteria);
//						configure.getStopcondition().setValue(stopCondition);
//						configure.getStopcondition().setValue(0);
						break;
					case 2:
						stopCriteria = ssrCriteria;
//						configure.setStopcondition(stopCriteria);
//						configure.getStopcondition().setValue(stopCondition);
//						configure.getStopcondition().setValue(0);
						break;
					default:
						System.out.println("stop Condition number: " + stopCondition);
						break;
					}
					
					configure.setStopcondition(stopCriteria);
					configure.getStopcondition().setValue(0);
					configure.setTraceabilitymatrix(tracabilityMatrix);

					configure.setTransitions(fsm.getfsmTruthTable());

					TestCasesGenerator testcasesGenerator = new TestCasesGenerator(
							graph, configure);
					testcasesGenerator.setOneTestSuit(isOneTestSuit);
					testcasesGenerator.setFrequencySSR("");
					// graph.AllPathsByDFS(graph, 4, 3);
					// Random algorithm is selected
					if (selectedAlg[2]) {
						testcasesGenerator.RandomWalk();
					}
					// BFS Algorithm is selected
					else if (selectedAlg[1]) {
						testcasesGenerator.RandomWalkByBFS();
					}
					// DFS Algorithm is selected
					else if (selectedAlg[0]) {
						testcasesGenerator.RandomWalkByDFS();
					}
					testcases = 0;
					testsuite = 0;
					// fill table of test cases
					generateTestCaseTableModel(testcasesGenerator.getTestSuites());
					// generate test suits in tree
					TreeSet<TestSuite> treeSuites = ShowTreeofTestCases(testcasesGenerator
							.getTestSuites());
					
					// open Test Cases Result Editor
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							openTestCaseResultEditor();
							TCGeneratorPluginUtils.showView(LogErrorView.ID);
							//TCGeneratorPluginUtils.hideView(TestCaseHistogrammView.ID);
							if(ProjectInformation.getTypeOfUse() == 2){
								TCGeneratorPluginUtils.showView(TestCaseHistogrammView.ID);
							}						
						}
					});
					
					// setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

					// try {
					String msg = "The test cases has been successfully generated \n"
							+ "STPATCG-> Stop Condition is "
							+ configure.getStopcondition().getType();
					String message = "[INFO] "
							+ TCGeneratorPluginUtils.getCurrentTime() + msg;
					TCGeneratorPluginUtils.getInfoOS().println(message);
					// txtConsole.getDocument().insertString(txtConsole.getDocument().getLength(),
					// "\n STPATCG-> The test cases has been successfully generated \n",
					// getColor(Color.GREEN));
					// txtConsole.getDocument().insertString(txtConsole.getDocument().getLength(),
					// "\n STPATCG-> Stop Condition is " +
					// configure.getStopcondition().getType() + "\n",
					// getColor(Color.red));
					msg += showALLCoverageResults(testcasesGenerator);
					String frequency = testcasesGenerator.getFrequencySSR();

					Map<String, Integer> frequenceyOFSTPA = testcasesGenerator
							.getFrequncyofSTPASSR(frequency);

//					String results = new SaveExcel().createExecl(treeSuites);
//					String str = "Both";
//					// BFS Algorithm is selected
//					if (selectedAlg[2]) {
//						str = "BFS";
//					}
//					// DFS Algorithm is selected
//					else if (selectedAlg[1]) {
//						str = "DFS";
//					}
					// TODO write testCaseTable in oder view and save
					// new SaveExcel().writeCSVfile(tbl_TestCaseTable, str);
					// new SaveExcel().exportTable (tbl_TestCaseTable, new File
					// ("testCasesTable3.xls"));
					// new SaveExcel().toCSVString(frequenceyOFSTPA);

//					new SaveExcel().toCSVFile(frequenceyOFSTPA, str);
//					msg += "\n" + results;
//					message = "STPATCG->" + results;
//					TCGeneratorPluginUtils.getInfoOS().println(message);

					long time2 = System.currentTimeMillis();
					long timeTaken = (time2 - time1) / 1000;
					message = "\n Time Taken " + timeTaken + " sec , " + timeTaken
							/ 1000 * 60 + " min\n";
					infoMsgRowContent[6] = timeTaken + " sec or " + timeTaken
							/ 1000 * 60 + " min";
					msg += "\n Time Taken " + timeTaken + " sec , " + timeTaken
							/ 1000 * 60 + " min\n";
					
					infoMsgTableModel.setColumnIdentifiers(infoMsgTableColumns);
					infoMsgTableModel.addRow(infoMsgRowContent);
					STPATCGModelController.setInfoMsgTableModel(infoMsgTableModel);
					
					final String report = msg;
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							TCGeneratorPluginUtils
									.showInfoDialog(
											"Information about Test Generation",
											report);
						}
					});
					// }
					// catch (BadLocationException ex) {
					// Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE,
					// null, ex);
					// } catch (IOException ex) {
					// Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE,
					// null, ex);
					// } catch (ClassNotFoundException ex) {
					// Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE,
					// null, ex);
					// }

					// new GraphVisualizer().createGraph(fsm);

				}
			}			
		} else {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					TCGeneratorPluginUtils
							.showErrorDialog(
									"Error",
									"fsm and tree model should not be null!");
				}
			});
		}
		return Status.OK_STATUS;
	}

	private String showALLCoverageResults(TestCasesGenerator testcasesGenerator) {
		String str = "\n Total no. generated TestSuite="
				+ testcasesGenerator.getTestSuiteSize();
		infoMsgRowContent[0] = String.valueOf(testcasesGenerator.getTestSuiteSize());
		str += "\n Total no. generated Test Cases="
				+ testcasesGenerator.getTotalTestCases();
		// str += "\n Total no. Tracibility SSR=" + testcases;
		infoMsgRowContent[1] = String.valueOf(testcasesGenerator.getTotalTestCases());
		str += "\n Total no. of test steps=" + testcasesGenerator.getStep();
		infoMsgRowContent[2] = String.valueOf(testcasesGenerator.getStep());
		for (CoverageCriteria c : testcasesGenerator.getTestconfig()
				.getCoverages()) {
			switch (c.getType()) {
			case Statescoverage:
				str += "\n ALL States coverage=" + c.getDivision() + "="
						+ c.getValue() + "%";
				String coverage1 = c.getDivision() + "=" + c.getValue() + "%";
				infoMsgRowContent[3] = coverage1;
				break;

			case TransitionsCoverage:
				str += "\n ALL Transitions coverage=" + c.getDivision() + "="
						+ c.getValue() + "%";
				String coverage2 = c.getDivision() + "=" + c.getValue() + "%";
				infoMsgRowContent[4] = coverage2;
				break;

			case ALLSTPARequirmentsCoverage:
				str += "\n ALL STPA Safety Requriements coverage="
						+ c.getDivision() + "=" + c.getValue() + "%";
				String coverage3 = c.getDivision() + "=" + c.getValue() + "%";
				infoMsgRowContent[5] = coverage3;
				break;
			default:
				str += "\n Default "+c.getType().name()+"=" + c.getDivision() + "="
						+ c.getValue() + "%";
				break;
			}

		}
		String message = "[INFO] " + TCGeneratorPluginUtils.getCurrentTime()
				+ str;
		TCGeneratorPluginUtils.getInfoOS().println(message);

		DefaultTableModel infoMsgTableModel = new DefaultTableModel();
		
		return str;
	}

	private TreeSet<TestSuite> ShowTreeofTestCases(Set<TestSuite> testsuites) {
		// TODO Implement GUI
//		jTreeOfTestCases.removeAll();
//        jTreeOfTestCases.setModel(null);
        TreeSet<TestSuite> sortedTestSuite = new TreeSet<TestSuite>(new Comparator<TestSuite>() {
            @Override
            public int compare(TestSuite o1, TestSuite o2) {
                if (o1.getId() > o2.getId()) {
                    return -1;
                } else if (o1.getId() < o2.getId()) {
                    return 1;
                } else {
                    return 0;
                }

            }
        });
        sortedTestSuite.addAll(testsuites);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Generated Test Cases");
        DefaultTreeModel model = new DefaultTreeModel(root);
        int i = 1;
        for (TestSuite ts : sortedTestSuite) {
            DefaultMutableTreeNode testsuiteNode = new DefaultMutableTreeNode("Test Suite " + i);

            TreeSet<TestCase> sortedTestCases = new TreeSet<TestCase>(new Comparator<TestCase>() {
                @Override
                public int compare(TestCase o1, TestCase o2) {

                    if (o1.getId() > o2.getId()) {
                        return -1;
                    } else if (o1.getId() < o2.getId()) {
                        return 1;
                    } else {
                        return 0;
                    }

                }

            });
            testsuite++;
            sortedTestCases.addAll(ts.getTestCase());
            int j = 1;
            for (TestCase tc : sortedTestCases) {

                DefaultMutableTreeNode testCaseNode = new DefaultMutableTreeNode("Test Cases " + j);
                DefaultMutableTreeNode input = new DefaultMutableTreeNode("Pre-Conditions and Actions");

                String[] preconditons = tc.getPreConditions().split("\n");
                for (String s : preconditons) {
                    if (s != null && !s.equals("")) {
                        DefaultMutableTreeNode inputvaraibles = new DefaultMutableTreeNode(s);
                        input.add(inputvaraibles);
                    }
                }
                testCaseNode.add(input);
                DefaultMutableTreeNode output = new DefaultMutableTreeNode("Expected Result");
                String[] postconditons = tc.getPostConditions().split("\n");
                for (String s : postconditons) {
                    if (s != null && !s.equals("")) {
                        DefaultMutableTreeNode outputvaraibles = new DefaultMutableTreeNode(s);
                        output.add(outputvaraibles);
                    }
                }
                testCaseNode.add(output);

                testsuiteNode.add(testCaseNode);
                j++;
                testcases++;
            }
            root.add(testsuiteNode);
            i++;
        }
        model.reload(root);
        STPATCGModelController.setTestCaseTreeModel(model);
        // TODO implement GUI
//        jTreeOfTestCases.setExpandsSelectedPaths(true);
//        jTreeOfTestCases.setModel(model);

        return sortedTestSuite;
	}

	private void generateTestCaseTableModel(Set<TestSuite> ts) {
		DefaultTableModel aModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        TCGeneratorPluginUtils.deleteTableContent(aModel);
        //setting the column name
        Object[] tableColumnNames = new Object[7];
        tableColumnNames[0] = "Suite_ID";
        tableColumnNames[1] = "TestCase_ID";
        tableColumnNames[2] = "Transition_ID";
        tableColumnNames[3] = "STPA_SSR_ID";
        tableColumnNames[4] = "Pre-Conditions";
        tableColumnNames[5] = "Post-Conditons";
        tableColumnNames[6] = "Test Results";
        aModel.setColumnIdentifiers(tableColumnNames);
        
		if (ts != null) {
            //System.out.println("test suite" + ts.size());
            for (TestSuite t : ts) {
                if (t != null) {
                    String[] objects = new String[7];

                    for (TestCase tc : t.getTestCase()) {
                        if (tc != null) {
                            objects[0] = Integer.toString(t.getId());
                            objects[1] = String.valueOf(tc.getId());
                            objects[2] = tc.getTranID();
                            if (tc.getRelatedSTPASSR() != null) {
                                objects[3] = tc.getRelatedSTPASSR();
                            }
                            objects[4] = tc.getPreConditions();
                            objects[5] = tc.getPostConditions();
                            objects[6] = "";
                            aModel.addRow(objects);
                            System.out.println(tc);
                        }
                    }

                }
            }
        }
		STPATCGModelController.setTestCaseTableModel(aModel);
	}

	private void updateDataVariables2(EFSM fsm2) {
		List<DataVariable> dataVariables = new ArrayList<>();
		if (inputVariablesTable != null) {
			DefaultTableModel aModel = (DefaultTableModel) inputVariablesTable
					.getModel();
			for (int i = 0; i < aModel.getRowCount(); i++) {
				for (DataVariable var : fsm.getDatavariables()) {

					if (var.getSSID().equals(aModel.getValueAt(i, 0))) {

						var.setInitalvalue(aModel.getValueAt(i, 4).toString());
						var.setMinimumValue(aModel.getValueAt(i, 5).toString());
						var.setMaximumValue(aModel.getValueAt(i, 6).toString());

					}
				}

			}
		}
	}
	
	private void openTestCaseResultEditor() {
		TCGeneratorEditorInput input = new TCGeneratorEditorInput(TestCaseResultEditor.ID);
		input.runCommand("open test case result");
	}

}
