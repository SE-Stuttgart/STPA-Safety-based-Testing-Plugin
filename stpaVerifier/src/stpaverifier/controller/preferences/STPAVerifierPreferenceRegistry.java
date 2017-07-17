package stpaverifier.controller.preferences;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import stpaverifier.Activator;
import xstampp.ui.common.ProjectManager;

public class STPAVerifierPreferenceRegistry {
	public static final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	private HashMap<String,Control> controlsToConstants = new HashMap<>();

	/**
	 * this routine adds two listener objects to a Text widget which represents an 
	 * integer input for controlling a string preference
	 *  
	 * @param input a Text widget for controlling a preference
	 * @param constant a constant which should belong to a integer preference
	 */
	public void registerIntegerInput(final Text input,final String constant){
		controlsToConstants.put(constant, input);
		input.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(input.getText().isEmpty()){
					store.setValue(constant,store.getDefaultInt(constant));
					return;
				}
				double value = Double.parseDouble(input.getText());
				if(value <= Integer.MAX_VALUE){
					store.setValue(constant,(int)value);
				}else{				
					input.setText(String.valueOf(store.getInt(constant)));
					input.setSelection(input.getText().length());
				}
				savePreferences();

			
			}
		});
		//
		store.addPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if(input.isDisposed()){
					store.removePropertyChangeListener(this);
				}else if(event.getProperty().equals(constant) && ! input.getText().equals(event.getNewValue().toString())){
				
					input.setText(String.valueOf(event.getNewValue()));
					input.setSelection(input.getText().length());
				}
				
			}
		});
		input.setText(String.valueOf(store.getInt(constant)));
	}
	/**
	 * this routine adds two listener objects to a Text widget which represents an 
	 * integer input for controlling a string preference
	 *  
	 * @param input a Text widget for controlling a preference
	 * @param constant a constant which should belong to a integer preference
	 */
	public void registerComboInput(final Combo input,final String constant){
		controlsToConstants.put(constant, input);
		input.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(!input.getText().isEmpty()){
					store.setValue(constant,input.getSelectionIndex());
					savePreferences();
				}
			}
		});
		//
		store.addPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if(input.isDisposed()){
					store.removePropertyChangeListener(this);
				}else if(event.getProperty().equals(constant) && ! input.getText().equals(event.getNewValue().toString())){
				
					input.select((int) event.getNewValue());
				}
				
			}
		});
		input.select(store.getInt(constant));
	}
	
	
	/**
	 * this routine adds two listener objects to a Text widget which represents an 
	 * integer input for controlling a string preference
	 *  
	 * @param input a Text widget for controlling a preference
	 * @param constant a constant which should belong to a integer preference
	 */
	public void registerStringInput(final Text input,final String constant){
		controlsToConstants.put(constant, input);
		input.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(!input.getText().isEmpty()){
					store.setValue(constant,input.getText());
					savePreferences();
				}
			}
		});
		//
		store.addPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if(input.isDisposed()){
					store.removePropertyChangeListener(this);
				}else if(event.getProperty().equals(constant) && ! input.getText().equals(event.getNewValue().toString())){
				
					input.setText(String.valueOf(event.getNewValue()));
					input.setSelection(input.getText().length());
					
				}
				
			}
		});

		ProjectManager.getLOGGER().debug("set "+constant +" to " + store.getString(constant));
		input.setText(store.getString(constant));
	}
	/**
	 * this routine adds two listener objects to a button which represents a 
	 * boolean input for controlling a boolean preference
	 *  
	 * @param input a button (mostly a check or radio box) for controlling a boolean preference
	 * @param constant a constant which should belong to a boolean preference
	 */
	public void registerBooleanInput(final Button input,final String constant){
		controlsToConstants.put(constant, input);
		input.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				store.setValue(constant, input.getSelection());
				savePreferences();
				
			}
		});
		store.addPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if(event.getProperty().equals(constant)){
					input.setSelection((boolean) event.getNewValue());
				}
				
			}
		});
		input.setSelection(store.getBoolean(constant));
	}

	private void savePreferences(){
		if(store instanceof IPersistentPreferenceStore){
			try {
				((IPersistentPreferenceStore) store).save();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void loadDefaults(){
		for(Entry<String, Control> entry : controlsToConstants.entrySet()){
			if(entry.getValue() instanceof Button){
				((Button)entry.getValue()).setSelection(store.getDefaultBoolean(entry.getKey()));
			}else if(entry.getValue() instanceof Text){
				((Text)entry.getValue()).setText(store.getDefaultString(entry.getKey()));
			}
		}
	}
}
