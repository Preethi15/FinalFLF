package com.volvo.mfg.test;



import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.volvo.mfg.commonutilis.CommonWrapperMethods;
import com.volvo.mfg.commonutilis.JExcelUtils;



public class TestBase extends CommonWrapperMethods  {


	
	
	
	private JExcelUtils dataBook;
	LinkedHashMap<String , LinkedHashMap<String, String>> testData= new LinkedHashMap<String , LinkedHashMap<String, String>>();
	String temp;
	int count=0;
	String className; 
	public String testName;
	CommonWrapperMethods commonWrapperMethods;
	String UserName;
	
	@BeforeTest
	
	public void startTest() {
		System.out.println("BeforeTest");
		startResult();
		loadObject();
		suiteVariables();
		

	}
	
	@AfterTest
	public static void endTest(){
		
		
		
		endTestcase();
		

		

	}
	
	@BeforeClass
	
	public void setUp() throws Exception
	{
		System.out.println("Beforeclass");

		dataBook = new JExcelUtils(System.getProperty("user.dir") + "//src//test//resources//testData//"+"InputData.xls");

		
		className= this.getClass().getSimpleName();
		

		
	
	}
	
	@AfterClass
	public void cleanUp()
	{

		
		
		  endResult();
		
		  testDisplayResult();

	}
	
	@BeforeMethod
	@Parameters({ "browser"})
	public void InitTestMethod(@Optional("b") String browser,Method testMethod){
		
		launchBrowser(Browser);
		launchUrl(Url,"");
		
		this.testCaseName = testMethod.getName();
		//Added Newly
		if(!testData.isEmpty()){
		Set<String> dkey= testData.keySet();
		List<String> stringsList = new ArrayList<String>(dkey);
		temp = stringsList.get(count);
		testName = this.className +"_"+temp;
		System.out.println("testName: "+testName);
		Scenario_Name = testMethod.getName();
		Test testRunning = testMethod.getAnnotation(Test.class);
	    testDescription = testRunning.description();
		}
		else
		{
			testName = this.className;
		}
		
		  test = startTestCase(testName,testDescription);
	      test.assignAuthor("TechM_Automation");
	      test.assignCategory("Regression_Testing");

		
	}
	
	@AfterMethod
	public void afterMethods() {
		
		if (driver !=null) {
			 driver.quit();
		  System.out.println("...Exiting Automation Suite");
		  
		}
		
		System.out.println("@After Method");
		
		
	}
	
	@DataProvider(name="TestDataProvider")
	public Object[][] getTestData(Method testMethod)
	{
		count=0;
		testData.clear();
		System.out.println(className);
		
		this.dataBook.setSheet(className);
		
		LinkedHashMap<String , String> demo = null;
		int rowCnt= this.dataBook.getRowCount();
		String value, key = null;
		String [] rows = null;
		String[] columnNames =this.dataBook.getRowContent(1);
		for(int i = 2;i<rowCnt+1 ;i++)
		{
			rows = this.dataBook.getRowContent(i);
			if(rows[2].equalsIgnoreCase("Yes") && testMethod.getName().equalsIgnoreCase(rows[1]))
			//if(rows[2].equalsIgnoreCase("Yes") && testMethod.getName().equalsIgnoreCase(rows[1])&& rows[3].equalsIgnoreCase(userName1))
			{
				demo= new LinkedHashMap<String , String>();
				demo.clear();
				for (int index = 0; index < columnNames.length; index++)
				{
					key = columnNames[index];
					value= rows[index];
					if(value.startsWith("-"))
					{
						value = "";
					}
				    demo.put(key, value);
				}
				 
				testData.put(rows[0], demo);
		}
		}
		Object[][] objData = new Object[testData.size()][1];
        int i = 0;
        for (String eachValue : testData.keySet()) {
        	objData[i][0] = testData.get(eachValue);
            i++;
        }
        return objData;
	}

	
	
	public static ExtentTest getTestReporter() {
		return test;
	}

	public static void setTestReporter(ExtentTest test) {
		TestBase.test = test;
	}
	
	public static String getTimeStamp()
	{
		return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	}
	

}
