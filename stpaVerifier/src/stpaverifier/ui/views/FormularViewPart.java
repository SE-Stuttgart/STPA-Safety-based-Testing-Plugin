package stpaverifier.ui.views;

import java.util.ArrayList;
import java.util.Observable;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import stpaverifier.controller.IProperty;
import stpaverifier.controller.ObserverValues;
import stpaverifier.controller.model.STPAVerifierController;
import stpaverifier.model.properties.LTLFormular;
import stpaverifier.ui.views.utils.AModelContentView;
import stpaverifier.ui.views.utils.CounterexampleSelection;
import stpaverifier.ui.views.utils.PropertyEditingSupport;
import stpaverifier.ui.views.utils.PropertyHoldsProvider;
import stpaverifier.util.ColorManager;
import xstampp.ui.common.ProjectManager;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class 
FormularViewPart extends AModelContentView implements ISelectionProvider{

	public static final String ID = "stpaVerifier.view.formulars";
	private TableViewer tFormularViewer;
	private PropertyEditingSupport editPropertySupport;
	private ArrayList<ISelectionChangedListener> selectionListeners;
	
	private class PropertyMouseListener extends EditingSupport{
		
		public PropertyMouseListener(ColumnViewer viewer) {
			super(viewer);
		}

		@Override
		protected void setValue(Object element, Object value) {
			// this method is not used because the editing functionality isn't wished
			
		}
		
		@Override
		protected Object getValue(Object element) {
			// this method is not used because the editing functionality isn't wished
			return null;
		}
		
		@Override
		protected CellEditor getCellEditor(Object element) {
			// this method is not used because the editing functionality isn't wished
			return null;
		}
		
		@Override
		protected boolean canEdit(Object element) {
			int state = ((IProperty)element).getState();
			if((state & IProperty.STATE_VALIDATING) != 0){
				getdModel().setPropertyState(((IProperty)element).getUUID(), IProperty.STATE_CANCELED);
			}
			if((state & IProperty.STATE_COUNTEREXAMPLE) != 0){
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(CounterexampleViewPart.ID);
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;
		}
	}
	
	/**
	 * This method creates a view that contains a Table in which the ltl/ctl formulars
	 * can either be imported from an STPA Analysis done in A-STPA or manually added and than further managed
	 *  
	 * @param parent the Composite on which the view should be created
	 * 
	 *  @author Lukas Balzer
	 */
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		
		
		/*
		 * START of table creation
		 * Create the table for managing the Ltl and ctl propertys which are provided by the data model
		 * as IPropertys and displayed 
		 */
			Composite tFormularComposite = new Composite(parent, SWT.None);
			TableColumnLayout tLayout = new TableColumnLayout();
			tFormularComposite.setLayout(tLayout);
			
			tFormularViewer = new TableViewer(tFormularComposite, SWT.BORDER
					| SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.MULTI |SWT.CHECK);
			tFormularViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					for(int i=0; selectionListeners!= null && i<selectionListeners.size();i++){
						selectionListeners.get(i).selectionChanged(new SelectionChangedEvent(FormularViewPart.this, getSelection()));
					}
				}
			});
			setControl(tFormularViewer.getControl());
			tFormularViewer.setContentProvider(new ArrayContentProvider());
			tFormularViewer.getTable().setHeaderVisible(true);
			tFormularViewer.getTable().setLinesVisible(true);
			tFormularViewer.getTable().addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent event) {
					if((event.detail & SWT.CHECK) != 0){
						getdModel().setUseProperty(((IProperty)event.item.getData()).getUUID(),((TableItem)event.item).getChecked());
					}
				}
			});
			if(!System.getProperty("os.name").toLowerCase().contains("mac")){
				//this column is used to display a checkbox which tells the verifier
				//whether to check this property in a run or to ignore it 
				TableViewerColumn colFormularUse = new TableViewerColumn(tFormularViewer, SWT.CENTER);
				tLayout.setColumnData(colFormularUse.getColumn(), new ColumnWeightData(1, 30, false));
				colFormularUse.getColumn().setWidth(30);
				colFormularUse.setLabelProvider(new ColumnLabelProvider(){
					@Override
					public String getText(Object element) {
						return null;
					}
					@Override
					public String getToolTipText(Object element) {
						return "select to use this propertie";
					}
				});
			}
			
			//this column displays the literal ids stored for the input property
			TableViewerColumn colFormularIds = new TableViewerColumn(tFormularViewer, SWT.CENTER); 
			
			// activate the tooltip support for the viewer
			ColumnViewerToolTipSupport.enableFor(tFormularViewer, ToolTip.NO_RECREATE); 
			colFormularIds.getColumn().setText("IDs");
			tLayout.setColumnData(colFormularIds.getColumn(), new ColumnWeightData(1, 30, false));
			colFormularIds.getColumn().setWidth(20);
			colFormularIds.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return ((IProperty) element).getsID();
				}
				
				@Override
				public String getToolTipText(Object element) {
					if(element instanceof LTLFormular && ((LTLFormular) element).getLtlProvider() != null){
						return "RSR1."+((LTLFormular) element).getLtlProvider().getNumber()+": \n"
								+((LTLFormular) element).getLtlProvider().getSafetyRule();
					}
					return super.getToolTipText(element);
				}
			});
			
			
			//Create a column for displaying the formula value of the property
			TableViewerColumn colFormulars = new TableViewerColumn(tFormularViewer, SWT.LEFT); 
			colFormulars.getColumn().setText("LTL/CTL Formular");
			tLayout.setColumnData(colFormulars.getColumn(), new ColumnWeightData(3, 30, false));
			colFormularIds.getColumn().setWidth(50);
			colFormulars.setLabelProvider(new  ColumnLabelProvider() {
				@Override
				public Color getForeground(Object element) {
					String formular = ((IProperty) element).getsFormular(getdModel().isUseSpin(), false);
					if(formular == null || formular.isEmpty()){
						return ColorManager.COLOR_grey;
					}
					return super.getForeground(element);
				}
				@Override
				public String getText(Object element) {
					String formular = ((IProperty) element).getsFormular(getdModel().isUseSpin(), false);
					if(formular == null || formular.isEmpty() && element instanceof LTLFormular){
						return "LTL Property";
					}else if(formular == null || formular.isEmpty()){
						return "CTL Property";
					}
					return formular;
				}
			});
			editPropertySupport =new PropertyEditingSupport(tFormularViewer);
			editPropertySupport.setController(getdModel());
			colFormulars.setEditingSupport(editPropertySupport);
			
			
			//Create a column for displaying the status of the property input
			TableViewerColumn colPropStatus = new TableViewerColumn(tFormularViewer, SWT.LEFT); 
			colPropStatus.getColumn().setText("Status");
			tLayout.setColumnData(colPropStatus.getColumn(), new ColumnWeightData(3, 30, false));
			colFormularIds.getColumn().setWidth(50);
			colPropStatus.setLabelProvider(new PropertyHoldsProvider());
			colPropStatus.setEditingSupport(new PropertyMouseListener(tFormularViewer));
		/*
		 * END of table creation
		 */
		getSite().setSelectionProvider(this);
	}
	@Override
	public void setFocus() {
		// nothing happens

	}
	
	@Override
	public void setdModel(STPAVerifierController dModel) {
		super.setdModel(dModel);
		if(this.editPropertySupport != null){
			this.editPropertySupport.setController(dModel);
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		ObserverValues value = (ObserverValues) arg1;
		switch(value){
			case USE_SPIN:
			case FORMULARS_CHANGED:
				if(tFormularViewer.getControl().isDisposed()){
					arg0.deleteObserver(this);
				}else if(tFormularViewer != null){
					Display.getDefault().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							try{
								Control control = tFormularViewer.getTable();
								if (control != null && !control.isDisposed()) {
									
									tFormularViewer.setInput(getdModel().getAllProperties());
									for (TableItem item : tFormularViewer.getTable().getItems()) {
										item.setChecked(((IProperty)item.getData()).useProperty());
									}
									
								}
							}catch(SWTError e){
								ProjectManager.getLOGGER().error(e.getMessage());
							}
						}
					});		
				}
				break;
		default:
			break;
		}
		
	}
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if(this.selectionListeners == null){
			this.selectionListeners = new ArrayList<ISelectionChangedListener>();
		}
		this.selectionListeners.add(listener);
		
	}
	@Override
	public ISelection getSelection() {
		ISelection selection = tFormularViewer.getSelection();
		if(selection instanceof StructuredSelection){
			CounterexampleSelection ceSelection= new CounterexampleSelection();
			for (Object property : ((StructuredSelection) selection).toList()) {
					ceSelection.addExample(((IProperty) property).getCounterexample());
			}
			if(!ceSelection.isEmpty()){
				return ceSelection;
			}
		}
		return selection;
	}
	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		if(this.selectionListeners != null){
			this.selectionListeners.remove(listener);
		}
	}
	@Override
	public void setSelection(ISelection selection) {
		tFormularViewer.setSelection(selection);
	}

}
