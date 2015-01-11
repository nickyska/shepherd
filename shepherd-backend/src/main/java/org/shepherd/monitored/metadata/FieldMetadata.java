package org.shepherd.monitored.metadata;

/**
 * 
 * @author nickolayb
 * @date Jan 8, 2015
 * 
 * @param <T>
 */
public interface FieldMetadata<T> {

	/**
	 * @author nickolayb
	 * @date Jan 8, 2015 
	 * @returns the class representation of the field
	 */
	public Class<T> getFieldClass();
	
	/**
	* @author nickolayb
	* @date Jan 8, 2015
	* @returns display name for the field
	*/
	public String getFieldDisplayName();
}
