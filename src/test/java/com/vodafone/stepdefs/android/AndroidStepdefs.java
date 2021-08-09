package com.vodafone.stepdefs.android;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import com.apalya.models.Channel;
import com.vodafone.locators.android.AndroidLocators.GeneralLoc;
import com.vodafone.utils.CSVParserUtil;
import com.vodafone.utils.GlobalUtil;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AndroidStepdefs extends BizComps {

	@Given("^user click on TV$")
	public void user_click_free_content_video() {
		delay(1000);
		click(GeneralLoc.tv, "I click tv button");
		delay(1000);
	}

	@When("^user click search button$")
	public void I_click_search_button() {
		delay(1000);
		click(GeneralLoc.searchBtn, "I click tv button");
		delay(1000);
		click(GeneralLoc.searchTxt, "Searching live tv content");
		delay(1000);

	}

	@Then("^validate the contents from csv file$")
	public void validate_the_contentsfrom_csv_file() {
		String testName = System.getProperty("testName");
		String a1 = System.getProperty(testName + "Start");
		String b1 = System.getProperty(testName + "End");
		int a = Integer.parseInt(System.getProperty(testName + "Start"));
		int b = Integer.parseInt(System.getProperty(testName + "End"));
		for (int i = a; i <= b; i++) {
			delay(3000);
			Channel channel = CSVParserUtil.getChannels("" + i);
			String channelName = channel.getChannelName();
			delay(3000);
			setValue(GeneralLoc.searchTxt, channelName, "Searched for channel:" + channelName);
			System.out.println(channel.getChannelName());
			try {
				if (getMDriver().findElement(GeneralLoc.title).isDisplayed()) {
					click(GeneralLoc.title, "Clicked on " + channelName);
					delay(3000);
					setValue(GeneralLoc.editTxt, config.getTDValue("Mobilenumber"), "Entered mobile number");
					delay(3000);
					try {
						setValue(GeneralLoc.editTxt, config.getTDValue("Mobilenumber"), "Entered mobile number");
						delay(3000);
						click(GeneralLoc.continueBtn, "I click tv button");
						delay(15000);
						if (System.getProperty("deviceName").contains("emulator")) {
							click(GeneralLoc.otpTxt, "I click tv button");
							setValue(GeneralLoc.otpTxt, config.getTDValue("Otp"), "Entered otp");
							delay(3000);
							click(GeneralLoc.goBtn, "I click tv button");

							delay(3000);
						}
					
						click(GeneralLoc.continueBtn, "I click tv button");
						delay(15000);
                        click(GeneralLoc.playPauseIcon, "user cliked play button for channel: " + channelName);
						delay(1000);
						click(GeneralLoc.playPauseIcon, "user cliked pause button for channel:" + channelName);
						delay(1000);
						back();
						delay(1000);

					} catch (Exception e) {
						back();
						System.out.println("The following exception during execution:" + e.getStackTrace().toString());

					}
					click(GeneralLoc.searchBtn, "I click tv button");
					delay(1000);
					click(GeneralLoc.searchTxt, "Searching live tv content");
					delay(1000);
				}

			} catch (NoSuchElementException | TimeoutException e) {
			}
			delay(3000);

		}

	}

	@And("^enter valid credentials$")
	public void enter_valid_credentials() {
		setValue(GeneralLoc.editTxt, GlobalUtil.dataMap.get("Mobilenumber"), "Entered mobile number");
		delay(15000);
		setValue(GeneralLoc.otpTxt, GlobalUtil.dataMap.get("Otp"), "Entered password");
	}

	@And("^click playpause button$")
	public void click_playpause_button() {
		click(GeneralLoc.playPauseIcon, "user cliked play button");
		delay(1000);
		click(GeneralLoc.playPauseIcon, "user cliked pause button");
		delay(1000);

	}
}