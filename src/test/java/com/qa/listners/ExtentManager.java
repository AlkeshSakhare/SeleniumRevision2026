package com.qa.listners;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

	private static ExtentReports extent;

	public static ExtentReports getInstance() {
		if (extent == null) {

			ExtentSparkReporter spark = new ExtentSparkReporter(
					System.getProperty("user.dir") + "/test-output/ExtentReport.html");

			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("Execution Report");

			extent = new ExtentReports();
			extent.attachReporter(spark);

			// 🔹 System info will be set later (after driver init)
			setBasicSystemInfo();
		}
		return extent;
	}

	private static void setBasicSystemInfo() {
		extent.setSystemInfo("OS", System.getProperty("os.name"));

		extent.setSystemInfo("OS Version", System.getProperty("os.version"));

		extent.setSystemInfo("Java Version", System.getProperty("java.version"));

		extent.setSystemInfo("User", System.getProperty("user.name"));
	}

	// Call this after driver creation
	public static void setBrowserInfo(WebDriver driver) {

		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();

		extent.setSystemInfo("Browser", cap.getBrowserName());

		extent.setSystemInfo("Browser Version", cap.getBrowserVersion());
	}
}