package xstampp.stpatcgenerator.util.jobs;

import java.util.List;
import java.util.Observable;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;

import xstampp.stpatcgenerator.ui.editors.GTCConfigEditor;
import xstampp.util.XstamppJob;

/**
 * This class controls the job for updating traceability matrix table.
 * 
 * @author Ting Luk-He
 *
 */
public class UpdateTraceTableJob extends XstamppJob{
	
	GTCConfigEditor editor;
	int selectedIndex;
	double minSimilarity;
	
	public UpdateTraceTableJob(String name, GTCConfigEditor editor2, int selectedIndex) {
		super(name);
		this.editor = editor2;
		this.selectedIndex = selectedIndex;
	}

	@Override
	protected Observable getModelObserver() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void setMinSimilarity(int selectedIndex){
		switch(selectedIndex){
		case 1:
			minSimilarity = 0.05;
			break;
		case 2:
			minSimilarity = 0.1;
			break;
		case 3:
			minSimilarity = 0.15;
			break;
		case 4:
			minSimilarity = 0.2;
			break;
		case 5:
			minSimilarity = 0.25;
			break;
		case 6:
			minSimilarity = 0.3;
			break;
		case 7:
			minSimilarity = 0.35;
			break;
		case 8:
			minSimilarity = 0.4;
			break;
		case 9:
			minSimilarity = 0.45;
			break;
		case 10:
			minSimilarity = 0.5;
			break;
		case 11:
			minSimilarity = 0.55;
			break;
		case 12:
			minSimilarity = 0.6;
			break;
		case 13:
			minSimilarity = 0.65;
			break;
		case 14:
			minSimilarity = 0.7;
			break;
		case 15:
			minSimilarity = 0.75;
			break;
		case 16:
			minSimilarity = 0.8;
			break;
		case 17:
			minSimilarity = 0.85;
			break;
		case 18:
			minSimilarity = 0.9;
			break;
		case 19:
			minSimilarity = 0.95;
			break;
		case 20:
			minSimilarity = 1.0;
			break;
		default:
			minSimilarity = 0;
			break;		
		}
	}
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		setMinSimilarity(selectedIndex);
		
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {				
				editor.updateTraceTable(minSimilarity);
			}
		});
		return Status.OK_STATUS;
	}

}
