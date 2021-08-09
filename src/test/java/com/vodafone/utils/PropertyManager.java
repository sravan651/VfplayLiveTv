package com.vodafone.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {
	private static Properties props = new Properties();

	public Properties getProps() throws IOException {
		InputStream is = null;
		String propsFileName = "mobileprops.properties";

		if (props.isEmpty()) {
			try {
				is = getClass().getClassLoader().getResourceAsStream(propsFileName);
				props.load(is);
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (is != null) {
					is.close();
				}
			}
		}
		return props;
	}
}
