package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
@XmlAccessorType(XmlAccessType.FIELD)
public class PTransition {

	@XmlAttribute(name = "SSID")
	private String ssid;
   

	 
	

	 
	public String getSsid() {
		return ssid;
	}



	public void setSsid(String ssid) {
		this.ssid = ssid;
	}



	public PTransition(  String value) {
		super();
	 
		this.value = value;
	}
	@XmlValue
    private String value;
	public PTransition() {
		super();
	}



	public String getValue() {
    	           
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}

