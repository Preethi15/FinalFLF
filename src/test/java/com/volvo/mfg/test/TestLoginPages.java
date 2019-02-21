package com.volvo.mfg.test;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.testng.annotations.Test;

import com.volvo.mfg.commonutilis.CommonWrapperMethods;
import com.volvo.mfg.pagecomponents.LoginPages;
	

public class TestLoginPages extends TestBase {
	
	LoginPages loginPages;	

	CommonWrapperMethods commonWrapperMethods;

	
	 @Test(dataProvider="TestDataProvider")
	 
	 public void FLFLogin(LinkedHashMap<String,String>testData) throws IOException, InterruptedException
	 {	 
		   loginPages=new LoginPages(driver);
		 
		
		 
		 loginPages.LogintoFLF(testData.get("UserName"),testData.get("Password"),testData.get("Expected"));
		
		 System.out.println("FLF launched Successfully");

		  
	      }

	 
	 
	 
}