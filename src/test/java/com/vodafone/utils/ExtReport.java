package com.vodafone.utils;

import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;

import com.relevantcodes.extentreports.ExtentReports;

public class ExtReport {
	public static Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
	public static long time = cal.getTimeInMillis();
	public static ConfigReader config = new ConfigReader();
	static GlobalParams params = new GlobalParams();
	public static ExtentReports Report;
	public static String htmlReportFilePath;

	public synchronized static void setReport(String className) {
		htmlReportFilePath = System.getProperty("user.dir") + config.getValue("extentReportPath") + File.separator
				+ className.split("Runner")[0] + File.separator + "extentReport.html";
		Report = new ExtentReports(htmlReportFilePath);
		Report.addSystemInfo("Host Name", "TestMachine").addSystemInfo("Environment", "Automation Testing")
				.addSystemInfo("User Name", System.getProperty("user.name"));
		Report.loadConfig(new File(System.getProperty("user.dir") + config.getValue("extentConfigPath")));

	}

	public synchronized static ExtentReports getReport() {
		return Report;
	}
}