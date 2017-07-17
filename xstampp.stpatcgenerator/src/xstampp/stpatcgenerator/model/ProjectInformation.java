package xstampp.stpatcgenerator.model;

/**
*
* @author Asim Abdulkhaleq
*/

public class ProjectInformation {
	private static int typeOfUse;
    private static String STPApath;
    private static String Stateflow;
    private static String nusmvPath;

    public static int getTypeOfUse() {
        return typeOfUse;
    }

    public static void setTypeOfUse(int type) {
        typeOfUse = type;
    }

    public static String getSTPAPath() {
        return STPApath;
    }

    public static void setSTPAPath(String path) {
        STPApath = path;
    }

    public static void setStateflow(String path) {
        Stateflow = path;
    }

    public static String getStateflow() {
        return Stateflow;
    }

	public static String getNusmvPath() {
		return nusmvPath;
	}

	public static void setNusmvPath(String nusmvPath) {
		ProjectInformation.nusmvPath = nusmvPath;
	}
    
    
}
