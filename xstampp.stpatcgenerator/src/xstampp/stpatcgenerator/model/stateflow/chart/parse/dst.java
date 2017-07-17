package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class dst {
	public dst() {
		super();
		List<PTransition> destination = new ArrayList<PTransition> () ;
	}

	public dst(List<PTransition> destination) {
		super();
		this.destination = destination;
	}

	@XmlElement(name="P")
	private List<PTransition> destination = new ArrayList<PTransition> () ;

	public List<PTransition> getDestination() {
		return destination;
	}

	public void setDestination(List<PTransition> destination) {
		this.destination = destination;
	}
}

