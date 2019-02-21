package com.volvo.mfg.reports;

import java.io.File;
import java.util.Date;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public abstract class Reports {
	
	public static ExtentTest test = null;
	public static ExtentReports extent;
	public String testCaseName, testDescription, categories, authors;
	String reportFileName;
	
	public ExtentReports startResult(){
		try {
			Date dtTimeStamp = new Date();
			reportFileName = "Volvo_MFG_Execution_Report_" + dtTimeStamp.toString().replaceAll(":","_").replaceAll(" ","_");
			extent = new ExtentReports("./reports/" + reportFileName +".html", true);
			//extent.loadConfig(new File("./extent-config.xml"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return extent;
	}
	
	public ExtentTest startTestCase(String testCaseName, String testDescription){
		test = extent.startTest(testCaseName, testDescription);
		return test;
	}

	public void endResult() {
		extent.flush();
	}

	public static void endTestcase() {
		extent.endTest(test);
		
		
		test = null;
	}
	
	
	public void testDisplayResult() {
		try {
			
			String browser="C:/Program Files/Internet Explorer/iexplore.exe ";
			Runtime runtime=Runtime.getRuntime();
			File resFile = new File("./reports/"+reportFileName+".html");
			runtime.exec(browser+ (resFile.getAbsolutePath()));
			
		}
		catch (Exception e) {
			System.err.println("Result file opening failed: "+reportFileName);
		}
	}
	
	
	public void reportStep(String desc, String status, boolean screenShot) {
		String strImagePath = "";
		
		if (desc.contains("#B")) {
			desc = desc.replace("#B", "<B><Font size=\"+1\">");
			if (desc.contains("#C"))
			{
				desc = desc.replace("#C", "</Font></B>");
			}
			else
			{
				desc = desc + "</Font></B>";
			}
		}
		else if (desc.contains("#C"))
		{
			desc = desc.replaceAll("#C", "");
		}
		
		long snapNumber = 100000l;
		try {
			if(screenShot == true) {
					
				snapNumber= takeSnap();
				File imgPath = new File("images/"+snapNumber+".png"); 
                String strimagePath=imgPath.toString();
                strImagePath = test.addScreenCapture(strimagePath); 
			}
			
			switch (status.toUpperCase())
			{
				case "PASS":
					test.log(LogStatus.PASS, desc+strImagePath);
					break;
				
				case "FAIL":
					test.log(LogStatus.FAIL, desc+strImagePath);
					//throw new RuntimeException("FAILED");
					break;
					
				case "INFO":
					test.log(LogStatus.INFO, desc+strImagePath);
					break;
				
				case "WARN": 
				case "WARNING":
					test.log(LogStatus.WARNING, desc+strImagePath);
					break;
				
				case "ERROR":
					test.log(LogStatus.ERROR, desc+strImagePath);
					break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
				
	}

	public abstract long takeSnap();
	
}
