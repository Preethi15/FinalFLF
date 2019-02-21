package com.volvo.mfg.pagecomponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.volvo.mfg.commonutilis.CommonWrapperMethods;

public class LoginPages extends CommonWrapperMethods {
	private WebDriver driver;

	
	public LoginPages(WebDriver driver) {
		this.driver = driver;
	}
	
	/*
	 * 01 verify login to FLF Test Case Id:3700
	 */
	public void LogintoFLF(String uName,String pWord,String eValue) throws InterruptedException {
		
		if (Environment.equalsIgnoreCase("Test") && Browser.equalsIgnoreCase("IE")) {
			anyClick("Continue to this link", By.xpath(prop.getProperty("Loginpage.User.Recommended.Link")));
		} 
		
		anyClick("Go to FLF Home Page", By.xpath(prop.getProperty("Loginpage.User.FLF.Home.Page.Link")));
		
		if (verifyAuthenticationPopup(uName, decryptPassword(pWord)) == true) {
		sendKeys("UserName", By.xpath(prop.getProperty("LoginPage.User.Name")), uName);
		sendKeysPassword("Password", By.xpath(prop.getProperty("LoginPage.User.Password")), pWord);
		anyClick("Submit", By.xpath(prop.getProperty("LoginPage.Login.Button")));
		reportStep("#B Verified the functionality of logging into FLF application Test Case Id:3700 #C", "pass", false);
		
			// Verify Page displayed
			verifyPageTitle(eValue);
		} else {
			reportStep(uName + " User already logged in", "Info",
					false);
		}
}
}