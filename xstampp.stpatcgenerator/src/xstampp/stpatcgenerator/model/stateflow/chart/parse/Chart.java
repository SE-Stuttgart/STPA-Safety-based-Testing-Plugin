package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
 
 
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Chart")
public class Chart {
	@XmlAttribute(name = "id")
	private String id;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	@XmlElement(name = "Children" )
	private Children root = new Children();
	
	
	public Children getRoot() {
		return root;
	}
		
	public void setRoot(Children root) {
		this.root = root;
	}
 	@XmlElement(name = "P")
	private List<P> p = new ArrayList <P> () ;
	
	public List<P> getP() {
		return p;
	}
	
	public void setP(List<P> p) {
		this.p = p;
	}

	public Chart() {
		 this.p=  new ArrayList<P>();
		 this.root= new Children ();
	}

	public Chart(Children root, List<P> p) {
		 
		this.root = root;
		this.p = p;
	}
	
	
	
}
