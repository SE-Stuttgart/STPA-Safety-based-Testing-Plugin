package acast.controller;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import messages.Messages;

import org.eclipse.swt.custom.StyleRange;

public class ProjectDataController {
	private String projectName = Messages.NewProject;
	private String accidentDescription = "";

	private String accidentDate = "";
	private String accidentLocation = "";
	private String accidentCompany = "";
	private String currentPicture = "";

	
	@XmlElementWrapper(name = "pictures")
	@XmlElement (name = "picture")
	private List<String> pictureList;

	@XmlJavaTypeAdapter(StyleRangeAdapter.class)
	@XmlElementWrapper(name = "styleRanges")
	@XmlElement(name = "styleRange")
	private List<StyleRange> styleRanges;

	public String getAccidentCompany() {
		return accidentCompany;
	}

	public void setAccidentCompany(String accidentCompany) {
		this.accidentCompany = accidentCompany;
	}

	public String getAccidentDate() {
		return accidentDate;
	}

	public void setAccidentDate(String accidentDate) {
		this.accidentDate = accidentDate;
	}

	public String getAccidentLocation() {
		return accidentLocation;
	}

	public void setAccidentLocation(String accidentLocation) {
		this.accidentLocation = accidentLocation;
	}

	/**
	 * Constructor of the project data controller
	 * 
	 * 
	 */
	public ProjectDataController() {
		this.styleRanges = new ArrayList<>();
	}

	/**
	 * Gets the name/title of the A-CAST-Project
	 * 
	 * @return current name/title of the A-CAST-Project
	 * 
	 */
	public String getProjectName() {
		return this.projectName;
	}

	/**
	 * Sets the name/title of the A-CAST-Project
	 * 
	 * @param projectName
	 *            which will be set
	 * 
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Gets the AccidentDescription of the A-CAST-Project
	 * 
	 * @return current AccidentDescription of the A-CAST-Project
	 * 
	 */
	public String getAccidentDescription() {
		return this.accidentDescription;
	}

	/**
	 * Sets the AccidentDescription of the A-CAST-Project
	 * 
	 * @param accidentDescription
	 *            which has to be set
	 * 
	 */
	public void setAccidentDescription(String accidentDescription) {
		this.accidentDescription = accidentDescription;
	}

	/**
	 * Getter for style ranges.
	 * 
	 */
	public List<StyleRange> getStyleRanges() {
		return this.styleRanges;
	}

	public List<String> getPictureList() {
		return this.pictureList;
	}

	public boolean setPictureList(String[] pictureList) {

		if (this.pictureList != null) {
			this.pictureList.clear();
		} else {
			this.pictureList = new ArrayList<String>();
		}
		for (String picture : pictureList) {
			this.pictureList.add(picture);
		}
		return true;
	}

	/**
	 * Adds a style range
	 * 
	 * 
	 * @param styleRange
	 *            the new style range
	 * @return true, if the style range has been added
	 */
	public boolean addStyleRange(StyleRange styleRange) {
		return this.styleRanges.add(styleRange);
	}

	/**
	 * Getter for the style ranges
	 * 
	 * 
	 * @return the style ranges as array
	 */
	public StyleRange[] getStyleRangesAsArray() {
		return this.styleRanges.toArray(new StyleRange[this.styleRanges.size()]);
	}

	public String getCurrentPicture() {
		return currentPicture;
	}

	public void setCurrentPicture(String currentPicture) {
		this.currentPicture = currentPicture;
	}

}
