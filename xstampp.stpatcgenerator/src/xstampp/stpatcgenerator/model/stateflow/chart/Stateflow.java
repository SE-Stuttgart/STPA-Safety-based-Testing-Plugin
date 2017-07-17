package xstampp.stpatcgenerator.model.stateflow.chart;
import java.util.List;   
import javax.xml.bind.annotation.*;
 
@XmlRootElement(name="Stateflow")
public class Stateflow {
	
	@XmlElementWrapper(name = "machine")
	private List machineStateflow;
	@XmlAttribute (name="id")
	private String machineId;
    @XmlElement (name="P")
    private List<PElementmachineStateflow> plist;
	 
	public void setMachineStateflow(List machineStateflow) {
		this.machineStateflow = machineStateflow;
	}
	 
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	 
	public void setPlist(List<PElementmachineStateflow> plist) {
		this.plist = plist;
	}
     
	
	
}
