package xstampp.stpatcgenerator.model.stateflow.chart;
import java.util.List;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
@XmlAccessorType(XmlAccessType.FIELD)
public class machine {
	 @XmlElement(name="ParentID")
	    private String parentID;
	 private List<ChildrenMachine> childern;
}
