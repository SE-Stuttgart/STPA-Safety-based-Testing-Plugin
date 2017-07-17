package xstampp.stpatcgenerator.model.stateflow.chart.parse;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Array {
	
	@XmlElement(name = "P")
	List<P> arraydata = new ArrayList<P>();

	public Array() {
	 
		this.arraydata= new ArrayList<P>();
	}

	public Array(List<P> array) {
		
		this.arraydata = array;
	}

	public List<P> getArray() {
		return arraydata;
	}

	public void setArray(List<P> array) {
		this.arraydata = array;
	}
}
