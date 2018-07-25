
package com.xxx.rh.sms.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Globals implements ApplicationContextAware {

	private static ApplicationContext context;

	public static ApplicationContext getContext() {
		return context;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static Object getBean(String name) {
		if (getContext().containsBean(name)) {
			return getContext().getBean(name);
		} else {
			return null;
		}
	}
}
