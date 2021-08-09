package com.vodafone.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class HTMLReportUtil {

	static String html;

	public static String testFailMobileTakeScreenshot(String imagePath) {
		File src = ((TakesScreenshot) new DriverUtil().getMDriver()).getScreenshotAs(OutputType.FILE);
		File des = new File(imagePath);
		try {
			FileUtils.copyFile(src, des);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(des);
		return des.getAbsolutePath();
	}

	public static String failStringRedColor(String stepName) {
		html = "<span style='color:red'><b>" + stepName + "</b></span>";
		return html;
	}

	public static String passStringGreenColor(String stepName) {
		html = "<span style='color:#008000'><b>" + stepName + " - PASSED" + "</b></span>";
		return html;
	}
}
