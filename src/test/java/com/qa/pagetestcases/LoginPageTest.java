package com.qa.pagetestcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qa.pages.LoginPage;
import com.qa.testbase.TestBase;

public class LoginPageTest extends TestBase {

	static LoginPage loginPage;

	public LoginPageTest() {
		// TODO Auto-generated constructor stub
		super();
	}

	@BeforeMethod
	public void setUp() {
		initialization();
		loginPage = new LoginPage();
		log.info("Im setup done");
	}

	@Test
	public void loginTest() {
		loginPage.login(username, password);
		testUtil.wait(5);
		WebElement logo = driver.findElement(By.xpath("//*[@*='client brand banner']"));
		testUtil.assertElementDisplayed(logo, true);
	}

	@Test
	public void loginPageTitleTest() {
		testUtil.assertTitle("OPENCRM");
	}

}
