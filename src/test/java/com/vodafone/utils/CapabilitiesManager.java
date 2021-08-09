package com.vodafone.utils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.remote.MobileCapabilityType;

public class CapabilitiesManager {

	public DesiredCapabilities getCaps() throws IOException {
		GlobalParams params = new GlobalParams();
		Properties props = new PropertyManager().getProps();

		try {
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability(MobileCapabilityType.PLATFORM_NAME, params.getPlatformName());
			caps.setCapability(MobileCapabilityType.UDID, params.getUDID());
			caps.setCapability(MobileCapabilityType.DEVICE_NAME, params.getDeviceName());

			switch (params.getPlatformName()) {
			case "Android":
				caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, props.getProperty("androidAutomationName"));
				// caps.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
				// caps.setCapability("chromedriverExecutableDir",
				// System.getProperty("user.dir") + "/lib/");
				caps.setCapability("appPackage", props.getProperty("androidAppPackage"));
				caps.setCapability("appActivity", props.getProperty("androidAppActivity"));
				caps.setCapability("systemPort", params.getSystemPort());
				caps.setCapability("chromeDriverPort", params.getChromeDriverPort());
				// String androidAppUrl = System.getProperty("user.dir") + File.separator +
				// "src" + File.separator + "test"
				// + File.separator + "resources" + File.separator + "apps" + File.separator
				// + "Mobile.Sample.app.apk";
				// utils.log().info("appUrl is" + androidAppUrl);
				// caps.setCapability("app", androidAppUrl);
				break;
			case "iOS":
				// String iOSAppUrl =
				// getClass().getResource(props.getProperty("iOSAppLocation")).getFile();
				String iOSAppUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
						+ File.separator + "resources" + File.separator + "apps" + File.separator
						+ "SwagLabsMobileApp.app";
				// utils.log().info("appUrl is" + iOSAppUrl);
				caps.setCapability("bundleId", props.getProperty("iOSBundleId"));
				caps.setCapability("wdaLocalPort", params.getWdaLocalPort());
				caps.setCapability("webkitDebugProxyPort", params.getWebkitDebugProxyPort());
				caps.setCapability("app", iOSAppUrl);
				break;
			}
			return caps;
		} catch (Exception e) {
			e.printStackTrace();
			// utils.log().fatal("Failed to load capabilities. ABORT!!" + e.toString());
			throw e;
		}
	}
}
