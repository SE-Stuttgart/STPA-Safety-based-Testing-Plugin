package stpaverifier.ui.views;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import stpaverifier.controller.IVerificationResult;
import stpaverifier.controller.ObserverValues;
import stpaverifier.model.VerificationResult;
import stpaverifier.ui.views.utils.AModelContentView;
import stpaverifier.ui.views.utils.IDrawablePart;
import stpaverifier.util.ColorManager;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class ResultsPieViewPart extends AModelContentView implements PaintListener, IDrawablePart{

	public static final String ID ="stpaVerifier.views.resultPie";
	
	private Canvas pieCanvas;
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
		parent.setLayout(new GridLayout());
		
		pieCanvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		pieCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		pieCanvas.addPaintListener(this);
		pieCanvas.redraw();
		
		
	}

	
	@Override
	public void setFocus() {
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		ObserverValues value = (ObserverValues) arg1;
		if(value == ObserverValues.RESULTS_CHANGED){
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					pieCanvas.redraw();
				}
			});
		}
	}


	@Override
	public void paintControl(PaintEvent e) {
		paint(e.gc,pieCanvas.getBounds(), true);
	}

	@Override
	public void paint(GC gc,Rectangle rect, boolean paintControls){

		
		if(getdModel() != null){
			int legendwidth = 200;
			int legendHeight = 80;
			float successNr=0;
			float failedNr=0;
			float errorNr=0;
			float total = 1;
			/*
			 * Calculation of the current result values
			 */
			if(getdModel().getResults() != null && !getdModel().getResults().isEmpty()){
				total = getdModel().getResults().size();
				for(IVerificationResult result: getdModel().getResults()){
					switch(result.getResult()){
					case IVerificationResult.SATISFIED: successNr++; break;
					case IVerificationResult.INTERRUPTED: errorNr++; break;
					case IVerificationResult.FAILS: failedNr++; break;
					}
				}
			}
			gc.fillRectangle(rect);
			int maxSize = Math.min(rect.width,rect.height - legendHeight)-1;
			Point legendPosition = new Point(0,maxSize); 
			
			if(rect.width - legendwidth > maxSize){
				maxSize =  Math.min(rect.height,rect.width - legendwidth)-1;
				legendPosition = new Point(maxSize,0); 
			}
			
			float d = maxSize;
			int textHeight = gc.getFontMetrics().getHeight();
			int labelOffset = textHeight + 5;
			
			gc.setAntialias(SWT.ON);
			
			gc.setBackground(ColorManager.COLOR_GREEN);
			
			int start = (int) ((int) successNr/total * 360);
			int errorStart = (int) ((int) errorNr/total * 360);
			int failedStart = (int) ((int) failedNr/total * 360);
			if(start > 0){
				start = 360 - (errorStart + failedStart);
			}else if(errorStart > 0){
				errorStart = 360 - (start + failedStart);
			}else if(failedStart > 0){
				failedStart = 360 - (errorStart + start);
			}
			gc.fillArc(0, 0, (int)d, (int)d, 0,start);
			gc.setBackground(ColorManager.COLOR_RED);
			gc.fillArc(0, 0, (int)d, (int)d, start,failedStart);
			gc.setBackground(ColorManager.COLOR_YELLOW);
			gc.fillArc(0, 0, (int)d, (int)d,start + failedStart,errorStart);
			gc.setBackground(ColorManager.COLOR_BLACK);
			gc.drawOval(0, 0, (int)d, (int)d);
				

			int column = (int) (legendPosition.x + 10);
			gc.setBackground(ColorManager.COLOR_GREEN);
			gc.fillRectangle(column,legendPosition.y + 1 * labelOffset, textHeight, textHeight);
			gc.setBackground(ColorManager.COLOR_RED);
			gc.fillRectangle(column,legendPosition.y + 2 * labelOffset, textHeight, textHeight);
			gc.setBackground(ColorManager.COLOR_YELLOW);
			gc.fillRectangle(column,legendPosition.y + 3 * labelOffset, textHeight, textHeight);
			
			column = column + labelOffset;
			gc.setBackground(ColorManager.COLOR_BLACK);
			gc.drawString((int)successNr + "("+ 100 * successNr/total+"%) - " +VerificationResult.SATISFIED, column,legendPosition.y + 1 * labelOffset, true);
			gc.drawString((int)failedNr + "("+ 100 * failedNr/total+"%) - " + VerificationResult.FAILS, column,legendPosition.y + 2 * labelOffset, true);
			gc.drawString((int)errorNr + "("+ 100 * errorNr/total+"%) - " + VerificationResult.INTERRUPTED, column,legendPosition.y + 3 * labelOffset, true);
			
		}
			
	
	}
}
