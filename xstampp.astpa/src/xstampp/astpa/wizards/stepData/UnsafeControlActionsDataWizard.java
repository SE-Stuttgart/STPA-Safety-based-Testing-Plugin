package xstampp.astpa.wizards.stepData;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.ui.unsafecontrolaction.UnsafeControlActionsView;
import xstampp.astpa.util.jobs.ICSVExportConstants;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.ui.wizards.CSVExportPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class UnsafeControlActionsDataWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public UnsafeControlActionsDataWizard() {
		super(UnsafeControlActionsView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters,
				Messages.UnsafeControlActions + Messages.AsDataSet, Activator.PLUGIN_ID));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.UNSAFE_CONTROL_ACTION);
	}
}
