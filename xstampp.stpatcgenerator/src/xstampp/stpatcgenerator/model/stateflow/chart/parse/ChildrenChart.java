package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChildrenChart {
	@XmlElement(name = "chart")	
	private Chart chart =new Chart () ;
		
	public Chart getChart() {
		return chart;
	}

	public void setChart(Chart chart) {
		this.chart = chart;
	}

	public ChildrenChart() {
		  this.chart =new Chart();
	}

	public ChildrenChart(Chart chart) {
		 
		this.chart = chart;
	}

	
}
