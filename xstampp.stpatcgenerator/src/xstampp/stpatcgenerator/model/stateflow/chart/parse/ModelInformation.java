package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.naming.spi.ObjectFactory;
import javax.rmi.CORBA.Tie;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlSchema;;
@XmlRootElement(name="ModelInformation")
@XmlAccessorType(XmlAccessType.FIELD)
 public class ModelInformation {
	@XmlElement(name="Stateflow")	
	private Stateflow stateflow  ;
	
	
	public Stateflow getStateflow() {
		return stateflow;
	}
	
	public void setStateflow(Stateflow stateflow) {
		this.stateflow = stateflow;
	}
	
	public Model getModel() {
		return model;
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	@XmlElement(name="Model")
	private Model model  ;

	public ModelInformation(Stateflow stateflow, Model model) {
	 
		this.stateflow = stateflow;
		this.model = model;
	}
	public ModelInformation()
	{
		this.stateflow= new Stateflow();
		this.model=new Model();
	}
	
	 
	
	
}
