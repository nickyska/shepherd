package org.shepherd.monitored.service;

import java.util.Collection;

import org.shepherd.monitored.metadata.FieldMetadata;

/**
 * Reponsible to provide metadata for classes
 * @author nickolayb
 * @param <T>
 *
 */
public interface MetadataProvider<K,V> {

	/**
	 * return all metadata
	 * @param <T>
	 */
	public Collection<FieldMetadata<V>> getAllMetadata();
	
	/**
	 * return metadata by class
	 * @param <T>
	 * @param clazz
	 */
	public FieldMetadata<V> getMetadata(Class<K> clazz);
}
