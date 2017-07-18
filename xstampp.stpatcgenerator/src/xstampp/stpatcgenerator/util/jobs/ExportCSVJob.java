package xstampp.stpatcgenerator.util.jobs;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.table.DefaultTableModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.ui.common.ProjectManager;
import xstampp.util.BufferedCSVWriter;

/**
 * This class controls the jobs for export CSV.
 * 
 * @author Ting Luk-He
 *
 */
public class ExportCSVJob extends Job{

	public static final int STATEFLOW_TRUTHTABLE = 1 << 0;
	public static final int FINAL_REPORT = 1 << 1;
	public static final int STATEFLOW_PROPERTIES = 1 << 2;
	public static final int EFSM_TRUTHTABLE = 1 << 3;
	public static final int TEST_INPUT_AND_CONFIG = 1 << 4;
	public static final int TRACE_MATRIX = 1 << 5;
	public static final int Test_Result = 1 << 6;
	
	private File csvFile;
	private char seperator =';';
	private boolean switchCol_Row=false;
	private int exportCode;
		
	public ExportCSVJob(String name,String path,int code){
		super(name);
		try{
			this.csvFile = new File(path);
		}catch(NullPointerException e){
			this.csvFile = null;
		}
		this.exportCode = code;
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if(csvFile == null){
			return Status.CANCEL_STATUS;
		}
		if(!csvFile.exists()){
			try {
				csvFile.createNewFile();
			} catch (IOException e) {
				return Status.CANCEL_STATUS;
			}
		}
			try {
				BufferedCSVWriter csvWriter = new BufferedCSVWriter(new FileWriter(csvFile),seperator);
				try{
					if((exportCode & STATEFLOW_TRUTHTABLE) != 0){
						writeStateFlowTruthTable(csvWriter);
					}
					if((exportCode & FINAL_REPORT) != 0){
						writeFinalReport(csvWriter);
					}
					if((exportCode & STATEFLOW_PROPERTIES) != 0){
						writeStateFlowProperties(csvWriter);
					}
					if((exportCode & EFSM_TRUTHTABLE) != 0){
						writeEFSMTruthTable(csvWriter);
					}
					if((exportCode & TEST_INPUT_AND_CONFIG) != 0){
						writeTestInputAndConfig(csvWriter);
					}
					if((exportCode & TRACE_MATRIX) != 0){
						writeTraceMatrix(csvWriter);
					}
					if((exportCode & Test_Result) != 0){
						writeTestResult(csvWriter);
					}
				}catch(Exception e){
					csvWriter.close();
					return Status.CANCEL_STATUS;
				}
				csvWriter.close();
				if (csvFile.exists() && Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(csvFile);
				}
				return Status.OK_STATUS;
			} catch (IOException e) {
				ProjectManager.getLOGGER().error(e.getMessage(), e);
				return Status.CANCEL_STATUS;
			}
		
	}

	private void writeTestResult(BufferedCSVWriter csvWriter) throws IOException{
		csvWriter.write("Information of Generated Test Case Result");
		csvWriter.newLine();
		DefaultTableModel tModel = STPATCGModelController.getInfoMsgTableModel();		
		writeTableContentToCSV(tModel, csvWriter);
		
		csvWriter.newLine();
		
		csvWriter.write("Generated Test Cases");
		csvWriter.newLine();
		tModel = STPATCGModelController.getTestCaseTableModel();		
		writeTableContentToCSV(tModel, csvWriter);
		
	}

	private void writeTraceMatrix(BufferedCSVWriter csvWriter) throws IOException{
		csvWriter.write("Traceability Matrix between Extended Finite State Machine Transitions and STPA Software Safety Requirements");
		csvWriter.newLine();
		DefaultTableModel tModel = STPATCGModelController.getTraceMatrixTableModel();		
		writeTableContentToCSV(tModel, csvWriter);
		
		csvWriter.newLine();
		csvWriter.newLine();
		
		csvWriter.write("Extended Finite State Machine Transitions");
		csvWriter.newLine();
		tModel = STPATCGModelController.getTransConditionTableModel();		
		writeTableContentToCSV(tModel, csvWriter);
		
		csvWriter.newLine();
		csvWriter.newLine();
		
		csvWriter.write("STPA Software Safety Requirements");
		csvWriter.newLine();
		tModel = STPATCGModelController.getSsrTableModel();		
		writeTableContentToCSV(tModel, csvWriter);
	
	}

	private void writeTestInputAndConfig(BufferedCSVWriter csvWriter) throws IOException{
		csvWriter.write("Test Case Input Variables");
		csvWriter.newLine();
		DefaultTableModel tModel = STPATCGModelController.getTestInputVarTableModel();		
		writeTableContentToCSV(tModel, csvWriter);	
		
		csvWriter.newLine();
		
		csvWriter.write("Configration");
		csvWriter.newLine();
		tModel = STPATCGModelController.getGenTCConfigTableModel();		
		writeTableContentToCSV(tModel, csvWriter);
	}

	private void writeEFSMTruthTable(BufferedCSVWriter csvWriter) throws IOException{
		csvWriter.write("Extended Finite State Machine Truth Table");
		csvWriter.newLine();
		DefaultTableModel tModel = STPATCGModelController.getEfsmTruthTableModel();		
		writeTableContentToCSV(tModel, csvWriter);		
	}

	private void writeStateFlowProperties(BufferedCSVWriter csvWriter) throws IOException{
		csvWriter.write("Safe Behavioral Model Properties");
		csvWriter.newLine();
		csvWriter.newLine();
		csvWriter.write("Safe Behavioral Model States");
		csvWriter.newLine();
		DefaultTableModel tModel = STPATCGModelController.getStatesTableModel();		
		writeTableContentWithoutLastColumn(tModel, csvWriter);
		
		csvWriter.newLine();
		csvWriter.write("Safe Behavioral Model Input Variables");
		csvWriter.newLine();
		tModel = STPATCGModelController.getInputVarTableModel();		
		writeTableContentWithoutLastColumn(tModel, csvWriter);
		
		csvWriter.newLine();
		csvWriter.write("Safe Behavioral Model Local Variables");
		csvWriter.newLine();
		tModel = STPATCGModelController.getLocalVarTableModel();		
		writeTableContentWithoutLastColumn(tModel, csvWriter);
		
		csvWriter.newLine();
		csvWriter.write("Safe Behavioral Model Output Variables");
		csvWriter.newLine();
		tModel = STPATCGModelController.getOutputVarTableModel();		
		writeTableContentWithoutLastColumn(tModel, csvWriter);
	}

	private void writeFinalReport(BufferedCSVWriter csvWriter) throws IOException{
		csvWriter.write("Test Case Input Variables");
		csvWriter.newLine();
		DefaultTableModel tModel = STPATCGModelController.getTestInputVarTableModel();		
		writeTableContentToCSV(tModel, csvWriter);	
		
		csvWriter.newLine();
		
		csvWriter.write("Extended Finite State Machine Transitions");
		csvWriter.newLine();
		tModel = STPATCGModelController.getTransConditionTableModel();		
		writeTableContentToCSV(tModel, csvWriter);
		
		csvWriter.newLine();
		
		csvWriter.write("STPA Software Safety Requirements");
		csvWriter.newLine();
		tModel = STPATCGModelController.getSsrTableModel();		
		writeTableContentToCSV(tModel, csvWriter);
		
		csvWriter.newLine();
		
		csvWriter.write("Traceability Matrix between Extended Finite State Machine Transitions and STPA Software Safety Requirements");
		csvWriter.newLine();
		tModel = STPATCGModelController.getTraceMatrixTableModel();		
		writeTableContentToCSV(tModel, csvWriter);
		
		csvWriter.newLine();
		
		csvWriter.write("Configration");
		csvWriter.newLine();
		tModel = STPATCGModelController.getGenTCConfigTableModel();		
		writeTableContentToCSV(tModel, csvWriter);
		
		csvWriter.newLine();
		
		csvWriter.write("Information of Generated Test Case Result");
		csvWriter.newLine();
		tModel = STPATCGModelController.getInfoMsgTableModel();		
		writeTableContentToCSV(tModel, csvWriter);
		
		csvWriter.newLine();
		
		csvWriter.write("Generated Test Cases");
		csvWriter.newLine();
		tModel = STPATCGModelController.getTestCaseTableModel();		
		writeTableContentToCSV(tModel, csvWriter);
	}

	private void writeStateFlowTruthTable(BufferedCSVWriter csvWriter) throws IOException{
		csvWriter.write("State Flow Truth Table");
		csvWriter.newLine();
		DefaultTableModel tModel = STPATCGModelController.getStateFlowTruthTableModel();
		writeTableContentToCSV(tModel, csvWriter);
	}
	
	private void writeTableContentToCSV(DefaultTableModel tModel, BufferedCSVWriter csvWriter) throws IOException{
		for(int j = 0; j < tModel.getColumnCount(); j++){
			csvWriter.writeCell(tModel.getColumnName(j));
		}
		csvWriter.newLine();		
		Object[] rowContent = new Object[tModel.getColumnCount()];
		for(int i = 0; i < tModel.getRowCount(); i++){
			for(int j = 0; j < tModel.getColumnCount(); j++){
				rowContent[j] = tModel.getValueAt(i, j);
				if (rowContent[j] != null){
					String cell = rowContent[j].toString();
					if( cell.indexOf(",") != -1 ){
						cell = cell.replaceAll(",", " ");
					}					
					csvWriter.writeCell(cell);				
				} else {
					csvWriter.writeCell("");
				}
			}	
			csvWriter.newLine();
		}
	}
	
	private void writeTableContentWithoutLastColumn(DefaultTableModel tModel, BufferedCSVWriter csvWriter) throws IOException{
		for(int j = 0; j < tModel.getColumnCount() - 1; j++){
			csvWriter.writeCell(tModel.getColumnName(j));
		}
		csvWriter.newLine();		
		Object[] rowContent = new Object[tModel.getColumnCount() - 1];
		for(int i = 0; i < tModel.getRowCount(); i++){
			for(int j = 0; j < tModel.getColumnCount() - 1; j++){
				rowContent[j] = tModel.getValueAt(i, j);
				if (rowContent[j] != null){
					String cell = rowContent[j].toString();
					if( cell.indexOf(",") != -1 ){
						cell = cell.replaceAll(",", " ");
					}					
					csvWriter.writeCell(cell);				
				} else {
					csvWriter.writeCell("");
				}
			}	
			csvWriter.newLine();
		}
	}
}
