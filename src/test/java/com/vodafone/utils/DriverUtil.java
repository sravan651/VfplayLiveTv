package com.vodafone.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

/**
 * This DriverUtil class refer to browsers, os details, browser versions and
 * will close all browsers
 *
 */

public class DriverUtil extends GlobalUtil {

	public static int row;
	public static String NDevice_Name = null;
	public static String ODevice_Name;
	public static String username;

	public static final String IE = "IE";
	public static final String REMOTE = "Remote";
	public static final String EDGE = "edge";
	public static final String CHROME = "Chrome";
	public static final String FIREFOX = "Firefox";
	private static Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();
	// public static AndroidDriver<MobileElement> Mdriver;

	public static DesiredCapabilities capabilities = new DesiredCapabilities();

	public static XSSFWorkbook wb;
	public static XSSFSheet sheet1;

	public static void closeAllDriver() {

		drivers.entrySet().forEach(key -> {
			key.getValue().quit();
			key.setValue(null);
		});

		LogUtil.infoLog(DriverUtil.class, "Closing Browsers");
	}

	public class DriverManager {
		private ThreadLocal<AppiumDriver<?>> driver = new ThreadLocal<>();

		public AppiumDriver<?> getDriver() {
			return driver.get();
		}

		public void setDriver(AppiumDriver<?> driver2) {
			driver.set(driver2);
		}

		public void initializeDriver() throws Exception {
			AppiumDriver<?> driver = null;
			GlobalParams params = new GlobalParams();

			if (driver == null) {
				try {
					switch (params.getPlatformName()) {
					case "Android":
						driver = new AndroidDriver<>(new ServerManager().getServer().getUrl(),
								new CapabilitiesManager().getCaps());
						break;
					case "iOS":
						driver = new IOSDriver<>(new ServerManager().getServer().getUrl(),
								new CapabilitiesManager().getCaps());
						break;
					}
					if (driver == null) {
						throw new Exception("mdriver is null. ABORT!!!");
					}
					mdriver.set(driver);
				} catch (IOException e) {
					e.printStackTrace();
					// utils.log().fatal("Driver initialization failure. ABORT !!!!" +
					// e.toString());
					throw e;
				}
			}
		}
	}

	public AppiumDriver<?> getMDriver() {
		return mdriver.get();
	}

	public AndroidDriver<?> getADriver() {
		return (AndroidDriver<?>) mdriver.get();
	}
}
