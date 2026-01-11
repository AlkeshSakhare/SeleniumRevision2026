package com.qa.utils;

import com.qa.testbase.TestBase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;


public class TestUtil extends TestBase {

  public static Workbook book;
  public static Sheet sheet;
  public static FileInputStream fi;
  public static FileOutputStream fo;
  public static XSSFWorkbook wb;
  public static XSSFSheet ws;
  public static XSSFRow row;
  public static XSSFCell cell;
  private String parentWindow;

  public static Object[][] getTestData(String sheetName) {
    FileInputStream file = null;
    Object[][] data = null;
    try {
      file = new FileInputStream(TESTDATA_SHEET_PATH);
      book = WorkbookFactory.create(file);
      sheet = book.getSheet(sheetName);
      System.out.println(sheet.getLastRowNum() + "--------" + sheet.getRow(0).getLastCellNum());
      data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];
      for (int i = 0; i < sheet.getLastRowNum(); i++) {
        for (int k = 0; k < sheet.getRow(0).getLastCellNum(); k++) {
          data[i][k] = sheet.getRow(i + 1).getCell(k).toString();
          System.out.println(data[i][k]);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return data;
  }

  public static void setCellData(String sheetName, int rownum, int colnum, String data) {
    try {
      fi = new FileInputStream(TESTDATA_SHEET_PATH);
      wb = new XSSFWorkbook(fi);
      ws = wb.getSheet(sheetName);
      row = ws.getRow(rownum);
      cell = row.createCell(colnum);
      cell.setCellValue(data);
      fo = new FileOutputStream(TESTDATA_SHEET_PATH);
      wb.write(fo);
      wb.close();
      fi.close();
      fo.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static String captureScreen(WebDriver driver, String tname) throws IOException {
    TakesScreenshot ts = (TakesScreenshot) driver;
    File source = ts.getScreenshotAs(OutputType.FILE);
    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    String filePath =
        System.getProperty("user.dir") + "/screenshots/" + tname + "_" + timeStamp + ".png";
    File target = new File(filePath);
    FileUtils.copyFile(source, target);
    System.out.println("Screenshot taken");
    return filePath;
  }

  public void downloadFile(String url) {
    try {
      URL website = new URL(url);
      ReadableByteChannel byteChannel = Channels.newChannel(website.openStream());
      FileOutputStream fileOutputStream = new FileOutputStream(userdir + "/Downloads/sample.png");
      fileOutputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
      fileOutputStream.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String randomestring(int length) {
    String generatedstring = RandomStringUtils.randomAlphabetic(length);
    return (generatedstring);
  }

  public String randomeNum(int no) {
    String randomNo = RandomStringUtils.randomNumeric(no);
    return randomNo;
  }

  public void startTclog(String message) {
    log.info("********** " + " Starting " + message + " ********** ");
  }

  public void endTclog(String message) {
    log.info("********** " + " Ending " + message + " ********** ");
  }

  public void setHighlightToggle(boolean toggle) {
    highlightToggle = toggle;
  }

  public void highlightElement(WebElement element, int duration) {
    if (highlightToggle) {

      String original_style = element.getAttribute("style");
      js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])",
          new Object[]{element, "style", "border: 2px solid red; border-style: dashed;"});
      if (duration > 0) {
        try {
          Thread.sleep((duration * 1000));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])",
            new Object[]{element, "style", original_style});
      }
    }
  }

  public void assertElementAttribute(WebElement element, String attributeName, String expected) {
    log.info("Assert element attribute " + element);
    try {
      String actual = getElementAttribute(element, attributeName);
      Assert.assertEquals(actual, expected);
    } catch (Exception e) {
      log.error("Element Assertion Failed " + element + e.toString());
      throw e;
    }
  }

  public void assertCssAttribute(WebElement element, String attribute, String expected) {
    log.info("Assert CSS attribute " + element);
    try {
      String actual = getCssAttribute(element, attribute);
      Assert.assertEquals(actual, expected);
    } catch (Exception e) {
      log.error("CSS Assertion Failed " + element + e.toString());
      throw e;
    }
  }

  public void assertElementBlank(WebElement element, boolean expected) {
    log.info("Assert element is blank " + element);
    try {
      String elementText = getElementText(element);
      boolean actual = elementText.isEmpty();
      Assert.assertEquals(actual, expected);
    } catch (Exception e) {
      log.error("Element Assertion failed, element is not blank " + element + e.toString());
      throw e;
    }
  }

  public void assertElementDisplayed(WebElement element, boolean expected) {
    log.info("Assert element is displayed " + element);
    try {
      waitForElementVisibility(element);
    } catch (Exception e) {
      log.error("Wait for element to be displayed timed out.");
    }
    try {
      boolean actual = elementDisplayed(element);
      Assert.assertEquals(actual, expected);
    } catch (Exception e) {
      log.error("Element Assertion failed, element is not displayed " + element + e.toString());
      throw e;
    }
  }

  public void assertElementEnabled(WebElement element, boolean expected) {
    log.info("Assert element is enabled " + element);
    try {
      waitForElementVisibility(element);
    } catch (Exception e) {
      log.error("Wait for element to be enabled timed out.");
    }
    try {
      boolean actual = elementEnabled(element);
      Assert.assertEquals(actual, expected);
    } catch (Exception e) {
      log.error("Element Assertion failed, element is not enabled " + element + e.toString());
      throw e;
    }

  }

  public void assertElementText(WebElement element, String expected, boolean wantEqual) {
    String condition = " not";
    String errCondition = "";
    if (wantEqual) {
      condition = "";
      errCondition = " not";
    }
    log.info("Assert element text " + getElementText(element) + " is" + condition
        + " equal to expected value "
        + expected + element);
    try {
      String actual = getElementText(element);
      Assert.assertEquals(expected.equals(actual), wantEqual);
    } catch (Exception e) {
      log.error("Element Assertion failed " + getElementText(element) + " is" + errCondition
          + "\" equal to expected value " + expected + element + e.toString());
      throw e;
    }
  }

  public void assertElementText(WebElement element, String expected) {
    log.info(
        "Assert element text " + getElementText(element) + " is equal to expected value " + expected
            + element);
    try {
      String actual = getElementText(element);
      Assert.assertEquals(actual, expected);
    } catch (Exception e) {
      log.error(
          "Element Assertion failed " + getElementText(element) + " is not equal to expected value "
              + expected + element + e.toString());
      throw e;
    }
  }

  public void assertElementTextNotEquals(WebElement element, String passedValue) {
    log.info(
        "Assert actual element text " + getElementText(element) + " is not equal to expected value "
            + passedValue + element);
    try {
      String actual = getElementText(element);
      Assert.assertNotEquals(actual, passedValue);
    } catch (Exception e) {
      log.error(
          "Element Assertion failed " + getElementText(element) + " is equal to expected value "
              + passedValue + element + e.toString());
      throw e;
    }
  }

  public void assertTitle(String expected) {
    log.info("Assert page title " + expected);
    try {
      Assert.assertEquals(getPageTitle(), expected);
    } catch (Exception e) {
      log.error("Title Page assertion failed, does not match " + expected + e.toString());
      throw e;
    }
  }

  public void assertUrl(String expected) {
    log.info("Assert url " + expected);
    try {
      Assert.assertEquals(getUrl(), expected);
    } catch (Exception e) {
      log.error("Url assertion failed, does not match " + expected + e.toString());
      throw e;
    }
  }

  public void assertUrlContains(String expected) {
    log.info("Assert url " + expected);
    try {
      Assert.assertTrue(getUrl().contains(expected));
    } catch (Exception e) {
      log.error("Url assertion failed, does not match " + expected + e.toString());
      throw e;
    }
  }

  public void browserBack() {
    log.info("Browser back");
    try {
      driver.navigate().back();
    } catch (Exception e) {
      log.error("Browser failed to navigate back " + e.toString());
      throw e;
    }
  }

  public void browserForward() {
    log.info("Browser forward");
    try {
      driver.navigate().forward();
    } catch (Exception e) {
      log.error("Browser failed to navigate forward " + e.toString());
      throw e;
    }
  }

  public void clear(WebElement element) {
    log.info("Clear " + element);
    try {
      elementWhenVisible(element).clear();
    } catch (Exception e) {
      log.error("Element failed to clear " + element + e.toString());
      throw e;
    }
  }

  public void clearAllCheckboxes(List<WebElement> elements) {
    log.info("Clear all checkboxes " + elements);
    try {
      for (WebElement webElement : elements) {
        if (webElement.isSelected()) {
          webElement.click();
        }
      }
    } catch (Exception e) {
      log.error("Clear all checkboxes failed " + elements + e.toString());
      throw e;
    }
  }

  public void clearCheckbox(WebElement element) {
    log.info("Clear checkbox " + element);
    try {
      if (elementWhenVisible(element).isSelected()) {
        elementWhenVisible(element).click();
      }
    } catch (Exception e) {
      log.error("Clear checkbox failed " + element + e.toString());
      throw e;
    }
  }

  public void click(WebElement element) {
    log.info("Click " + element);
    try {
      elementWhenClickable(element).click();
    } catch (Exception e) {
      log.error("Element failed to click " + element + e.toString());
      throw e;
    }
  }

  public WebElement elementWhenVisible(WebElement element) {
    log.info("Find elementWhenVisible " + element);
    try {
      waitForElementVisibility(element);
      highlightElement(element, 1);
      return element;
    } catch (Exception e) {
      log.error("Element was not found " + element + e.toString());
      throw e;
    }
  }

  public WebElement elementWhenClickable(WebElement element) {
    log.info("Find elementWhenVisible " + element);
    try {
      waitForElementClickability(element);
      highlightElement(element, 1);
      return element;
    } catch (Exception e) {
      log.error("Element was not found " + element + e.toString());
      throw e;
    }
  }

  public boolean elementDisplayed(WebElement element) {
    log.info("Element displayed " + element);
    try {
      return element.isDisplayed();
    } catch (Exception e) {
      log.error("Element is not displayed " + element + e.toString());
      throw e;
    }
  }

  public boolean elementEnabled(WebElement element) {
    log.info("Element enabled " + element);
    try {
      return element.isEnabled();
    } catch (Exception e) {
      log.error("Element is not enabled " + element + e.toString());
      throw e;
    }
  }

  public WebElement element(WebElement element) {
    log.info("Find element " + element);
    try {
      highlightElement(element, 1);
      return element;
    } catch (Exception e) {
      log.error("Element was not found " + element + e.toString());
      throw e;
    }
  }

  public void enterKeyOnElement(WebElement element) {
    log.info("Enter key on element " + element);
    try {
      elementWhenVisible(element).sendKeys(Keys.ENTER);
    } catch (Exception e) {
      log.error("Enter key on element failed " + element + e.toString());
      throw e;
    }
  }

  public void enterText(WebElement element, int integerValue, boolean clearField) {
    log.info("Enter text integer " + element);
    try {
      Integer integerText = Integer.valueOf(integerValue);
      String stringText = Integer.toString(integerText.intValue());
      enterText(element, stringText, clearField);
    } catch (Exception e) {
      log.error("Failed to enter text " + integerValue + " to " + element + e.toString());
      throw e;
    }
  }

  public void enterText(WebElement element, double doubleValue, boolean clearField) {
    log.info("Enter text double " + element);
    try {
      String stringText = Double.toString(doubleValue);
      enterText(element, stringText, clearField);
    } catch (Exception e) {
      log.error("Failed to enter text " + doubleValue + " to " + element + e.toString());
      throw e;
    }
  }

  public void enterText(WebElement element, String textToEnter, boolean clearField) {
    log.info("Enter text " + element);
    try {
      if (clearField) {
        clear(element);
      }
      elementWhenVisible(element).sendKeys(new CharSequence[]{textToEnter});
    } catch (Exception e) {
      log.error("Failed to enter text " + textToEnter + " to " + element + e.toString());
      throw e;
    }
  }

  public void fillCheckbox(List<WebElement> elements, boolean filled) {
    String actionPerformed = "CLEAR";
    if (filled) {
      actionPerformed = "FILL";
    }
    log.info(actionPerformed + " all checkboxes " + elements);
    try {
      for (WebElement webElement : elements) {
        if ((!webElement.isSelected() && filled) || (webElement.isSelected() && !filled)) {
          webElement.click();
        }
      }
    } catch (Exception e) {
      log.error("{}|{}|{} "
          + new Object[]{"Failed to " + actionPerformed + " checkbox " + elements, e.toString()});
      throw e;
    }
  }

  public void fillAllCheckboxes(List<WebElement> elements) {
    log.info("Fill all checkboxes " + elements);
    try {
      for (WebElement webElement : elements) {
        if (!webElement.isSelected()) {
          webElement.click();
        }
      }
    } catch (Exception e) {
      log.error("Failed to fill all checkboxes " + elements + e.toString());
      throw e;
    }
  }

  public void fillCheckbox(WebElement element) {
    log.info("Fill checkbox " + element);
    try {
      if (!elementWhenVisible(element).isSelected()) {
        elementWhenVisible(element).click();
      }
    } catch (Exception e) {
      log.error("failed to fill checkbox " + element + e.toString());
      throw e;
    }
  }

  public WebElement findFirst(List<WebElement> elements) {
    log.info("Find first element " + elements);
    try {

      for (WebElement element : elements) {
        if (isVisible(element)) {
          return element;
        }
      }
      return null;
    } catch (Exception e) {
      log.error("Failed to find first element " + elements + e.toString());
      throw e;
    }
  }

  public String getCurrentDate() {
    log.info("Get current date");
    try {
      Date date = new Date();
      return (new SimpleDateFormat("ddMMYYYY")).format(date);
    } catch (Exception e) {
      log.error("Failed to get current date " + e.toString());
      throw e;
    }
  }

  public String getCurrentDayOfMonthPlusOne() {
    log.info("Get current day of month plus one");
    try {
      Calendar calendar = Calendar.getInstance();
      int date = calendar.get(5);
      date++;
      StringBuilder sb = new StringBuilder();
      sb.append(date);
      return sb.toString();
    } catch (Exception e) {
      log.error("Failed to get current day of month plus one " + e.toString());
      throw e;
    }
  }

  public String getCurrentMonth() {
    log.info("Get current month");
    try {
      Calendar calendar = Calendar.getInstance();
      return calendar.getDisplayName(2, 2, null);
    } catch (Exception e) {
      log.error("Failed to get current month " + e.toString());
      throw e;
    }
  }

  public String getCurrentTime() {
    log.info("Get current time");
    try {
      Date time = new Date();
      return (new SimpleDateFormat("HHmm")).format(time);
    } catch (Exception e) {
      log.error("Failed to get current time " + e.toString());
      throw e;
    }
  }

  public String getElementAttribute(WebElement element, String attributeName) {
    log.info("Get element attribute " + element);
    try {
      return elementWhenVisible(element).getAttribute(attributeName);
    } catch (Exception e) {
      log.error("Failed to get element attribute " + element + e.toString());
      throw e;
    }
  }

  public String getCssAttribute(WebElement element, String attribute) {
    log.info("Get CSS attribute " + element);
    try {
      return elementWhenVisible(element).getCssValue(attribute);
    } catch (Exception e) {
      log.error("Failed to get CSS attribute " + element + e.toString());
      throw e;
    }
  }

  public String getElementText(WebElement element) {
    log.info("Get element text " + element);
    try {
      return elementWhenVisible(element).getText();
    } catch (Exception e) {
      log.error("Failed to get element text " + element + e.toString());
      throw e;
    }
  }

  public String getPageTitle() {
    log.info("Get page title");
    try {
      return driver.getTitle();
    } catch (Exception e) {
      log.error("Failed to get page title " + e.toString());
      throw e;
    }
  }

  public String getUrl() {
    log.info("Get url");
    try {
      return driver.getCurrentUrl();
    } catch (Exception e) {
      log.error("Failed to get url " + e.toString());
      throw e;
    }
  }

  public void getWindowHandle() {
    log.info("Get window handle");
    try {
      parentWindow = driver.getWindowHandle();
    } catch (Exception e) {
      log.error("Failed to get window handle " + e.toString());
      throw e;
    }
  }

  public boolean isElementExist(WebElement element) {
    log.info("Element exists " + element);
    try {
      return (elementWhenVisible(element) != null);
    } catch (Exception e) {
      log.error("Element does not exist " + element + e.toString());
      throw e;
    }
  }

  private boolean isVisible(WebElement element) {
    log.info("Is elementWhenVisible visible " + element);
    try {
      Dimension d = element.getSize();
      return (d.getHeight() > 0 && d.getWidth() > 0);
    } catch (Exception e) {
      log.error("Element is not visible " + element + e.toString());
      throw e;
    }
  }

  public void openNewUrl(String url) {
    log.info("Open new url " + url);
    try {
      driver.get(url);
    } catch (Exception e) {
      log.error("Failed to open new url " + url + e.toString());
      throw e;
    }
  }

  public void pressTabKeyOnElement(WebElement element) {
    log.info("Tab key on element " + element);
    try {
      elementWhenVisible(element).sendKeys(Keys.TAB);
    } catch (Exception e) {
      log.error("Failed to tab key on element " + element + e.toString());
      throw e;
    }
  }

  public void keyPress(String... keys) {
    log.info("Press multiple keys " + keys);
    try {
      Keys.chord((CharSequence[]) keys).toUpperCase();
    } catch (Exception e) {
      log.error("Failed to press multiple keys " + keys + e.toString());
      throw e;
    }
  }

  public void selectFromDroplist(WebElement element, String visibleText) {
    log.info("Select from drop list " + element);
    try {
      Select option = new Select(elementWhenVisible(element));
      option.selectByVisibleText(visibleText);
    } catch (Exception e) {
      log.error("Failed to select from drop list " + element + e.toString());
      throw e;
    }
  }

  public void selectFromDroplistWithClick(WebElement element, String visibleText) {
    log.info("Select from drop list with click " + element);
    try {
      WebElement droplist = elementWhenVisible(element);
      droplist.click();
      Select option = new Select(droplist);
      option.selectByVisibleText(visibleText);
    } catch (Exception e) {
      log.error("Failed to select from drop list with click " + element + e.toString());
      throw e;
    }
  }

  public void switchToChildWindow() {
    log.info("Switch to child window");
    try {
      Set<String> tabs = driver.getWindowHandles();
      tabs.remove(parentWindow);
      Object[] objTabs = tabs.toArray();
      driver.switchTo().window((String) objTabs[0]);
    } catch (Exception e) {
      log.error("Failed to switch to child window " + e.toString());
      throw e;
    }
  }

  public void switchToParentWindow(boolean closeChild) {
    log.info("Switch to parent window");
    try {
      if (closeChild) {
        driver.close();
      }
      driver.switchTo().window(parentWindow);
    } catch (Exception e) {
      log.error("Failed to switch to parent window " + e.toString());
      throw e;
    }
  }

  public Alert switchToAlert() {
    log.info("Switch to alert");
    try {
      return driver.switchTo().alert();
    } catch (Exception e) {
      log.error("Failed to switch to alert " + e.toString());
      return null;
    }
  }

  public void switchToFrameByIndex(int index) {
    log.info("Switch to frame by index " + Integer.valueOf(index));
    try {
      driver.switchTo().frame(index);
    } catch (Exception e) {
      log.error("{}|{}|{} "
          + new Object[]{"Failed to switch to frame by index " + Integer.valueOf(index),
          e.toString()});
      throw e;
    }
  }

  public void switchToFrameByNameOrId(String nameOrId) {
    log.info("Switch to frame by name or Id " + nameOrId);
    try {
      driver.switchTo().frame(nameOrId);
    } catch (Exception e) {
      log.error("Failed to switch to frame " + nameOrId + e.toString());
      throw e;
    }
  }

  public void switchToFrameByWebelement(WebElement element) {
    log.info("Switch to frame by Webelement " + element);
    try {
      driver.switchTo().frame(elementWhenVisible(element));
    } catch (Exception e) {
      log.error("Failed to switch to frame by Webelement " + element + e.toString());
      throw e;
    }
  }

  public void switchToDefaultContent() {
    log.info("Switch to default content");
    try {
      driver.switchTo().defaultContent();
    } catch (Exception e) {
      log.error("Failed to switch to default content " + e.toString());
      throw e;
    }
  }

  public String takeScreenShot(ITestResult tr) {
    log.info("Take screenshot");
    String screenshotFileName = "";
    try {
      String date = (new SimpleDateFormat("MMdd_HHmmss")).format(new Date());
      screenshotFileName = tr.getName() + "_" + date + ".png";
      File scrFile = (File) ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      try {
        FileUtils.copyFile(scrFile, new File("target/Screeshots/" + screenshotFileName));
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      log.error("Failed to take screenshot " + e.toString());
      throw e;
    }
    return screenshotFileName;
  }

  public String truncateString(String value, int length) {
    log.info("Truncate String " + value);
    try {
      if (value != null && value.length() > length) {
        value = value.substring(0, length);
      }
      return value;
    } catch (Exception e) {
      log.error("Failed to truncate String " + value + e.toString());
      throw e;
    }
  }

  public void wait(int seconds) {
    log.info("Wait " + Integer.valueOf(seconds));
    try {
      try {
        TimeUnit.SECONDS.sleep(seconds);
      } catch (InterruptedException exception) {
        log.error("Interrupted exception on wait " + exception);
      }
    } catch (Exception e) {
      log.error("Failed to wait " + Integer.valueOf(seconds) + e.toString());
      throw e;
    }
  }

  public void waitForElementClickability(WebElement element) {
    log.info("Wait for clickability of element " + element);
    try {
      wait.until(ExpectedConditions.elementToBeClickable(element));
    } catch (Exception e) {
      log.error("Failed to wait for clickability of element  " + element + e.toString());
      throw e;
    }
  }

  public void waitForElementVisibility(WebElement element) {
    log.info("Wait for visibility of element " + element);
    try {
      wait.until(ExpectedConditions.visibilityOf(element));
    } catch (Exception e) {
      log.error("Failed to wait for visibility of element  " + element + e.toString());
      throw e;
    }
  }

  public void waitForElementInvisible(By element) {
    log.info("Wait until element goes invisible " + element);
    try {
      wait.until(ExpectedConditions.invisibilityOf(driver.findElement(element)));
    } catch (Exception e) {
      log.error("Failed to wait until element invisible " + element + e.toString());
      throw e;
    }
  }

  public void waitForAlert() {
    log.info("Wait for presence of alert");
    try {
      wait.until(ExpectedConditions.alertIsPresent());
    } catch (Exception e) {
      log.error("Failed to wait for presence of alert " + e.toString());
      throw e;
    }
  }

  public void webpageRefresh() {
    log.info("Refresh webpage");
    try {
      driver.navigate().refresh();
    } catch (Exception e) {
      log.error("Failed to refresh webpage " + e.toString());
      throw e;
    }
  }

  public void scrollToElement(By element, String direction) {
    log.info("Scroll to element " + element);
    try {

      Boolean isColumnDisplayed = Boolean.valueOf(driver.findElement(element).isDisplayed());
      switch (direction) {
        case "right":
          while (!isColumnDisplayed.booleanValue()) {
            action.sendKeys(new CharSequence[]{(CharSequence) Keys.ARROW_RIGHT}).build().perform();
            isColumnDisplayed = Boolean.valueOf(driver.findElement(element).isDisplayed());
          }
          break;
        case "left":
          while (!isColumnDisplayed.booleanValue()) {
            action.sendKeys(new CharSequence[]{(CharSequence) Keys.ARROW_LEFT}).build().perform();
            isColumnDisplayed = Boolean.valueOf(driver.findElement(element).isDisplayed());
          }
          break;
        case "down":
          while (!isColumnDisplayed.booleanValue()) {
            action.sendKeys(new CharSequence[]{(CharSequence) Keys.ARROW_DOWN}).build().perform();
            isColumnDisplayed = Boolean.valueOf(driver.findElement(element).isDisplayed());
          }
          break;
        case "up":
          while (!isColumnDisplayed.booleanValue()) {
            action.sendKeys(new CharSequence[]{(CharSequence) Keys.ARROW_UP}).build().perform();
            isColumnDisplayed = Boolean.valueOf(driver.findElement(element).isDisplayed());
          }
          break;
      }
    } catch (Exception e) {
      log.error("Failed to scroll to element  " + element + e.toString());
      throw e;
    }
  }

  public void doubleClick(WebElement element) {
    log.info("Double click " + element);
    try {

      action.doubleClick(elementWhenVisible(element)).build().perform();
    } catch (Exception e) {
      log.error("Failed to double Click " + element + e.toString());
      throw e;
    }
  }

  public void contextClick(WebElement element) {
    log.info("Context click " + element);
    try {

      action.contextClick(elementWhenVisible(element)).build().perform();
    } catch (Exception e) {
      log.error("Failed to context Click " + element + e.toString());
      throw e;
    }
  }

  public void moveToElement(WebElement element) {
    log.info("Move to element " + element);
    try {
      action.moveToElement((element)).build().perform();
    } catch (Exception e) {
      log.error("Failed to move to element " + element + e.toString());
      throw e;
    }
  }

  public void clickAndHold(WebElement element) {
    log.info("Click and Hold " + element);
    try {
      action.clickAndHold(elementWhenVisible(element)).build().perform();
    } catch (Exception e) {
      log.error("Failed to click and Hold " + element + e.toString());
      throw e;
    }
  }

  public void release() {
    log.info("Release");
    try {

      action.release().build().perform();
    } catch (Exception e) {
      log.error("Failed to release " + e.toString());
      throw e;
    }
  }

  public void releaseAtLocation(WebElement element) {
    log.info("Release at location " + element);
    try {
      action.release(elementWhenVisible(element)).build().perform();
    } catch (Exception e) {
      log.error("Failed to Release at location " + element + e.toString());
      throw e;
    }
  }

  public void dragAndDrop(WebElement element, WebElement target) {
    log.info("Drag and Drop " + element + target);
    try {

      action.dragAndDrop(elementWhenVisible(element), elementWhenVisible(target));
      action.build();
      action.perform();
    } catch (Exception e) {
      log.error("Failed to Drag and Drop " + element + target + e.toString());
      throw e;
    }
  }

  public void mouseHoverJScript(WebElement HoverElement) {

    String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
    js.executeScript(mouseOverScript, HoverElement);
  }
}