package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
@XmlAccessorType(XmlAccessType.FIELD)
public class props {
	
	
	@XmlElement(name = "array")
	private  Array  arr = new  Array ();
	@XmlElement(name = "type")
	private  type   datatype = new  type ();
	@XmlElement(name = "P")
	private  List<P>  pdata = new  ArrayList<P> ();
	
	public Array getArray() {
		return arr;
	}
	public void setArray(Array array) {
		this.arr = array;
	}
	public List<P> getP() {
		return pdata;
	}
	public void setP(List<P> p) {
		this.pdata = p;
	}
	public props(type typedata, Array array, List<P> p) {
		
		this.datatype = typedata;
		this.arr = array;
		this.pdata = p;
	}
	public props(type typedata, Array arraydata) {
		
		this.datatype = typedata;
		this.arr = arraydata;
		this.pdata=new ArrayList<P>();  
		 
	}
	public props() {
		 this.datatype= new type ();
		 this.arr=new Array();
			this.pdata=new ArrayList<P>();  

	}
	public type getTypedata() {
		return datatype;
	}
	public void setTypedata(type typedata) {
		this.datatype = typedata;
	}
	 
}
