package stpaverifier.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import stpaverifier.Activator;
import stpaverifier.ui.views.ResultsDiagViewPart;
import stpaverifier.util.jobs.ExportPieJob;
import stpaverifier.util.jobs.PDFExportJob;
import xstampp.ui.wizards.TableExportPage;
import xstampp.util.JAXBExportJob;

public class IMGWizardofResultDiagram extends STPAVerifierExportWizard {

	public static final String ID ="stpaVerifier.exportWizard.csv.counterexample";
	
	public IMGWizardofResultDiagram() {
		setWindowTitle("Export the Result as Pie chart");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
		super.init(workbench, selection);
		
		TableExportPage page = new TableExportPage(new String[]{"*.png","*.jpg"},"Results Diagram",Activator.PLUGIN_ID);
		page.setNameSuggestion("Results_Diagram_");
		page.setNeedProject(false);
		page.setShowTextConfig(false);
		setExportPage(page);
		
	}

	@Override
	public boolean performFinish() {
		if(super.performFinish()){
			ExportPieJob job = new ExportPieJob("Export Results diagram...", getController(),getPage().getExportPath(),ResultsDiagViewPart.class,ResultsDiagViewPart.ID);
			job.setShowPreview(true);
			job.schedule();
			return true;
		}
		return false;
	}


	
}
