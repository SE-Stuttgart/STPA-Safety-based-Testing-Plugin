package stpaverifier.controller.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import stpaverifier.Activator;
import stpaverifier.controller.preferences.nusmv.NuSMVPreferenceConstants;

public class STPAVerifierPreferenceInitializer extends	AbstractPreferenceInitializer implements ModelCheckerPreferences{
	private IPreferenceStore store;
	
	public STPAVerifierPreferenceInitializer() {
		this.store = Activator.getDefault().getPreferenceStore();
		
	}
	@Override
	public void initializeDefaultPreferences() {

		this.store.setDefault(PREF_USE_SPIN,true);
		this.store.setDefault(NuSMVPreferenceConstants.NuSMV_PATH,"NuSMV");
		this.store.setDefault(NuSMVPreferenceConstants.Pref_NuSMV_USE_BDD,true);
		this.store.setDefault(NuSMVPreferenceConstants.Pref_NuSMV_USE_BMC,false);
		this.store.setDefault(NuSMVPreferenceConstants.Pref_NuSMV_NR_COUNTEREX_PLUGIN,0);
		this.store.setDefault(NuSMVPreferenceConstants.Pref_NuSMV_INT_BMC_DEPTH,10);
		this.store.setDefault(NuSMVPreferenceConstants.Pref_NuSMV_INT_BMC_LOOPBACK,-1);
	}

}
