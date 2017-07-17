package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
 
@XmlAccessorType(XmlAccessType.FIELD)
public class Machine {
	@XmlAttribute(name = "id")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@XmlElement(name = "P" )
	private List<P> p = new ArrayList<P> ();

	public List<P> getP() {
		return p;
	}

	public void setP(List<P> p) {
		this.p = p;
	}
	@XmlElement(name = "Children" )
	 private ChildrenChart childrenchart;

	public ChildrenChart getChildrenchart() {
		return childrenchart;
	}

	public void setChildrenchart(ChildrenChart childrenchart) {
		this.childrenchart = childrenchart;
	}

	public Machine(String id, List<P> p, ChildrenChart childrenchart) {
		super();
		this.id = id;
		this.p = p;
		this.childrenchart = childrenchart;
	}

	public Machine() {
		super();
		this.p=new ArrayList<P> ();
		this.childrenchart= new ChildrenChart();
	}
	 
	
	

}

