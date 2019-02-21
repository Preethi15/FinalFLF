package com.volvo.mfg.commonutilis;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
//Java packages
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
//Selenium packages
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.volvo.mfg.reports.Reports;




public class CommonWrapperMethods extends Reports {
	
	protected static Properties config, prop,prop1, queryProp;
	protected static int Default_Wait_4_Page,Default_Implicit_Wait;
	public static String Environment,Browser,Application_Name,Scenario_Name,Test_Sheet_Path, refTestDataName,Url;
	protected static WebDriver driver;
//	public static RemoteWebDriver driver;
	
	public static String[] browser = new String[5];
	public static Set<String> windows = null;
	//Created by Renuka -- Logj4 Setup Variables
	public static  Logger logger = Logger.getLogger("devpinoyLogger");
	 public static  String LOG_PROPERTIES_FILE = "./log4j.properties";
	 String node="http://10.244.215.254:4444/wd/hub";
	//Constructor to load configuration properties
	public CommonWrapperMethods() {
		
		//Loading the configuration properties file
		config = new Properties();
		try {
			config.load(new FileInputStream(new File("./config.properties")));
			
		}
		catch (FileNotFoundException e) {
			System.err.println("'config.properties' file load Error. Please check the file exist/name of the file");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("'config.properties' file load Error. Please check the Input data of the file");
			e.printStackTrace();
		}
	}
	
	//**************************************************************************************************
	//@Method Name: loadObject
	//@Description: Wrapper function - Load the properties file
	//@Input Parameters: NIL
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 18-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public void loadObject() {
		prop = new Properties();
		prop1 = new Properties();
		queryProp=new Properties();
		try {
			prop.load(new FileInputStream(new File("./User1.properties")));
			prop.load(new FileInputStream(new File("./userDetails.properties")));
			prop.load(new FileInputStream(new File("./applicationURL.properties")));
			prop.load(new FileInputStream(new File("./pageObjects.properties")));
			queryProp.load(new FileInputStream(new File("./QueryObjects.properties")));
			prop.load(new FileInputStream(new File("./temp.properties")));
			
			/*String path1=System.getProperty("user1");
			
			prop.load(new FileInputStream(new File("./"+path1)));
		*/
		}
		catch (FileNotFoundException e) {
			System.err.println("'*.properties' multiple file load Error. Please check the file exist/name of the file");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("'*.properties' multiple file load Error. Please check the Input data of the file");
			e.printStackTrace();
		}
	}
	
	//**************************************************************************************************
	//@Method Name: suiteVariables
	//@Description: Initialize the Global variables.
	//@Input Parameters: NIL
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 18-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public void suiteVariables() {
		//Assigning time out values
		Default_Wait_4_Page = Integer.parseInt(config.getProperty("Default_Wait_4_Page"));
		Default_Implicit_Wait=Integer.parseInt(config.getProperty("Default_Implicit_Wait"));
		//Application Name
		Application_Name=config.getProperty("Application_Name");
		//Environment 
		Environment=config.getProperty("Environment");
		//Browser to be launched
		Browser=config.getProperty("Browser");
		//URL pick depends on Environment
		
		Url=prop.getProperty(Environment + ".URL." + Application_Name);
		//Test Case Sheet Path
		Test_Sheet_Path=config.getProperty("Test_Sheet_Path");
		
		
		
		
	}
	
	//**************************************************************************************************
	//@Method Name: clearingMemory
	//@Description: Object and variables memory clearing
	//@Input Parameters: NIL
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 18-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public void clearingMemory() {
		//Clearing variables memory
		prop.clear();
		prop = null;
		config.clear();
		config = null;
		Default_Wait_4_Page=0;
		Application_Name="";
		Environment="";
		Browser="";
		
		Test_Sheet_Path="";
		
		Scenario_Name="";
		
	}
	
	//**************************************************************************************************
	//@Method Name: launchUrl
	//@Description: Url to navigate in the browser
	//@Input Parameters: Url to be launched and Title to verify(if required)
	//@Output Parameters: Boolean : True or False
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 18-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
//	public void launchUrl(String url, String verifyTitle, int browserIndex) {
	public void launchUrl(String url, String verifyTitle) {
		try {
			
			
			
			//Verify the URL
			driver.get(url);
			
			if (!verifyTitle.equalsIgnoreCase("")) {
				if (!driver.getTitle().equalsIgnoreCase(verifyTitle)) {
					System.out.println("Browser Launch failed");
					logger.info(url+"  launch failed");
				}
			}
				
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Application Launch Failed", e);
			reportStep("Application Launch Failed", "Fail", true);
		}
		
	}
	
	//**************************************************************************************************
	//@Method Name: launchBrowser
	//@Description: Launch the Browser depends on browser type
	//@Input Parameters: Name of the Browser 
	//@Output Parameters: Boolean : True or False
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 18-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public boolean launchBrowser(String browserName) {
		boolean bReturn = false;
		
		try {
			
			switch (browserName.toUpperCase())
			{
				case "CHROME":
					
					System.out.println("Launching Chrome Browser");
					logger.info("Launching Chrome Browser");
					String chrome = "./"+ config.getProperty("Browser_Drivers_Path") +"/chromedriver.exe";
					System.setProperty("webdriver.chrome.driver", chrome);
					
					//Cleaning the Chrome Memory
//					Runtime.getRuntime().exec("taskkill /F /IM ChromeDriver.exe");
					
					//Setting up IE chrome options
					ChromeOptions chromeOptions = new ChromeOptions();
					chromeOptions.addArguments("disable-infobars");
					chromeOptions.addArguments("start-maximized");
					chromeOptions.addArguments("chrome.switches", "--disable-extensions");
					
					//Creating the driver variable
					driver = new ChromeDriver(chromeOptions);
					
					/*DesiredCapabilities capabilities = DesiredCapabilities.chrome();
					capabilities.setBrowserName("chrome");
					capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
					capabilities.setPlatform(Platform.WINDOWS);
					driver = new RemoteWebDriver(new URL(node), capabilities);*/
					
//					 DesiredCapabilities capa =DesiredCapabilities.chrome();
					driver.manage().timeouts().implicitlyWait(Default_Wait_4_Page, TimeUnit.SECONDS);
					
					bReturn = true;
					break;
				
				case "IE":
					
					System.out.println("Launching IE Browser");
					logger.info("Launching IE Browser");
					String IE = "./"+ config.getProperty("Browser_Drivers_Path") + "/IEDriverServer.exe";
					System.setProperty("webdriver.ie.driver", IE);
					
					//Cleaning the iexplore memory
					Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
					Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
					
					//Setting up IE browser options
					InternetExplorerOptions ieOptions = new InternetExplorerOptions();
					ieOptions.introduceFlakinessByIgnoringSecurityDomains();
					ieOptions.requireWindowFocus();
					ieOptions.ignoreZoomSettings();
					ieOptions.introduceFlakinessByIgnoringSecurityDomains();
					Thread.sleep(2000);
					//Creating the driver variable
					driver = new InternetExplorerDriver(ieOptions);
					
					/*capabilities = DesiredCapabilities.internetExplorer();
			 		capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
			 		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			 		capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			 		capabilities.setCapability("allow-blocked-content", true);
			 		capabilities.setCapability("allowBlockedContent", true);
			 		capabilities.setPlatform(Platform.WINDOWS);
					driver = new RemoteWebDriver(new URL(node), capabilities);*/
					Thread.sleep(2000);
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(Default_Wait_4_Page, TimeUnit.SECONDS);
					
					WebElement zoomSettings = driver.findElement(By.tagName("html"));
					zoomSettings.sendKeys(Keys.chord(Keys.CONTROL, "0"));
					
					bReturn = true;
					break;
					
				case "FIREFOX":
					
					System.out.println("Launching FireFox Browser");
					logger.info("Launching FireFox Browser");
					String fireFox = "./"+ config.getProperty("Browser_Drivers_Path") +"/geckodriver.exe";
					System.setProperty("webdriver.gecko.driver", fireFox);
					
					FirefoxOptions firefoxOptions = new FirefoxOptions();
					
					//Creating the driver variable
					driver = new FirefoxDriver(firefoxOptions);
					driver.manage().timeouts().implicitlyWait(Default_Wait_4_Page, TimeUnit.SECONDS);
					
					bReturn = true;
					break;
			}
		}
		catch (Exception e) {
			System.err.println("Browser driver initiation failed - Exception");
			logger.error("Browser driver initiation failed - Exception",e);
			e.printStackTrace();
		}
		return bReturn;
	}
	
	
	//**************************************************************************************************
	//@Method Name: sendKeys
	//@Description: Enter values in text box, list, etc.
	//@Input Parameters: Field Name for reporting, By locator (Xpath, Id, Class) and Time out seconds 
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 18-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public void sendKeys(String fieldName, By locator, String strValue) {
		System.out.println(locator);
		WebElement element = waitForElement(driver, locator, 10);
		
		try {
			
			//Highlight
			flash(element);
			element.clear();
			element.sendKeys(strValue);
			logger.info(strValue+" is entered in the field "+fieldName);
			
			//Reporting feature to implemented
			reportStep(strValue +" is entered in the field: "+ fieldName , "PASS", false);
		}
		catch (Exception e) {
			reportStep(strValue +" is not entered in the field: "+ fieldName , "FAIL", false);
			logger.error(strValue+" is not  entered in the field "+fieldName,e);
		}
	}
	
	//**************************************************************************************************
	//@Method Name: sendKeys
	//@Description: Enter values in text box, list, etc.
	//@Input Parameters: Field Name for reporting, By locator (Xpath, Id, Class) and Time out seconds 
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 18-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public void sendKeysPassword(String fieldName, By locator, String strValue) {
		System.out.println(locator);
		WebElement element = waitForElement(driver, locator, 10);
		
		try {
			
			element.clear();
			flash(element);
			element.sendKeys(decryptPassword(strValue));
			
			//Reporting feature to implemented
			reportStep(strValue +" is entered in the field: "+ fieldName , "PASS", false);
			logger.info(strValue+" is entered in the field "+fieldName);
		}
		catch (Exception e) {
			reportStep(strValue +" is not entered in the field: "+ fieldName , "FAIL", false);
			logger.error(strValue+" is not  entered in the field "+fieldName,e);
		}
	}
	
	//**************************************************************************************************
	//@Method Name: clickButtonByName
	//@Description: Click the button by name
	//@Input Parameters: Field's/Button's text value 
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 18-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public void clickButtonByName(String fieldName) {
		String locator="//button[text()='"+fieldName+"']";
		System.out.println(locator);
		WebElement element = waitForElement(driver, By.xpath(locator), 10);		
		try {
			element.click();
			//Reporting
			flash(element);
			
			logger.info(fieldName +" is clicked susscessfully");
			//reportStep(fieldName +" is clicked susscessfully", "PASS", false);
		}
		catch (Exception e) {
			reportStep(fieldName +" is not clicked susscessfully", "Fail", true);
			logger.error(fieldName +" is  not clicked susscessfully",e);
			e.printStackTrace();
		}
	}
	
	//Radio button,button,checkbox,editbox,link
	public void anyClick(String fieldName, By locator) {
		System.out.println(locator);
		WebElement element = waitForElement(driver, locator, 30);
		
		try {
			
			flash(element);
			element.click();
			Thread.sleep(2000);
			logger.info(fieldName +" is clicked successfully");
			//reportStep(fieldName +" is clicked successfully", "PASS", false);
		}
		catch (Exception e) {
			
			reportStep(fieldName +" is not clicked successfully", "Fail", true);
			logger.error(fieldName +" is  not clicked successfully",e);
			e.printStackTrace();
		}
	}
	
	//**************************************************************************************************
	//@Method Name: waitForElement
	//@Description: Sync handling
	//@Input Parameters: driver, By object, timeout Seconds 
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 18-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public WebElement waitForElement(WebDriver driver,final By by,int timeOutInSeconds) {
		WebElement element;
		try {
			
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify the default timeout
			
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			
			element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			
			driver.manage().timeouts().implicitlyWait(Default_Wait_4_Page, TimeUnit.SECONDS);
			return element;//return the element
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//**************************************************************************************************
	//@Method Name: htmlTableColumnNamePosition
	//@Description: To get the column position 
	//@Input Parameters: Column name and Xpath of the table
	//@Output Parameters: Integer of column position
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 18-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public int htmlTableColumnNamePosition(String colName, String  Xpath) {
		int iReturn = -1;
		try {
			System.out.println();
			List<WebElement> headerRow = driver.findElements(By.xpath(Xpath));
			List<WebElement> columns;
			//nullify the Default wait time.
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			
			//Verify the Header tags 
			if(headerRow.get(0).findElements(By.tagName("th")).size() > 0)
			{
				columns=headerRow.get(0).findElements(By.tagName("th"));
			}
			else
			{
				columns=headerRow.get(0).findElements(By.tagName("td"));
			}
			int colCount = columns.size();
			for (int i=0; i<colCount; i++) 
			{
				if(columns.get(i).getText().equalsIgnoreCase(colName)) 
				{
					iReturn = i;
					break;
				}
			}
			
		}
		catch (Exception e) {
			System.out.println("Column header not found exception: "+colName);
			logger.error("Column header not found exception: "+colName,e);
			reportStep("Column header not found exception: "+ colName, "Failed", true);
		}
		//Reset to Default timeout
		driver.manage().timeouts().implicitlyWait(Default_Wait_4_Page, TimeUnit.SECONDS);
		return iReturn;
	}
	
	//**************************************************************************************************
	//@Method Name: selectDropDownValue
	//@Description: To select the value in the list  
	//@Input Parameters: fieldName, Locator and selecting value
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 18-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public void selectDropDownValue(String fieldName, By locator, String strValue) {
		
		WebElement element = waitForElement(driver, locator, 10);
		try {
			
			//element.click();
			Select selectValue = new Select(element);
			flash(element);
			//element.click();
			selectValue.selectByValue(strValue);
			//selectValue.selectByIndex(1);
			
			if (selectValue.getFirstSelectedOption().getText().equalsIgnoreCase(strValue)) {
				reportStep(strValue+" value selected successfully in "+ fieldName, "Pass", false);
				logger.info(strValue+" value selected successfully in "+fieldName);
			}
			else
			{
				reportStep(strValue+" value is not selected in "+ fieldName, "Fail", true);
				logger.info(strValue+" value is not selected successfully in "+fieldName);
			}
			
		}
		catch(Exception e) {
			System.out.println("Drop down selection failed, Exception "+ fieldName);
			logger.error("Drop down selection failed, Exception "+fieldName,e);
			reportStep("Drop down selection failed, Exception "+ fieldName, "Failed", true);
		}
		
	}
	
	//**************************************************************************************************
	//@Method Name: selectDropDownValue
	//@Description: To select the value in the list  
	//@Input Parameters: fieldName, Locator and selecting value
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 18-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public void selectDropDownByIndex(String fieldName, By locator, String strValue) {
		
		WebElement element = waitForElement(driver, locator, 10);
		try {
			
			//element.click();
			Select selectValue = new Select(element);
			
			List<WebElement> lsDropdown = selectValue.getOptions();
			
			for (int i = 0; i < lsDropdown.size() - 1; i++)
			{
				if (lsDropdown.get(i).getText().equalsIgnoreCase(strValue))
				{
					selectValue.selectByIndex(i);
					break;
				}
				else if (i == lsDropdown.size()-1) {
					break;
				}
			}
			
			element = waitForElement(driver, locator, 10);
			selectValue = new Select(element);
			
			System.out.println(selectValue.getOptions().get(0).getText());
			
			if (selectValue.getFirstSelectedOption().getText().equalsIgnoreCase(strValue)) {
				reportStep(strValue+" value selected successfully in "+ fieldName, "Pass", false);
				logger.info(strValue+" value selected successfully in "+fieldName);
			}
			else
			{
				reportStep(strValue+" value is not selected in "+ fieldName, "Fail", true);
				logger.error(strValue+" value is not selected successfully in "+fieldName);
			}
			
		}
		catch(Exception e) {
			System.out.println("Drop down selection failed, Exception "+ fieldName);
			logger.error("Drop down selection failed, Exception "+fieldName,e);
			reportStep("Drop down selection failed, Exception "+ fieldName, "Failed", true);
		}
		
	}
	
	//**************************************************************************************************
	//@Method Name: selectRadioButtonByValue
	//@Description: To select the value in the list  
	//@Input Parameters: fieldName, Locator and selecting value
	//@Output Parameters: Boolean 
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 13-02-2018
	//@Last Modified: 
	//**************************************************************************************************
	public boolean selectRadioButtonByValue(String fieldName, By locator, String strValue) {
		boolean bReturn = false;
		
		try {
			
			List<WebElement> radio = driver.findElements(locator);
			int size = radio.size();
			
			if (size > 0) 
			{
				for (int i=0; i<=size-1; i++)
				{	
					int j = i + 1;
					String actualText = radio.get(i).getAttribute("value");
					if (actualText.trim().equals(strValue))
					{
						radio.get(i).click();
						reportStep(fieldName+" radio button value: "+ strValue +" is clicked", "Pass", true);
						logger.info(fieldName+" radio button value: "+ strValue +" is clicked");
						
						bReturn = true;
						break;
					}
					else if (i == size -1)
					{
						reportStep(fieldName+" radio button value: "+ strValue +" is not available", "Fail", true);
						logger.error(fieldName+" radio button value: "+ strValue +" is not available");
					}
				}
			}
			else
			{
				reportStep(fieldName+" radio button object not exist", "Fail", true);
				logger.error(fieldName+" radio button object not exist");
			}
		}
		catch(Exception e) {
			System.out.println("Drop down selection failed, Exception "+ fieldName);
			logger.error("Drop down selection failed, Exception "+ fieldName,e);
			reportStep("Drop down selection failed, Exception "+ fieldName, "Failed", true);
		}
		return bReturn;
	}
	
	//**************************************************************************************************
	//@Method Name: verifySelectedDropDownValue
	//@Description: To select the value in the list  
	//@Input Parameters: fieldName, Locator and selecting value
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 29-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public void verifySelectedDropDownValue(String fieldName, By locator, String strValue) {
		
		WebElement element = waitForElement(driver, locator, 10);
		try {

			Select selectValue = new Select(element);
			
			if (selectValue.getFirstSelectedOption().getText().equalsIgnoreCase(strValue)) {
				reportStep(strValue+" value selected successfully in "+ fieldName, "Pass", false);
				logger.info(strValue+" value selected successfully in "+ fieldName);
			}
			else
			{
				reportStep(strValue+" value is not selected in "+ fieldName, "Fail", true);
				logger.error(strValue+" value is not selected in "+ fieldName);
			}
			
		}
		catch(Exception e) {
			System.out.println("Drop down verify failed, Exception "+ fieldName);
			logger.error("Drop down verify failed, Exception "+ fieldName, e);
			reportStep("Drop down verify failed, Exception "+ fieldName, "Failed", true);
		}
		
	}
	
	//**************************************************************************************************
	//@Method Name: verifyElementExistReturn
	//@Description: To check the element exist (without reporting feature)  
	//@Input Parameters: Locator (object locator to find)
	//@Output Parameters: True or False
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 29-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public boolean verifyElementExistReturn(By locator) {
		boolean bReturn = false;
		
		try {
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); //nullify the default timeout
			WebElement element = driver.findElement(locator);
			if (element.isDisplayed() == true && element.isEnabled() == true) {
				bReturn = true;
			}
			
				
		}
		catch (Exception e) {
			System.out.println("Element not exist method - thrown Exception"+e);
			logger.error("Element not exist method - thrown Exception",e);
		}
		driver.manage().timeouts().implicitlyWait(Default_Wait_4_Page, TimeUnit.SECONDS);
		return bReturn;
	}
	
	//**************************************************************************************************
	//@Method Name: verifyStringCompare
	//@Description: To compare strings  
	//@Input Parameters: field, expected, actual
	//@Output Parameters: True or False
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 29-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public boolean verifyStringCompare(String fieldName, String expValue, String actValue) {
		boolean bReturn = false;
		
		try {
			Thread.sleep(2000);
			
			if (expValue.equalsIgnoreCase(actValue)) {
				reportStep(fieldName +" displayed as expected - value: "+ expValue, "PASS", false);
				logger.info(fieldName +" displayed as expected - value: "+ expValue);
				bReturn = true;
			}
			else
			{
				reportStep(fieldName +" Expected: "+ expValue +" Actual: "+actValue, "FAIL", true);
				logger.error(fieldName +" Expected: "+ expValue +" Actual: "+actValue);
			}
				
		}
		catch (Exception e) {
			System.out.println(fieldName + " Element not exist method - thrown Exception");
			logger.error(fieldName + " Element not exist method - thrown Exception",e);
			reportStep(fieldName + " element not exist method - thrown Exception", "FAIL", true);
		}
		
		return bReturn;
	}
	
	public boolean verifyAuthenticationPopup(String UserName, String Password) {
		boolean bReturn = false;
		
		try {
			
			WebDriverWait wait = new WebDriverWait(driver, 10); 
			
			Alert alertPopUp = wait.until(ExpectedConditions.alertIsPresent());
			
			alertPopUp = driver.switchTo().alert();
						
			
			Robot rb = new Robot();

		     //Enter user name by ctrl-v
		     StringSelection username = new StringSelection(UserName);
		     Toolkit.getDefaultToolkit().getSystemClipboard().setContents(username, null);            
		     rb.keyPress(KeyEvent.VK_CONTROL);
		     rb.keyPress(KeyEvent.VK_V);
		     rb.keyRelease(KeyEvent.VK_V);
		     rb.keyRelease(KeyEvent.VK_CONTROL);

		     //tab to password entry field
		     rb.keyPress(KeyEvent.VK_TAB);
		     rb.keyRelease(KeyEvent.VK_TAB);
		     Thread.sleep(2000);

		     //Enter password by ctrl-v
		     StringSelection pwd = new StringSelection(Password);
		     Toolkit.getDefaultToolkit().getSystemClipboard().setContents(pwd, null);
		     rb.keyPress(KeyEvent.VK_CONTROL);
		     rb.keyPress(KeyEvent.VK_V);
		     rb.keyRelease(KeyEvent.VK_V);
		     rb.keyRelease(KeyEvent.VK_CONTROL);

		     //press enter
		     rb.keyPress(KeyEvent.VK_ENTER);
		     rb.keyRelease(KeyEvent.VK_ENTER); 
			
		     System.out.println("Test12311");
			
		}
		catch (Exception e)
		{
			//No Pop up
			bReturn = true;
		}
		return bReturn;
	}
	
	//**************************************************************************************************
	//@Method Name: verifyElementExist
	//@Description: To check the element exist  
	//@Input Parameters: Locator (object locator to find)
	//@Output Parameters: True or False
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 29-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public boolean verifyElementExist(String fieldName, By locator) {
		boolean bReturn = false;
		
		try {
			WebElement element = waitForElement(driver, locator, 30);
			
			if (element.isDisplayed() == true && element.isEnabled() == true) {
				reportStep(fieldName +" element displayed as expected", "PASS", false);
				logger.info(fieldName +" element displayed as expected");
				bReturn = true;
			}
			else
			{
				reportStep(fieldName +" element is not displayed", "FAIL", true);
				logger.error(fieldName +" element is not displayed");
			}
				
		}
		catch (Exception e) {
			System.out.println(fieldName + " Element not exist method - thrown Exception"+e);
			logger.error(fieldName + " Element not exist method - thrown Exception",e);
			reportStep(fieldName + " element not exist method - thrown Exception"+e, "FAIL", true);
		}
		
		return bReturn;
	}
	
	//**************************************************************************************************
	//@Method Name: verifyPageTitle
	//@Description: To check the page title 
	//@Input Parameters: Browser title name as String
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 29-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public void verifyPageTitle(String titleValue) {
		
		try {
			
			
			if (driver.getTitle().equalsIgnoreCase(titleValue)) {
				reportStep(titleValue +" page title displayed as expected", "PASS", false);
				logger.info(titleValue +" page title displayed as expected");
			}
			else
			{
				reportStep("Actual Page Title: " + driver.getTitle() +" Expected Page Title: "+ titleValue +" Error: Title mismatched", "Fail", true);
				logger.error("Actual Page Title: " + driver.getTitle() +" Expected Page Title: "+ titleValue +" Error: Title mismatched");
			}

		}
		catch (Exception e) {
			
			reportStep(titleValue +" title verify exeception", "Fail", true);
			logger.error(titleValue +" title verify exeception",e);
			e.printStackTrace();
		}
	}
	
	//**************************************************************************************************
	//@Method Name: takeSnap
	//@Description: Abstract method of Reporting class. To copy the screenshot.
	//@Input Parameters: NIL
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 29-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public long takeSnap(){
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L; 
		try {
			
			File screenShot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			
			FileUtils.copyFile(screenShot, new File("./reports/images/"+number+".png"));
			
		} catch (WebDriverException e) {
			reportStep("The browser has been closed.", "FAIL", true);
			logger.info("The browser has been closed.");
		} catch (IOException e) {
			reportStep("The snapshot could not be taken", "WARN", false);
			logger.error("The snapshot could not be taken",e);
		}
		return number;
	}
	
	//**************************************************************************************************
	//@Method Name: decryptPassword
	//@Description: To decrypt the password 
	//@Input Parameters: Send the encrypted password 
	//@Output Parameters: Decrypted password
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 29-Dec-2017
	//@Last Modified: 
	//**************************************************************************************************
	public String decryptPassword(String strPassword) {
		String strTemp = ""; 
		try {
			
			int intLen = strPassword.length();
			 for(int i=0; i< intLen-1; i++) {
				 if (i%2 == 0)
				 {
					 strTemp = strTemp + strPassword.substring(i, i+1);
				 }
			 }
			 String strPassword1 = new StringBuffer(strTemp).reverse().toString();
			 
			 return strPassword1;
			
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception - Password Decrypt Method");
			return null;
		}
		
	}
	
	//**************************************************************************************************
	//@Method Name: isAlertPresent
	//@Description: To Verify Alert Present 
	//@Input Parameters: String - send OK or Cancel 
	//@Output Parameters: true
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 20-Feb-2017
	//@Last Modified: 
	//**************************************************************************************************
	
	public boolean isAlertPresent(String clickAction)
	{
		boolean bReturn = false;
		try {
				if (clickAction.equalsIgnoreCase("OK"))
				{
					driver.switchTo().alert().accept();
					reportStep("Clicked "+ clickAction +" in alert message" , "PASS", false);
					logger.info("Clicked "+ clickAction +" in alert message");
					bReturn = true;
				}
				else
				{
					driver.switchTo().alert().dismiss();
					reportStep("Clicked "+ clickAction +" in alert message" , "PASS", false);
					logger.info("Clicked "+ clickAction +" in alert message");
					bReturn = true;
				}
		}
		catch (Exception e)
		{
			bReturn = false;
			logger.error("Alert box is not present ", e);
		}
		return bReturn;
	}
	
	//**************************************************************************************************
	//@Method Name: verifyAlertText
	//@Description: To decrypt the password 
	//@Input Parameters: Send the encrypted password 
	//@Output Parameters: Decrypted password
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 20-Feb-2017
	//@Last Modified: 
	//**************************************************************************************************
	
	public boolean verifyAlertText(String data)
	{
		boolean bReturn = false;
		try {
			String text = driver.switchTo().alert().getText();
			if (text.contains(data))
			{
				reportStep("Alert message- As Expected: "+ text, "PASS", false);
				logger.info("Alert message- As Expected: "+ text);
				driver.switchTo().alert().accept();
			}
			else
			{
				reportStep("Incorrect alert message- Expected: "+ data + " Actual: " + text, "FAIL", true);
				logger.info("Incorrect alert message- Expected: "+ data + " Actual: " + text);
				driver.switchTo().alert().accept();
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("No alert available in the screen - failed" , "FAIL", true);
			logger.error("No alert available in the screen - failed", e);
		}
		return bReturn;
	}
	
	//**************************************************************************************************
	//@Method Name: dtDateConversion
	//@Description: To convert the date to dd/MMM/yyyy 
	//@Input Parameters: Send the data 
	//@Output Parameters: converted date
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 20-Feb-2017
	//@Last Modified: 
	//**************************************************************************************************
	public String dtDateConversion(String data) {
		int dateValue;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
		Calendar c = Calendar.getInstance();
		try {
			data = data.replace("<", "").replace(">", "");
			if (data.contains("+"))
			{
				dateValue = Integer.parseInt(data.split("\\+")[1]);
				c.add(Calendar.DATE, dateValue);
				
			}
			else if(data.contains("-"))
			{
				dateValue = Integer.parseInt(data.split("-")[1]);
				c.add(Calendar.DATE, -dateValue);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Element - Unhandled exception: Date retrieve failed. Data: "+ data , "FAIL", false);
			logger.error("Element - Unhandled exception: Date retrieve failed. Data: "+ data, e);
		}
		
		return sdf.format(c.getTime());
	}
		
	//**************************************************************************************************
	//@Method Name: dtCalendarDateSelection
	//@Description: To select the given date month and year
	//@Input Parameters: 
	//@Output Parameters: Decrypted password
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 20-Feb-2017
	//@Last Modified: 
	//**************************************************************************************************
	public boolean dtCalendarDateSelection(String calObjString, String dtSelect) {
       boolean bReturn = false;
       		
       		System.out.println(dtSelect);
       		
          String calObj = prop.getProperty(calObjString + ".Calendar");
          String calMonth = prop.getProperty(calObjString + ".Calendar.Month.Text");
          String calYear = prop.getProperty(calObjString + ".Calendar.Year.Text");
          String calLeftClick = prop.getProperty(calObjString + ".Calendar.Left");
          String calRightClick = prop.getProperty(calObjString + ".Calendar.Right");
          String calDayClick = prop.getProperty(calObjString + ".Calendar.Day");
          
          String day = dtSelect.split("/")[0];
          String month = dtSelect.split("/")[1];
          int year =  Integer.parseInt(dtSelect.split("/")[2]);
          
          String key = calRightClick;
          try {
                 //Verifying the Calendar Exist
                 if (verifyElementExistReturn(By.xpath(calObj)) == true)
                 {
                       //Selecting the Year
                       int displayedYear = Integer.parseInt(driver.findElement(By.xpath(calYear)).getText());
                       int countValue;
                       countValue = displayedYear - year;
                       if (countValue > 0) {
                              key = calLeftClick;
                       } 
                       
                       int intTemp = 0;
                       int intTemp1 = 0;
                       while (intTemp != Math.abs(countValue))
                       {
                              do {
                                     driver.findElement(By.xpath(key)).click();
                                     if (Integer.parseInt(driver.findElement(By.xpath(calYear)).getText()) == year) {
                                          break;
                                     }
                                     intTemp1 = intTemp1 + 1;
                              } while(intTemp1 != 12);
                              intTemp = intTemp + 1;
                       }
                       if (!(Integer.parseInt(driver.findElement(By.xpath(calYear)).getText()) == year)) {
                              reportStep("Selection of year is failed: "+year,"Fail",true);
                              return false;
                       }
                       key = calRightClick;
                       
                       //Selecting the Month
                       intTemp = 0;
                       int selectMonth = dtMonthValue(month);
                       int displayedMonth = dtMonthValue(driver.findElement(By.xpath(calMonth)).getText().trim());
                       countValue = displayedMonth - selectMonth;
                       if (countValue > 0) {
                              key = calLeftClick;
                       } 
                       
                       while (!driver.findElement(By.xpath(calMonth)).getText().toLowerCase().contains(month.toLowerCase()))
                       {
                              driver.findElement(By.xpath(key)).click();
                              intTemp = intTemp + 1;
                              if (intTemp == 12) {
                                     reportStep("Selection of month is failed: "+month,"Fail",true);
                                     break;
                              }
                       }
                       
                       day = String.valueOf(Math.abs(Integer.parseInt(day)));
                       //Selecting the Day
                       driver.findElement(By.xpath(calDayClick.replace("&Value", day))).click();
                       
                 }
                 else
                 {
                       reportStep("Calendar :"+ calObjString +" not exist","FAIL", true);
                 }
                 
                 
          }
          
          catch (Exception e)  {
                 e.printStackTrace();
                 reportStep("Element - Unhandled exception: Calendar select failed. Data: ", "FAIL", false);
                 logger.error("Element - Unhandled exception: Calendar select failed. Data: ", e);
          }
          return bReturn;
       }

	//**************************************************************************************************
	//@Method Name: dtMonthValue
	//@Description: To retrieve the month value in number format
	//@Input Parameters: month Name
	//@Output Parameters: month number
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 20-Feb-2017
	//@Last Modified: 
	//**************************************************************************************************
	public int dtMonthValue(String monthName) {
		try {
			Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthName);
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(date);
		    int selectMonth = cal.get(Calendar.MONTH);
		    cal.clear();
		    return selectMonth;
		}
		catch (Exception e) {
			System.out.println("InCorrect Month Exception. Month: "+monthName);
			logger.error("InCorrect Month Exception. Month: ", e);
			 return -1;
		}
	}	

	//**************************************************************************************************
	//@Method Name: tsTimeConversion
	//@Description: To convert the time format to HH:MM
	//@Input Parameters: Send the data
	//@Output Parameters: converted time
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 20-Feb-2017
	//@Last Modified: 
	//**************************************************************************************************
	public String tsTimeConversion(String data) {
		int TimeValue;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");
		Calendar t = Calendar.getInstance();
		try {
			data = data.replace("<", "").replace(">", "");
			if (data.contains("+"))
			{
				TimeValue = Integer.parseInt(data.split("\\+")[1]);
				t.add(Calendar.HOUR, TimeValue);
				
			}
			else if(data.contains("-"))
			{
				TimeValue = Integer.parseInt(data.split("-")[1]);
				t.add(Calendar.HOUR, -TimeValue);
				
			}
			  //System.out.println(" " +TimeValue); 
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Element - Unhandled exception: time retrieve failed. Data: "+ data , "FAIL", false);
			logger.error("Element - Unhandled exception: time retrieve failed. Data: "+ data, e);
		}
		
		return sdf.format(t.getTime());
	}
		
	//**************************************************************************************************
	//@Method Name: tsCalendarTimeSelection
	//@Description: To select the given time
	//@Input Parameters: 
	//@Output Parameters: 
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 20-Feb-2017
	//@Last Modified: 
	//**************************************************************************************************

	public boolean tsCalendarTimeSelection(String timeObjString, String tSelect) {
		boolean bReturn = false;

		System.out.println(tSelect);

		String timeObj = prop.getProperty(timeObjString + ".Time");

		String timeUpClick = prop.getProperty(timeObjString + ".Time.Up");
		String timeDownClick = prop.getProperty(timeObjString + ".Time.Down");

		SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");
		Calendar t = Calendar.getInstance();

		String curTime = sdf.format(t.getTime());
		System.out.println(curTime);
		int hour = Integer.parseInt(tSelect.split(":")[0]);

		try {

			String key = timeDownClick;
			int intTemp = 0;
			int countValue = Integer.parseInt(curTime.split(":")[0]) - hour;
			System.out.println(countValue);
			if (countValue > 0) {
				key = timeUpClick;
			}
			System.out.println(hour);
			timeObj = timeObj.replace("&Value", String.valueOf(hour));

			while (!driver.findElements(By.xpath(timeObj)).get(0).isDisplayed()) {
				driver.findElement(By.xpath(key)).click();
				intTemp = intTemp + 1;
				if (intTemp == Math.abs(countValue)) {
					reportStep("Selection of Date is failed: " + hour, "Fail", true);
					break;
				}
			}

			anyClick("Clicking the time: " + hour, By.xpath(timeObj));
			if (driver.findElements(By.xpath(timeObj)).get(0).isDisplayed())
			{
				anyClick("Clicking the time: " + hour, By.xpath(timeObj));
			}

		}

		catch (Exception e) {
			e.printStackTrace();
			reportStep("Element - Unhandled exception: Calendar select failed. Data: ", "FAIL", false);
			logger.error("Element - Unhandled exception: Calendar select failed. Data: ", e);
			
		}
		return bReturn;
	}
	//**************************************************************************************************
	//@Method Name: Random Number Generator
	//@Description: To generate random num
	//@Input Parameters: range
	//@Output Parameters: 
	//@Created by: Priya 
	//@Date Created: 07-Mar-2018
	//@Last Modified: 
	//**************************************************************************************************
	public int getRandomNumber(int range) {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(range);
		return randomInt;
	}
	
	//**************************************************************************************************
	//@Method Name: initializeLogger
	//@Description: Log4j - Creation of logs
	//@Input Parameters: NIL
	//@Output Parameters: NIL
	//@Created by: Renukadevi 
	//@Date Created: 07-Mar-2018
	//@Last Modified: 
	//**************************************************************************************************
	protected void initializeLogger() {
		Properties logProperties = new Properties();
		
		try {
		    // load log4j properties configuration file
		    logProperties.load(new FileInputStream(LOG_PROPERTIES_FILE));
		    PropertyConfigurator.configure(logProperties);
		    logger.info("Logging initialized.");
		} catch (IOException e) {
			logger.error("Unable to load logging property :", e);
		}
		/*try {
		   // FileInputStream fstream = new FileInputStream("./Log_Masweb.txt");
			FileInputStream fstream = new FileInputStream("./test.logs.txt");
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    while ((strLine = br.readLine()) != null) {
		        System.out.println(strLine);
		    }
		    in.close();
		} 
		catch (FileNotFoundException fe) {
		    logger.error("File Not Found", fe);
		    logger.warn("This is a warning message");
		    logger.trace("This message will not be logged since log level is set as DEBUG");
		} catch (IOException e) {
			logger.error("IOEXception occured:", e);
		}*/
		
		// Create reference variable “log” referencing getLogger method of Logger class, it is used to store logs in selenium.txt
		Logger log = Logger.getLogger("devpinoyLogger");
		
		// Call debug method with the help of referencing variable log and log the information in test.logs file
		//log.debug("Getting errors - Exception");
	}
	
	//**************************************************************************************************
	//@Method Name: flash
	//@Description: To Highlight the Object
	//@Input Parameters: NIL
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar S 
	//@Date Created: 07-Mar-2018
	//@Last Modified: 
	//**************************************************************************************************
	public static void flash(WebElement element) {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        String bgcolor  = element.getCssValue("backgroundColor");
        for (int i = 0; i <  3; i++) {
            changeColor("rgb(0,200,0)", element, js);
            changeColor(bgcolor, element, js);
        }
    }
	
	//**************************************************************************************************
	//@Method Name: changeColor
	//@Description: To highlight in different color
	//@Input Parameters: NIL
	//@Output Parameters: NIL
	//@Created by: Senthil Kumar S
	//@Date Created: 07-Mar-2018
	//@Last Modified: 
	//**************************************************************************************************
    public static void changeColor(String color, WebElement element,  JavascriptExecutor js) {
        js.executeScript("arguments[0].style.backgroundColor = '"+color+"'",  element);

        try {
            Thread.sleep(20);
        }  catch (InterruptedException e) {
        	
        }
     }

    
    public boolean retryingFindClick(By by) {
        boolean result = false;
        int attempts = 0;
        while(attempts < 3) {
            try {
                driver.findElement(by).click();
                result = true;
                break;
            } catch(Exception e) {
            }
            attempts++;
        }
        return result;
    } 

    
  //public void launchUrl(String url, String verifyTitle, int browserIndex) {
  	public void Agv_launchUrl(String url, String verifyTitle) {	
  	try {
  			
  			
  			//Verify the URL
  		driver.get(url);
  		driver.manage().window().maximize();
  		logger.info(url+" launched successfully");
  		
  		
  			if (!verifyTitle.equalsIgnoreCase("")) {
  				if (!driver.getTitle().equalsIgnoreCase(verifyTitle)) {
  					System.out.println("Browser Launch failed");
  					logger.error("Browser Launch failed");
  				}
  			}
  				
  		}
  		catch (Exception e) {
  			e.printStackTrace();
  			reportStep("Application Launch Failed", "Fail", true);
  			logger.error("Application Launch Failed", e);
  		}
  		
  	}

	public static boolean displayElement(WebDriver driver, WebElement e) {
		try

		{
			if (e.isDisplayed()) {
				return true;
			}
		}

		catch (Throwable t) {

		}
		return false;
	}

  	

	public static void clearByLocator( final By locator ) {
 		
  		 // final long startTime = System.currentTimeMillis();
  		  driver.manage().timeouts().implicitlyWait( 5, TimeUnit.SECONDS );
  		  Wait<WebDriver> wait = new FluentWait<WebDriver>( driver )
  		        .withTimeout(90000, TimeUnit.MILLISECONDS)
  		        .pollingEvery(5500, TimeUnit.MILLISECONDS);
  		        //.ignoring( StaleElementReferenceException.class );		
  		  wait.until( new ExpectedCondition<Boolean>() { 
  		    @Override 
  		    public Boolean apply( WebDriver webDriver ) {
  		      try {
  		        webDriver.findElement(locator).clear();
  		        System.out.println(locator);
  		        System.out.println("cleared");
  		        return true;
  		      } catch ( StaleElementReferenceException e ) {						
  		        
  		        return false;
  		      }		
  		    } 
  		  } );		
  		driver.manage().timeouts().implicitlyWait( Default_Implicit_Wait, TimeUnit.SECONDS );
  		}
	
	
public static void clickByLocator( final By locator ) {
        
        // final long startTime = System.currentTimeMillis();
         driver.manage().timeouts().implicitlyWait( 5, TimeUnit.SECONDS );
         Wait<WebDriver> wait = new FluentWait<WebDriver>( driver )
               .withTimeout(90000, TimeUnit.MILLISECONDS)
               .pollingEvery(5500, TimeUnit.MILLISECONDS);
               //.ignoring( StaleElementReferenceException.class );         
         wait.until( new ExpectedCondition<Boolean>() { 
           @Override 
           public Boolean apply( WebDriver webDriver ) {
             try {
               webDriver.findElement(locator).click();
               System.out.println(locator);
               System.out.println("clicked");
               return true;
             } catch ( StaleElementReferenceException e ) {                                     
               
               return false;
             }              
           } 
         } );        
       driver.manage().timeouts().implicitlyWait( Default_Wait_4_Page, TimeUnit.SECONDS );
       }

}
