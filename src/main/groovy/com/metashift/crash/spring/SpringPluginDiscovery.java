package com.metashift.crash.spring;

import org.crsh.plugin.CRaSHPlugin;
import org.crsh.plugin.ServiceLoaderDiscovery;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpringPluginDiscovery extends ServiceLoaderDiscovery {

	private BeanFactory factory;

	public SpringPluginDiscovery(ClassLoader classLoader, BeanFactory factory)
	        throws NullPointerException {
		super(classLoader);
		this.factory = factory;
	}

	@SuppressWarnings("rawtypes")
    @Override
    public Iterable<CRaSHPlugin<?>> getPlugins() {
		List<CRaSHPlugin<?>> serviceAndSpringPlugins = new ArrayList<CRaSHPlugin<?>>();
		
		for (CRaSHPlugin<?> cRaSHPlugin : super.getPlugins()) {
	        serviceAndSpringPlugins.add(cRaSHPlugin);
        }
		
		if (factory instanceof ListableBeanFactory) {
            Collection<CRaSHPlugin> springPlugins = ((ListableBeanFactory)factory)
            .getBeansOfType(CRaSHPlugin.class).values();
			
			for (CRaSHPlugin cRaSHPlugin : springPlugins) {
	            serviceAndSpringPlugins.add(cRaSHPlugin);
            }
		}
		
		return serviceAndSpringPlugins;
    }

}
