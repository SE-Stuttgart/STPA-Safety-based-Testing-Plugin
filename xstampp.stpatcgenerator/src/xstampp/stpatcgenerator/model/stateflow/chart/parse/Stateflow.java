package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.util.ArrayList;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
@XmlRootElement 
@XmlAccessorType(XmlAccessType.FIELD)
 public class Stateflow {
	@XmlElement(name="machine")	
private Machine machine =new Machine() ;

public Machine  getMachine() {
	return machine;
}

public void setMachine(Machine machine) {
	this.machine = machine;
}

public Stateflow(Machine machine) {
 
	this.machine = machine;
}

public Stateflow() {
	 this.machine= new Machine();
}

 
 
}
