package stpaverifier.ui.views;

import java.util.Observable;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import stpaverifier.controller.ObserverValues;
import stpaverifier.controller.model.ModexController;
import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.controller.preferences.spin.SpinPreferenceConstants;
import stpaverifier.ui.views.utils.AbstractConfigurationFolder;
import stpaverifier.ui.views.utils.FoldableView;

public class ConfigureModexFolder extends AbstractConfigurationFolder implements SpinPreferenceConstants,FoldableView{

//	private ScrolledComposite scrollModexComp;
//	private Composite modexConfigComp;
	private ModexController modexController;

	@Override
	public void createPartControl(Composite parent) {
//		FormData data = new FormData();
//		data.left =new FormAttachment(100);
//		data.right = new FormAttachment(0);
//		data.top = new FormAttachment(0);
//		data.bottom = new FormAttachment(100);
//
//		parent.setLayout(new FormLayout());
//		scrollModexComp = new AdaptedConfiguartionComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
//		scrollModexComp.setLayout(new FormLayout());
//		scrollModexComp.setLayoutData(data);
//		
//		modexConfigComp= new Composite(scrollModexComp, SWT.NONE);
		parent.setLayout(new GridLayout(1,false));
//		modexConfigComp.setLayoutData(data);
		String tooltip ="The path to the modex executable";
		String title = "Modex Path: ";
		Text prefField =createPathPrefInput(parent, title, tooltip, null);
		prefField.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent e) {
				e.doit = verifyCommand(new String[]{e.text,"-V"});
			}
		});
		registry.registerStringInput(prefField,PREF_MODEX_PATH);
		
		if(System.getProperty("os.name").toLowerCase().contains("win")){
			
			tooltip ="The path to the cygwin dll, this dinamic link library is needed to execute modex under Windows";
			title = "Cygwin dll Path: ";
			prefField =createDirPrefInput(parent, title, tooltip);
			prefField.addVerifyListener(new VerifyListener() {
				
				@Override
				public void verifyText(VerifyEvent e) {
					e.doit = e.text.toLowerCase().contains("cygwin");
				}
			});
			registry.registerStringInput(prefField,PREF_CYGWIN_DLL_PATH);
			
		}
		
		Text tSource =createPathPrefInput(parent, "Source File:", "Choose the source you want to extract the"
													+ "model from", new String[]{"*.c"});
		tSource.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent e) {
				e.doit =  modexController != null && modexController.setSourceFile(e.text);
			}
		});
		Button startModex = new Button(parent, SWT.PUSH);
		startModex.setText("Generate Model");
		startModex.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( modexController != null){
					String error = modexController.start(getdModel());
					if(error != null){
						MessageDialog.openError(Display.getDefault().getActiveShell(), "Failed running modex", error);
					}
				}
			}
		});
		super.createPartControl(parent);
//		modexConfigComp.setSize(modexConfigComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//		scrollModexComp.setContent(modexConfigComp);
		
	}

	@Override
	public void setFocus() {
		
	}
	@Override
	public void setdModel(STPAVerifierController dModel) {
		super.setdModel(dModel);
		this.modexController = dModel.getModexController();
	}

	
	@Override
	public void update(Observable o, Object arg) {
		ObserverValues value = (ObserverValues) arg;
		switch(value){
		case USE_SPIN:
//			if(getdModel().isUseSpin()){
//				scrollModexComp.setVisible(true);
//			}else{
//				scrollModexComp.setVisible(false);
//			}
		
		default:
			break;
			
		}
	}

	@Override
	public void activate() {
		
	}

}
