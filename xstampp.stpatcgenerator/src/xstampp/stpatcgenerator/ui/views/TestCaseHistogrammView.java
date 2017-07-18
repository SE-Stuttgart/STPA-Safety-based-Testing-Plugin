package xstampp.stpatcgenerator.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import xstampp.astpa.model.extendedData.RefinedSafetyRule;
import xstampp.stpatcgenerator.controller.STPATCGModelController;
import xstampp.stpatcgenerator.model.astpa.ParseSTPAMain;

/**
 * This class defines the view of test case histogram.
 * @author Ting Luk-He
 *
 */
public class TestCaseHistogrammView extends ViewPart{
	
	public final static String ID = "xstampp.stpatcgenerator.view.tcHistogramm";

	Composite parentFrame;
	private JPanel panel;
	private Frame frame;
	DefaultTableModel tcModel = STPATCGModelController.getTestCaseTableModel();
	private JFreeChart chart;
	private ParseSTPAMain parseSTPA = STPATCGModelController.getConfWizard().getParseSTPA();
	List<RefinedSafetyRule> STPAConstraints = (List<RefinedSafetyRule>) (List<?>) parseSTPA
			.getdataModel().getAllRefinedRules(null);
	
	@Override
	public void createPartControl(Composite parent) {
		parentFrame = new Composite(parent, SWT.EMBEDDED);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		frame = SWT_AWT.new_Frame(parentFrame);
		frame.setLayout(new BorderLayout());
		
		int[] traceResult = calSSRTraceResult(tcModel);
		int minX = getMinTraceNumber(traceResult);
		int maxX = getMaxTraceNumber(traceResult);
		IntervalXYDataset dataset = createDataset(traceResult);
        chart = createChart(dataset, minX, maxX);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		
		JScrollPane scroll = new JScrollPane(chartPanel);
	    panel.add(scroll);
	    panel.setVisible(true);
		frame.add(panel);
		STPATCGModelController.setTcHistrogrammView(this);
	}

	private JFreeChart createChart(IntervalXYDataset dataset, int minX, int maxX) {
		final JFreeChart chart = ChartFactory.createXYBarChart(
	            "Number of Test Cases for each STPA Software Safety Requirement",
	            "STPA Software Safety Requirements", 
	            false,
	            "Number of Test Cases", 
	            dataset,
	            PlotOrientation.VERTICAL,
	            false,
	            true,
	            false
	        );
	        XYPlot plot = (XYPlot) chart.getPlot();
	        plot.setDomainCrosshairVisible(true);
	        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
	        domain.setRange(minX, maxX);
	        domain.setTickUnit(new NumberTickUnit(1));
	        final IntervalMarker target = new IntervalMarker(400.0, 700.0);
	        target.setLabel("Target Range");
	        target.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));
	        target.setLabelAnchor(RectangleAnchor.LEFT);
	        target.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
	        target.setPaint(new Color(222, 222, 255, 128));
	        plot.addRangeMarker(target, Layer.BACKGROUND);
	        return chart;  
	}

	private IntervalXYDataset createDataset(int[] data) {
		final XYSeries series = new XYSeries("Test Case Number");
		for(int i = 0; i < data.length; i++){
			series.add(i+1, data[i]);
		}
        final XYSeriesCollection dataset = new XYSeriesCollection(series);
        return dataset;
	}

	private int[] calSSRTraceResult(DefaultTableModel tcModel){
		int ssrNumber = STPAConstraints.size();
//		int ssrNumber = STPATCGModelController.getGtcConfigEditor().getSTPAConstraints().size();
		int[] ssrTraceResult = new int[ssrNumber];
		for(int i = 0; i < tcModel.getRowCount(); i++){	
			// get traced ssr id for each test case
			String tmp = (String) tcModel.getValueAt(i, 3);
			String[] tmpSplit = tmp.split(",");
			for(String s: tmpSplit){
				if(!s.equals(" ") && !s.equals("")){
					Integer ssrId = Integer.parseInt(s.trim());				
					// count the test case number for relevant ssr 
					ssrTraceResult[ssrId-1]++;
				}				
			}
		}
		return ssrTraceResult;
	}
	
	private int getMinTraceNumber (int[] traceResult) {
		int min = 1;
		for(int i = 0; i < traceResult.length; i++){
			if(traceResult[i] > 0){
				min = i + 1;
				break;
			}
		}
		return min;
	}
	
	private int getMaxTraceNumber (int[] traceResult) {
		int max = 1;
		for(int i = traceResult.length - 1; i > -1 ; i--){
			if(traceResult[i] > 0){
				max = i + 1;
				break;
			}
		}
		return max;
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	public JFreeChart getChart() {
		return chart;
	}

	public void setChart(JFreeChart chart) {
		this.chart = chart;
	}

}
