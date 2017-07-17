package stpaverifier.ui.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import stpaverifier.controller.ObserverValues;
import stpaverifier.model.AbstractCounterexample;
import stpaverifier.model.ICounterexample;
import stpaverifier.model.NuSMVCounterexample;
import stpaverifier.model.SpinTrail;
import stpaverifier.ui.views.utils.AModelContentView;
import stpaverifier.ui.views.utils.CounterexampleSelection;

public class CounterexampleViewPart extends AModelContentView implements ISelectionChangedListener {
	public static final String ID="stpaVerifier.views.counterexample";	
	private Table tableTrace;
	private List<ICounterexample> exampleList;
	private CounterexampleSelection currentSelection;
	private Tree treeTrace;
	private Text traceTitle;

	@Override
	public void createPartControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new FormLayout());
		
		FormData data = new FormData();
		traceTitle = new Text(comp, SWT.READ_ONLY);
		traceTitle.setText("Counterexample");
		data.right = new FormAttachment(100);
		data.left = new FormAttachment(0);
		data.top = new FormAttachment(0);
		traceTitle.setLayoutData(data);
		

		data = new FormData();
		data.right = new FormAttachment(100);
		data.left = new FormAttachment(0);
		data.top = new FormAttachment(traceTitle);
		data.bottom = new FormAttachment(100);
		/*
		 * START of table creation
		 * Create the table for managing the Ltl and ctl propertys which are provided by the data model
		 * as IPropertys and displayed 
		 */
			
			tableTrace = new Table(comp, SWT.BORDER
					| SWT.FULL_SELECTION | SWT.V_SCROLL|SWT.H_SCROLL | SWT.MULTI);
			tableTrace.setLinesVisible(true);
			tableTrace.setLayoutData(data);
			tableTrace.setVisible(false);
			
		/*
		 * END of table creation
		 * START Spin counterexample tree
		 */
			treeTrace = new Tree(comp, SWT.FULL_SELECTION|SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			treeTrace.setLayoutData(data);
			
			initializeCounterexample();
			
		ISelectionProvider part= (ISelectionProvider) PlatformUI.getWorkbench().getActiveWorkbenchWindow().
												getActivePage().findView(FormularViewPart.ID);
		if(part!= null){
			part.addSelectionChangedListener(this);
			if(part.getSelection() != null && part.getSelection() instanceof CounterexampleSelection){
				this.currentSelection = (CounterexampleSelection)part.getSelection();
				this.exampleList= new ArrayList<ICounterexample>(currentSelection.getExampleList());
				deriveEntrys(exampleList.get(0));
			}
		}
	}


	private void deriveEntrys(ICounterexample trail){
		treeTrace.clearAll(true);
		treeTrace.setVisible(false);
		tableTrace.setVisible(false);
		traceTitle.setText("Counterexample of " + ((AbstractCounterexample) trail).getStringID());
		if(trail instanceof SpinTrail){
			for(TreeItem item : treeTrace.getItems()){
				item.dispose();
			}
			TreeItem item = new TreeItem(treeTrace, SWT.NONE);
	    	item.setText(((SpinTrail) trail).getTrailEnd());
	    	for (String value:((SpinTrail) trail).getStates()){
	    		TreeItem subItem = new TreeItem(item, SWT.NONE);
	    		subItem.setText(value);
	    	}
	    	item = new TreeItem(treeTrace, SWT.NONE);
	    	item.setText("global variables");
	    	for (String value:((SpinTrail) trail).getVariables()) {
	    		TreeItem subItem = new TreeItem(item, SWT.NONE);
	    		subItem.setText(value);
	    	}
	    	item.setExpanded(true);
	    	item = new TreeItem(treeTrace, SWT.NONE);
	    	item.setExpanded(false);
	    	item.setText("local variables");
	    	for (String value:((SpinTrail) trail).getLocalVars()) {
	    		TreeItem subItem = new TreeItem(item, SWT.NONE);
	    		subItem.setText(value);
	    	}
	    	this.treeTrace.setVisible(true);
		}else if(trail instanceof NuSMVCounterexample){
			for(TableColumn col: this.tableTrace.getColumns()){
				col.dispose();
			}
			for(TableItem col: this.tableTrace.getItems()){
				col.dispose();
			}
			List<List<String>> entries = ((NuSMVCounterexample) trail).getEntryList();
			if(!entries.isEmpty()){
				int width = tableTrace.getBounds().width / entries.get(0).size();
				for(int i=0; i < entries.get(0).size();i++){
					TableColumn col = new TableColumn(tableTrace, SWT.None);
					col.setResizable(true);
					col.setWidth(width);
				}
				for(List<String> entryList : entries){
					TableItem item = new TableItem(tableTrace, 0);
					item.setText(entryList.toArray(new String[0]));
				}
			}
			tableTrace.setVisible(true);
		}

	}
	
	public List<ICounterexample> getExampleList() {
		return this.exampleList;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if(event.getSelection() instanceof CounterexampleSelection && !event.getSelection().isEmpty()){
			this.currentSelection = (CounterexampleSelection)event.getSelection();
			this.exampleList= new ArrayList<ICounterexample>(currentSelection.getExampleList());
			deriveEntrys(exampleList.get(0));
		}
	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void update(Observable o, Object arg) {
		ObserverValues value = (ObserverValues) arg;
		switch(value){
		case FORMULARS_CHANGED:
			break;
		case MODEL_CHANGED:
			break;
		case RESET:
			initializeCounterexample();
			break;
		case RESULTS_CHANGED:
			break;
		case USE_SPIN:
			break;
		default:
			break;
		
		}
	}
	
	private void initializeCounterexample(){
		treeTrace.clearAll(true);
		treeTrace.setVisible(false);
		tableTrace.setVisible(false);
		traceTitle.setText("no Counterexample");
	}
}
