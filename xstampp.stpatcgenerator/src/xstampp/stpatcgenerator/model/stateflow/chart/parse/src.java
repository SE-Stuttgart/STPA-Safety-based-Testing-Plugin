package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class src {
	@XmlElement(name="P")
	private List<PTransition> source = new ArrayList<PTransition> ()  ;

	public src() {
		super();
		List<PTransition> source = new ArrayList<PTransition> ()  ;
	}

	public List<PTransition> getSource() {
		return source;
	}

	public void setSource(List<PTransition> source) {
		this.source = source;
	}

	
	@Override
	public String toString() {
		return "src [source=" + source + "]";
	}
}
