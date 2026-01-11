package com.qa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.qa.testbase.TestBase;

public class LoginPage extends TestBase {

	@FindBy(xpath = "//*[@*='username']")
	WebElement usernameTxt;
	@FindBy(xpath = "//*[@*='password']")
	WebElement passwordTxt;
	@FindBy(xpath = "//*[@*='submit']")
	WebElement loginBtn;

	@FindBy(xpath = "//*[@*='client brand banner']")
	WebElement logo;

	public LoginPage() {
		// TODO Auto-generated constructor stub
		try {
			PageFactory.initElements(driver, this);
		} catch (Exception e) {
			log.error("Exception in LoginPage() : " + e);
		}
	}

	public void login(String user, String pass) {
		try {
			testUtil.enterText(usernameTxt, user, false);
			testUtil.enterText(passwordTxt, pass, false);
			testUtil.click(loginBtn);
		} catch (Exception e) {
			log.error("Exception in login is : " + e);
		}
	}

}
