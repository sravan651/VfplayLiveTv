package com.vodafone.utils;

import java.io.File;
import java.util.HashMap;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class ServerManager {
	private static ThreadLocal<AppiumDriverLocalService> server = new ThreadLocal<>();

	public AppiumDriverLocalService getServer() {
		return server.get();
	}

	@SuppressWarnings("static-access")
	public void startServer(int port) {
		System.out.println("starting appium server");
		// utils.log().info("starting appium server");
		AppiumDriverLocalService server = System.getProperty("os.name").contains("Windows")
				? WindowsGetAppiumService(port)
				: MacGetAppiumService(port);
		server.start();
		if (server == null || !server.isRunning()) {
			System.out.println("Appium server not started. ABORT!!!");
			throw new AppiumServerHasNotBeenStartedLocallyException("Appium server not started. ABORT!!!");
		}
		server.clearOutPutStreams();
		this.server.set(server);
		System.out.println("Appium Server started");
	}

	public AppiumDriverLocalService getAppiumServerDefault() {
		return AppiumDriverLocalService.buildDefaultService();
	}

	public AppiumDriverLocalService WindowsGetAppiumService(int port) {
		GlobalParams params = new GlobalParams();
		return AppiumDriverLocalService.buildService(
				new AppiumServiceBuilder().usingPort(port).withArgument(GeneralServerFlag.SESSION_OVERRIDE).withLogFile(
						new File("AppiumLogs/" + params.getPlatformName() + "_" + params.getDeviceName() + ".log")));
	}

	public AppiumDriverLocalService MacGetAppiumService(int port) {
		GlobalParams params = new GlobalParams();
		HashMap<String, String> environment = new HashMap<>();
		environment.put("PATH",
				"/Library/Java/JavaVirtualMachines/jdk1.8.0_161.jdk/Contents/Home/bin:/Users/testing/Library/Android/sdk/tools:/Users/testing/Library/Android/sdk/platform-tools:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
						+ System.getenv("PATH"));
		environment.put("ANDROID_HOME", "/Users/testing/Library/Android/sdk");
		environment.put("JAVA_HOME", "/Library/Java/JavaVirtualMachines/jdk1.8.0_161.jdk/Contents/Home");
		return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
				.usingDriverExecutable(new File("/usr/local/bin/node"))
				.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js")).usingPort(port)
				.withArgument(GeneralServerFlag.SESSION_OVERRIDE).withEnvironment(environment).withLogFile(
						new File("AppiumLogs/" + params.getPlatformName() + "_" + params.getDeviceName() + ".log")));
	}

	public void stopServer() {
		getServer().stop();
	}
}
