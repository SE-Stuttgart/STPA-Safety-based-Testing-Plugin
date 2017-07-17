package stpaverifier.util.jobs;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import stpaverifier.controller.IProperty;
import stpaverifier.controller.IVerificationResult;
import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.ui.views.utils.PropertyHoldsProvider;
import xstampp.ui.common.ProjectManager;
import xstampp.util.BufferedCSVWriter;

public class ExportVerifierCSVJob extends Job {

	public static final int COUNTEREXAMPLE = 1 << 0;

	public static final int RESULTS = 1 << 1;
	
	public static final int PROPERTIES = 1 << 2;
	
	
	private File csvFile;
	private char seperator =';';
	private boolean switchCol_Row=false;
	private int exportCode;
	
	private STPAVerifierController verifierController;

	public ExportVerifierCSVJob(String name,STPAVerifierController controller,String path,int code) {
		super(name);
		this.verifierController = controller;
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
					if((exportCode & COUNTEREXAMPLE) != 0){
						writeCounterexample(csvWriter);
					}
					if((exportCode & RESULTS) != 0){
						if(switchCol_Row){
							writeResultRowTable(csvWriter);
						}else{
							writeResults(csvWriter);
						}
					}
					if((exportCode & PROPERTIES) != 0){
						writeProperties(csvWriter,true);
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

	private void writeCounterexample(BufferedCSVWriter csvWriter) throws IOException{
		for(IProperty example : verifierController.getAllProperties()){
			if(example.getCounterexample() != null && example.getState() == IProperty.STATE_COUNTEREXAMPLE){
				csvWriter.writeCell("Counterexample for:");
				csvWriter.writeCell(example.getsID());
				csvWriter.writeCell(example.getsFormular(true, false));
				csvWriter.newLine();
				csvWriter.newLine();
				for(String line: example.getCounterexample().getContent()){
					csvWriter.writeCell(line);
					csvWriter.newLine();
				}
			}
			
		}
	}

	private void writeResults(BufferedCSVWriter csvWriter) throws IOException{
		
		csvWriter.write("Result View");
		csvWriter.newLine();
		csvWriter.writeCell("ID");
		csvWriter.writeCell("#Depth");
		csvWriter.writeCell("#Stored States");
		csvWriter.writeCell("#Transitions");
		csvWriter.writeCell("#Time");
		csvWriter.writeCell("#Memory Usage(MB)");
		csvWriter.writeCell("Result");
		csvWriter.newLine();
		for(IVerificationResult example : verifierController.getResults()){
			csvWriter.writeCell("SSR1."+example.getProvider().getNumber());
			csvWriter.writeCell(example.getDepth());
			csvWriter.writeCell(example.getStoredStates());
			csvWriter.writeCell(example.getTransitions());
			csvWriter.writeCell(example.getTime());
			csvWriter.writeCell(example.getUsedMemory());
			csvWriter.writeCell(example.getResult());
			csvWriter.newLine();
		}
	}
	
	private void writeResultRowTable(BufferedCSVWriter csvWriter) throws IOException{
		
		csvWriter.write("Result View");
		csvWriter.newLine();
		csvWriter.writeCell("ID");
		for(IVerificationResult example : verifierController.getResults()){
			csvWriter.writeCell("SSR1."+example.getProvider().getNumber());
		}
		csvWriter.newLine();
		csvWriter.writeCell("#Depth");
		for(IVerificationResult example : verifierController.getResults()){
			csvWriter.writeCell(example.getDepth());
		}
		csvWriter.newLine();
		csvWriter.writeCell("#Stored States");
		for(IVerificationResult example : verifierController.getResults()){
			csvWriter.writeCell(example.getStoredStates());
		}
		csvWriter.newLine();
		csvWriter.writeCell("#Transitions");
		for(IVerificationResult example : verifierController.getResults()){
			csvWriter.writeCell(example.getTransitions());
		}
		csvWriter.newLine();
		csvWriter.writeCell("#Time");
		for(IVerificationResult example : verifierController.getResults()){
			csvWriter.writeCell(example.getTime());
		}
		csvWriter.newLine();
		csvWriter.writeCell("#Memory Usage(MB)");
		for(IVerificationResult example : verifierController.getResults()){
			csvWriter.writeCell(example.getUsedMemory());
		}
		csvWriter.newLine();
		csvWriter.writeCell("Result");
		for(IVerificationResult example : verifierController.getResults()){
			csvWriter.writeCell(example.getResult());
		}
		csvWriter.newLine();
	}
	private void writeProperties(BufferedCSVWriter csvWriter,boolean isSpin) throws IOException{
		PropertyHoldsProvider provider = new PropertyHoldsProvider();
		csvWriter.write("Properties View");
		csvWriter.newLine();
		csvWriter.writeCell("ID");
		csvWriter.writeCell("Formula");
		csvWriter.writeCell("Status");
		csvWriter.newLine();
		for(IProperty example : verifierController.getAllProperties()){
			csvWriter.writeCell("SSR1."+example.getsID());
			csvWriter.writeCell(example.getsFormular(isSpin, false));
			csvWriter.writeCell(provider.getText(example));
			csvWriter.newLine();
		}
	}

	/**
	 * @return the switchCol_Row
	 */
	public boolean isSwitchCol_Row() {
		return this.switchCol_Row;
	}

	/**
	 * @param switchCol_Row the switchCol_Row to set
	 */
	public void setSwitchCol_Row(boolean switchCol_Row) {
		this.switchCol_Row = switchCol_Row;
	}
}



