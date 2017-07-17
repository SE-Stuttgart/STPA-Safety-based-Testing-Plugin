package xstampp.stpatcgenerator.util.jobs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.smv.GeneratorSMVFile;
import xstampp.stpatcgenerator.ui.views.LTLTableView;
import xstampp.stpatcgenerator.ui.views.LogErrorView;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.util.XstamppJob;

/**
 * This class controls the job for verify smv with local model checker.
 * 
 * @author Ting Luk-He
 *
 */
public class VerifySMVJob extends XstamppJob{
	private String nusmvPath;
	private String smvFilePath;
	String message;
	List<String> ltlResults = new ArrayList<String>();
	
	public VerifySMVJob(String name, String nusmvPath, String smvFilePath) {
		super(name);
		this.nusmvPath = nusmvPath;
		this.smvFilePath = smvFilePath;
		System.out.println("smvIFilePath" + smvFilePath);
	}

	@Override
	protected Observable getModelObserver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if (smvFilePath == null) {
            return Status.CANCEL_STATUS;
        }
        String filePath = nusmvPath + " " + smvFilePath;
        try {
            Runtime rt = Runtime.getRuntime();

            Process pr = rt.exec(filePath);

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            String line = null;

            while ((line = input.readLine()) != null) {
            	if(line.isEmpty()) continue;
            	message = "[INFO] " + TCGeneratorPluginUtils.getCurrentTime() + " NuSMV-> " + line;
            	TCGeneratorPluginUtils.getInfoOS().println(message);
            	if(line.contains("specification")){
            		String result = line.substring(line.lastIndexOf("is") + 3, line.length());
//            		System.out.println("Result of specificaion: " + result);
            		if (result.contains("true")){
            			ltlResults.add("true");
            		}else if (result.contains("false")){
            			ltlResults.add("false");
            		}else {
            			ltlResults.add("unknown");
            		}
            	}
            }
            
            int counter = 0;
            while ((line = error.readLine()) != null) {
            	if(line.isEmpty()) continue;
            	message = "[ERROR] " + TCGeneratorPluginUtils.getCurrentTime() + " NuSMV-> " + line;
            	TCGeneratorPluginUtils.getErrorOS().println(message);
            	if (counter == 0){
            		LogErrorView.writeError(message, true);
            	}else {
            		LogErrorView.writeError(message, false);
            	}
            	counter++;
            }
//            if(counter > 0){
//            	Display.getDefault().asyncExec(new Runnable() {
//    				@Override
//    				public void run() {
//    					TCGeneratorPluginUtils.showView(LogErrorView.ID);
//    				}
//            	});
//            }
            int exitVal = pr.waitFor();
            if (exitVal == 0) {
            	message = "[INFO] " + TCGeneratorPluginUtils.getCurrentTime() + " STPATCG-> The SMV Model " + GeneratorSMVFile.getFilename() + " has been successfully verifierd.";
            	TCGeneratorPluginUtils.getInfoOS().println(message);
            	Display.getDefault().asyncExec(new Runnable() {
    				@Override
    				public void run() {
    					STPATCGModelController.setLtlResults(ltlResults);
    					updateLTLTable();
    					TCGeneratorPluginUtils
    							.showInfoDialog(
    									"Confirmation Message",
    									"The SMV Model " + GeneratorSMVFile.getFilename() + " has been successfully verifierd");  
    					TCGeneratorPluginUtils.showView(LTLTableView.ID);
    				}

					private void updateLTLTable() {
						IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					    .getActivePage().findView(LTLTableView.ID);
						if (part != null){
							for(int i = 0; i < ltlResults.size(); i++){
								if(ltlResults.get(i).equals("true")){
									LTLTableView.getTableModel().setValueAt(LTLTableView.okIcon, i, 1);
								}else if(ltlResults.get(i).equals("false")){
									LTLTableView.getTableModel().setValueAt(LTLTableView.wrongIcon, i, 1);
								}
							}							
						}
					}
    			});
            	if (ltlResults != null){
            		STPATCGModelController.setLtlResults(ltlResults);
            		Display.getDefault().asyncExec(new Runnable() {
        				@Override
        				public void run() {
        					LTLTableView.updateLTLTable(ltlResults);
        				}
        			});            		
            	}            	
            } else {
            	message = "[ERROR] " + TCGeneratorPluginUtils.getCurrentTime() + " STPATCG-> Exited with error code ";
            	TCGeneratorPluginUtils.getErrorOS().println(message);
            	Display.getDefault().asyncExec(new Runnable() {
    				@Override
    				public void run() { 					
    					TCGeneratorPluginUtils
    							.showErrorDialog(
    									"Error Message",
    									"NuSMV exited with error: Please see error trace in Error Log");   					
    				}
    			});
            	Display.getDefault().asyncExec(new Runnable() {
    				@Override
    				public void run() { 					
    					TCGeneratorPluginUtils.showView(LogErrorView.ID);
    				}
    			});
            }
        } catch (Exception e) {
            System.out.println(e.toString());
			message = e.toString();
//                txtConsole.getDocument().insertString(txtConsole.getDocument().getLength(), e.toString() + "\n", getColor(Color.RED));
			e.printStackTrace();
        }
	return Status.OK_STATUS;
	}

}
