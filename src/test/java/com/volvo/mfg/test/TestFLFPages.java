package com.volvo.mfg.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.testng.annotations.Test;

import com.volvo.mfg.commonutilis.CommonWrapperMethods;
import com.volvo.mfg.pagecomponents.FLF_Cases;
import com.volvo.mfg.pagecomponents.LoginPages;
	

public class TestFLFPages extends TestBase {
	
	LoginPages loginPages;
	FLF_Cases FLFcases;

	CommonWrapperMethods commonWrapperMethods;

	
	 @Test(dataProvider="TestDataProvider")
	 
	 public void PickingofKollis(LinkedHashMap<String,String>testData) throws IOException, InterruptedException, SQLException
	 {	 
		   loginPages=new LoginPages(driver);
		   FLFcases=new FLF_Cases(driver);
		
		 
		 loginPages.LogintoFLF(testData.get("UserName"),testData.get("Password"),testData.get("Expected"));
		 //FLFcases.Picking_Kollis(testData.get("Status"),"TestFLFPages",testData.get("TestCase_Name"));
		 //FLFcases.Deliver_Kollis(testData.get("Status"),testData.get("UpdateTrainName"),testData.get("Checkcode"));
	 	 FLFcases.Planning_Kolli(testData.get("Status"), "TestFLFPages", testData.get("TestCase_Name"));
	 	 //FLFcases.Making_Redcolour(testData.get("Status"));
	 	 FLFcases.Sok_Larm();
	 	 FLFcases.Release_Functionality(testData.get("Status"));
	 	 FLFcases.Change_Driver(testData.get("Status"));
	 	 FLFcases.Fold_out_Fold_in_Functionality();
		 FLFcases.Mad_Article(testData.get("StartAvMad"),testData.get("FromMad"),testData.get("ToMad"));
		 FLFcases.Mad_Edit_Functionality(testData.get("MadChk"));
		 FLFcases.Room(testData.get("Fabrik"), testData.get("Rum"), testData.get("Rumbeskrivning"));
		 FLFcases.Create_Room();
		 FLFcases.Edit_Room(testData.get("Fabrik"));
		 FLFcases.Remove_Room();
		 FLFcases.Tur_Search(testData.get("Tur"),testData.get("LeadTime"),testData.get("ChangeTime"));
		 FLFcases.MAD_to_MAS_Functionality();
		 FLFcases.Link_Tur(testData.get("Tur"));
		 //FLFcases.Link_Tur_Edit_Functionality();
		 //FLFcases.Move_Link_Tur(testData.get("Fabrik"),testData.get("Tur"));
		 FLFcases.Administration(testData.get("userDropdown"),testData.get("Cdsid"));
				  
		 System.out.println("FLF launched Successfully");
		 

		  
	      }
	
}