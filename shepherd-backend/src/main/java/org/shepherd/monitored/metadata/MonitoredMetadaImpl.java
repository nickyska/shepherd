package org.shepherd.monitored.metadata;

import java.lang.reflect.Field;

import org.shepherd.monitored.Monitored;

public class MonitoredMetadaImpl<T extends Monitored> implements FieldMetadata<T> {


	private Class<T> fieldClass;
	
	private String fieldDisplayName;
	
	private Field field;
	
	
	public MonitoredMetadaImpl(Class<T> fieldClass, String fieldDisplayName,Field field) {
		this.fieldClass = fieldClass;
		this.fieldDisplayName = fieldDisplayName;
		this.field = field;
	}
	
	@Override
	public Class<T> getFieldClass() {
		return this.fieldClass;
	}

	@Override
	public String getFieldDisplayName() {
		return this.fieldDisplayName;
	}

	public void setFieldClass(Class<T> fieldClass) {
		this.fieldClass = fieldClass;
	}

	public void setFieldDisplayName(String fieldDisplayName) {
		this.fieldDisplayName = fieldDisplayName;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

}
