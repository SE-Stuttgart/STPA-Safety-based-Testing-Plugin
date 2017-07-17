package stpaverifier.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import stpaverifier.ui.views.utils.FoldableView;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class CompositeWithUDContent extends Composite {

	private static final int HEADER_HEIGHT = 30;
	private final Composite cMiddleContent;
	private final HeaderComoposite bTopContentTrigger;
	private final Composite cTopContent;
	private final HeaderComoposite bMiddleContentTrigger;
	private final HeaderComoposite bBottomContentTrigger;
	private final Composite cBottomContent;
	private FoldableView middleView;
	private FoldableView topView;
	private FoldableView bottomView;

	public CompositeWithUDContent(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		/*
		 * first define all the trigger buttons, each trigger will receive a FormData Object 
		 * which will change according to the selection 
		 */
		 bTopContentTrigger = new HeaderComoposite(this);
			bTopContentTrigger.setText("General Configuration");
			bTopContentTrigger.addSelectionListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent e) {
					openTop();
					bTopContentTrigger.setSelected(true);
					bMiddleContentTrigger.setSelected(false);
					bBottomContentTrigger.setSelected(false);
				}
			});
		   
		

		 bBottomContentTrigger = new HeaderComoposite(this);
			
		    bBottomContentTrigger.setText("Extract Model");
		    bBottomContentTrigger.addSelectionListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent e) {
					openBottom();
					bTopContentTrigger.setSelected(false);
					bMiddleContentTrigger.setSelected(false);
					bBottomContentTrigger.setSelected(true);
				}
			});
		 
			
		 bMiddleContentTrigger = new HeaderComoposite(this);
			
		    bMiddleContentTrigger.setText("Advanced Configuration");
		    bMiddleContentTrigger.addSelectionListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent e) {
					openMiddle();
					bTopContentTrigger.setSelected(false);
					bMiddleContentTrigger.setSelected(true);
					bBottomContentTrigger.setSelected(false);
				}
			});
		 /* 
		  * in the second step the content composites are defined to be in between the triggers
		  *  and  receive a FillLayout for optimal container properties
		  */
		 cBottomContent = new Composite(this, SWT.None);
			cBottomContent.setLayout(new FillLayout());
			cBottomContent.setVisible(true);
		 cMiddleContent = new Composite(this, SWT.None);
			cMiddleContent.setVisible(true);
			cMiddleContent.setLayout(new FillLayout());
		    
		 cTopContent = new Composite(this, SWT.None);
			cTopContent.setLayout(new FillLayout());
			cTopContent.setVisible(true);
			initializeContent();
		openTop();
		bTopContentTrigger.setSelected(true);
	}
	
	private void initializeContent(){

		cBottomContent.setLayoutData(getFormData(bBottomContentTrigger.getControl(), null));
	    cMiddleContent.setLayoutData(getFormData(bMiddleContentTrigger.getControl(), bBottomContentTrigger.getControl()));
		cTopContent.setLayoutData(getFormData(bTopContentTrigger.getControl(), bMiddleContentTrigger.getControl()));
	}
	private void openTop(){

		//hide/show the top content
			bTopContentTrigger.setLayoutData(getFitData(bTopContentTrigger.isVisible(),new FormAttachment(0),null));
			cTopContent.setVisible(true);

		//hide/show the bottom content
			bBottomContentTrigger.setLayoutData(getFitData(bBottomContentTrigger.isVisible(),null, new FormAttachment(100)));
			cBottomContent.setVisible(false);
		
		//hide/show the middle content
			bMiddleContentTrigger.setLayoutData(getFitData(bMiddleContentTrigger.isVisible(),null, new FormAttachment(bBottomContentTrigger.getControl())));
			cMiddleContent.setVisible(false);
		layout(true);
		cTopContent.layout(true);
		if(topView != null){
			topView.activate();
		}
	}
	private void openMiddle(){
		//hide/show the top content
		bTopContentTrigger.setLayoutData(getFitData(bTopContentTrigger.isVisible(),new FormAttachment(0),null));
		cTopContent.setVisible(false);
		
		//hide/show the middle content
		bMiddleContentTrigger.setLayoutData(getFitData(bMiddleContentTrigger.isVisible(),new FormAttachment(bTopContentTrigger.getControl()),null));
		cMiddleContent.setVisible(true);

		//hide/show the bottom content
		bBottomContentTrigger.setLayoutData(getFitData(bBottomContentTrigger.isVisible(),null, new FormAttachment(100)));
		cBottomContent.setVisible(false);
		layout(true);
		if(middleView != null){
			middleView.activate();
		}
	}
	private void openBottom(){
		//hide/show the top content
		bTopContentTrigger.setLayoutData(getFitData(bTopContentTrigger.isVisible(),new FormAttachment(0),null));
		cTopContent.setVisible(false);
		
		//hide/show the middle content
		bMiddleContentTrigger.setLayoutData(getFitData(bMiddleContentTrigger.isVisible(),new FormAttachment(bTopContentTrigger.getControl()),null));
		cMiddleContent.setVisible(false);

		//hide/show the bottom content
		bBottomContentTrigger.setLayoutData(getFitData(bBottomContentTrigger.isVisible(),new FormAttachment(bMiddleContentTrigger.getControl()),null));
		cBottomContent.setVisible(true);
		layout(true);
		cBottomContent.layout(true);
		if(bottomView != null){
			bottomView.activate();
		}
	}

	public void setcMiddleContent(FoldableView middleContent) {
		middleContent.createPartControl(cMiddleContent);
		middleView = middleContent;
		this.bMiddleContentTrigger.setVisible(true);
		openTop();
	}

	public void setTopTitle(String title){
		bTopContentTrigger.setTitle(title);
	}

	public void setcTopContent(FoldableView topContent) {
		topContent.createPartControl(cTopContent);
		topView = topContent;
		bTopContentTrigger.setVisible(true);
		openTop();
	}


	public void setcBottomContent(FoldableView bottomContent) {
		bottomContent.createPartControl(cBottomContent);
		bottomView = bottomContent;
		bBottomContentTrigger.setVisible(true);
		openTop();
	}

	/**
	 * 
	 * @param top the control which limits this layout on the top
	 * @param bottom the control which limits this layout on the bottom
	 * @return
	 */
	private FormData getFormData(Control top,Control bottom){
		FormData fData=  new FormData();
		fData.top = new FormAttachment(top);
		fData.right = new FormAttachment(100);
		fData.left = new FormAttachment(0);
		if(bottom == null){
			fData.bottom = new FormAttachment(100);
		}else{
			fData.bottom = new FormAttachment(bottom);
		}
		return fData;
	}

	private FormData getFitData(boolean b, FormAttachment top, FormAttachment bottom){
		FormData fData=  new FormData();
		if(top != null){
			fData.top =top;
		}if(bottom != null){
			fData.bottom=bottom;
		}
		fData.right = new FormAttachment(100);
		if(b){	
			fData.height = HEADER_HEIGHT;
		}else{
			fData.height = 0;
		}
		fData.left = new FormAttachment(0);
		return fData;
	}
}
