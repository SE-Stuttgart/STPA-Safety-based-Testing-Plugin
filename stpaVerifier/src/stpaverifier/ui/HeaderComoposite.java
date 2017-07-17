package stpaverifier.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * this object draws a canvas which shows a half-rounded rectangle with a triangle 
 * indicating the selection and a defined text line
 * 
 * @author Lukas Balzer
 *
 */
public class HeaderComoposite implements PaintListener {
	
	private Button cHeader;
	private String sLine;
	private boolean selected;
	private String headTitle;
	private boolean show;

	public HeaderComoposite(Composite parent) {
		this.selected = false;
		headTitle = "";
//		this.cHeader = new Canvas(parent, SWT.None);
//		this.cHeader.addPaintListener(this);
		this.cHeader = new Button(parent, SWT.PUSH);
		this.cHeader.setVisible(true);
		this.show = false;
	}
	public void setVisible(boolean visible){
		this.cHeader.setVisible(visible);
		this.show = visible;
	}
	
	public boolean isVisible(){
		return show;
	}
	public void setTitle(String title){
		this.headTitle = title;
		this.cHeader.setText(headTitle + " " + sLine);
	}
	public void setSelected(boolean select){
		this.selected= select;
		this.cHeader.redraw();
	}
	public void setText(String text){
		this.sLine = text;
		cHeader.setText(text);
	}

	@Override
	public void paintControl(PaintEvent e) {
		Rectangle rec = ((Control)e.getSource()).getBounds();
		e.gc.setAntialias(SWT.ON);
		int radius = rec.height;
		e.gc.drawRoundRectangle(0, 0, rec.width,2 * radius, 2*radius, 2*radius);
		e.gc.setBackground(new Color(null, 0, 30, 200));
		int[] trianglePoints = new int[6];
		//x1
		trianglePoints[0] =radius;
		//y1
		trianglePoints[1] = (int) ((1f/3f) * rec.height);
		if(selected){
			//x2
			trianglePoints[2] =radius + (int) ((1f/3f) * rec.height);
			//y2
			trianglePoints[3] = trianglePoints[1];
			//x3
			trianglePoints[4] =radius + (int) ((1f/6f) * rec.height);
			//y3
			trianglePoints[5] = (int) ((2f/3f) *rec.height);
		}else{
			//x2
			trianglePoints[2] =radius;
			//y2
			trianglePoints[3] = (int) ((2f/3f) * rec.height);
			//x3
			trianglePoints[4] =radius + (int) ((1f/3f) * rec.height);
			//y3
			trianglePoints[5] = (int) ((1f/2f) *rec.height);
		}
		e.gc.fillPolygon(trianglePoints);
		e.gc.drawString(sLine, 2*radius, trianglePoints[1],true);
		
	}

	public void addSelectionListener(MouseListener mouseListener) {
		this.cHeader.addMouseListener(mouseListener);
		
	}

	public void setLayoutData(FormData fData) {
		this.cHeader.setLayoutData(fData);
	}
	
	public Control getControl(){
		return cHeader;
	}
}
