package org.shepherd.monitored.provider;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shepherd.monitored.AbstractMonitoringTest;
import org.shepherd.monitored.metadata.FieldMetadata;
import org.shepherd.monitored.service.MetadataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/bootstrap/bootstrap.xml", "classpath:/META-INF/monitored-provider-test-context.xml" })
public class MetadataMonitoredProviderTest<K,V> extends AbstractMonitoringTest {

	@Autowired
	private MetadataProvider<K, V> metadataProvider;

	@Test
	public void testDefaultMonitoredProvider() {
		Collection<FieldMetadata<V>> allMetadata = this.metadataProvider.getAllMetadata();
		Assert.assertEquals(1, allMetadata.size());
	}

}
