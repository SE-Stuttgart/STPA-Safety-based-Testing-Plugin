package stpaverifier.util;

public interface ICMDScanner {
	/**
	 * checks the given string for special sequences
	 * the implementation depends on the algorithm which is used in this job
	 *   
	 * @param line the line which should be checked
	 * @param current the state constant defining the current state of the property
	 * @return the integer indicating the state this property is in,
	 * 		   should be one of the constants defined in IProperty
	 */
	public abstract int scanLine(String line, int current);
}
