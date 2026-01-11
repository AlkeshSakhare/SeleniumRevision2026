package com.qa.testbase;

import java.io.FileReader;
import java.time.Duration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;

import com.qa.listners.ExtentManager;
import com.qa.utils.TestUtil;

public class TestBase {

	public static Properties properties;
	public static String browser;
	public static String url;
	public static String username;
	public static String password;
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static JavascriptExecutor js;
	public static Actions action;
	public static Logger log;
	public static String userdir = System.getProperty("user.dir");
	public static String TESTDATA_SHEET_PATH = userdir + "/src/test/resources/TestData.xlsx";
	public static boolean highlightToggle;
	public static TestUtil testUtil;

	public TestBase() {
		// TODO Auto-generated constructor stub
		try {
			properties = new Properties();
			properties.load(new FileReader(userdir + "/src/test/resources/config.properties"));
			log = LogManager.getLogger(this.getClass());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception in TestBase:" + e);
		}

	}

	public static void initialization() {
		try {
			browser = properties.get("browser").toString();
			url = properties.get("url").toString();
			username = properties.get("username").toString();
			password = properties.get("password").toString();
			highlightToggle = Boolean.valueOf(properties.getProperty("highlightToggle"));
			testUtil = new TestUtil();
			log.info("browserName is: " + browser);
			log.info("url is: " + url);

			log.info("username is: " + username);
			log.info("password is: " + password);

			switch (browser.toUpperCase()) {
			case "CHROME":
				driver = new ChromeDriver();
				break;
			case "FIREFOX":
				driver = new FirefoxDriver();
				break;
			case "EDGE":
				driver = new EdgeDriver();
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + browser);
			}

			wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			js = (JavascriptExecutor) driver;
			action = new Actions(driver);
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(50));
			driver.get(url);
			ExtentManager.setBrowserInfo(driver);
		} catch (Exception e) {
			log.error("Exception in initialization is : " + e);
		}
	}

	@AfterMethod
	public void tearDown() {
		// TODO Auto-generated method stub
		driver.quit();
	}
}
