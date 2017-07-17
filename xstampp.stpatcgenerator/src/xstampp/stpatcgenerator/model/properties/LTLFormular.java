package xstampp.stpatcgenerator.model.properties;

import xstampp.model.AbstractLTLProvider;


/**
 *  
 * @author Lukas Balzer
 * @since 1.0.0
 *
 */
public class LTLFormular extends AbstractProperty{

	
	private AbstractLTLProvider ltlProvider;
	

	public LTLFormular(String idLiteral, String formular) {
		super(idLiteral, formular);
		ltlProvider = null;
	}

	public LTLFormular(AbstractLTLProvider provider){
		super("SSR1." +provider.getNumber(),provider.getLtlProperty());
		ltlProvider = provider;
	}

	/**
	 * @return the ltlProvider
	 */
	public AbstractLTLProvider getLtlProvider() {
		return this.ltlProvider;
	}
	
}
