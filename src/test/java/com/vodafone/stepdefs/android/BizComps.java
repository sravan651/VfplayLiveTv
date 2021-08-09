package com.vodafone.stepdefs.android;

import com.vodafone.locators.android.AndroidLocators.GeneralLoc;
import com.vodafone.utils.MobileKeywords;

public class BizComps extends MobileKeywords {
	public void bannerpopup() throws Exception {
		try {
			Thread.sleep(2000);
			getMDriver().findElement(GeneralLoc.interstitialImg).isDisplayed();
			System.out.println("displayed");
			getMDriver().findElement(GeneralLoc.img2).click();
			System.out.println("cliked");
		}

		catch (Exception e) {
			System.out.println("ssss");
		}
	}

}
