package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)

public class type {
	
	@XmlElement(name = "P")
	private List<P> pdata = new ArrayList<P>();

	public type() {
		this.pdata=new ArrayList<P>()  ;
	}

	public type(List<P> pdata) {
	
		this.pdata = pdata;
	}

	public List<P> getPdata() {
		return pdata;
	}

	public void setPdata(List<P> pdata) {
		this.pdata = pdata;
	}
	
	
}
