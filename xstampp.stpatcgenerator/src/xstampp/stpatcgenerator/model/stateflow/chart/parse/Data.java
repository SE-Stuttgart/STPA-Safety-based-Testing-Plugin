package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
 

@XmlAccessorType(XmlAccessType.FIELD)
public class Data {
	@XmlElement(name = "P")
	private List<P> pdata = new ArrayList<P>();
	@XmlAttribute(name = "SSID")
	private String sSID;
	@XmlAttribute(name = "name")
	private String name;
	@XmlElement(name = "props")
	private  props  props = new  props ();
	
	
	

	public Data(String sSID, String name, List<P> pdata, props props) {
		 
		this.sSID = sSID;
		this.name = name;
		this.pdata = pdata;
		this.props = props;
	}
	public props getProps() {
		return props;
	}
	public void setProps(props props) {
		this.props = props;
	}
	public Data( props props) {
		 
		this.props = props;
	}
	public String getsSID() {
		return sSID;
	}
	public List<P> getPdata() {
		return pdata;
	}
	public void setPdata(List<P> pdata) {
		this.pdata = pdata;
	}
	 
	public void setsSID(String sSID) {
		this.sSID = sSID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<P> getP() {
		return pdata;
	}
	public void setP(List<P> p) {
		this.pdata = p;
	}
	public Data() {
		 
        this.pdata=new ArrayList<P>();
        this.props =new props();
	}
	 
	@Override
	public String toString() {
		return "Data [sSID=" + sSID + ", name=" + name + "]";
	}

}
