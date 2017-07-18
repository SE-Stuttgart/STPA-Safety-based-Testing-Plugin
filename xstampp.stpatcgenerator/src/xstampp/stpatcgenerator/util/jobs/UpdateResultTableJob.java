package xstampp.stpatcgenerator.util.jobs;

import java.util.Observable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;

import xstampp.stpatcgenerator.ui.editors.GTCConfigEditor;
import xstampp.stpatcgenerator.ui.editors.TestCaseResultEditor;
import xstampp.util.XstamppJob;

/**
 * This class controls the job for updating test case result table.
 * 
 * @author Ting Luk-He
 *
 */
public class UpdateResultTableJob extends XstamppJob{
	TestCaseResultEditor editor;
	int selectedIndex;
	
	public UpdateResultTableJob(String name, TestCaseResultEditor editor, int selectedIndex) {
		super(name);
		this.editor = editor;
		this.selectedIndex = selectedIndex;
	}

	@Override
	protected Observable getModelObserver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {	
				if(selectedIndex == 0){
					editor.updateResultTable(true);
				} else {
					editor.updateResultTable(false);
				}
				
			}
		});
		return Status.OK_STATUS;
	}
	
	
}
