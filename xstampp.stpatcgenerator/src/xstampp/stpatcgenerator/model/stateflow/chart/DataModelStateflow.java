package xstampp.stpatcgenerator.model.stateflow.chart;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
 

@XmlRootElement (name="Model")
public class DataModelStateflow {

	  @XmlAttribute (name="Version")
	  private String version;
	 
	  @XmlElement(name="Stateflow")
	   private Stateflow stateflowModel;

	public Stateflow getStateflow() {
		return stateflowModel;
	}

	public void setStateflow( Stateflow  stateflow) {
		stateflowModel = stateflow;
	}
 
	 
}
