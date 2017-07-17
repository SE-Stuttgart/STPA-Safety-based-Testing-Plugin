package stpaverifier.controller.preferences.spin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import stpaverifier.Activator;

public class SPINPreferenceInitializor extends AbstractPreferenceInitializer implements SpinPreferenceConstants{

	private IPreferenceStore store;
	
	public SPINPreferenceInitializor() {
		this.store = Activator.getDefault().getPreferenceStore();
		
	}
	@Override
	public void initializeDefaultPreferences() {
		this.store.setDefault(PREF_SPIN_INT_DMA_N, 1520);
		this.store.setDefault(PREF_SPIN_INT_DVECTORSZ, 2048);
		this.store.setDefault(PREF_SPIN_INT_DMEMLIM,1024);
		this.store.setDefault(PREF_SPIN_BOOL_DNOCLAIM, false);
		this.store.setDefault(PREF_SPIN_BOOL_DCOLLAPSE, true);
		this.store.setDefault(PREF_SPIN_BOOL_DSAFETY, true);
		this.store.setDefault(PREF_SPIN_BOOL_DXUSAFE, true);
		this.store.setDefault(PREF_SPIN_INT_RUN_MAX,5000);
		this.store.setDefault(PREF_SPIN_PATH,"spin");
		this.store.setDefault(PREF_C_PATH,"gcc");
		this.store.setDefault(PREF_MODEX_PATH,"modex");
	}

}
