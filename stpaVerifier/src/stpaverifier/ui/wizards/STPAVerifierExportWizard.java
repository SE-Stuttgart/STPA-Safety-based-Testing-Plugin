package stpaverifier.ui.wizards;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import stpaverifier.Activator;
import stpaverifier.controller.model.STPAVerifierController;
import xstampp.ui.wizards.AbstractExportPage;

public abstract class STPAVerifierExportWizard extends Wizard implements IExportWizard{

	private AbstractExportPage page;
	private STPAVerifierController controller;
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
		Activator.getDefault().fetchWizardInfos(this);
	}

	@Override
	public boolean performFinish() {
		File target= new File(page.getExportPath());
		if(target.exists() && !target.delete()){
			MessageDialog.openError(getShell(), "File cannot be deleted!", 
												"The file for the choosen export path "
												+ "cannot be deleted");
			return false;
		}
		return true;
	}

	protected void setExportPage(AbstractExportPage page) {
		page.setNeedProject(false);
		this.page = page;
		addPage(page);
	}

	public AbstractExportPage getPage() {
		return this.page;
	}

	/**
	 * @return the controller
	 */
	public STPAVerifierController getController() {
		return this.controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(STPAVerifierController controller) {
		this.controller = controller;
	}
}
