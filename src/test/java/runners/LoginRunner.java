package runners;

import java.util.Date;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.vodafone.utils.ConfigReader;
import com.vodafone.utils.DriverUtil;
import com.vodafone.utils.ExcelDataUtil;
import com.vodafone.utils.ExtReport;
import com.vodafone.utils.GlobalParams;
import com.vodafone.utils.GlobalUtil;
import com.vodafone.utils.KeywordUtil;
import com.vodafone.utils.LogUtil;
import com.vodafone.utils.MobileKeywords;
import com.vodafone.utils.ServerManager;
import com.vodafone.utils.VideoRecorder;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import cucumber.api.testng.TestNGCucumberRunner;

@CucumberOptions(features = "classpath:features/Vodafone.feature", glue = "com.vodafone.stepdefs.android", plugin = {
		"pretty", "html:target/cucumber-html-report", "json:target/cucumber.json",
		"rerun:target/rerunlogin.txt" }, tags = "@MobileTest")
public class LoginRunner extends AbstractTestNGCucumberTests {
	private static final ThreadLocal<TestNGCucumberRunner> testNGCucumberRunner = new ThreadLocal<>();
	public static ConfigReader config = new ConfigReader();
	GlobalParams params = new GlobalParams();

	public static TestNGCucumberRunner getRunner() {
		return testNGCucumberRunner.get();
	}

	protected static void setRunner(TestNGCucumberRunner testNGCucumberRunner1) {
		testNGCucumberRunner.set(testNGCucumberRunner1);
	}

	@BeforeSuite
	public void beforeSuite() {
		ExtReport.setReport(this.getClass().getSimpleName());
	}

	@BeforeTest
	public void beforeTest(ITestContext itc) {
		System.setProperty("testName", itc.getName());
	}

	@Parameters({ "platformName", "udid", "deviceName", "systemPort", "chromeDriverPort", "wdaLocalPort",
			"webkitDebugProxyPort", "appiumPort", "tdRangeStart", "tdRangeEnd" })
	@BeforeClass
	public void onStart(String platformName, String udid, String deviceName, @Optional("Android") String systemPort,
			@Optional("Android") String chromeDriverPort, @Optional("iOS") String wdaLocalPort,
			@Optional("iOS") String webkitDebugProxyPort, String appiumPort, String tdRangeStart, String tdRangeEnd) {
		System.out.println(System.getProperty("testName"));
		System.setProperty(System.getProperty("testName") + "Start", tdRangeStart);
		System.setProperty(System.getProperty("testName") + "End", tdRangeEnd);
		MobileKeywords mk = new MobileKeywords();
		try {
			if (platformName == "")
				platformName = System.getProperty("platformName");
			if (udid == "")
				udid = System.getProperty("udid");
			if (deviceName == "")
				deviceName = System.getProperty("deviceName");
			if (appiumPort == "")
				appiumPort = System.getProperty("appiumPort");

			params.setPlatformName(platformName);
			params.setUDID(udid);
			params.setDeviceName(deviceName);
			init();
			switch (platformName) {
			case "Android":
				params.setSystemPort(systemPort);
				params.setChromeDriverPort(chromeDriverPort);
				break;
			case "iOS":
				params.setWdaLocalPort(wdaLocalPort);
				params.setWebkitDebugProxyPort(webkitDebugProxyPort);
				break;
			}
			new ServerManager().startServer(Integer.parseInt(appiumPort));
			new DriverUtil().new DriverManager().initializeDriver();
			new VideoRecorder().startRecording();
			setRunner(new TestNGCucumberRunner(this.getClass()));
			mk.delay(3000);
			/*
			 * ExtTest.setTest(ExtReport.getReport().startTest("Launch"));
			 * ExtReport.getReport().endTest(ExtTest.getTest());
			 */
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.errorLog(getClass(), "Common Settings not properly set may not run the scripts properly");
		}
	}

	@AfterClass
	public void onFinish() {
		new VideoRecorder().stopRecording();
		new DriverUtil().getMDriver().quit();
		new ServerManager().stopServer();
		ExtReport.getReport().flush();
		new KeywordUtil().onExecutionFinish();

		LogUtil.infoLog(getClass(), " suite finished" + " at " + new Date());
		LogUtil.infoLog(getClass(),
				"\n\n+===========================================================================================================+");
		ExtReport.getReport().flush();
		DriverUtil.closeAllDriver();
	}

	@AfterSuite
	public void afterSuite() {
		ExtReport.getReport().close();
	}

	private void init() {
		// Get all the common setting from excel file that are required for
		GlobalUtil.setCommonSettings(new ExcelDataUtil().getCommonSettings());

		String browser = "";
		browser = GlobalUtil.getCommonSettings().getBrowser();

		String executionEnv = "";
		executionEnv = GlobalUtil.getCommonSettings().getExecutionEnv();

		String url = "";
		url = GlobalUtil.getCommonSettings().getUrl();

		if (browser == null)
			browser = config.getValue("defaultBrowser");

		if (executionEnv == null)
			executionEnv = config.getValue("defaultExecutionEnvironment");
		if (url == null) {
			url = config.getValue("BASE_URL");
			GlobalUtil.getCommonSettings().setUrl(url);
		}
	}
}