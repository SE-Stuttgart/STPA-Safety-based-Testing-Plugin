package stpaverifier.ui.views.utils;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import stpaverifier.controller.preferences.STPAVerifierPreferenceRegistry;
import stpaverifier.util.ConsoleRuntime;
import stpaverifier.util.ICMDScanner;

/**
 * This class provides a functions and components for creating a configuration folder
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 */
public abstract class AbstractConfigurationFolder extends AModelContentView {

	protected STPAVerifierPreferenceRegistry registry = new STPAVerifierPreferenceRegistry();
	
	protected class AdaptedConfiguartionComposite extends ScrolledComposite{

		public AdaptedConfiguartionComposite(Composite parent, int style) {
			super(parent, style);
		}
		@Override
		public void setBounds(Rectangle rect) {
			for(Control child : getChildren()){
				child.setSize(child.computeSize(rect.width, SWT.DEFAULT));
			}
			super.setBounds(rect);
		}
		
	}
	
	@Override
	public void createPartControl(Composite parent) {
		if(parent.getLayout() instanceof GridLayout){
			GridLayout layout = (GridLayout) parent.getLayout();
			Button loadDefaults = new Button(parent, SWT.PUSH);
			loadDefaults.setText("Load defaults");
			loadDefaults.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					registry.loadDefaults();
				}
			});
			loadDefaults.setLayoutData(new GridData(SWT.CENTER, SWT.END, false, false,layout.numColumns,1));
		}
	}
	
	
	protected Text createIntegerPrefInput(Composite parent,String msg,String tooltip){
		final Text tField = createStringPrefInput(parent,0, msg, tooltip);
		tField.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent e) {
				if(e.character == '\b'){
					e.doit = true;
				}else if(e.character == SWT.DEL){
					e.doit = true;
				}else{
					for(char c : e.text.toCharArray()){
						if(!Character.isDigit(c)){
							e.doit = false;
						}
					}
				}
			}
		});
		return tField;
	}
	
	/**
	 * this method creates a text component with a leading name label 
	 * and a button to open a path chooser
	 * it expects a parent composite with a GridLayout this will fill one cell
	 * 
	 * @param parent a parent composite with a GridLayout containing one column
	 * @param msg the title of the input 
	 * @param tooltip the tooltip that will be displayed as a hint
	 * @param the filter strings  {@link FileDialog#setFilterExtensions(String[])}
	 * @return the created Text component 
	 */
	protected Text createPathPrefInput(Composite parent,String msg,String tooltip, final String[] filter){
		Composite comp = new Composite(parent, SWT.None);
		comp.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		comp.setLayout(new GridLayout(2,false));
		final Text tField = createStringPrefInput(comp,SWT.READ_ONLY, msg, tooltip);
		Button prefButton =new Button(comp, SWT.PUSH);
		prefButton.setText("Choose Path");
		prefButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
				dialog.setFilterExtensions(filter);
				String path = dialog.open();
				if(path != null){
					tField.setText(path);
				}
			}
		});
		return tField;
	}
	
	/**
	 * this method creates a read only text component with a leading name label and a 
	 * button to open a directory path chooser
	 * it expects a parent composite with a GridLayout this will fill one cell
	 * 
	 * @param parent a parent composite with a GridLayout containing one column
	 * @param msg the title of the input 
	 * @param tooltip the tooltip that will be displayed as a hint
	 * @return the created Text component 
	 */
	protected Text createDirPrefInput(Composite parent,String msg,String tooltip){
		Composite comp = new Composite(parent, SWT.None);
		comp.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		comp.setLayout(new GridLayout(2,false));
		final Text tField = createStringPrefInput(comp,SWT.READ_ONLY, msg, tooltip);
		Button prefButton =new Button(comp, SWT.PUSH);
		prefButton.setText("Choose Directory");
		prefButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
				String path = dialog.open();
				if(path != null){
					tField.setText(path);
				}
			}
		});
		return tField;
	}
	
	/**
	 * this method creates a text component with a leading label
	 * it expects a parent composite with a GridLayout this will fill one cell
	 * 
	 * @param parent a parent composite with a GridLayout containing one column
	 * @param style the SWT style constant which is assigned to the Text Component together with
	 * 				SWT.BORDER and SWT.SINGLE
	 * @param msg the title of the input 
	 * @param tooltip the tooltip that will be displayed as a hint
	 * @return the created Text component 
	 */
	protected Text createStringPrefInput(Composite parent,int style,String msg,String tooltip){
		Composite comp = new Composite(parent, SWT.None);
		comp.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		comp.setLayout(new GridLayout(2,false));
		Label label = new Label(comp, SWT.None);
		label.setText(msg);
		label.setToolTipText(tooltip);
		final Text tField = new Text(comp, style|SWT.BORDER|SWT.SINGLE);
		tField.setTextDirection(SWT.LEFT_TO_RIGHT);
		tField.setToolTipText(tooltip);
		tField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		return tField;
	}
	/**
	 * this method creates a combo component with a leading label
	 * it expects a parent composite with a GridLayout this will fill one cell
	 * 
	 * @param parent a parent composite with a GridLayout containing one column
	 * @param style the SWT style constant which is assigned to the Text Component together with
	 * 				SWT.BORDER and SWT.SINGLE
	 * @param msg the title of the input 
	 * @param tooltip the tooltip that will be displayed as a hint
	 * @return the created Text component 
	 */
	protected Combo createComboPrefInput(Composite parent,int style,String msg,String tooltip){
		Composite comp = new Composite(parent, SWT.None);
		comp.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		comp.setLayout(new GridLayout(2,false));
		Label label = new Label(comp, SWT.None);
		label.setText(msg);
		label.setToolTipText(tooltip);
		final Combo tField = new Combo(comp, style|SWT.BORDER|SWT.SINGLE);
		tField.setTextDirection(SWT.LEFT_TO_RIGHT);
		tField.setToolTipText(tooltip);
		tField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		return tField;
	}
	
	/**
	 * this method creates a Button component 
	 * it expects a parent composite with a GridLayout this will fill one cell
	 * 
	 * @param parent a parent composite with a GridLayout containing one column
	 * @param style the SWT style constant which is assigned to the Button Component
	 * @param msg the title of the input 
	 * @param tooltip the tooltip that will be displayed as a hint
	 * @return the created Text component 
	 */
	protected Button createBooleanPrefInput(Composite parent,String msg,String tooltip,int style){
		Button prefButton =new Button(parent, style);
		prefButton.setText(msg);
		prefButton.setToolTipText(tooltip);
		
		return prefButton;
	}
	
	public boolean verifyCommand(String[] cmd){
		if(cmd.length == 0 || cmd[0].isEmpty()){
			return false;
		}
		ICMDScanner scn = new ICMDScanner() {
			@Override
			public int scanLine(String line, int current) {
				return 1;
				
			}
		};
		try {
			ConsoleRuntime runtime = new ConsoleRuntime(new ProcessBuilder(cmd), null, null, scn);
			runtime.run();
		
			runtime.join();

			if(runtime.getReturn() != 1){
				MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "No valid command!",
						cmd[0] + " is no executeable command!");
			}else{
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;				
	}
	
}
