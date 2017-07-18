package xstampp.stpatcgenerator.model.properties;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class PropertyExportModel {

	private String formula;
	private String id;
	private String status;
	

	@XmlElementWrapper(name="counterexampleLines")
	@XmlElement(name="line")
	private List<String> counterexample;

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return this.formula;
	}

	/**
	 * @param formula the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @param list the counterexample to set
	 */
	public void setCounterexample(List<String> list) {
		this.counterexample = list;
	}

}
