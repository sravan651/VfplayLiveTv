package com.vodafone.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class MobileKeywords extends KeywordUtil {
	public Class<MobileKeywords> thisClass = MobileKeywords.class;
	public Dimension size;
	private static final int DEFAULT_WAIT_SECONDS = 20;
	public int fail = 0;
	static WebElement webElement;
	public String URL = "";
	public boolean flag1 = false;
	public String logging_step;
	static int row = 1;
	static Logger errorLogger;
	static Logger normalLogger;
	static FileAppender normalFileApp;
	static FileAppender errorFileApp;
	static ConsoleAppender conApp;
	public boolean isInit = false;
	public ExtentTest test = null;
	private static ExtentTest testHist = null;
	static PatternLayout patternLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p - %m%n");
	static PatternLayout consolePatternLayout = new PatternLayout("\tLOG-: [%m -  %d{yyyy-MM-dd HH:mm:ss a}] %n");

	public AndroidDriver<MobileElement> MobileDriver;

	public boolean isDisplayed;

	public DesiredCapabilities capabilities = new DesiredCapabilities();

	public String getValue(String key) {
		File file = new File(System.getProperty("user.dir") + "/src/main/resources/Config/config.properties");
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Properties prop = new Properties();

		try {
			prop.load(fileInput);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String strbaseURL = prop.getProperty(key);
		return strbaseURL;
	}

	public int getIntValue(String key) {
		File file = new File(System.getProperty("user.dir") + "/src/main/resources/Config/config.properties");

		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Properties prop = new Properties();

		try {
			prop.load(fileInput);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String strbaseURL = prop.getProperty(key);
		return Integer.parseInt(strbaseURL);
	}

	public By locatortype(String type, String value) {

		By locName = null;
		if (type.equalsIgnoreCase("xpath")) {
			locName = By.xpath(value);
		} else if (type.equalsIgnoreCase("id")) {
			locName = By.id(value);
		} else if (type.equalsIgnoreCase("linkText")) {
			locName = By.linkText(value);
		} else if (type.equalsIgnoreCase("classname")) {
			locName = By.className(value);
		} else if (type.equalsIgnoreCase("name")) {
			locName = By.name(value);
		} else
			locName = By.partialLinkText(value);
		return locName;

	}

	public boolean isWebElementPresent(String path, String type) {
		Boolean flag = false;
		if (getMDriver().findElements(MobileBy.xpath(path)).size() > 0) {
			flag = true;
		}
		return flag;
	}

	public boolean isWebElementVisible(String path, String type) {
		Boolean flag = false;
		if (getMDriver().findElements(locatortype(type, path)).size() > 0) {
			flag = true;
			// LogUtil.infoLog(thisClass, "Element Visible");
		}

		return flag;

	}

	public boolean isWebElementNotPresent(String path, String type) {

		@SuppressWarnings("unchecked")
		List<AndroidElement> elements = (List<AndroidElement>) getMDriver().findElements(locatortype(type, path));

		if (elements.size() > 0) {
			System.out.println("Element Present");
			LogUtil.infoLog(thisClass, "Element present");
			return false;
		} else {
			System.out.println("Element is not Present");
			LogUtil.infoLog(thisClass, "Element Not present");

			return true;
		}
	}

	public boolean writeInInput(String path, String type, String data, String logstep) {
		// driver1=(Appiumdriver1<MobileElement>) GlobalUtil.getMdriver1();
		WebElement element = getMDriver().findElement(locatortype(type, path));

		// element.clear();
		element.sendKeys(data);

		System.out.println("Value Entered");
		ExtTest.getTest().log(LogStatus.PASS, logstep);
		LogUtil.infoLog(thisClass, "Test Data entered successfully");
		return true;

	}

	public boolean setValue(String path, String type, String data, String logstep) {
		MobileElement element = (MobileElement) getMDriver().findElement(locatortype(type, path));
		element.sendKeys(data);
		System.out.println("Value Entered");
		ExtTest.getTest().log(LogStatus.PASS, logstep);

		return true;

	}

	public boolean enter() throws IOException {
		Runtime.getRuntime().exec("adb shell input keyevent 66");
		return true;

	}

	public boolean clearAppData() throws IOException {
		Runtime.getRuntime().exec("adb shell pm clear com.cloudfmgroup.cloudFMApp");
		return true;

	}

	public boolean swipeIOS(String path, String type, String data) {
		MobileElement element = (MobileElement) getMDriver().findElement(locatortype(type, path));
		element.setValue(data);

		System.out.println("Value Entered");
		return true;

	}

	public boolean enterInput(String path, String type, String data, String logstep) {
		WebElement element = getMDriver().findElement(locatortype(type, path));

		((JavascriptExecutor) getMDriver()).executeScript("arguments[0].value = arguments[1]", element, data);

		System.out.println("Value Entered");
		ExtTest.getTest().log(LogStatus.PASS, logstep);

		return true;

	}

	public WebElement explicitWaitForElement(String path, String type) {
		WebDriverWait wait = new WebDriverWait(getMDriver(), getIntValue("explicit_timeout"));

		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locatortype(type, path)));

		return element;

	}

	public WebElement explicitWaitForElementfor20(String path, String type) {
		WebDriverWait wait = new WebDriverWait(getMDriver(), 15);

		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locatortype(type, path)));

		return element;
	}

	public void clickByAcID(String accessibilityId, String logMsg) {
		new WebDriverWait(getMDriver(), 20)
				.until(ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId(accessibilityId))).click();
		ExtTest.getTest().log(LogStatus.PASS, logMsg);
	}

	public void gettextAlert() {

		Alert alert = getMDriver().switchTo().alert();
		alert.getText();

	}

	public void closeapp() {
		getMDriver().closeApp();
		;
	}

	public void back() {
		executeAndroidKeyCodes(AndroidKey.BACK);
	//	getMDriver().navigate().back();
	}

	public void clickUsingCoordinates(int xcordinate, int ycordinate) {
		TouchAction<?> actions = new TouchAction<>(getMDriver());
		actions.tap(PointOption.point(xcordinate, ycordinate)).release().perform();

	}

	public void refresh(String logStep) {
		GlobalUtil.getDriver().navigate().refresh();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		ExtTest.getTest().log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

	}

	public void isElementDisplayed(By locator, String logStep) {
		WebDriverWait wait = new WebDriverWait(getMDriver(), 20);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			ExtTest.getTest().log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
		} catch (TimeoutException e) {
			ExtTest.getTest().log(LogStatus.FAIL, HTMLReportUtil.failStringRedColor(logStep));
		} catch (org.openqa.selenium.NoSuchElementException e) {
			ExtTest.getTest().log(LogStatus.FAIL, HTMLReportUtil.failStringRedColor(logStep));
		}
	}

	// mobile
	public void click(By locator, String logStep) {
		WebDriverWait wait = new WebDriverWait(getMDriver(), 20);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		element.click();

		KeywordUtil.lastAction = "Find Element: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		ExtTest.getTest().log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
	}

	public void click_New(WebElement ele, String logStep) {
		WebDriverWait wait = new WebDriverWait(getMDriver(), 20);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(ele));
		element.click();

		KeywordUtil.lastAction = "Find Element: " + ele.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		ExtTest.getTest().log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
	}

	public void swipeHorizontal_FindElementClick(int howManySwipes, By locator, String logStep) {
		Dimension size = getMDriver().manage().window().getSize();
		int startX = (int) (size.width * 0.70);
		// int endX = (int) (size.width * 0.20);
		int startY = (size.height / 2);
		delay(2000);
		try {
			for (int i = 1; i <= howManySwipes; i++) {
				boolean isElmPresent = getMDriver().findElements(locator).size() > 0;
				if (isElmPresent) {
					getMDriver().findElement(locator);
					break;
				}
				TouchAction<?> actions = new TouchAction<>(getMDriver());
				actions.longPress(PointOption.point(startX, startY)).moveTo(PointOption.point(startY, startY)).release()
						.perform();
				System.out.println("swipeUp");
				LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
				ExtTest.getTest().log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void swipeUp_FindElementClick(int howManySwipes, By locator, String logStep) {
		Dimension size = getMDriver().manage().window().getSize();
		int startY = (int) (size.height * 0.70);
		int endY = (int) (size.height * 0.20);
		int startX = (size.width / 2);
		delay(400);
		try {
			for (int i = 1; i <= howManySwipes; i++) {
				boolean isElmPresent = getMDriver().findElements(locator).size() > 0;
				if (isElmPresent) {
					getMDriver().findElement(locator);
					break;
				}
				TouchAction<?> actions = new TouchAction<>(getMDriver());
				actions.longPress(PointOption.point(startX, startY)).moveTo(PointOption.point(startX, endY)).release()
						.perform();
				System.out.println("swipeUp");
				LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
				ExtTest.getTest().log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// jojo

	public void horizontalSwipeWRTOelment(String xpath, int swipes, int startx, int endx, String logStep) {

		int y = getMDriver().findElementByXPath(xpath).getLocation().getY();
		int h = getMDriver().findElementByXPath(xpath).getRect().getHeight();

		size = getMDriver().manage().window().getSize();
		int starty2 = y + h / 2;
		try {
			for (int i = 1; i <= swipes; i++) {
				TouchAction<?> actions = new TouchAction<>(getMDriver());
				actions.longPress(PointOption.point(startx, starty2)).moveTo(PointOption.point(endx, starty2)).release()
						.perform();
				System.out.println("swipeHorizontal");
				LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
				ExtTest.getTest().log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}

	public String generateRandomEmail() {
		String emailBody = "abcdefghijklmnopqrstuvwxyz1234567890";
		StringBuilder emailFL = new StringBuilder();
		Random rnd = new Random();
		while (emailFL.length() < 6) {
			int index = (int) (rnd.nextFloat() * emailBody.length());
			emailFL.append(emailBody.charAt(index));
		}
		String email = "autoTest" + emailFL.toString() + "@g.com";
		return email;
	}

	public String generateRandomMobile() {
		String mobileNum = "1234567890";
		StringBuilder mobile = new StringBuilder();
		Random rnd = new Random();
		while (mobile.length() < 10) {
			int index = (int) (rnd.nextFloat() * mobileNum.length());
			mobile.append(mobileNum.charAt(index));
		}
		return mobile.toString();
	}

	public void HorizontalSwipe(int howManySwipes) {
		Dimension size = getMDriver().manage().window().getSize();

		int startX = (int) (size.height * 0.1);
		int endX = (int) (size.height * 0.8);
		int startY = (size.width / 2);
		try {
			for (int i = 1; i <= howManySwipes; i++) {
				TouchAction<?> actions = new TouchAction<>(getMDriver());
				actions.longPress(PointOption.point(startX, startY)).moveTo(PointOption.point(endX, startY)).release()
						.perform();
				System.out.println("swipeHorizontal");
			}
		} catch (Exception e)

		{
			System.out.println(e.getMessage());

		}

	}

	public void swipeUp(int howManySwipes) {
		Dimension size = getMDriver().manage().window().getSize();
		// calculate coordinates for vertical swipe
		int startY = (int) (size.height * 0.7);
		int endY = (int) (size.height * 0.1);
		int startX = (size.width / 2);
		try {
			for (int i = 1; i <= howManySwipes; i++) {
				TouchAction<?> actions = new TouchAction<>(getMDriver());
				actions.longPress(PointOption.point(startX, startY)).moveTo(PointOption.point(startX, endY)).release()
						.perform();
				System.out.println("swipeUp");

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}

	public void swipeUp1(int howManySwipes) {
		Dimension size = getMDriver().manage().window().getSize();
		// calculate coordinates for vertical swipe
		int startY = (int) (size.height * 0.3);
		int endY = (int) (size.height * 0.1);
		int startX = (size.width / 2);
		try {
			for (int i = 1; i <= howManySwipes; i++) {
				TouchAction<?> actions = new TouchAction<>(getMDriver());
				actions.longPress(PointOption.point(startX, startY)).moveTo(PointOption.point(startX, endY)).release()
						.perform();
				System.out.println("swipeUp");

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}

	// jojo
	public void swipeUp(int howManySwipes, String logStep) {
		Dimension size = getMDriver().manage().window().getSize();
		// calculate coordinates for vertical swipe
		int startY = (int) (size.height * 0.7);
		int endY = (int) (size.height * 0.1);
		int startX = (size.width / 2);
		try {
			for (int i = 1; i <= howManySwipes; i++) {
				TouchAction<?> actions = new TouchAction<>(getMDriver());
				actions.press(PointOption.point(startX, startY)).moveTo(PointOption.point(startX, endY)).release()
						.perform();
				LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
				ExtTest.getTest().log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}

	public void swipeUpWithPercentages(int howManySwipes, double fromPct, double toPct, String logStep) {
		Dimension size = getMDriver().manage().window().getSize();
		int startY = (int) (size.height * fromPct);
		int endY = (int) (size.height * toPct);
		int startX = (size.width / 2);
		try {
			for (int i = 1; i <= howManySwipes; i++) {
				TouchAction<?> actions = new TouchAction<>(getMDriver());
				actions.press(PointOption.point(startX, startY)).moveTo(PointOption.point(startX, endY)).release()
						.perform();
				LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
				ExtTest.getTest().log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}

	public void landscapemode(String logStep) {
		String x = getMDriver().getOrientation().name();
		System.out.println(x);
		if (x.equalsIgnoreCase("portrait")) {
			getMDriver().rotate(ScreenOrientation.LANDSCAPE);
			LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
			ExtTest.getTest().log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
		}
	}

	/**
	 * 
	 * This method use to execute AndroidKeyCodes on the Device Under Test. <br>
	 * <b>Usage example:</b> <br>
	 * <code>executeAndroidKeyCodes(AndroidKey.VOLUME_DOWN)</code>
	 * 
	 * @param ${AndroidKey}
	 *            key
	 */
	public void executeAndroidKeyCodes(AndroidKey key) {
		getADriver().pressKey(new KeyEvent(key));
	}

	/**
	 * This method use to set orientation of the Device Under Test. <br>
	 * <b>Usage:</b> <br>
	 * <code>setOrientation(ScreenOrientation.LANDSCAPE);</code> <br>
	 * or<br>
	 * <code>setOrientation(ScreenOrientation.PORTRAIT);</code>
	 * 
	 * @param orientation
	 */
	public void setOrientation(ScreenOrientation orientation) {
		getMDriver().rotate(orientation);
	}

	public void swipeDown(int howManySwipes, String logStep) {
		Dimension size = getMDriver().manage().window().getSize();
		int startY = (int) (size.height * 0.7);
		int endY = (int) (size.height * 0.3);
		int startX = (size.width / 2);
		try {
			for (int i = 1; i <= howManySwipes; i++) {
				TouchAction<?> actions = new TouchAction<>(getMDriver());
				actions.longPress(PointOption.point(startX, endY)).moveTo(PointOption.point(startX, startY)).release()
						.perform();
				System.out.println("swipeDown");
				LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
				ExtTest.getTest().log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean isWebElementNotPresent(By locator) {
		KeywordUtil.lastAction = "Check Element not present: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		try {
			List<WebElement> elements = (new WebDriverWait(getMDriver(), DEFAULT_WAIT_SECONDS))
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
			if (elements.isEmpty()) {
				return true;
			}
		} catch (NoSuchElementException | TimeoutException e) {
			return true;
		}
		return false;
	}

	public boolean isWebElementPresent(By locator) {
		List<WebElement> elements = (new WebDriverWait(getMDriver(), DEFAULT_WAIT_SECONDS))
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
		if (elements.isEmpty()) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public void deactivateDevices(By locator, By locator1) {
		List<WebElement> l = (List<WebElement>) getMDriver().findElements(locator);
		System.out.println(l.size());
		for (int i = 0; i < l.size(); i++) {
			if (getMDriver().findElement(locator).isDisplayed()) {
				getMDriver().findElement(locator).click();
				getMDriver().findElement(locator1).click();
			} else {
				break;
			}
		}
	}

	public void getData(String data) {
		String x = KeywordUtil.lastAction = data;
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		System.out.println(x);
		ExtTest.getTest().log(LogStatus.PASS, "" + x);

	}

	public WebElement waitForClickable(By locator) {
		WebDriverWait wait = new WebDriverWait(getMDriver(), DEFAULT_WAIT_SECONDS);
		wait.ignoring(ElementNotVisibleException.class);
		wait.ignoring(WebDriverException.class);

		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public String getText(By locator) {
		lastAction = "Get Element text: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, lastAction);
		WebElement elm = waitForClickable(locator);
		return elm.getText();
	}

	public void stepInfo(String stepName) {
		test.log(LogStatus.PASS, stepName);
		testHist.log(LogStatus.PASS, stepName);

	}

	public void executionDelay(long time) {

		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

	public boolean verifyAllVauesOfDropDown(String path, String type, String data) {

		boolean flag = false;
		WebElement element = explicitWaitForElement(path, type);
		List<WebElement> options = element.findElements(By.tagName("option"));
		String temp = data;
		String allElements[] = temp.split(",");
		String actual;
		for (int i = 0; i < allElements.length; i++) {

			System.out.println("Actual : " + options.get(i).getText());

			System.out.println("Expected: " + allElements[i].trim());
			actual = options.get(i).getText().trim();
			if (actual.equals(allElements[i].trim())) {
				flag = true;
			} else {
				flag = false;
				break;
			}
		}

		return flag;

	}

	public boolean verifyCurrentDateInput(String path, String type) {
		boolean flag = false;
		WebElement element = explicitWaitForElement(path, type);
		String actual = element.getAttribute("value").trim();
		DateFormat DtFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		DtFormat.setTimeZone(TimeZone.getTimeZone("US/Central"));
		String expected = DtFormat.format(date).toString().trim();
		if (actual.trim().contains(expected)) {
			flag = true;

		}
		return flag;

	}

	public Boolean validateNotesInput(String path, String type, String errorMessage) {
		Boolean flag = false;

		WebElement element = explicitWaitForElement(path, type);
		String pattern[] = { "<", ">", "(", ")", "'", "\\" };
		for (int i = 0; i < pattern.length; i++) {
			element.clear();
			element.sendKeys(pattern[i]);
			flag = isWebElementPresent(errorMessage, type);

			if (!flag) {
				break;
			}

		}

		return flag;

	}

	public boolean selectList(final String path, String type, String data, String logstep) {

		Boolean flag = false;

		WebElement select = explicitWaitForElement(path, type);

		List<WebElement> options = select.findElements(By.tagName("option"));
		String expected = data.trim();
		System.out.println("Expected: " + expected);
		for (WebElement option : options) {

			String actual = option.getText().trim();
			System.out.println("Actual: " + actual);
			if (actual.equals(expected)) {

				option.click();
				ExtTest.getTest().log(LogStatus.PASS, logstep);

				flag = true;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return flag;
			}
		}

		return flag;
	}

	public boolean verifyDropdownSelectedValue(String path, String type, String data) {

		Boolean flag = false;
		WebElement select = explicitWaitForElement(path, type);

		Select sel = new Select(select);
		String defSelectedVal = sel.getFirstSelectedOption().getText();

		if (defSelectedVal.trim().equals(data.trim())) {
			flag = true;

			return flag;
		} else {
			return flag;
		}
	}

	public boolean verifyElementSize(String path, String type, int size) {

		@SuppressWarnings("unchecked")
		List<AndroidElement> elements = (List<AndroidElement>) getMDriver().findElements(locatortype(type, path));

		if (elements.size() == size) {
			System.out.println("Element is Present " + size + "times");
			return true;
		} else {
			System.out.println("Element is not Present with required size");

			return false;
		}
	}

	public boolean uploadFilesUsingSendKeys(String path, String type, String data) throws InterruptedException {
		WebElement element = getMDriver().findElement(locatortype(type, path));
		element.clear();
		element.sendKeys(System.getProperty("user.dir") + "\\src\\test\\resources\\uploadFiles\\" + data);
		System.out.println("Value Entered");
		return true;

	}

	public boolean writeInInputCharByChar(String path, String type, String data, String logstep)
			throws InterruptedException {
		WebElement element = getMDriver().findElement(locatortype(type, path));
		element.clear();
		String b[] = data.split("");

		for (int i = 0; i < b.length; i++) {

			element.sendKeys(b[i]);
			Thread.sleep(1000);

		}
		System.out.println("Value Entered");
		ExtTest.getTest().log(LogStatus.PASS, logstep);

		return true;

	}

	public boolean isRadioSelected(String path, String type) {

		WebElement element = (explicitWaitForElement(path, type));
		if (element.isSelected()) {
			return true;
		} else {

			return false;
		}
	}

	public boolean isRadioNotSelected(String path, String type) {

		WebElement element = (explicitWaitForElement(path, type));
		if (element.isSelected()) {
			return false;
		} else {

			return true;
		}
	}

	public void stepPass(String stepName) {
		String html = "<span style='color:green'><b>" + stepName + "-PASS</b></span>";
		test.log(LogStatus.PASS, html);
		testHist.log(LogStatus.PASS, html);

	}

	public void stepFail(String stepName) {
		String html = "<span style='color:red'><b>" + stepName + "-FAIL</b></span>";
		test.log(LogStatus.INFO, html);
		testHist.log(LogStatus.INFO, html);

	}
	/*
	 * public void logResult(boolean status, String logStep ){ if (status) {
	 * infoLog(MobileKeywords.class, logStep + "-PASS "); stepPass(logStep); } else
	 * { infoLog(MobileKeywords.class, logStep + "-FAIL "); stepFail(logStep);
	 * 
	 * }
	 * 
	 * 
	 * }
	 */

	/*
	 * public boolean selectByIndex(String path, String type, Integer index){
	 * 
	 * boolean status = false; boolean elemPresent = isWebElementPresent(path,
	 * type); if(elemPresent){ WebElement elem =
	 * driver1.findElement(locatortype(type, path)); Select select = new
	 * Select(elem); select.selectByIndex(index); status = true; logResult(status,
	 * "Select action is performed !!!" ); return status; }else{ status = false;
	 * logResult(status, "No Select action performed !!!" ); return status; }
	 * 
	 * }
	 */

	public boolean clearInput(String path, String type) {
		WebElement element = getMDriver().findElement(locatortype(type, path));

		element.clear();

		System.out.println("input field cleared");
		return true;

	}

	public boolean delDirectory() {
		File delDestination = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\downloadFile");
		if (delDestination.exists()) {
			File[] files = delDestination.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					delDirectory();
				} else {
					files[i].delete();
				}
			}
		}
		return (delDestination.delete());
	}

	public boolean verifyCssProperty(String path, String type, String data) {

		String property[] = data.split(":", 2);
		String exp_prop = property[0];
		String exp_value = property[1];
		boolean flag = false;
		String prop = (explicitWaitForElement(path, type)).getCssValue(exp_prop);
		System.out.println(prop);

		if (prop.trim().equals(exp_value.trim())) {
			flag = true;
			return flag;
		}

		else {
			return flag;

		}

	}

	public boolean switchContext() {
		boolean colFlag1 = false;
		// driver1= getMDriver();
		Set<String> contextNames = getMDriver().getContextHandles();
		for (String contextName : contextNames) {
			if (contextName.contains("cloud")) {
				getMDriver().context(contextName);
				System.out.println(getMDriver().context(contextName));
				Set<String> contextNamesa = getMDriver().getContextHandles();
				System.out.println(contextNamesa);
				// System.out.println("switched to webview");
				colFlag1 = true;
				break;
			}

		}
		return colFlag1;

	}

	public boolean switchContext1() {
		boolean colFlag1 = false;
		Set<String> contextNames = getMDriver().getContextHandles();
		for (String contextName : contextNames) {
			if (contextName.contains("NATIVE")) {
				getMDriver().context(contextName);
				// System.out.println("switched to native");
				colFlag1 = true;
				break;
			}

		}
		return colFlag1;

	}

	public boolean changeContext(String data) {
		while (!flag1) {
			try {
				if (data.contains("WEBVIEW")) {
					flag1 = switchContext();
				}

				else {
					flag1 = switchContext1();
				}
			} catch (Exception e) {
				if (e.getMessage().contains("NoSuchContextException")) {
					flag1 = false;
				}
			}

		}
		flag1 = false;
		return true;
	}

	public String GetTextOfElement(By value) {

		WebElement element = getMDriver().findElement(value);

		return element.getText();
	}

	public String verifyCurrentDate() {

		DateFormat DtFormat = new SimpleDateFormat("MMM dd yyyy");
		Date date = new Date();
		DtFormat.setTimeZone(TimeZone.getTimeZone("BST"));
		String expected = DtFormat.format(date).toString().trim();
		return expected;
	}

	public String currentdateWithDay() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = new Date();
		String expected = dateFormat.format(date1).toString().trim();

		String[] dateArray = expected.split("/");

		int year = Integer.parseInt(dateArray[0]);
		int month = Integer.parseInt(dateArray[1]);
		int day = Integer.parseInt(dateArray[2]);

		String dateString = String.format("%d-%d-%d", year, month, day);
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);

		return dayOfWeek;
	}

	public String TimesheetDayAspercloudfm(String actual) {
		String DayaccordingtoCloudFm;
		String[] date = actual.split(" ");

		if (date[2].contains("th") || date[2].contains("st") || date[2].contains("nd") || date[2].contains("rd")) {
			if (date[2].length() == 3) {
				String str = date[2];
				date[2] = 0 + str.substring(0, 1);
				// System.out.println(date[2]);
			}
			if (date[2].length() == 4) {
				String str = date[2];
				date[2] = str.substring(0, 2);
				// System.out.println(date[2]);
			}
			if (date[0].length() > 3) {
				String str = date[0];
				date[0] = str.substring(0, 3);
				// System.out.println(date[2]);
				date[3] = date[3].substring(0, 4);
			}
		}
		DayaccordingtoCloudFm = date[0] + " " + date[1] + " " + date[2] + " " + date[3];
		return DayaccordingtoCloudFm;
	}

	public String TimesheetPage1() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println(expecteddate);
		return expecteddate;
	}

	public String TimesheetPage2() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println(expecteddate);
		return expecteddate;
	}

	public String TimesheetPage3() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println(expecteddate);
		return expecteddate;
	}

	public String TimesheetPage4() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println(expecteddate);
		return expecteddate;
	}

	public String TimesheetPage5() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println(expecteddate);
		return expecteddate;
	}

	public String TimesheetPage6() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println(expecteddate);
		return expecteddate;
	}

	public String TimesheetPage7() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		int dt = (Integer.parseInt(expecteddateArray[2])) + 01;
		expecteddateArray[2] = Integer.toString(dt);
		if (expecteddateArray[2].length() <= 1) {
			expecteddateArray[2] = "0" + expecteddateArray[2];
		}
		expecteddateArray[0] = "Sun";
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println(expecteddate);
		return expecteddate;
	}

	// ******************************************************************New
	// Implimention**********************************************************************
	public String GetCurrentDate(String path, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d YYYY");
		Date d = new Date();
		String Currentdate = sdf.format(d);
		return Currentdate;
	}

	public String Gettext(String path, String type) throws InterruptedException {
		Thread.sleep(1500);
		WebElement element = getMDriver().findElement(locatortype(type, path));
		String s = element.getText();
		System.out.println("Text has copyed in clipboard");
		return s;
	}

	public String GetAttribute(String path, String type) throws InterruptedException {
		Thread.sleep(1500);
		WebElement element = getMDriver().findElement(locatortype(type, path));
		String s = element.getAttribute("name");
		// System.out.println("Text has copyed in clipboard: "+s);
		return s;
	}

	public String GetMondayOfCurrentWeek() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println("Current Monday of the week: " + expecteddate);
		return expecteddate;
	}

	public String GetTuesdayOfCurrentWeek() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println("Current Tuesday of the week: " + expecteddate);
		return expecteddate;
	}

	public String GetWednesdayOfCurrentWeek() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println("Current Wednesday of the week: " + expecteddate);
		return expecteddate;
	}

	public String GetThursdayOfCurrentWeek() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println("Current Thrusday of the week: " + expecteddate);
		return expecteddate;
	}

	public String GetFridayOfCurrentWeek() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println("Current Friday date of the week: " + expecteddate);
		return expecteddate;
	}

	public String GetSaturdayOfCurrentWeek() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println("Current Saturday of the week: " + expecteddate);
		return expecteddate;
	}

	public String GetSundayOfCurrentWeek() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String expecteddate = c.getTime().toString().trim();
		String[] expecteddateArray = expecteddate.split(" ");
		expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
				+ expecteddateArray[5];
		System.out.println("Current Sunday of the week: " + expecteddate);
		return expecteddate;
	}

	public void verifyPastDate(String pastdate) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		String currentDate = verifyCurrentDate();
		System.out.println(currentDate);
		// Date date = format.parse(currentDate);
		// c1.setTime(currentDate.);
		// c2.setTime(someOtherDate);

		c1.get(Calendar.YEAR);
		c2.get(Calendar.YEAR);
		c1.get(Calendar.MONTH);
		c2.get(Calendar.MONTH);
		c1.get(Calendar.DAY_OF_MONTH);
		c2.get(Calendar.DAY_OF_MONTH);
	}

	/*
	 * public void verticalSwipeDown() { executionDelay(900); Dimension dim =
	 * getMDriver().manage().window().getSize(); int height = dim.getHeight(); int
	 * width = dim.getWidth(); int x = width / 2; int starty = (int) (height *
	 * 0.60); int endy = (int) (height * 0.20); getMDriver().swipe(x, starty, x,
	 * endy, 750);
	 * 
	 * }
	 */

	@SuppressWarnings("rawtypes")
	public void scrollDown() {

		for (int i = 1; i <= 9; i++) {

			executionDelay(200);

			Dimension dim = getMDriver().manage().window().getSize();
			int height = dim.getHeight();
			int width = dim.getWidth();
			int x = width / 2;
			int top_y = (int) (height * 0.80);
			int bottom_y = (int) (height * 0.20);
			System.out.println("coordinates :" + x + "  " + top_y + " " + bottom_y);
			TouchAction ts = new TouchAction(getMDriver());
			ts.longPress(PointOption.point(x, top_y)).moveTo(PointOption.point(x, bottom_y)).release().perform();

			// MobileElement element = (MobileElement)
			// getMDriver().findElements(MobileBy.AccessibilityId("Add to Cart Add to
			// Shopping Cart"));
			// ts.longPress(element, 3000).moveTo(x,bottom_y).release().perform();

			// ts.longPress(PointOption.point(x,
			// top_y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
			// .moveTo(PointOption.point(x, top_y)).release().perform();

			if (getMDriver().findElements(MobileBy.AccessibilityId("Add to Cart Add to Shopping Cart")).size() > 0) {
				System.out.println("FOUND");
				// getMDriver().findElement(By.xpath("//android.widget.Button[@content-desc=\"Add
				// to Cart Add to Shopping Cart\"]")).click();
				getMDriver().findElement(MobileBy.AccessibilityId("Add to Cart Add to Shopping Cart")).click();
				break;
			}

			else if (getMDriver().findElements(By.xpath("//*[@resource-id='add-to-cart-button']")).size() > 0) {
				System.out.println("FOUND");
				getMDriver().findElement(By.xpath("//*[@resource-id='add-to-cart-button']")).click();
				break;
			}

			else {
				System.out.println("NOT FOUND");
			}

		}
		ExtTest.getTest().log(LogStatus.PASS, "Selected Product Added to the cart");

		/*
		 * String text = "Add to Cart"; WebElement el =
		 * getMDriver().findElement(MobileBy
		 * .AndroidUIAutomator("new UiScrollable(new UiSelector().resourceId(\"add-to-cart-button\")).setAsVerticalList().scrollIntoView("
		 * + "new UiSelector().text(\""+text+"\"));")); el.click();
		 */

	}

	@SuppressWarnings("rawtypes")
	public void swipeHorizontal(double startPercentage, double finalPercentage, int duration) {
		Dimension size = getMDriver().manage().window().getSize();
		int anchor = (int) (size.height * 0.5);
		int startPoint = (int) (size.width * startPercentage);
		int endPoint = (int) (size.width * finalPercentage);
		new TouchAction(getMDriver()).press(PointOption.point(startPoint, anchor))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
				.moveTo(PointOption.point(endPoint, anchor)).release().perform();
	}

	// Call these methods for the below purposes:

	// For right to left:
	// swipeHorizontal(0.9,0.01,0.5,3000);
	//
	// For left to right:
	// swipeHorizontal(0.01,0.9,0.5,3000);

	//
	// For scroll up: swipeVertical((0.9,0.1,0.5,3000);
	//
	// For scroll down: swipeVertical(0.1,0.9,0.5,3000);

	@SuppressWarnings("rawtypes")
	public void swipeVertical(double startPercentage, double finalPercentage, int duration) {
		Dimension size = getMDriver().manage().window().getSize();
		int anchor = (int) (size.width * 0.5);
		int startPoint = (int) (size.height * startPercentage);
		int endPoint = (int) (size.height * finalPercentage);
		new TouchAction(getMDriver()).press(PointOption.point(anchor, startPoint))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
				.moveTo(PointOption.point(anchor, endPoint)).release().perform();
	}

	public void verticalSwipeUp() {
		int duration = 750;
		double startPercentage = 0.20;
		double finalPercentage = 0.60;
		swipeVertical(startPercentage, finalPercentage, duration);
	}

	public void verticalSwipeDown() {
		int duration = 750;
		double startPercentage = 0.60;
		double finalPercentage = 0.20;
		swipeVertical(startPercentage, finalPercentage, duration);
	}

	/*
	 * public void swipeVertical(double startPercentage, double finalPercentage) {
	 * try { Thread.sleep(2000); } catch (InterruptedException e) {
	 * e.printStackTrace(); } Dimension size =
	 * getMDriver().manage().window().getSize(); int anchor = (int) (size.width *
	 * 0.5); int startPoint = (int) (size.height * startPercentage); int endPoint =
	 * (int) (size.height * finalPercentage); new TouchAction(
	 * getMDriver()).press((WebElement) PointOption.point(anchor,
	 * startPoint)).moveTo((WebElement) PointOption.point(anchor,
	 * endPoint)).release().perform(); }
	 */

	public void scrollInMobile() throws InterruptedException, IOException {
		/*
		 * executionDelay(1000);
		 * 
		 * try { for (int i = 0; i < 5; i++) { if (isElementDisplayed(element)) { break;
		 * } else { swipeUp(); } } } catch (Exception e) {
		 * System.out.println("Scroll to mobile element failed"); }
		 */
	}

	public void scrollAndClick(String visibleText) {
		getADriver().findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""
						+ visibleText + "\").instance(0))")
				.click();
	}

	public void scrollTillMobileElementDisplayed(By locator, int maxNumberOfScrolls) {
		boolean isElementDisplayed = false;
		try {
			isElementDisplayed = getMDriver().findElement(locator).isDisplayed();
		} catch (NoSuchElementException e) {
		}

		int i = 0;
		while (!isElementDisplayed) {
			if (i == maxNumberOfScrolls)
				break;
			delay(100);
			verticalSwipeDown();
			delay(100);
			try {
				isElementDisplayed = getMDriver().findElement(locator).isDisplayed();
			} catch (NoSuchElementException e) {
			}
			i += 1;
		}
	}

	public boolean setValue(By locator, String data, String logstep) {
		MobileElement element = (MobileElement) getMDriver().findElement(locator);
		element.sendKeys(data);
		ExtTest.getTest().log(LogStatus.PASS, logstep);
		return true;
	}
}