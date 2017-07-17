package stpaverifier.ui;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * this class defines a composite for navigating through a defined amount of sub views by adding
 * a navigation icon for each subview which will on click be displayed in the {@link StepComposite#stepContent)
 *  
 * @author Lukas Balzer
 *
 */
public class StepComposite{

	private Composite mainComposite;
	private Composite stepContent;
	private Composite[] stepComposites;
	public int activeContentNr;

	/**
	 * this constructor creates a new Composite inside the parent which contains as much
	 * step buttons as there are labels given those buttons decide the content on the stepContent
	 * Composite defined in {@link StepComposite#getNewContentFor(int)}
	 * 
	 * 
	 * @param parent should be an empty composite with a FillLayout or a GridLayout since there
	 * 				 is no way to add a LayoutData to the mainComposite
	 * @param style the style of the main Composite which contains the step contents and the navigation
	 * @param steps the string which define the amount of steps for which this composite is created
	 */
	public StepComposite(Composite parent, int style,String[] steps) {
		mainComposite = new Composite(parent, style);
		mainComposite.setLayout(new GridLayout(steps.length, true));
		stepComposites = new Composite[steps.length];
		for(int i = 0;i <steps.length;i++){
			Button stepFrame = new Button(mainComposite, SWT.PUSH);
			stepFrame.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
			stepFrame.addSelectionListener(new ContentChooseListener(i));
			stepFrame.setText(steps[i]);
		}
		stepContent= new Composite(mainComposite, SWT.NONE);
		GridData gData =  new GridData(SWT.FILL, SWT.FILL, true, true);
		gData.horizontalSpan = steps.length;
		stepContent.setLayoutData(gData);
		stepContent.setLayout(new FormLayout());
	}

	/**
	 * 
	 * @param stepNr the number for which an empty composite should be returned
	 * 
	 * @return an empty composite which can be used as a new parent or null if the stepNr is not in the range defined
	 */
	public Composite getNewContentFor(int stepNr){
		if(stepComposites.length >stepNr && stepNr >= 0){
			stepComposites[stepNr]  = new Composite(stepContent, SWT.NONE);
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(0);
			data.bottom = new FormAttachment(100);
			stepComposites[stepNr].setLayoutData(data);
			stepComposites[stepNr].setVisible(false);
			return stepComposites[stepNr];
		}else{
			return null;
		}
	}
	
	private void activate(int nr){
		if(stepComposites != null && nr < stepComposites.length && nr >=0){
			if(activeContentNr <stepComposites.length && activeContentNr > 0){
				stepComposites[activeContentNr].setVisible(false);
			}
			activeContentNr = nr;
			stepComposites[activeContentNr].setVisible(true);
		}
	}
	private class ContentChooseListener extends SelectionAdapter{
		private int stepNumber;

		public ContentChooseListener(int stepNr) {
			Assert.isNotNull(stepComposites);
			Assert.isLegal(stepComposites.length > stepNr && stepNr >= 0,
							"The given number is not in the range of stepComposites: " + stepNr);
			stepNumber = stepNr;
		}
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			activate(stepNumber);
		}
		
	}
	

}
