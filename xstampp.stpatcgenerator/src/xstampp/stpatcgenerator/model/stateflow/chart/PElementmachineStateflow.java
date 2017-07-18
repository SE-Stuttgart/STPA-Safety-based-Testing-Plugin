package xstampp.stpatcgenerator.model.stateflow.chart;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "P")
public class PElementmachineStateflow {
	 List Pelements;
	 @XmlElement( name = "P" )
	  
	     public void setCountries( List p )
	  
	     {
	  
	         this.Pelements = p;
	  
	     }

	private String created;
	private String isLibrary;
 
	private String sfversion;
	
	public PElementmachineStateflow() {
		 
		// TODO Auto-generated constructor stub
	}
	public PElementmachineStateflow(String created, String isLibrary, String sfversion) {
		this. created = created;
		this.isLibrary =isLibrary;
		this. sfversion= sfversion;

		// TODO Auto-generated constructor stub
	}
}
