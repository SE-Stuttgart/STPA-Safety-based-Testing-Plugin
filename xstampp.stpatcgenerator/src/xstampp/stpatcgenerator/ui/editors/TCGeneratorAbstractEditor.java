package xstampp.stpatcgenerator.ui.editors;

import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.STPAEditorInput;
/**
 * An abstract class for editors.
 * @author Ting Luk-He
 *
 */
public abstract class TCGeneratorAbstractEditor extends EditorPart {
	private UUID projectID;
	
	public void partActivated(IWorkbenchPart arg0) {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor() != null) {
			if (!PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().getActiveEditor().getSite().getId()
					.equals("acast.steps.step2_1")) {
				if (arg0 == this) {
					((TCGeneratorEditorInput) getEditorInput()).activate();
				} else if (arg0 != this) {
					((TCGeneratorEditorInput) getEditorInput()).deactivate();
				}
			}
		}
	}
	
	public UUID getProjectID() {
		return projectID;
	}

	public void setProjectID(UUID projectID) {
		this.projectID = projectID;
	}
	
	
}
