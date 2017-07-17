package xstampp.stpatcgenerator.wizards.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import xstampp.stpatcgenerator.util.TCGeneratorPluginUtils;
import xstampp.ui.wizards.AbstractWizardPage;

/**
 * This class defines the layout for the wizard of verify with local model
 * checker.
 * 
 * @author Ting Luk-He
 *
 */
public class VerifyWizardPage extends AbstractWizardPage implements SelectionListener {
	private Composite composite;
	private PathComposite pathChooser;
	private String[] filters = { "NuSMV" };

	public VerifyWizardPage(String pageName) {
		super(pageName);
		setTitle("Configuration of SMV Model Verify");
		setDescription("Select Model Checker NuSMV");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {

		// create the composite to hold the widgets
		composite = new Composite(parent, SWT.NONE);
		// create the desired layout for this wizard page
		int nCol = 4;
		GridLayout layout = new GridLayout(nCol, true);
		composite.setLayout(layout);
		// NuSMV path chooser
		pathChooser = new PathComposite(this.filters, this.filters, composite, PathComposite.PATH_DIALOG,
				"NuSMV Path: ");
		String filePath = TCGeneratorPluginUtils.readLocationFromFile(TCGeneratorPluginUtils.NUSMV_LOCATION_FILE);
		pathChooser.setText(filePath);
		GridData gd = new GridData();
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = nCol;
		pathChooser.setLayoutData(gd);

		setControl(composite);
		setPageComplete(checkFinish());
	}

	public String getNuSMVpath() {
		return this.pathChooser.getText();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkFinish() {
		this.setErrorMessage(null);
		if (((this.pathChooser == null) || this.pathChooser.getText().equals(""))) { //$NON-NLS-1$
			return false;
		}
		return true;
	}

}
