package com.vodafone.locators.android;



import org.openqa.selenium.By;

public class AndroidLocators  {

	public static class GeneralLoc {
		public static By interstitialImg = By.id("com.vodafone.vodafoneplay:id/interstitial_image");
		public static By img2 = By.xpath("(//*[@class='android.widget.ImageView'])[2]");
		public static By tv = By.xpath("//android.widget.TextView[@text='TV']");
		public static By searchBtn = By
				.xpath("//android.widget.ImageView[@resource-id='com.vodafone.vodafoneplay:id/search_button']");
		public static By searchTxt = By
				.xpath("//android.widget.EditText[@resource-id='com.vodafone.vodafoneplay:id/search_src_text']");

		// need to parameterize
		// android.widget.TextView[@resource-id='com.vodafone.vodafoneplay:id/title'][@text='
		public static By editTxt = By.xpath("//android.widget.EditText");
		public static By continueBtn = By.xpath("//android.widget.Button[@text='Continue']");
		public static By otpTxt = By.xpath("//android.widget.EditText[@text='Enter 6-digit OTP']");
		public static By goBtn = By.xpath("//android.widget.Button[@text='Go']");
		public static By playPauseIcon = By.xpath(
				"//android.widget.ImageView[@resource-id='com.vodafone.vodafoneplay:id/media_player_play_pause_icon']");
		public static By movieNaameDescription = By.xpath(
				"//android.widget.TextView[@resource-id='com.vodafone.vodafoneplay:id/carddetailbreifdescription_movename']");
		public static By showMoreBtn = By.id("com.vodafone.vodafoneplay:id/showMoreButton");
		public static By closeExitPopUp = By.id("com.vodafone.vodafoneplay:id/closeExitPopUp");
		
	    public static By title=By.xpath("//android.widget.TextView[@resource-id='com.vodafone.vodafoneplay:id/title']");
	    public static By VisibleChNametext =By.xpath("//android.widget.TextView[@resource-id='com.vodafone.vodafoneplay:id/title'][@text='\" + x + \"']");
	    
	 // (//android.widget.TextView[@resource-id='com.vodafone.vodafoneplay:id/title'])[1]
		
	}
}

