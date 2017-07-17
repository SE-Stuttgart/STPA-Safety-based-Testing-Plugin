package stpaverifier.ui.views;

import java.util.List;
import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import stpaverifier.Activator;
import stpaverifier.controller.IVerificationResult;
import stpaverifier.controller.ObserverValues;
import stpaverifier.ui.views.utils.AModelContentView;
import stpaverifier.ui.views.utils.IDrawablePart;
import stpaverifier.util.ColorManager;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class ResultsDiagViewPart extends AModelContentView implements PaintListener, IDrawablePart, MouseWheelListener, MouseListener, MouseMoveListener{

	public static final String ID ="stpaVerifier.views.resultDiagramm";
	public static final String DEPTH_COLOR ="depth color";
	public static final String TIME_COLOR ="TIME_COLOR";
	public static final String MEMORY_COLOR ="MEMORY_COLOR";
	public static final String TRANS_COLOR ="TRANS_COLOR";
	private static final Image LEFT_ARROW = Activator.getImageDescriptor("icons/views/left_arrow.png").createImage();
	private static final Image LEFT_ARROW_PRESSED = Activator.getImageDescriptor("icons/views/left_arrow_pressed.png").createImage();
	private static final Image RIGHT_ARROW = Activator.getImageDescriptor("icons/views/right_arrow.png").createImage();
	private static final Image RIGHT_ARROW_PRESSED = Activator.getImageDescriptor("icons/views/right_arrow_pressed.png").createImage();
	private static final Image PLUS = Activator.getImageDescriptor("icons/views/plus.png").createImage();
	private static final Image PLUS_PRESSED = Activator.getImageDescriptor("icons/views/plus_pressed.png").createImage();
	private static final Image MINUS = Activator.getImageDescriptor("icons/views/minus.png").createImage();
	private static final Image MINUS_PRESSED = Activator.getImageDescriptor("icons/views/minus_pressed.png").createImage();
	private static final int diagramBottomOffset = 5;
	/**
	 * this field stores the margins of this view with :<ul>
	 * <li> x = left
	 * <li> y = top
	 * <li> width = right
	 * <li> height = bottom
	 * </ul>
	 */
	private static final Rectangle margin = new Rectangle(2, 5,2,2);
	private Rectangle[] resultSpaces;
	private String[] resultMessages;
	private Rectangle leftArrowHandle;
	private Rectangle rightArrowHandle;
	private Rectangle plusHandle;
	private Rectangle minusHandle;
	private Canvas pieCanvas;
	private Point interval = null;
	private int total = 0;
	private boolean leftPressed;
	private boolean rightPressed;
	private boolean plusPressed;
	private boolean minusPressed;
	private Rectangle sleighBounds;
	private boolean sleighPressed;
	private int lastSleightX;
	private Rectangle scrollBar;
	private Rectangle diagramSpace;


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
		this.plusPressed = false;
		this.minusPressed = false;
		this.leftPressed = false;
		this.rightPressed = false;
		sleighPressed = false;
		ColorManager.registerColor(DEPTH_COLOR, new RGB(20, 100, 100));
		ColorManager.registerColor(TIME_COLOR,new RGB(200, 50, 100));
		ColorManager.registerColor(MEMORY_COLOR,new RGB(200, 150, 100));
		ColorManager.registerColor(TRANS_COLOR,new RGB(10, 150, 100));
		pieCanvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		pieCanvas.addMouseWheelListener(this);
		pieCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		pieCanvas.addPaintListener(this);
		pieCanvas.addMouseListener(this);
		pieCanvas.addMouseMoveListener(this);
		pieCanvas.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseHover(MouseEvent e) {
				boolean noHit = true;
				if(diagramSpace != null  && resultSpaces != null && resultMessages != null&& diagramSpace.contains(e.x, e.y)){
					for(int i=0;i < resultSpaces.length;i++){
						if(resultSpaces[i].contains(e.x, e.y)){
							pieCanvas.setToolTipText(resultMessages[i]);
							noHit = false;
							return;
						}
					}
				}else if(plusHandle.contains(e.x, e.y)){
					pieCanvas.setToolTipText("Zoom in the diagram");
					noHit = false;
				}else if(minusHandle.contains(e.x, e.y)){
					pieCanvas.setToolTipText("zoom out of the diagram");
					noHit = false;
				}
				if(noHit && pieCanvas.getToolTipText() != null){
					pieCanvas.setToolTipText(null);
				}
			}
			
		});
		if(getdModel() != null){
			total = getdModel().getResults().size();
			interval = new Point(0, total-1);
			pieCanvas.redraw();
		}
		
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
					total = getdModel().getResults().size();
					interval = new Point(0, total-1);
					pieCanvas.redraw();
				}
			});
		}
	}


	@Override
	public void paintControl(PaintEvent e) {
		paint(e.gc, pieCanvas.getBounds(), true);
	}

	/* (non-Javadoc)
	 * @see stpaverifier.ui.views.IDrawablePart#paint(org.eclipse.swt.graphics.GC, org.eclipse.swt.graphics.Rectangle)
	 */
	@Override
	public void paint(GC gc,Rectangle rect, boolean paintControls){
		diagramSpace = new Rectangle(margin.x	, margin.y, 0, 0);
		if(getdModel() != null){
			
			gc.fillRectangle(rect);
			int diagLabelOffset = gc.getFontMetrics().getHeight();
			Rectangle bottomBarBounds = new Rectangle(0, 0, 0, 0); 
			bottomBarBounds.height = 20;
			int yOff = rect.height * 5/100;
			if(paintControls){
				diagramSpace.y += bottomBarBounds.height;
			}
			diagramSpace.height = rect.height - (diagramSpace.y + margin.height + diagLabelOffset);
			
			//DEFINITION of the y axis descriptors 
				Point maxExtend = gc.stringExtent("100%");
				gc.drawString("100%", margin.x, diagramSpace.y - (maxExtend.y/2));
				for(int i=1; i<10;i++){
					gc.drawString(" "+(10-i)+"0%", margin.x, diagramSpace.y +(diagramSpace.height*i/10)- (maxExtend.y/2));
				}
				gc.drawString("  0%", margin.x, diagramSpace.y + diagramSpace.height- (maxExtend.y/2));
				diagramSpace.x = 2*margin.x + maxExtend.x;
			
			//DEFINITION of the legend
			int legendWidth = drawLegend(gc, rect, yOff);
				
			//
			diagramSpace.width = rect.width - (legendWidth + diagramSpace.x);
			bottomBarBounds.y = margin.y;
			bottomBarBounds.x = diagramSpace.x;
			//the bottom bar must expand along the x axis of the diagram plus must contain the left and right button
			bottomBarBounds.width = diagramSpace.width - 5;
			if(paintControls){
				drawBottomBar(gc, bottomBarBounds);
			}
			gc.setLineJoin(SWT.JOIN_ROUND);
			//drawing of the x- and y-axis 
			gc.drawLine(diagramSpace.x , diagramSpace.y+diagramSpace.height, diagramSpace.x + diagramSpace.width, diagramSpace.y+diagramSpace.height);
			gc.drawLine(diagramSpace.x, diagramSpace.y, diagramSpace.x, diagramSpace.y+diagramSpace.height);
			
			if(getdModel().getResults() != null && !getdModel().getResults().isEmpty()){
				drawData(gc);
			}
			
			
		}
			
	}
	private void drawBottomBar(GC gc,Rectangle rect){
		 Color foreground= gc.getForeground();
		 Color background= gc.getBackground();
		 int buttonWidth = LEFT_ARROW.getBounds().width;
		 int buttonHeight = LEFT_ARROW.getBounds().height;
		leftArrowHandle = new Rectangle(rect.x, rect.y, buttonWidth, buttonHeight);
		if(leftPressed){
			gc.drawImage(LEFT_ARROW_PRESSED, leftArrowHandle.x, leftArrowHandle.y);
		}else{
			gc.drawImage(LEFT_ARROW, leftArrowHandle.x, leftArrowHandle.y);
		}
		rightArrowHandle = new Rectangle(rect.x + rect.width - 3* buttonWidth -2, rect.y, buttonWidth, buttonHeight);
		if(rightPressed){
			gc.drawImage(RIGHT_ARROW_PRESSED, rightArrowHandle.x, rightArrowHandle.y);
		}else{
			gc.drawImage(RIGHT_ARROW, rightArrowHandle.x, rightArrowHandle.y);
		}
		minusHandle = new Rectangle(rect.x + rect.width - 2*buttonWidth, rect.y, buttonWidth, buttonHeight);
		if(minusPressed){
			gc.drawImage(MINUS_PRESSED, minusHandle.x, minusHandle.y);
		}else{
			gc.drawImage(MINUS, minusHandle.x, minusHandle.y);
		}
		plusHandle = new Rectangle(rect.x + rect.width -  buttonWidth, rect.y, buttonWidth, buttonHeight);
		if(plusPressed){
			gc.drawImage(PLUS_PRESSED, plusHandle.x, plusHandle.y);
		}else{
			gc.drawImage(PLUS, plusHandle.x, plusHandle.y);
		}
		
		scrollBar = new Rectangle(0, rect.y, 0, 16);
		scrollBar.x = leftArrowHandle.x + leftArrowHandle.width;
		scrollBar.width = rightArrowHandle.x - scrollBar.x;
		sleighBounds = new Rectangle(0, rect.y +1, 0, 14);
		float min = 0;
		float max = scrollBar.width ;
		if(interval != null){
			min = (((float)interval.x) / (total-1) )* (float)scrollBar.width;
			max = (((float)interval.y) / (total-1)) ;
			max *= (float)scrollBar.width;
		}
		sleighBounds.x = scrollBar.x + (int) min;
		sleighBounds.width = (int) (max - min);
		gc.setForeground(ColorManager.COLOR_grey);
		gc.drawRectangle(scrollBar);
		if(sleighPressed){
			gc.setForeground(ColorManager.COLOR_WHITE);
			gc.setBackground(ColorManager.COLOR_grey);
		}else{
			gc.setBackground(ColorManager.COLOR_WHITE);
		}
		gc.fillGradientRectangle(sleighBounds.x,sleighBounds.y,sleighBounds.width,sleighBounds.height,true);
		gc.drawRectangle(sleighBounds);
		gc.setForeground(foreground);
		gc.setBackground(background);
		
	}
	private int drawLegend(GC gc, Rectangle rect, int yOff){
		Point maxExtend = gc.stringExtent("transitions ");
		int rect_width = 6;
		int legendWidth = 2 +rect_width  + maxExtend.x;
		int leg_xstart = rect.width - legendWidth;
		gc.setBackground(ColorManager.getColor(DEPTH_COLOR));
		gc.fillRectangle(1 + leg_xstart, yOff+ maxExtend.y/2 - rect_width/2, rect_width, rect_width);
		gc.drawString("depth", 2 +rect_width + leg_xstart, yOff,true);

		gc.setBackground(ColorManager.getColor(TIME_COLOR));
		gc.fillRectangle(1 + leg_xstart, yOff+ maxExtend.y * 2 - rect_width/2, rect_width, rect_width);
		gc.drawString("time", 2 +rect_width + leg_xstart, yOff + maxExtend.y *3/2,true);

		gc.setBackground(ColorManager.getColor(TRANS_COLOR));
		gc.fillRectangle(1 + leg_xstart, yOff+ maxExtend.y * 7/2 - rect_width/2, rect_width, rect_width);
		gc.drawString("transitions", 2 +rect_width + leg_xstart, yOff + maxExtend.y *3,true);
		
		gc.setBackground(ColorManager.getColor(MEMORY_COLOR));
		gc.fillRectangle(1 + leg_xstart, yOff+ maxExtend.y * 10/2 - rect_width/2, rect_width, rect_width);
		gc.drawString("memory", 2 +rect_width + leg_xstart, yOff + maxExtend.y *9/2,true);
		return legendWidth;
	}
	
	
	private void drawData(GC gc){
		if(interval != null && total != 0){
	
			int intervalLength= 1+interval.y - interval.x;
	 		int dataWidth = (int) Math.floor(diagramSpace.width/intervalLength);
			gc.setAntialias(SWT.ON);
			double maxTime = 0;
			double[] timeDoubles = new double[intervalLength];
			double maxDepth = 0;
			double[] depthDoubles = new double[intervalLength];
			double maxMem = 0;
			double[] memoryDoubles = new double[intervalLength];
			double maxTrans = 0;
			double[] transDoubles = new double[intervalLength];
			String[] literals = new String[intervalLength];
			int maxLiteralExtend=0;
			resultMessages = new String[intervalLength];
			List<IVerificationResult> results = getdModel().getResults();
			/*
			 * this loop fills the double arrays with the data from the result entries by parsing the string values
			 * stored in the results to double values, in case the parsing fails the maximum value is inserted
			 */
			for(int index = 0; index < intervalLength; index++){
				IVerificationResult result = results.get(interval.x + index);
				literals[index] = result.getSsrLiteral();
				resultMessages[index] = result.getSsrLiteral() + ":\n";
				maxLiteralExtend = Math.max(maxLiteralExtend, gc.stringExtent(literals[index]).x);
				try{
					resultMessages[index] = resultMessages[index] + "time: "+result.getTime() + "s\n";
					double time = Double.valueOf(result.getTime());
					if(time > maxTime){
						maxTime = time;
					}
					timeDoubles[index] = time;
				}catch(Exception exc){
					timeDoubles[index] = maxTime;
				}
				try{
					resultMessages[index] = resultMessages[index] + "depth: "+result.getDepth() + "\n";
					double depth = Double.valueOf(result.getDepth());
					if(depth > maxDepth){
						maxDepth = depth;
					}
					depthDoubles[index] = depth;
				}catch(Exception exc){
					depthDoubles[index] = maxDepth;
				}
				try{
					resultMessages[index] = resultMessages[index] + "memory: "+result.getUsedMemory() + "\n";
					double memory = Double.valueOf(result.getUsedMemory());
					if(Double.compare(memory, maxMem)>0){
						maxMem = memory;
					}
					memoryDoubles[index] = memory;
				}catch(Exception exc){
					memoryDoubles[index] = maxMem;
				}
				try{
					resultMessages[index] = resultMessages[index] + "transitions: "+result.getTransitions() + "\n";
					double transitions = Double.valueOf(result.getTransitions());
					if(transitions > maxTrans){
						maxTrans = transitions;
					}
					transDoubles[index] = transitions;
				}catch(Exception exc){
					transDoubles[index] = maxTrans;
				}
			}
	
			int[] timePoints = new int[2*intervalLength];
			int[] depthPoints = new int[2*intervalLength];
			int[] memoryPoints = new int[2*intervalLength];
			int[] transPoints = new int[2*intervalLength];
			int diagLevel = diagramSpace.y +diagramSpace.height;
			resultSpaces = new Rectangle[intervalLength];
			for (int i = 1; i <2*intervalLength; i+=2) {
				int relativeI = ((i-1)/2);
				int x =diagramSpace.x + dataWidth/2 + (dataWidth * (relativeI));
				resultSpaces[relativeI]= new Rectangle(x - dataWidth/2, diagramSpace.y, dataWidth, diagramSpace.height);
				gc.setForeground(ColorManager.COLOR_BLACK);
				gc.drawLine(x , diagLevel, x, diagLevel +diagramBottomOffset);
				gc.setLineStyle(SWT.LINE_DOT);
				gc.setAlpha(100);
				gc.drawLine(x , diagramSpace.y, x, diagLevel);
				gc.setAlpha(255);
				gc.setLineStyle(SWT.LINE_SOLID);
				if(maxLiteralExtend < dataWidth){
					gc.drawString(literals[relativeI], x-maxLiteralExtend/2, diagLevel+diagramBottomOffset, true);
				}
				//calculate the actual y values of the points by dividing them with the individual max[..]
				timePoints[i-1] = x;
				timePoints[i] = diagLevel - (int) ((timeDoubles[relativeI]/maxTime) * diagramSpace.height);
	
				depthPoints[i-1] = x;
				depthPoints[i] = diagLevel - (int) ((depthDoubles[relativeI]/maxDepth) * diagramSpace.height);
				
				memoryPoints[i-1] = x;
				memoryPoints[i] = diagLevel - (int) ((memoryDoubles[relativeI]/maxMem) * diagramSpace.height);
				
				transPoints[i-1] = x;
				transPoints[i] = diagLevel - (int) ((transDoubles[relativeI]/maxTrans) * diagramSpace.height);
			}
			
			gc.setInterpolation(SWT.HIGH);
			gc.setLineWidth(2);
			gc.setForeground(ColorManager.getColor(MEMORY_COLOR));
			gc.drawPolyline(memoryPoints);
			gc.setForeground(ColorManager.getColor(TRANS_COLOR));
			gc.drawPolyline(transPoints);
			gc.setForeground(ColorManager.getColor(TIME_COLOR));
			gc.drawPolyline(timePoints);
			gc.setForeground(ColorManager.getColor(DEPTH_COLOR));
			gc.drawPolyline(depthPoints);
		}
	
	}
	
	

	@Override
	public void mouseScrolled(MouseEvent e) {
		zoom(e.count);
	}


	/**
	 * 
	 * @param count if smaller 0 than the diagram range is increased if greater than 0 its increased, 0 simply redraws the canvas
	 */
	private void zoom(int count) {
		if(interval != null && total != 0){
			//if the count is <0 the the zoom 
			if(count > 0 ){
				int oneFourth= (interval.y - interval.x)/4;
				interval.x = Math.min(interval.y-1, interval.x + oneFourth);
				interval.y = Math.max(interval.x+1, interval.y - oneFourth);
			}else if(count < 0 ){
				int oneFourth = (interval.y - interval.x)/2;
				interval.x = Math.max(0, interval.x - oneFourth);
				interval.y = Math.min(total-1, interval.y + oneFourth);
			}
			pieCanvas.redraw();
		}
	}


	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseDown(MouseEvent e) {
		boolean redraw = false;
		if(leftArrowHandle.contains(e.x,e.y)){
			leftPressed = true;
			redraw = true;
		}else if(rightArrowHandle.contains(e.x,e.y)){
			rightPressed = true;
			redraw = true;
		}else if(plusHandle.contains(e.x,e.y)){
			plusPressed = true;
			redraw = true;
		}else if(minusHandle.contains(e.x,e.y)){
			minusPressed = true;
			redraw = true;
		}else if(sleighBounds.contains(e.x,e.y)){
			sleighPressed = true;
			lastSleightX = e.x;
			redraw = true;
		}
		if(redraw){
			pieCanvas.redraw();
		}
	}


	@Override
	public void mouseUp(MouseEvent e) {
		this.plusPressed = false;
		this.minusPressed = false;
		this.leftPressed = false;
		this.rightPressed = false;
		sleighPressed = false;
		if(interval != null && total != 0){
			if(interval.x > 0 && leftArrowHandle.contains(e.x,e.y)){
				interval.x--;
				interval.y--;
			}else if(interval.y < total -1 && rightArrowHandle.contains(e.x,e.y)){
				interval.x++;
				interval.y++;
			}else if(plusHandle.contains(e.x,e.y)){
				zoom(1);
			}else if(minusHandle.contains(e.x,e.y)){
				zoom(-1);
			}
		}
		pieCanvas.redraw();
	}


	@Override
	public void mouseMove(MouseEvent e) {
		if(scrollBar != null && total > 0){
			boolean redraw = false;
			int threshold = scrollBar.width / total;
			if(sleighPressed && e.x - lastSleightX < -threshold && interval.x > 0 ){
				interval.x--;
				interval.y--;
				lastSleightX = e.x;
				redraw = true;
			}else if(sleighPressed && e.x - lastSleightX >threshold&& interval.y < total -1){
				interval.x++;
				interval.y++;
				lastSleightX = e.x;
				redraw = true;
			}
			if(redraw){
				pieCanvas.redraw();
			}
		}
	}
	
	
}
