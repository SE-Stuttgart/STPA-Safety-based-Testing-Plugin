package stpaverifier.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.jface.dialogs.MessageDialog;

import stpaverifier.controller.ObserverValues;
import stpaverifier.model.properties.CTLFormular;
import stpaverifier.model.properties.LTLFormular;
import stpaverifier.model.properties.ModelProperty;
import xstampp.model.AbstractLTLProvider;

/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class STPAVerifierModel{

	private Map<UUID,LTLFormular> ltlList;
	private Map<UUID,CTLFormular> ctlList;
	private List<VerificationResult> results;
	@SuppressWarnings("unused")
	private ExportInformation exportinformation;
	
	public STPAVerifierModel() {
		this.ltlList = new HashMap<>();
		this.ctlList = new HashMap<>();
	}
	
	public STPAVerifierModel(STPAVerifierModel model){
		this.ltlList = new HashMap<>(model.ltlList);
		this.ctlList = new HashMap<>(model.ctlList);
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
	
	public void addCTLFormular(String id,String formula){
		CTLFormular property;
		if(id == null){
			property=new CTLFormular("c"+getSizeOfCTLList(),formula);
		}else{
			property=new CTLFormular(id,formula);
		}
		this.ctlList.put(property.getUUID(), property);
	}
	
	public int getSizeOfCTLList(){
		return this.ctlList.size();
	}
	
	public List<LTLFormular> getLtlList() {
		return new ArrayList<>(this.ltlList.values());
	}
	public List<CTLFormular> getCtlList() {
		return new ArrayList<>(this.ctlList.values());
	}
	
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
		}else if(this.ctlList.containsKey(id)){
			result = this.ctlList.get(id).setState(state);
		}
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
		}else if(this.ctlList.containsKey(id)){
			this.ctlList.get(id).setsFormular(property);
		}
		
	}
	
	/**
	 * @param use indicates whether all properties should be verified or not
	 */
	public void setUseAllProperties(boolean use){
		for(LTLFormular prop : this.ltlList.values()){
			prop.use(use);
		}for(CTLFormular prop : this.ctlList.values()){
			prop.use(use);
		}
	}
	
	/**
	 * 
	 * @param id the id to which the formula Object is mapped which is used in this setter
	 * @param checked a boolean indicating whether the property should be verified in the next job or not
	 */
	public void setUseProperty(UUID id, boolean checked) {
		if(this.ltlList.containsKey(id)){
			this.ltlList.get(id).use(checked);
		}else if(this.ctlList.containsKey(id)){
			this.ctlList.get(id).use(checked);
		}
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
			this.ctlList.clear();
			return true;
		}else if(id != null && this.ltlList.containsKey(id)){
			this.ltlList.remove(id);
			return !this.ltlList.containsKey(id);
		}else if(id != null && this.ctlList.containsKey(id)){
			this.ctlList.remove(id);
			return !this.ctlList.containsKey(id);
		}
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
}
