package stpaverifier.ui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import stpaverifier.ui.views.ConfigurationViewPart;
import stpaverifier.ui.views.FormularViewPart;
import xstampp.ui.editors.StandartEditorPart;

public class STPAVerifierPart extends StandartEditorPart {


	@Override
	public String getId() {
		return "stpaverifier.ui.editors.STPAVerifierPart";
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FormLayout());
		FormData formData = new FormData();
		Composite ltlFormulars = new Composite(parent, SWT.BORDER);
		ltlFormulars.setLayout(new FillLayout());
		new FormularViewPart().createPartControl(ltlFormulars);
		
		Composite editorComp = new Composite(parent, SWT.BORDER);
		
		Composite configView = new Composite(parent, SWT.BORDER);
		ltlFormulars.setLayout(new FillLayout());
		new ConfigurationViewPart().createPartControl(ltlFormulars);
		
		TabFolder consoleFolder = new TabFolder(parent, SWT.BORDER);
		consoleFolder.setLayout(new FillLayout());
		TabItem consoleItem = new TabItem(consoleFolder, SWT.BORDER);
		consoleItem.setControl(new Composite(consoleFolder, SWT.BORDER));
		consoleItem.setText("Console");
		
		TabFolder resultFolder = new TabFolder(parent, SWT.BORDER);
		resultFolder.setLayout(new FillLayout());
		TabItem resultItem = new TabItem(resultFolder, SWT.BORDER);
		resultItem.setControl(new Composite(resultFolder, SWT.BORDER));
		resultItem.setText("Result");
		//START Form Layout definition
		
			formData = new FormData();
			formData.left= new FormAttachment(0);
			formData.right = new FormAttachment(60);
			formData.bottom = new FormAttachment(100);
			formData.top = new FormAttachment(60);
			consoleFolder.setLayoutData(formData);
			
			formData = new FormData();
			formData.left= new FormAttachment(consoleFolder);
			formData.right= new FormAttachment(100);
			formData.top = new FormAttachment(60);
			formData.bottom = new FormAttachment(100);
			resultFolder.setLayoutData(formData);
			
			formData = new FormData();
			formData.left= new FormAttachment(0);
			formData.right= new FormAttachment(50);
			formData.top = new FormAttachment(0);
			formData.bottom = new FormAttachment(60);
			ltlFormulars.setLayoutData(formData);
			
			formData = new FormData();
			formData.left= new FormAttachment(70);
			formData.right= new FormAttachment(100);
			formData.top = new FormAttachment(0);
			formData.bottom = new FormAttachment(consoleFolder);
			configView.setLayoutData(formData);
			
			formData = new FormData();
			formData.left= new FormAttachment(ltlFormulars);
			formData.right= new FormAttachment(configView);
			formData.top = new FormAttachment(100);
			formData.bottom = new FormAttachment(consoleFolder);
			editorComp.setLayoutData(formData);
			
			
			
			parent.layout(true);
		//END Form Layout definition
	}
}
