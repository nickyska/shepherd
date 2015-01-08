package org.shepherd.monitored.provider;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.shepherd.monitored.Monitored;
import org.shepherd.monitored.MonitoringTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author DavidM
 * @since Dec 28, 2014
 * @version 0.1.0
 */
@Component
public class DefaultMonitoredProvider implements MonitoredProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMonitoredProvider.class);

	@Value("${monitored.provider.scanned.package:org.shepherd}")
	private String scannedPackage;

	private Map<Class<Monitored>, Collection<Class<MonitoringTask<? extends Monitored>>>> providerMap;

	private Collection<Class<Monitored>> monitoredWIthDefaultConstractor;

	@PostConstruct
	protected void init() throws ClassNotFoundException {
		
		this.providerMap = new HashMap<Class<Monitored>, Collection<Class<MonitoringTask<? extends Monitored>>>>();
		this.monitoredWIthDefaultConstractor = new ArrayList<Class<Monitored>>(); 
		
		autoscanMonitored();
		autoscanMonitoringTasks();
		filterMonitoredClasses();
	}

	protected void autoscanMonitored() throws ClassNotFoundException {
		LOGGER.debug("Auto Scanning {} for Monitored classes", this.scannedPackage);
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AssignableTypeFilter(Monitored.class));
		for (BeanDefinition bd : scanner.findCandidateComponents(this.scannedPackage)) {
			String className = bd.getBeanClassName();
			LOGGER.debug("Found Monitored class {}", className);
			@SuppressWarnings("unchecked")
			//This cast is safe
			Class<Monitored> clas = (Class<Monitored>)Class.forName(className);
			if (!Modifier.isAbstract(clas.getModifiers()) && !Modifier.isInterface(clas.getModifiers())) {
				if (!this.providerMap.containsKey(clas)) {
					this.providerMap.put(clas, new ArrayList<Class<MonitoringTask<? extends Monitored>>>());
				}
			} else {
				LOGGER.warn("Igonring class {}, it's abstract or interface", className);
			}
		}
	}

	protected void autoscanMonitoringTasks() throws ClassNotFoundException {
		LOGGER.debug("Auto Scanning {} for MonitoringTask classes", this.scannedPackage);
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AssignableTypeFilter(MonitoringTask.class));
		for (BeanDefinition bd : scanner.findCandidateComponents(this.scannedPackage)) {
			String className = bd.getBeanClassName();
			LOGGER.debug("Found MonitoringTask class {}", className);
			@SuppressWarnings("unchecked")
			//This cast is safe
			Class<MonitoringTask<? extends Monitored>> clas = (Class<MonitoringTask<? extends Monitored>>)Class.forName(className);
			if (!Modifier.isAbstract(clas.getModifiers()) && !Modifier.isInterface(clas.getModifiers())) {
				Method getMonitoredMethod = null;
				try {
					getMonitoredMethod = clas.getMethod("getMonitored");
				} catch (NoSuchMethodException | SecurityException e) {
					LOGGER.warn("Couldn't locate getMonitored method, skipping class {}", clas.getName());
				}
				if (getMonitoredMethod != null) {
					@SuppressWarnings("unchecked")
					//This case is safe
					Class<? extends Monitored> monitoredType = (Class<? extends Monitored>)getMonitoredMethod.getReturnType();
					addMonitoringTaskToMonitored(clas, monitoredType);
				}
			} else {
				LOGGER.warn("Igonring class {}, it's abstract or interface", className);
			}
		}
	}

	private void addMonitoringTaskToMonitored(Class<MonitoringTask<? extends Monitored>> monitoringTask, Class<? extends Monitored> monitoredType) {
		if (this.providerMap.containsKey(monitoredType)) {
			Collection<Class<MonitoringTask<? extends Monitored>>> currentClassMonitoringTasks = this.providerMap.get(monitoredType);
			currentClassMonitoringTasks.add(monitoringTask);
		} else {
			boolean found = false;
			for (Class<Monitored> monitored : this.providerMap.keySet()) {
				if (monitoredType.isAssignableFrom(monitored)) {
					found = true;
					this.providerMap.get(monitored).add(monitoringTask);
				}
			}
			if (!found) {
				LOGGER.warn("Couldn't locate Monitored class {} in the registry, no implementations for that class, skipping class {}", new Object[] { monitoredType, monitoringTask });
			}
		}
	}

	@Override
	public Collection<Class<Monitored>> getAllMonitoredClasses() {
		return Collections.unmodifiableCollection(this.providerMap.keySet());
	}

	//That was the only way to satisfy the signature
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T extends Monitored> Collection<Class<MonitoringTask<T>>> getAllMonitoringTaskClasses(Class<T> monitoredClass) {
		Collection monitoredMonitoringTaskClasses = this.providerMap.get(monitoredClass);
		return Collections.unmodifiableCollection(monitoredMonitoringTaskClasses);
	}
	//if Monitored class doesn't have a default constructor remove it from Vaadin view create
	private void filterMonitoredClasses(){
		
		for (Class<Monitored> monitored : this.providerMap.keySet()) {

			//try to create instance, if failed there is no default constractor
			try {
				monitored.newInstance();
			} catch (Exception e) {
				LOGGER.warn("Couldn't locate default constractor ,skipping class. Either add default constractor or add manually bean to the context {}", new Object[] { monitored });
				continue;
			}
			
			this.monitoredWIthDefaultConstractor.add(monitored);
		}
	}
	
	@Override
	public <T extends Monitored> Collection<Class<Monitored>> getFilteredMonitoringClasses() {
		return this.monitoredWIthDefaultConstractor;
	}

}
