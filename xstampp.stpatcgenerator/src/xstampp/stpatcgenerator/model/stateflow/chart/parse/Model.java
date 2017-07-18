package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement 
@XmlAccessorType(XmlAccessType.FIELD)
public class Model {
	@XmlAttribute(name = "Name")

private String Name ;
 
public Model(String name,  List<P>  p) {
		 
		Name = name;
		this.p = p;
	}
public Model() {
	this.p=  new ArrayList<P>  ();
	 
}
 
@XmlElement(name = "P" )
private  List  <P>  p =new ArrayList<P>() ;

public String getName() {
	return Name;
}
public void setName(String name) {
	Name = name;
}

public   List<P>  getP() {
	return p;
}
public void setP( List<P>  p) {
	this.p = p;
} 

}
