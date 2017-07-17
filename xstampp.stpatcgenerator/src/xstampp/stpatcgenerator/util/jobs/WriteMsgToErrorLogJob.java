package xstampp.stpatcgenerator.util.jobs;

import java.util.Observable;

import javax.swing.table.DefaultTableModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;

import xstampp.stpatcgenerator.ui.views.LogErrorView;
import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.util.XstamppJob;

/**
 * This class controls the job for writing massages to error log view.
 * 
 * @author Ting Luk-He
 *
 */
public class WriteMsgToErrorLogJob extends XstamppJob{
	
	String message;
	String type;
	DefaultTableModel tableModel;
	boolean deleteOldMsg;
	
	public WriteMsgToErrorLogJob(String name, String type, String message, DefaultTableModel tableModel, boolean deleteOldMsg) {
		super(name);
		this.message = message;
		this.tableModel = tableModel;
		this.type = type;
		this.deleteOldMsg = deleteOldMsg;
	}
	@Override
	protected Observable getModelObserver() {
		return null;
	}
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if(message != null && tableModel != null){
			if(deleteOldMsg){
				TCGeneratorPluginUtils.deleteTableContent(tableModel);
				tableModel.addRow(new Object[]{"", ""});
				tableModel.removeRow(0);
			}
			if(type.equals("error")){
				tableModel.addRow(new Object[]{"<html><font color=\"red\">" + message + "</font></html>"});
			}else if (type.equals("warning")){
				tableModel.addRow(new Object[]{"<html><font color=\"orange\">" + message + "</font></html>"});
			}	
			return Status.OK_STATUS;
		} else {
			return Status.CANCEL_STATUS;
		}
		
	}

}
