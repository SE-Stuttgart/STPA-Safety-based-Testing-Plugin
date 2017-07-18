package xstampp.stpatcgenerator.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;

import org.eclipse.jface.dialogs.MessageDialog;

import xstampp.model.AbstractLTLProvider;
import xstampp.stpatcgenerator.model.ExportInformation;
import xstampp.stpatcgenerator.model.VerificationResult;
//import xstampp.stpatcgenerator.model.properties.CTLFormular;
import xstampp.stpatcgenerator.model.properties.LTLFormular;
import xstampp.stpatcgenerator.model.properties.ModelProperty;
import xstampp.stpatcgenerator.model.smv.SMV;
import xstampp.stpatcgenerator.model.stateflow.chart.generateStatesTree.Tree;
import xstampp.stpatcgenerator.model.stateflow.fsm.EFSM;
import xstampp.stpatcgenerator.ui.editors.GTCConfigEditor;
import xstampp.stpatcgenerator.ui.views.TestCaseHistogrammView;
import xstampp.stpatcgenerator.util.handler.BuildSafeTestModelHandler;
import xstampp.stpatcgenerator.wizards.ConfigurationWizard;
import xstampp.model.AbstractLTLProvider;
/**
 * This class contains a controller for all models for STPA TCGenerator. 
 * @author Ting Luk-He
 * 
 *
 */
public class STPATCGModelController{
	private static ConfigurationWizard confWizard;
	private static BuildSafeTestModelHandler sfmHandler;
	private static GTCConfigEditor gtcConfigEditor;
	private static TestCaseHistogrammView tcHistrogrammView;
	private static String lastStateFlowPath;
	private static String lastNuSMVPath;
	
	private static DefaultTreeModel testCaseTreeModel;
	private static DefaultTableModel testCaseTableModel;
	
	// table models for state flow properties
	private static DefaultTableModel statesTableModel;
	private static DefaultTableModel inputVarTableModel;
	private static DefaultTableModel outputVarTableModel;
	private static DefaultTableModel localVarTableModel;
	
	// table model for state flow truth table
	private static DefaultTableModel stateFlowTruthTableModel;
	
	private static Tree sbm;
	private static SMV smv;
	private static EFSM fsm;
	
	// table model for EFSM truth table
	private static DefaultTableModel efsmTruthTableModel;
	
	// models for final test cases result export
	private static DefaultTableModel testInputVarTableModel;
	private static DefaultTableModel transConditionTableModel;
	private static DefaultTableModel ssrTableModel;
	private static DefaultTableModel traceMatrixTableModel;
	private static DefaultTableModel genTCConfigTableModel;
	private static DefaultTableModel infoMsgTableModel;
	
	private Map<UUID,LTLFormular> ltlList;
	private static List<String> ltlResults;
//	private Map<UUID,CTLFormular> ctlList;
	private List<VerificationResult> results;	
	@SuppressWarnings("unused")
	private ExportInformation exportinformation;
	
	public STPATCGModelController() {
		this.ltlList = new HashMap<>();
//		this.ctlList = new HashMap<>();
	}
	
	public STPATCGModelController(STPATCGModelController model){
		this.ltlList = new HashMap<>(model.ltlList);
//		this.ctlList = new HashMap<>(model.ctlList);
		this.results = new ArrayList<>(model.results);
		this.exportinformation = new ExportInformation();
	}
	
	public void addLTLFormular(AbstractLTLProvider provider){
		LTLFormular property =new LTLFormular(provider);
		this.ltlList.put(property.getUUID(), property);
	}
	
	public void addLTLFormular(String id, String formular){
		LTLFormular property;
		if(id == null){
			property=new LTLFormular("p"+getSizeOfLTLList(),formular);
		}else{
			property=new LTLFormular(id,formular);
		}
		this.ltlList.put(property.getUUID(), property);
	}
	
	public int getSizeOfLTLList(){
		return this.ltlList.size();
	}
	
//	public void addCTLFormular(String id,String formula){
//		CTLFormular property;
//		if(id == null){
//			property=new CTLFormular("c"+getSizeOfCTLList(),formula);
//		}else{
//			property=new CTLFormular(id,formula);
//		}
//		this.ctlList.put(property.getUUID(), property);
//	}
//	
//	public int getSizeOfCTLList(){
//		return this.ctlList.size();
//	}
	
	public List<LTLFormular> getLtlList() {
		return new ArrayList<>(this.ltlList.values());
	}
//	public List<CTLFormular> getCtlList() {
//		return new ArrayList<>(this.ctlList.values());
//	}
	
	/**
	 * @param id the id to which the formula Object is mapped
	 * @param state the state to set
	 * 
	 * @return if the UUID is key to a legal ltl/ctl property and when if 
	 * 			the given state is legal
	 */
	public boolean setPropertyState(UUID id,int state) {
		boolean result = false;
		if(this.ltlList.containsKey(id)){
			result = this.ltlList.get(id).setState(state);
		}
//		else if(this.ctlList.containsKey(id)){
//			result = this.ctlList.get(id).setState(state);
//		}
		return result;
	}
	/**
	 * changes the  value of a the property that is stored with this id
	 *  
	 * @param id the id to which the formula Object is mapped
	 * @param property the new formula value
	 */
	public void setProperty(UUID id, String property){
		if(this.ltlList.containsKey(id)){
			this.ltlList.get(id).setsFormular(property);
		}
//		else if(this.ctlList.containsKey(id)){
//			this.ctlList.get(id).setsFormular(property);
//		}
		
	}
	
	/**
	 * @param use indicates whether all properties should be verified or not
	 */
	public void setUseAllProperties(boolean use){
		for(LTLFormular prop : this.ltlList.values()){
			prop.use(use);
		}
//		for(CTLFormular prop : this.ctlList.values()){
//			prop.use(use);
//		}
	}
	
	/**
	 * 
	 * @param id the id to which the formula Object is mapped which is used in this setter
	 * @param checked a boolean indicating whether the property should be verified in the next job or not
	 */
	public void setUseProperty(UUID id, boolean checked) {
		if(this.ltlList.containsKey(id)){
			this.ltlList.get(id).use(checked);
		}
//		else if(this.ctlList.containsKey(id)){
//			this.ctlList.get(id).use(checked);
//		}
	}
	
	/**
	 * Clear ltl import removes all ltl properties from the list of ltl properties 
	 * that were imported from astpa
	 * 
	 * @return whether or not the import was cleared
	 * 
	 * @see AbstractLTLProvider
	 */
	public boolean clearLTLImport(){
		List<UUID> keyList= new ArrayList<>(this.ltlList.keySet()); 
		ArrayList<UUID> importedLTL=new ArrayList<>();
		for (UUID key : keyList) {
			if(this.ltlList.get(key).getLtlProvider() != null){
				importedLTL.add(key);
			}
		}
		if(importedLTL.isEmpty() || MessageDialog.openConfirm(null, "overwrite LTL?", "Do you want to overwrite the currenty imported\n"
																	+ "LTL Propertys?")){	
			for (UUID uuid : keyList) {
				this.ltlList.remove(uuid);
			}
			return true;
		}
		return false;
		
	}
	/**
	 * removes a property from one of the two property lists
	 * and triggers an update to {@link ObserverValues#FORMULARS_CHANGED}
	 * 
	 * @param id the UUID key which should be removed, if null than false is returned
	 * @param all if true than all stored properties are removed
	 */
	public boolean removeProperty(UUID id, boolean all) {
		if(all){
			this.ltlList.clear();
//			this.ctlList.clear();
			return true;
		}else if(id != null && this.ltlList.containsKey(id)){
			this.ltlList.remove(id);
			return !this.ltlList.containsKey(id);
		}
//		else if(id != null && this.ctlList.containsKey(id)){
//			this.ctlList.remove(id);
//			return !this.ctlList.containsKey(id);
//		}
		return false;
	}
	
	/**
	 * @return the results of a check session
	 */
	public List<VerificationResult> getResults() {
		return this.results;
	}
	/**
	 *creates or clears the results list  and triggers a changed observer value
	 */
	public void resetResults() {
		if(this.results == null){
			this.results = new ArrayList<>();
		}else{
			this.results.clear();
		}
	}
	
	public boolean addResult(VerificationResult res){
		if(!res.getSsrLiteral().equals(ModelProperty.NULL_SID)){
			
			if(this.results == null){
				this.results = new ArrayList<>();
			}
			this.results.add(res);
			return true;
		}
		return false;
	}
	
	
	public void prepareForExport(){
		exportinformation = new ExportInformation();
		
	}

//	public static EFSM getFsm() {
//		return fsm;
//	}
//
//	public static void setFsm(EFSM fsm) {
//		STPATCGModel.fsm = fsm;
//	}

	public static DefaultTableModel getStatesTableModel() {
		return statesTableModel;
	}

	public static void setStatesTableModel(DefaultTableModel statesTableModel) {
		STPATCGModelController.statesTableModel = statesTableModel;
	}

	public static DefaultTableModel getInputVarTableModel() {
		return inputVarTableModel;
	}

	public static void setInputVarTableModel(DefaultTableModel inputVarTableModel) {
		STPATCGModelController.inputVarTableModel = inputVarTableModel;
	}

	public static DefaultTableModel getOutputVarTableModel() {
		return outputVarTableModel;
	}

	public static void setOutputVarTableModel(DefaultTableModel outputVarTableModel) {
		STPATCGModelController.outputVarTableModel = outputVarTableModel;
	}

	public static DefaultTableModel getLocalVarTableModel() {
		return localVarTableModel;
	}

	public static void setLocalVarTableModel(DefaultTableModel localVarTableModel) {
		STPATCGModelController.localVarTableModel = localVarTableModel;
	}

	public static DefaultTableModel getStateFlowTruthTableModel() {
		return stateFlowTruthTableModel;
	}

	public static void setStateFlowTruthTableModel(DefaultTableModel stateFlowTruthTableModel) {
		STPATCGModelController.stateFlowTruthTableModel = stateFlowTruthTableModel;
	}

	public static DefaultTreeModel getTestCaseTreeModel() {
		return testCaseTreeModel;
	}

	public static void setTestCaseTreeModel(DefaultTreeModel testCaseTreeModel) {
		STPATCGModelController.testCaseTreeModel = testCaseTreeModel;
	}

	public static DefaultTableModel getTestCaseTableModel() {
		return testCaseTableModel;
	}

	public static void setTestCaseTableModel(DefaultTableModel testCaseTableModel) {
		STPATCGModelController.testCaseTableModel = testCaseTableModel;
	}

	public static List<String> getLtlResults() {
		return ltlResults;
	}

	public static void setLtlResults(List<String> ltlResults) {
		STPATCGModelController.ltlResults = ltlResults;
	}

	public static ConfigurationWizard getConfWizard() {
		return confWizard;
	}

	public static void setConfWizard(ConfigurationWizard confWizard) {
		STPATCGModelController.confWizard = confWizard;
	}

	public static BuildSafeTestModelHandler getSfmHandler() {
		return sfmHandler;
	}

	public static void setSfmHandler(BuildSafeTestModelHandler sfmHandler) {
		STPATCGModelController.sfmHandler = sfmHandler;
	}

	public static GTCConfigEditor getGtcConfigEditor() {
		return gtcConfigEditor;
	}

	public static void setGtcConfigEditor(GTCConfigEditor gtcConfigEditor) {
		STPATCGModelController.gtcConfigEditor = gtcConfigEditor;
	}

	public static TestCaseHistogrammView getTcHistrogrammView() {
		return tcHistrogrammView;
	}

	public static void setTcHistrogrammView(TestCaseHistogrammView tcHistrogrammView) {
		STPATCGModelController.tcHistrogrammView = tcHistrogrammView;
	}

	public static String getLastStateFlowPath() {
		return lastStateFlowPath;
	}

	public static void setLastStateFlowPath(String lastStateFlowPath) {
		STPATCGModelController.lastStateFlowPath = lastStateFlowPath;
	}

	public static String getLastNuSMVPath() {
		return lastNuSMVPath;
	}

	public static void setLastNuSMVPath(String lastNuSMVPath) {
		STPATCGModelController.lastNuSMVPath = lastNuSMVPath;
	}

	public static DefaultTableModel getTestInputVarTableModel() {
		return testInputVarTableModel;
	}

	public static void setTestInputVarTableModel(DefaultTableModel testInputVarTableModel) {
		STPATCGModelController.testInputVarTableModel = testInputVarTableModel;
	}

	public static DefaultTableModel getTraceMatrixTableModel() {
		return traceMatrixTableModel;
	}

	public static void setTraceMatrixTableModel(DefaultTableModel traceMatrixTableModel) {
		STPATCGModelController.traceMatrixTableModel = traceMatrixTableModel;
	}

	public static DefaultTableModel getGenTCConfigTableModel() {
		return genTCConfigTableModel;
	}

	public static void setGenTCConfigTableModel(DefaultTableModel genTCConfigTableModel) {
		STPATCGModelController.genTCConfigTableModel = genTCConfigTableModel;
	}

	public static DefaultTableModel getInfoMsgTableModel() {
		return infoMsgTableModel;
	}

	public static void setInfoMsgTableModel(DefaultTableModel infoMsgTableModel) {
		STPATCGModelController.infoMsgTableModel = infoMsgTableModel;
	}

	public static DefaultTableModel getTransConditionTableModel() {
		return transConditionTableModel;
	}

	public static void setTransConditionTableModel(DefaultTableModel transConditionTableModel) {
		STPATCGModelController.transConditionTableModel = transConditionTableModel;
	}

	public static DefaultTableModel getSsrTableModel() {
		return ssrTableModel;
	}

	public static void setSsrTableModel(DefaultTableModel ssrTableModel) {
		STPATCGModelController.ssrTableModel = ssrTableModel;
	}

	public static DefaultTableModel getEfsmTruthTableModel() {
		return efsmTruthTableModel;
	}

	public static void setEfsmTruthTableModel(DefaultTableModel efsmTruthTableModel) {
		STPATCGModelController.efsmTruthTableModel = efsmTruthTableModel;
	}
	
	
}
