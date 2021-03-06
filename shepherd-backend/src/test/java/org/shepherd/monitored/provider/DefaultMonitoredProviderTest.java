package org.shepherd.monitored.provider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shepherd.monitored.AbstractMonitoringTest;
import org.shepherd.monitored.Monitored;
import org.shepherd.monitored.MonitoringTask;
import org.shepherd.monitored.process.jmx.JmxProcessImpl;
import org.shepherd.monitored.process.jmx.task.expression.ExpressionJmxTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/bootstrap/bootstrap.xml", "classpath:/META-INF/monitored-provider-test-context.xml" })
public class DefaultMonitoredProviderTest extends AbstractMonitoringTest {

	@Autowired
	private MonitoredProvider monitoredProvider;

	@Test
	public void testDefaultMonitoredProvider() {
		Collection<Class<Monitored>> allMonitoredClasses = this.monitoredProvider.getAllMonitoredClasses();
		Assert.assertEquals(2, allMonitoredClasses.size());
		Collection<Class<MonitoringTask<JmxProcessImpl>>> allMonitoringTaskClasses = this.monitoredProvider.getAllMonitoringTaskClasses(JmxProcessImpl.class);
		Assert.assertEquals(1, allMonitoringTaskClasses.size());
		Assert.assertEquals(ExpressionJmxTask.class, allMonitoringTaskClasses.iterator().next());
	}
	@Test
	public void testDefaultMonitoredProviderFiltering() {
		Collection<Class<Monitored>> filtered = this.monitoredProvider.getAllMonitoredClasses();
		Assert.assertEquals(2, filtered.size());
	}

}
