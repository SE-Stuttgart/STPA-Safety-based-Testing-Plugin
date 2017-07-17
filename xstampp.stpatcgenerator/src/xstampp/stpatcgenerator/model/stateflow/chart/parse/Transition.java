package xstampp.stpatcgenerator.model.stateflow.chart.parse;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Transition {
	@XmlAttribute(name = "SSID")	
private String SSID;
public Transition() {
	this.p= new ArrayList <P>()	 ;
	this.source = new src ();
	this.destination=new dst ();
	}
public Transition(String sSID, List<P> p, src source, dst destination) {
	 
		SSID = sSID;
		this.p = p;
		this.source = source;
		this.destination = destination;
	}

public String getSSID() {
	return SSID;
}
public void setSSID(String sSID) {
	SSID = sSID;
}

public List<P> getP() {
	return p;
}

public void setP(List<P> p) {
	this.p = p;
}

public src getSrc() {
	return source;
}

public void setSrc(src source) {
	 
	  this.source = source;
          if (source==null)
          {
              setIsDefault(true);
          }
}

public dst getDst() {
	return destination;
}

public void setDst(dst destination) {
	this.destination = destination;
}
 
@XmlElement(name="P")
private List<P> p =new ArrayList<P>();
 
@XmlElement(name="src")
private src source = new src ()  ;
 
@XmlElement(name="dst")
private dst destination = new dst  () ;
@Override
public String toString() {
	return "Transition [SSID=" + SSID ;
}

private boolean isDefault= false;

    public src getSource() {
        return source;
    }

    public void setSource(src source) {
        this.source = source;
    }

    public dst getDestination() {
        return destination;
    }

    public void setDestination(dst destination) {
        this.destination = destination;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }


}
