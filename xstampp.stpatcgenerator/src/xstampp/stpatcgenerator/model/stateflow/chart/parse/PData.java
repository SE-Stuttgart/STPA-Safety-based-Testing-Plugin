package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class PData {
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@XmlAttribute(name = "Name")
	@XmlID
	private String name;
	@XmlValue
	private String value;
	
	public PData() {
	     value=null;
		}

		 

		public PData(String name, String value) {
			super();
			this.name = name;
			this.value=value;
			 
		}
	
}