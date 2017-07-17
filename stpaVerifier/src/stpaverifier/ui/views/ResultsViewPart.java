package stpaverifier.ui.views;

import java.util.Observable;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import stpaverifier.model.VerificationResult;
import stpaverifier.ui.views.utils.AModelContentView;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class ResultsViewPart extends AModelContentView{

	public static final String ID ="stpaVerifier.views.result";
	private TableViewer tResultViewer;
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
		parent.setLayout(new GridLayout(7,false));
		
		
		/*
		 * START of table creation
		 * Create the table for managing the Ltl and ctl propertys which are provided by the data model
		 * as IPropertys and displayed 
		 */
			parent.setLayout(new FillLayout());
			Composite cResultsComposite = new Composite(parent, SWT.None);
			TableColumnLayout tLayout = new TableColumnLayout();
			cResultsComposite.setLayout(tLayout);
			
			tResultViewer = new TableViewer(cResultsComposite, SWT.BORDER
					| SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.MULTI);
			setControl(tResultViewer.getControl());
			tResultViewer.setContentProvider(new ArrayContentProvider());
			tResultViewer.getTable().setHeaderVisible(true);
			tResultViewer.getTable().setLinesVisible(true);

			// activate the tooltip support for the viewer
			ColumnViewerToolTipSupport.enableFor(tResultViewer, ToolTip.NO_RECREATE); 
			//this column is used to display a checkbox which tells the verifier
			//whether to check this property in a run or to ignore it 
			TableViewerColumn colSSR_ID = new TableViewerColumn(tResultViewer, SWT.CENTER);
			colSSR_ID.getColumn().setText("SSR");
			tLayout.setColumnData(colSSR_ID.getColumn(), new ColumnWeightData(1, 30, false));
			colSSR_ID.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return ((VerificationResult) element).getSsrLiteral();
				}
				@Override
				public String getToolTipText(Object element) {
					if(((VerificationResult) element).getProvider() != null){
						return ((VerificationResult) element).getProvider().getSafetyRule();
					}
					return null;
				}
			});
			
			//this column displays the literal ids stored for the input property
			TableViewerColumn colDepth = new TableViewerColumn(tResultViewer, SWT.CENTER); 
			colDepth.getColumn().setText("#Depth");
			tLayout.setColumnData(colDepth.getColumn(), new ColumnWeightData(1, 30, false));
			colDepth.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return ((VerificationResult) element).getDepth();
				}
			});
			
			
			//Create a column for displaying the formula value of the property
			TableViewerColumn colStoredStates = new TableViewerColumn(tResultViewer, SWT.LEFT); 
			colStoredStates.getColumn().setText("#StoredStates");
			tLayout.setColumnData(colStoredStates.getColumn(), new ColumnWeightData(3, 30, false));
			colStoredStates.setLabelProvider(new  ColumnLabelProvider() {
				
				@Override
				public String getText(Object element) {
					return ((VerificationResult) element).getStoredStates();
				}
			});
			
			
			//Create a column for displaying the status of the property input
			TableViewerColumn colTransitions = new TableViewerColumn(tResultViewer, SWT.LEFT); 
			colTransitions.getColumn().setText("#Transitions");
			tLayout.setColumnData(colTransitions.getColumn(), new ColumnWeightData(3, 30, false));
			colTransitions.setLabelProvider(new  ColumnLabelProvider() {
				
				@Override
				public String getText(Object element) {
					return ((VerificationResult) element).getTransitions();
				}
			});
			
			//Create a column for displaying the status of the property input
			TableViewerColumn colTime = new TableViewerColumn(tResultViewer, SWT.LEFT); 
			colTime.getColumn().setText("#Time");
			tLayout.setColumnData(colTime.getColumn(), new ColumnWeightData(3, 30, false));
			colTime.setLabelProvider(new  ColumnLabelProvider() {
				
				@Override
				public String getText(Object element) {
					return ((VerificationResult) element).getTime();
				}
			});
			
			//Create a column for displaying the status of the property input
			TableViewerColumn colMemory = new TableViewerColumn(tResultViewer, SWT.LEFT); 
			colMemory.getColumn().setText("#Memory usage (MB)");
			tLayout.setColumnData(colMemory.getColumn(), new ColumnWeightData(3, 30, false));
			colMemory.setLabelProvider(new  ColumnLabelProvider() {
				
				@Override
				public String getText(Object element) {
					return ((VerificationResult) element).getUsedMemory();
				}
			});
			
			//Create a column for displaying the status of the property input
			TableViewerColumn colResult = new TableViewerColumn(tResultViewer, SWT.LEFT); 
			colResult.getColumn().setText("Result");
			tLayout.setColumnData(colResult.getColumn(), new ColumnWeightData(3, 30, false));
			colResult.setLabelProvider(new  ColumnLabelProvider() {
				
				@Override
				public String getText(Object element) {
					return ((VerificationResult) element).getResult();
				}
			});
		/*
		 * END of table creation
		 */
		
		
	}

	
	@Override
	public void setFocus() {
		if(getdModel() != null){
			tResultViewer.setInput(getdModel().getResults());
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {

		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				if(getdModel() != null){
					tResultViewer.setInput(getdModel().getResults());
				}
			}
		});
	}

}
