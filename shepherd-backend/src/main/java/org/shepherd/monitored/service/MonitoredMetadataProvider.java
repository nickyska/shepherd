package org.shepherd.monitored.service;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.shepherd.monitored.Monitored;
import org.shepherd.monitored.annotation.UIField;
import org.shepherd.monitored.metadata.FieldMetadata;
import org.shepherd.monitored.metadata.MonitoredMetadaImpl;
import org.shepherd.monitored.provider.MonitoredProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("defaultMonitoredProvider")
public class MonitoredMetadataProvider<T extends Object,V extends Monitored> implements MetadataProvider<T,V> {

	@Autowired
	private MonitoredProvider monitoredProvider;
	
	private Map<Class<T>, FieldMetadata<V>> metadataMap;

	@PostConstruct
	protected void init() throws ClassNotFoundException {
		
		this.metadataMap = new HashMap<Class<T>, FieldMetadata<V>>(); 
		createMetadata(monitoredProvider.getAllMonitoredClasses());
	}

	@Override
	public Collection<FieldMetadata<V>> getAllMetadata() {
		return metadataMap.values();
	}

	@Override
	public FieldMetadata<V> getMetadata(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}



	@SuppressWarnings("unchecked")
	private void createMetadata(Collection<Class<Monitored>> classes) {


		for (Class<Monitored> class1 : classes) {

			Field[] fields = class1.getDeclaredFields();
			for (Field field : fields) {

				if (field.isAnnotationPresent(UIField.class)) {

					Class<T> declaringClass =  (Class<T>) field.getDeclaringClass();
					String displayName = field.getAnnotation(UIField.class).displayName();

					MonitoredMetadaImpl<Monitored> monitoredMetadaImpl = new MonitoredMetadaImpl(declaringClass, displayName,field);
					
					metadataMap.put(declaringClass, (FieldMetadata<V>) monitoredMetadaImpl);
				}
			}
		}


	}

}
