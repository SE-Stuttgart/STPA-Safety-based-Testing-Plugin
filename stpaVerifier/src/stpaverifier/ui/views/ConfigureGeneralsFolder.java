package stpaverifier.ui.views;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import stpaverifier.controller.ObserverValues;
import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.controller.preferences.nusmv.NuSMVPreferenceConstants;
import stpaverifier.controller.preferences.spin.SpinPreferenceConstants;
import stpaverifier.ui.views.utils.AbstractConfigurationFolder;
import stpaverifier.ui.views.utils.FoldableView;
import stpaverifier.util.ConsoleRuntime;
import stpaverifier.util.ICMDScanner;
import stpaverifier.util.jobs.CheckPROMELAJob;
import stpaverifier.util.jobs.CheckSMVJob;
/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class ConfigureGeneralsFolder extends AbstractConfigurationFolder implements SpinPreferenceConstants,FoldableView,NuSMVPreferenceConstants{

	private Combo tModelFile;
	private ScrolledComposite scrollSpinComp;
	private ScrolledComposite scrollNuSMVComp;
	/**
	 * This method creates a view in which the user can either extract a Promela(SPIN) model from c code
	 * or directly add a existing 
	 *  
	 * @param parent the Composite on which the view should be created
	 * 
	 *  @author Lukas Balzer
	 */
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(4,false));
		GridData gData = new GridData(SWT.FILL, SWT.LEFT, true, false);
		gData.horizontalSpan = 4;
		Group radioChooser = new Group(parent, SWT.None);
		radioChooser.setLayoutData(gData);
		radioChooser.setLayout(new FillLayout(SWT.VERTICAL));
		//radio group to choose whether a promela or a smv model is used,
		//since this decision affect the whole process the two radio buttons are global variables
			Button checkPromela = new Button(radioChooser, SWT.RADIO);
			checkPromela.setText("Promela Model");
			checkPromela.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					getdModel().setUseSpin(true);
				}
			});
			Button checkSMV = new Button(radioChooser, SWT.RADIO);
			checkSMV.setText("SMV Model");
			checkSMV.addSelectionListener(new SelectionAdapter() {


				@Override
				public void widgetSelected(SelectionEvent e) {
					getdModel().setUseSpin(false);
				}
			});
			checkPromela.setSelection(true);
			if(getdModel() != null){
				checkPromela.setSelection(getdModel().isUseSpin());
				checkSMV.setSelection(!getdModel().isUseSpin());
			}
			
		tModelFile = new Combo(parent, SWT.BORDER |SWT.DROP_DOWN|SWT.READ_ONLY);
		setControl(tModelFile);
		
		if(getdModel() != null){	
//			// author: Ting Luk-He
//			// add new generated smv file into model
//			if(GeneratorSMVFile.getsmvIFile() != null){
//				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Set tModelFile in Class ConfigureGeneralsFolder as "+GeneratorSMVFile.getSmvFile().toString()+" !!!!!!!!!!!!!!!!!!!!!");
//				getdModel().setFile(GeneratorSMVFile.getsmvIFile());
//			}	
			updateCombo();
		}
		tModelFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				File fModel = new File(tModelFile.getText());
				if(fModel != null && fModel.isFile()){
					if(getdModel() != null){
						getdModel().setsFile(tModelFile.getText());
					}
				}
			}
		});
		gData = new GridData(SWT.FILL, SWT.LEFT, true, false);
		gData.horizontalSpan = 4;
		tModelFile.setLayoutData(gData);
		Button bOpenChooser = new Button(parent, SWT.PUSH);
		bOpenChooser.setText("Choose");
		bOpenChooser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dChooser = new FileDialog(Display.getDefault().getActiveShell(),SWT.OPEN);
				if(getdModel().isUseSpin()){
					dChooser.setFilterExtensions(new String[]{"*.pml"});
					dChooser.setFilterNames(new String[]{"Promela Model"});
				}else{
					dChooser.setFilterExtensions(new String[]{"*.smv"});
					dChooser.setFilterNames(new String[]{"SMV Model"});
				}
				String sTemp = dChooser.open();
				if(sTemp == null){
					Display.getDefault().beep();
					return;
				}
				File fModel = new File(sTemp);
				if(fModel != null && fModel.isFile()){
					if(getdModel() != null){
						getdModel().setsFile(sTemp);
					}
				}
			}
		});
		
		Button bCheckModel = new Button(parent, SWT.PUSH);
		bCheckModel.setText("Check Model");
		bCheckModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Job checkJob;
				if(getdModel().isUseSpin()){
					checkJob = new CheckPROMELAJob("Check Model", getdModel());
				}else{
					checkJob = new CheckSMVJob("Check Model",getdModel());
				}
				checkJob.schedule();
			}
		});
		
		gData = new GridData(SWT.FILL, SWT.LEFT, true, true);
		gData.horizontalSpan = 4;
		Composite configPane = new Composite(parent, SWT.NONE);
		configPane.setLayoutData(gData);
		configPane.setLayout(new FormLayout());
		FormData fData = new FormData();
		fData.bottom = new FormAttachment(100);
		fData.top = new FormAttachment(0);
		fData.left = new FormAttachment(0);
		fData.right = new FormAttachment(100);
		generateGeneralConfig(configPane, fData);
		super.createPartControl(parent);
	}

	private void updateCombo(){
		if(tModelFile != null){
			String oldContent = tModelFile.getText();
			tModelFile.removeAll();
			for(IProject proj:ResourcesPlugin.getWorkspace().getRoot().getProjects(IWorkspaceRoot.INCLUDE_HIDDEN)){
				addModelResources(proj);
			}
			boolean preserve = false;
			for(String path:tModelFile.getItems()){
				if(path.equals(oldContent)){
					preserve = true;
				}
			}
	//		if(getdModel() != null && !preserve){
	//			getdModel().setsFile(tModelFile.getText());
	//		}else 
				if(preserve){
				tModelFile.setText(oldContent);
			}
		}
	}
	
	private void addModelResources(IContainer parent){
		try {
			for(IResource resource: parent.members()){
				if(resource instanceof IContainer){
					addModelResources((IContainer) resource);
				}else{
					boolean addResource = getdModel().isUseSpin() && resource.getName().endsWith(".pml");
					addResource = addResource || (!getdModel().isUseSpin() && resource.getName().endsWith("smv"));
					
					if(addResource){
						tModelFile.add(resource.getLocation().toOSString());
					}
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void generateGeneralConfig(Composite parent, FormData fData){
		FormData data = new FormData();
		data.left =new FormAttachment(100);
		data.right = new FormAttachment(0);
		data.top = new FormAttachment(0);
		data.bottom = new FormAttachment(100);
		
		scrollSpinComp = new AdaptedConfiguartionComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrollSpinComp.setLayout(new FormLayout());
		scrollSpinComp.setLayoutData(fData);
		creatGeneralSpinConfig(scrollSpinComp,data);
		
		scrollNuSMVComp = new AdaptedConfiguartionComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrollNuSMVComp.setLayout(new FormLayout());
		scrollNuSMVComp.setLayoutData(fData);
		creatGeneralSMVConfig(scrollNuSMVComp,data);
	}
	
	private void creatGeneralSpinConfig(ScrolledComposite scrollComp, FormData data){
		
		Composite spinConfigComp = new Composite(scrollComp, SWT.NONE);
		spinConfigComp.setLayout(new GridLayout(1, false));
		spinConfigComp.setLayoutData(data);
		String tooltip ="The path to the spin executable";
		String title = "Spin Path: ";
		Text prefField =createPathPrefInput(spinConfigComp, title, tooltip, null);
		prefField.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent e) {
				e.doit = verifyCommand(new String[]{e.text,"-V"});
			}
		});
		registry.registerStringInput(prefField,PREF_SPIN_PATH);
		
		
		tooltip ="The path to the c compiler";
		title = "Path to the C Compiler: ";
		prefField =createPathPrefInput(spinConfigComp, title, tooltip, null);
		if(verifyCommand(new String[]{"gcc","--version"})){
			prefField.setText("(default)");
		}
		prefField.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent e) {
				e.doit = verifyCommand(new String[]{e.text,"--version"});
			}
		});
		registry.registerStringInput(prefField,PREF_C_PATH);
		
		tooltip ="use a minimized DFA encoding for the state space,\n"
				+ "similar to a BDD, assuming a maximum of N bytes in the state-vector\n "
				+ "(this can be combined with -DCOLLAPSE for greater effect in cases when the original state vector is long)";
		title = "Limit for state space: ";
		prefField =createIntegerPrefInput(spinConfigComp, title, tooltip);
		registry.registerIntegerInput(prefField,PREF_SPIN_INT_DMA_N);
		
		tooltip ="set upperbound to the true number of Megabytes that can be allocated;\n"
				+ "usage, e.g.: -DMEMLIM=200 for a maximum of 200 Megabytes \n"
				+ "(meant to be a simple alternative to MEMCNT)";
		title = "Limit for memory allocation: ";
		prefField =createIntegerPrefInput(spinConfigComp, title, tooltip);
		registry.registerIntegerInput(prefField,PREF_SPIN_INT_DMEMLIM);
		
		tooltip ="optimize for the case where no cycle detection is needed\n"
				+ "(faster, uses less memory, disables both -l and -a)";
		title = "Optimize for Safety Properties";
		Button prefButton =createBooleanPrefInput(spinConfigComp, title, tooltip,SWT.CHECK);
		registry.registerBooleanInput(prefButton,PREF_SPIN_BOOL_DSAFETY);
		
		tooltip ="disable validity checks of x[rs] assertions\n"
				+ "(faster, and sometimes useful if the check is too strict,\n"
				+ "e.g. when channels are passed around as process parameters)";
		title = "Disable x[rs] assertions";
		prefButton =createBooleanPrefInput(spinConfigComp, title, tooltip,SWT.CHECK);
		registry.registerBooleanInput(prefButton,PREF_SPIN_BOOL_DXUSAFE);
		
		tooltip ="a state vector compression mode; collapses state vector sizes by up to 80% to 90%";
		title = "Use state space compression";
		prefButton =createBooleanPrefInput(spinConfigComp, title, tooltip,SWT.CHECK);
		registry.registerBooleanInput(prefButton,PREF_SPIN_BOOL_DCOLLAPSE);
		
		tooltip ="allocates memory (in bytes) for state vector usage";
		title = "Memory (in bytes) used for state vector: ";
		prefField =createIntegerPrefInput(spinConfigComp, title, tooltip);
		registry.registerIntegerInput(prefField,PREF_SPIN_INT_DVECTORSZ);
		
		tooltip ="a run time option to set the max search depth to N steps (default N=10000)";
		title = "Maximum search depth: ";
		prefField =createIntegerPrefInput(spinConfigComp, title, tooltip);
		registry.registerIntegerInput(prefField,PREF_SPIN_INT_RUN_MAX);
		
		spinConfigComp.setSize(spinConfigComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrollComp.setContent(spinConfigComp);
		
	}
	
	private void creatGeneralSMVConfig(ScrolledComposite scrollComp, FormData data){
		
		Composite smvConfigComp = new Composite(scrollComp, SWT.NONE);
		smvConfigComp.setLayout(new GridLayout(1, false));

		smvConfigComp.setLayoutData(data);
		String tooltip ="The path to the nusmv executable";
		String title = "NuSMV Path: ";
		Text prefField =createPathPrefInput(smvConfigComp, title, tooltip, null);
		
		
		tooltip ="select the plugin which should be used to create the counterexample";
		title = "Counterexample plugin";
		final Combo prefCombo = createComboPrefInput(smvConfigComp,SWT.READ_ONLY, title, tooltip);
		
		prefField.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent e) {
				e.doit = verifyCommand(new String[]{e.text});
				if(e.doit){
					prefCombo.removeAll();
					for(String plugin : scanCounterexamplePlugins(e.text)){
						prefCombo.add(plugin);
					}
					prefCombo.select(0);
				}
			}
		});

		registry.registerStringInput(prefField,NuSMV_PATH);
		registry.registerComboInput(prefCombo,Pref_NuSMV_NR_COUNTEREX_PLUGIN);
		Group nusmvGroup = new Group(smvConfigComp, SWT.SHADOW_OUT);
		nusmvGroup.setLayout(new GridLayout(1, false));
		tooltip ="If this is selected the NuSMV model checker will use BDD based model checking";
		title = "Use BDD model checking";
		Button prefButton = createBooleanPrefInput(nusmvGroup, title, tooltip,SWT.RADIO);
		registry.registerBooleanInput(prefButton,Pref_NuSMV_USE_BDD);

		tooltip ="If this is selected the NuSMV model checker will use SAT based Bounded Model Checking";
		title = "Use BMC";
		prefButton = createBooleanPrefInput(nusmvGroup, title, tooltip,SWT.RADIO);
		registry.registerBooleanInput(prefButton,Pref_NuSMV_USE_BMC);
		
		smvConfigComp.setSize(smvConfigComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrollComp.setContent(smvConfigComp);
	}
	

	public List<String> scanCounterexamplePlugins(String cmd){
		final List<String> plugins= new ArrayList<>();
		ICMDScanner scn = new ICMDScanner() {
			@Override
			public int scanLine(String line, int current) {
				if(line.contains("\t") && line.split("\t").length == 2){
					plugins.add(line.split("\t")[1]);
				}
				return 1;
				
			}
		};
		try {
			ConsoleRuntime runtime = new ConsoleRuntime(new ProcessBuilder(new String[]{cmd,"-int"}), null, null, scn);
			runtime.setInlineCommand("show_plugins -a");
			runtime.run();
		
			runtime.join();

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plugins;				
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		ObserverValues value = (ObserverValues) arg;
		switch(value){
		case MODEL_CHANGED:
			if(tModelFile != null){
				updateCombo();
				if(getdModel().getFile() == null){
					tModelFile.setText(new String());
				}else{
					tModelFile.setText(getdModel().getFile().getLocation().toOSString());
				}
			}
			break;
		case USE_SPIN:
			if(scrollNuSMVComp != null && scrollSpinComp != null && tModelFile != null){
				scrollSpinComp.setVisible(getdModel().isUseSpin());
				scrollNuSMVComp.setVisible(!getdModel().isUseSpin());
				updateCombo();
			}
			break;
		default:
			break;
			
		}
	}

	@Override
	public void setdModel(STPAVerifierController dModel) {
		super.setdModel(dModel);
		updateCombo();
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}
}
