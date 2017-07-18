package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;

@XmlAccessorType(XmlAccessType.FIELD)

public class P {

	 
	
	@XmlAttribute(name = "Name")
	@XmlID
	private String name;
	@XmlValue
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	 
	public P() {
     value=null;
	}

	 

	public P(String name, String value) {
		super();
		this.name = name;
		this.value=value;
		 
	}

}
