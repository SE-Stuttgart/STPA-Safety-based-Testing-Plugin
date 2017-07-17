package xstampp.stpatcgenerator.model.stateflow.chart;
import java.util.List;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class instance {
	 @XmlElement(name="ParentID")
	    private String parentID;

	    @XmlElementWrapper(name="PList")
	    @XmlElement(name="P")
	    private List<String> pList;
}
