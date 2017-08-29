package xstampp.stpatcgenerator.model.astpa;

import xstampp.astpa.model.DataModelController;

public class ParseSTPAMain {

	STPADataModelController STPAdataModel = new STPADataModelController();
	DataModelController dataModel = new DataModelController();
	// String STPAfilepath="./ACCSimulator1.hazx";
	String STPAfilepath = "";
	final String STPASchema = "hazschema.xsd";
	// Main method to get things started
	// public static void main(String args[]) {
	// Create an instance of the test application

	// }
	public DataModelController getdataModel() {
		return dataModel;
	}

	public STPADataModelController getSTPAdataModel() {
		return STPAdataModel;
	}
	public void setSTPAdataModel(STPADataModelController STPAdatamodel) {
		this.STPAdataModel=STPAdatamodel;
	}

	public ParseSTPAMain(String path) {
		this.STPAfilepath = path;

	}

	public ParseSTPAMain(String path, DataModelController datamodel) {
		this.STPAfilepath = path;

	}

	public ParseSTPAMain() {

	}

}
