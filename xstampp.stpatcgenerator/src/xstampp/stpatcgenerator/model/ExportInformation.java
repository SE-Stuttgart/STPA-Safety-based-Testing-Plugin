package xstampp.stpatcgenerator.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import xstampp.stpatcgenerator.controller.IVerificationResult;
import xstampp.stpatcgenerator.model.properties.AbstractProperty;
import xstampp.stpatcgenerator.model.properties.PropertyExportModel;
import xstampp.stpatcgenerator.ui.views.utils.PropertyHoldsProvider;
import xstampp.Activator;
import xstampp.preferences.IPreferenceConstants;

/**
 * Stores extra information for the export
 * 
 * @author Lukas Balzer
 * 
 */
@XmlRootElement(name="verificationModel")
public class ExportInformation {

	private final IPreferenceStore store = Activator.getDefault()
			.getPreferenceStore();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd.MM.yyyy, HH:mm:ss"); //$NON-NLS-1$

	private Date creationDate;
	private String logoPath;
	private String company;
	private String colorBackground;
	private String colorFont;
	private String modelName;
	private String modelType;
	private String stpaProjectName;
	private String modelCheckerVersion;
	
	
	@XmlElementWrapper(name="verificationResults")
	@XmlElement(name="result")
	private List<IVerificationResult> results;
	
	@XmlElementWrapper(name="safetyProperties")
	@XmlElement(name="property")
	private List<PropertyExportModel> properties;

	/**
	 * Constructor of the export information
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public ExportInformation() {
		this.creationDate = new Date();
		File logo = new File(this.store.getString(IPreferenceConstants.COMPANY_LOGO));
		if(logo.exists()){
			this.logoPath = logo.toURI().toString();
		}
		this.company = this.store.getString(IPreferenceConstants.COMPANY_NAME);
		this.colorBackground = this.rgbToHex(PreferenceConverter.getColor(
				this.store, IPreferenceConstants.COMPANY_BACKGROUND_COLOR));
		this.colorFont = this.rgbToHex(PreferenceConverter.getColor(this.store,
				IPreferenceConstants.COMPANY_FONT_COLOR));
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return this.dateFormat.format(this.creationDate);
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		// Nothing to do here. Is needed that JAXB does work
	}
	/**
	 * @return the logoPath
	 */
	public String getLogoPath() {
		if (this.logoPath != null && this.logoPath.isEmpty()) {
			return null;
		}
		return this.logoPath;
	}

	/**
	 * @param logoPath
	 *            the logoPath to set
	 * @return true, if the logoPath has been set
	 */
	public boolean setLogoPath(String logoPath) {
		this.logoPath = logoPath;
		return true;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		if (this.company.isEmpty()) {
			return null;
		}
		return this.company;
	}

	/**
	 * @param company
	 *            the company to set
	 * @return true, if the company has been set
	 */
	public boolean setCompany(String company) {
		this.company = company;
		return true;
	}

	/**
	 * @return the background color
	 */
	public String getBackgroundColor() {
		return this.colorBackground;
	}

	/**
	 * @param colorBackground
	 *            the color to set
	 * @return true, if the color has been set
	 */
	public boolean setBackgroundColor(String colorBackground) {
		this.colorBackground = colorBackground;
		return true;
	}

	/**
	 * @return the font color
	 */
	public String getFontColor() {
		return this.colorFont;
	}

	/**
	 * @param colorFont
	 *            the color to set
	 * @return true, if the color has been set
	 */
	public boolean setFontColor(String colorFont) {
		this.colorFont = colorFont;
		return true;
	}

	private String rgbToHex(RGB rgb) {
		String redHexadecimal = Integer.toHexString(rgb.red);
		String greenHexadecimal = Integer.toHexString(rgb.green);
		String blueHexadecimal = Integer.toHexString(rgb.blue);
		if (redHexadecimal.length() == 1) {
			redHexadecimal = "0" + redHexadecimal; //$NON-NLS-1$
		}
		if (greenHexadecimal.length() == 1) {
			greenHexadecimal = "0" + greenHexadecimal; //$NON-NLS-1$
		}
		if (blueHexadecimal.length() == 1) {
			blueHexadecimal = "0" + blueHexadecimal; //$NON-NLS-1$
		}
		return "#" + redHexadecimal + greenHexadecimal + blueHexadecimal; //$NON-NLS-1$
	}

	public void setResults(List<IVerificationResult> results) {
		this.results = results;
		
	}

	public void setProperties(List<AbstractProperty> allProperties,boolean useSpin) {
		this.properties = new ArrayList<>();
		PropertyHoldsProvider provider = new PropertyHoldsProvider();
		PropertyExportModel tmp = new PropertyExportModel();
		for (AbstractProperty property : allProperties) {
			tmp = new PropertyExportModel();
			tmp.setFormula(property.getsFormular(useSpin, true));
			tmp.setId(property.getsID());
			tmp.setStatus(provider.getText(property));
			this.properties.add(tmp);
//			if(property.getCounterexample() != null){
//				tmp.setCounterexample(property.getCounterexample().getLines());
//			}
		}
		
	}
	public void setProperties(List<PropertyExportModel> allProperties) {
		this.properties = new ArrayList<>(allProperties);
	}

	/**
	 * @return the modelName
	 */
	public String getModelName() {
		return this.modelName;
	}

	/**
	 * @param modelName the modelName to set
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	/**
	 * @return the modelType
	 */
	public String getModelType() {
		return this.modelType;
	}

	/**
	 * @param modelType the modelType to set
	 */
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	/**
	 * @return the stpaProjectName
	 */
	public String getStpaProjectName() {
		return this.stpaProjectName;
	}

	/**
	 * @param stpaProjectName the stpaProjectName to set
	 */
	public void setStpaProjectName(String stpaProjectName) {
		this.stpaProjectName = stpaProjectName;
	}

	/**
	 * @return the modelCheckerVersion
	 */
	public String getModelCheckerVersion() {
		return this.modelCheckerVersion;
	}

	/**
	 * @param modelCheckerVersion the modelCheckerVersion to set
	 */
	public void setModelCheckerVersion(String modelCheckerVersion) {
		this.modelCheckerVersion = modelCheckerVersion;
	}
	
//	public List<VerificationResult> getResults() {
//		return this.results;
//	}
//	
//	public List<PropertyExportModel> getProperties() {
//		return this.properties;
//	}
	
}
