package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.ui.unsafecontrolaction.UnsafeControlActionsView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.ui.wizards.TableExportPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class UCATableExportWizard extends AbstractExportWizard {
	TableExportPage exportPage;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public UCATableExportWizard() {
		super(UnsafeControlActionsView.ID);
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$
		this.exportPage = new TableExportPage(filters,
				Messages.ExportPreferences + Messages.AsImage, Activator.PLUGIN_ID);
		this.setExportPage(this.exportPage);
	}

	@Override
	public void addPages() {
		this.addPage(this.exportPage);
	}

	@Override
	public boolean performFinish() {

		return this.performXSLExport(
				"/fopuca.xsl", false, ""); //$NON-NLS-1$
	}
}
