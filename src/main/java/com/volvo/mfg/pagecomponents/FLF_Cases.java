package com.volvo.mfg.pagecomponents;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.volvo.mfg.commonutilis.CommonWrapperMethods;
import com.volvo.mfg.commonutilis.DB_Connectivity;
import com.volvo.mfg.commonutilis.ExcelUtils;
import com.volvo.mfg.commonutilis.QueryInput;


public class FLF_Cases extends CommonWrapperMethods implements QueryInput{
	
	private WebDriver driver;
	ExcelUtils excelUtils = new ExcelUtils();
	
	public FLF_Cases(WebDriver driver) {
		this.driver = driver;
	}
	public static String objectId="";
	public static String strFetchname="";
	String splitLevno="";
	String splitKollino="";
	public static String checkcode="";
	static String strLEDIGStatus="";
	static String strHAMTADStatus="";
	static String strFRISLAPPT="";
	static String strOplaneradStatus="";
	String TrainName="";
	static String NewTrainName="";
	private static final Pattern SPACE = Pattern.compile(" ");
	
	
	/*
	* 02 verify picking of Kollis in Köruppdrag tab Test Case Id:3701
	*/
	public void Picking_Kollis(String Status,String sheName,String colName) throws InterruptedException, SQLException, IOException{

	strLEDIGStatus = Status.split(",")[0];
	strHAMTADStatus = Status.split(",")[1];
	strFRISLAPPT = Status.split(",")[2];
	

	//Clicking on Köruppdrag tab
	anyClick("Köruppdrag Tab click", By.xpath(prop.getProperty("FLF.Home.Köruppdrag.Tab")));

	//clicking on Sok Uppd tab
	anyClick("Sok UPPD Tab click", By.xpath(prop.getProperty("FLF.Home.Sok.Uppd.Tab")));

	//verifying Fabrik and Välj is displayed
	verifyElementExistReturn(By.xpath(prop.getProperty("FLF.Köruppdrag.Page.Fabrik.Välj.Verify")));
	reportStep("#B Fabrik and Välj is displayed as expected #C", "pass", true);

	//clicking on 'C' button in Fabrik 
	driver.switchTo().frame("tabIframe");
	anyClick("C Button click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.C.Button")));

	//clicking on Rum in Välj
	anyClick("Rum click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.Rum")));

	//clicking on TCSLAST room
	anyClick("TCSLAST click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.TCSLAST")));

	driver.switchTo().defaultContent();
	//Clicking on Uppd tab
	anyClick("Uppd tab clcik", By.xpath(prop.getProperty("FLF.Home.Uppd.Tab")));
	driver.switchTo().frame("tabIframe");
	String LEDIGStatus = prop.getProperty("FLF.UPPD.Page.Status.Click").replace("&Value", strLEDIGStatus);
	String HAMTADStatus = prop.getProperty("FLF.UPPD.Page.Status.Click").replace("&Value", strHAMTADStatus);

	// Verify the UPPD table is displayed
	if (verifyElementExistReturn(By.xpath(LEDIGStatus)) == true) {

	//Selecting the Free[LEDIG] FLF Train
	driver.findElements(By.xpath(LEDIGStatus)).get(0).click();
	Thread.sleep(2000);

	

	strFetchname=driver.findElements(By.xpath(prop.getProperty("FLF.TrainName.HAMTAD.Status").replace("&Value", strHAMTADStatus))).get(0).getText();
	System.out.println("Train Names:" +strFetchname);
	excelUtils.UpdateValuesToExcel(sheName,"UpdateTrainName",colName,strFetchname);
	int fetchedTrainCount=driver.findElements(By.xpath(prop.getProperty("FLF.TrainName.PickedUp").replace("&Value", strFetchname))).size();
	System.out.println("fetchedTrainCount:" +fetchedTrainCount);

	
	for(int i=1;i<=fetchedTrainCount;i++) {
		
		if(i==1) {
			driver.findElements(By.xpath(prop.getProperty("FLF.TrainName.PickedUp").replace("&Value", strFetchname))).get(0).click();
		
			}else {
				driver.findElements(By.xpath(prop.getProperty("FLF.TrainName.PickedUp").replace("&Value", strFetchname))).get(0).click();
				driver.findElements(By.xpath(prop.getProperty("FLF.TrainName.PickedUp").replace("&Value", strFetchname))).get(0).click();
			}
		
		verifyElementExistReturn(By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window")));
		
		//Fetching objectId
		objectId=driver.findElements(By.xpath(prop.getProperty("FLF.UPPD.Page.ObjectID.Fetch"))).get(0).getText();
		System.out.println("objectId:" + objectId);
		reportStep("objectId:" +objectId, "pass", false);
		excelUtils.UpdateValuesToExcel(sheName,"ObjectID",colName,objectId);

		//Fetching Levnr,Kollinr and check code
		PickingKolli_DB_Verify(sheName,colName);

		reportStep("#B FLF train has been picked up from UPPD screen #C", "pass", false);
		reportStep("#B verified picking of Kollis in Köruppdrag tab Test Case Id:3701 #C", "pass", false);
		
	}

	
	}else {
	String FRISLAPPTStatus = prop.getProperty("FLF.UPPD.Page.Status.Click").replace("&Value", strFRISLAPPT);

	//Selecting the Free[LEDIG] FLF Train
	driver.findElements(By.xpath(FRISLAPPTStatus)).get(0).click();
	Thread.sleep(2000);

	String strFetchname=driver.findElements(By.xpath(prop.getProperty("FLF.TrainName.HAMTAD.Status").replace("&Value", strHAMTADStatus))).get(0).getText();
	excelUtils.UpdateValuesToExcel(sheName,"UpdateTrainName",colName,strFetchname);
	System.out.println("Train Names:" +strFetchname);
	int fetchedTrainCount=driver.findElements(By.xpath(prop.getProperty("FLF.TrainName.PickedUp").replace("&Value", strFetchname))).size();
	System.out.println("fetchedTrainCount:" +fetchedTrainCount);

	
	for(int i=1;i<=fetchedTrainCount;i++) {
		
		if(i==1) {
			driver.findElements(By.xpath(prop.getProperty("FLF.TrainName.PickedUp").replace("&Value", strFetchname))).get(0).click();
		
			}else {
				driver.findElements(By.xpath(prop.getProperty("FLF.TrainName.PickedUp").replace("&Value", strFetchname))).get(0).click();
				driver.findElements(By.xpath(prop.getProperty("FLF.TrainName.PickedUp").replace("&Value", strFetchname))).get(0).click();
			}
		
			verifyElementExistReturn(By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window")));

			//Fetching objectId
			objectId=driver.findElements(By.xpath(prop.getProperty("FLF.UPPD.Page.ObjectID.Fetch"))).get(0).getText();
			System.out.println("objectId:" + objectId);
			reportStep("objectId:" +objectId, "pass", false);
			excelUtils.UpdateValuesToExcel(sheName,"ObjectID",colName,objectId);

			//Fetching Levnr,Kollinr and check code
			PickingKolli_DB_Verify(sheName,colName);

			
				
			}

			reportStep("#B FLF train has been picked up from UPPD screen #C", "pass", false);
			reportStep("#B verified picking of Kollis in Köruppdrag tab Test Case Id:3701 #C", "pass", false);
}
}
	
	public void PickingKolli_DB_Verify(String sheName,String colName) throws InterruptedException,SQLException, IOException{
		
		// FLF connection details
		String ClassName = prop.getProperty(Environment +".FLF.ORACLE.ClassName");
		String ConnectionString = prop.getProperty(Environment +".FLF.ORACLE.Connection.String");
		String UserName = prop.getProperty(Environment +".FLF.ORACLE.User.Name");
		String Password = prop.getProperty(Environment +".FLF.ORACLE.Password");
		String Kollino="";
		
		DB_Connectivity db = new DB_Connectivity();
		ResultSet rsArticleNo= db.Connect_DB(ClassName, ConnectionString, UserName, Password,FLF_Picking_Kolli.replace("#objectId#", objectId));
		if(rsArticleNo.next()) {
			
			Kollino=rsArticleNo.getString("KOLLINR");
			System.out.println("Kollino:" + Kollino);
			reportStep("Kollino:" +Kollino, "pass", false);
			
			splitLevno=Kollino.substring(0, 5);
			System.out.println("splitLevno:" + splitLevno);
			//Entering the Lev No
			sendKeys("Lev No is entered", By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window.Lev.Nr")), splitLevno);
			reportStep("splitLevno:" +splitLevno, "pass", false);
			
			//Updating Lev no from DB to Test data
			excelUtils.UpdateValuesToExcel(sheName,"LevNo",colName,splitLevno);
			
			splitKollino=Kollino.substring(5).trim();
			System.out.println("splitKollino:" + splitKollino);
			//Entering Kolli no
			sendKeys("Kolli No is entered", By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window.Kollinr")), splitKollino);
			reportStep("splitKollino:" +splitKollino, "pass", false);
			
			//Updating Kolli no from DB to Test data
			excelUtils.UpdateValuesToExcel(sheName,"KolliNo",colName,splitKollino);
			
			
			checkcode=rsArticleNo.getString("FRAN_CHECKSIFFRA");
			System.out.println("checkcode:" + checkcode);
			//Entering Check code
			sendKeys("Checkcode is entered", By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window.Checkcode")), checkcode);
			reportStep("checkcode:" +checkcode, "pass", false);
			
			//Updating Check code from DB to Test data
			excelUtils.UpdateValuesToExcel(sheName,"Checkcode",colName,checkcode);
			
			//Clicking on green button
			anyClick("Ok button click", By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window.Ok.Button")));
			

			String errorCheckcode=driver.findElement(By.xpath(prop.getProperty("FLF.Picking.Window.Checkcode.ErrorMessage"))).getText();
			String errorLevNo=driver.findElement(By.xpath(prop.getProperty("FLF.Picking.Window.LevNo.ErrorMessage"))).getText();
			String errorKolliNo=driver.findElement(By.xpath(prop.getProperty("FLF.Picking.Window.KolliNo.ErrorMessage"))).getText();
			
			if(errorCheckcode.equalsIgnoreCase("Fel checkkod")||errorLevNo.equalsIgnoreCase("Fel lev.nr")||errorKolliNo.equalsIgnoreCase("Fel kollinr")) {
				
				System.out.println("FLF Error code message inside IF");
				
				try {
					
					do
								{
						
						rsArticleNo.next();
						
						Kollino=rsArticleNo.getString("KOLLINR");
						System.out.println("Kollino:" + Kollino);
						reportStep("Kollino:" +Kollino, "pass", false);
						
						
						
						splitLevno=Kollino.substring(0, 5);
						System.out.println("splitLevno:" + splitLevno);
						//Entering the Lev No
						sendKeys("Lev No is entered", By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window.Lev.Nr")), splitLevno);
						reportStep("splitLevno:" +splitLevno, "pass", false);
						
						//Updating Lev no from DB to Test data
						excelUtils.UpdateValuesToExcel(sheName,"LevNo",colName,splitLevno);
						
						splitKollino=Kollino.substring(5).trim();
						System.out.println("splitKollino:" + splitKollino);
						//Entering Kolli no
						sendKeys("Kolli No is entered", By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window.Kollinr")), splitKollino);
						reportStep("splitKollino:" +splitKollino, "pass", false);
						
						//Updating Kolli no from DB to Test data
						excelUtils.UpdateValuesToExcel(sheName,"KolliNo",colName,splitKollino);
						
						checkcode=rsArticleNo.getString("FRAN_CHECKSIFFRA");
						System.out.println("checkcode:" + checkcode);
						reportStep("checkcode:" +checkcode, "pass", false);
						//Updating Lev no from DB to Test data
						excelUtils.UpdateValuesToExcel(sheName,"Checkcode",colName,checkcode);
						
						//Entering Check code
						sendKeys("Checkcode is entered", By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window.Checkcode")), checkcode);
						
						//Clicking on green button
						anyClick("Ok button click", By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window.Ok.Button")));
						
					}while(!rsArticleNo.wasNull());
					
					
						}catch(Exception e){
							
							e.printStackTrace();
						}
				
			}
			
		}
		rsArticleNo.close();
	}
	
	
	/*
	 * 03 verify delivering of Kollis in Köruppdrag tab Test Case Id:3702
	 */
	public void Deliver_Kollis(String Status,String UpdateTrainName,String Checkcode) throws InterruptedException, SQLException {
		
		strLEDIGStatus = Status.split(",")[0];
		strHAMTADStatus = Status.split(",")[1];
		strFRISLAPPT = Status.split(",")[2];
		
		driver.switchTo().defaultContent();
		//Clicking on Köruppdrag tab
		driver.findElements(By.xpath(prop.getProperty("FLF.Home.Köruppdrag.Tab"))).get(0).click();
		
		//clicking on Sok Uppd tab
		anyClick("Sok UPPD Tab click", By.xpath(prop.getProperty("FLF.Home.Sok.Uppd.Tab")));
				
		//verifying Fabrik and Välj is displayed
		verifyElementExistReturn(By.xpath(prop.getProperty("FLF.Köruppdrag.Page.Fabrik.Välj.Verify")));
		reportStep("#B Fabrik and Välj is displayed as expected #C", "pass", true);
					
		//clicking on 'C' button in Fabrik 
		driver.switchTo().frame("tabIframe");
		anyClick("C Button click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.C.Button")));
					
		//clicking on Rum in Välj
		anyClick("Rum click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.Rum")));
		
		
		//Getting the background color of TCSLAST room, to make it unselect and select the UPPSTART room to deliver the articles
		String bgColor = driver.findElement(By.xpath(prop.getProperty("FLF.Köruppdrag.Page.TCSLAST.BgColor.Fetch"))).getCssValue("background-color");
		System.out.println("bgColor:" + bgColor);
		
		if(bgColor.equals("rgba(255, 145, 200, 1)")||bgColor.equals("rgb(255, 145, 200)")){
			
			//clicking on TCSLAST room
			anyClick("TCSLAST click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.TCSLAST")));
			
			//clicking on UPPSTART room
			anyClick("UPPSTART click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.UPPSTART")));
			
		}else {
			//clicking on UPPSTART room
			anyClick("UPPSTART click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.UPPSTART")));
		}
		
		driver.switchTo().defaultContent();
		//Clicking on Uppd tab
		anyClick("Uppd tab clcik", By.xpath(prop.getProperty("FLF.Home.Uppd.Tab")));
		
		driver.switchTo().frame("tabIframe");
		String fetchedTrainNameClick = prop.getProperty("FLF.UPPD.Page.Deliver.TrainName").replace("&Value", UpdateTrainName);
		//String FRISLAPPTStatus = prop.getProperty("FLF.UPPD.Page.Deliver.TrainName").replace("&Value", UpdateTrainName);
		
	
		//verify the train name is same as in TCSLAST room
		// Verify the UPPD table is displayed
		if (verifyElementExistReturn(By.xpath(fetchedTrainNameClick)) == true) {

				//Selecting the Free[LEDIG] FLF Train
				driver.findElements(By.xpath(fetchedTrainNameClick)).get(0).click();
				Thread.sleep(2000);
				
						
				//Fetching Object Id
				objectId=driver.findElements(By.xpath(fetchedTrainNameClick)).get(0).getText();
				reportStep("objectId:" +objectId, "pass", false);
				if(verifyElementExistReturn(By.xpath(prop.getProperty("FLF.Park.Train.Before.Deliver.Window.Verify")))==true) {
					
					//Clicking on cancel button
					anyClick("Park cancel button click before delivery", By.xpath(prop.getProperty("FLF.Park.Train.Before.Deliver.Window.Cancel.Button")));
					
					//Selecting the Free[LEDIG] FLF Train
					driver.findElements(By.xpath(fetchedTrainNameClick)).get(0).click();
					Thread.sleep(2000);
					
					
				}else {
					reportStep("Park train window is not displayed", "Pass", false);
				}
						
						
				}
		
		//Verifying pick up confirmation window displayed
		if(verifyElementExistReturn(By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window")))==true) {
			
			
			//Entering Check code
			sendKeys("#B Checkcode #C", By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window.Checkcode")), Checkcode);
			
			//Clicking on green button
			anyClick("Ok button click", By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window.Ok.Button")));
			
		}else {
			reportStep("Confirmation window is not displayed", "fail", false);
		}
		
		//need to fetch the row count to know the number of pallets present in the FLF train
		int i = 0;
		int rowCount = driver.findElements(By.xpath(prop.getProperty("FLF.UPPD.Page.Deliver.Pallets.Row.Count")+"/tbody/tr")).size();
		
		for(i=3;i<=rowCount;i++) {
			
			//clicking on each pallet in the train to deliver all the articles completely
			WebElement row=driver.findElement(By.xpath(prop.getProperty("FLF.UPPD.Page.Deliver.Pallets.Row.Count")+"/tbody/tr["+i+"]"));
			Thread.sleep(2000);
			
			//clicking on the pallets to deliver by getting the no of pallets present in the FLF tarin
			clickByLocator(By.xpath(prop.getProperty("FLF.UPPD.Page.Deliver.Pallets.Row.Count")+"/tbody/tr["+i+"]/td[2]"));
			
			if(verifyElementExistReturn(By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window")))==true) {
				
				//Clicking on green button
				anyClick("Ok button click", By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window.Ok.Button")));
				
			}else {
				reportStep("Confirmation window is not displayed", "fail", true);
			}
			
		}
		
		//Once everything has been delivered, Automatically Parking Screen will appear
		//Verify Parking screen display
		if(verifyElementExistReturn(By.xpath(prop.getProperty("FLF.Parking.Screen")))==true) {
					
				//Need to park the FLF, by sending values from DB to parking screen to park the FLF
				ParkFLF_DB_Verify();	
					
			}else {
					reportStep("Parking Screen is not displayed", "fail", true);
			}
				
		reportStep("#B verified delivering of Kollis in Köruppdrag tab Test Case Id:3702 #C", "pass", false);
		
	
	}
	
	
	/*
	 * DB Method to fetch the Lastplats Checkcode and park FLF Train
	 */
	
	public void ParkFLF_DB_Verify() throws InterruptedException,SQLException{
		
		// FLF connection details
		String ClassName = prop.getProperty(Environment +".FLF.ORACLE.ClassName");
		String ConnectionString = prop.getProperty(Environment +".FLF.ORACLE.Connection.String");
		String UserName = prop.getProperty(Environment +".FLF.ORACLE.User.Name");
		String Password = prop.getProperty(Environment +".FLF.ORACLE.Password");
		
		DB_Connectivity db = new DB_Connectivity();
		ResultSet rsLastPlats= db.Connect_DB(ClassName, ConnectionString, UserName, Password,FLF_Parking);
	
		do {
			rsLastPlats.next();
			
			String LastPlats=rsLastPlats.getString("CHECKSIFFRA");
			System.out.println("LastPlats:" + LastPlats);
			reportStep("LastPlats:" +LastPlats, "pass", false);
			
			//Entering value in Lastplats textbox
			sendKeys("#B Parking FLF code #C", By.xpath(prop.getProperty("FLF.Parking.Window.Lastplats.Textbox")), LastPlats);
			
			//Okay button click
			anyClick("Ok button click", By.xpath(prop.getProperty("FLF.UPPD.Page.Pickup.Confirmation.Window.Ok.Button")));
			
			if(verifyElementExistReturn(By.xpath(prop.getProperty("FLF.Parked.Message")))==true) {
			break;
			}
			
		}while(!rsLastPlats.wasNull());
		rsLastPlats.close();
		}
	
	/*
	 * 04 verify planning of Kollis in Köruppdrag tab Test case Id: 3703
	 */
	
	public void Planning_Kolli(String Status,String sheName,String colName) throws InterruptedException, IOException {
		
		strOplaneradStatus=Status.split(",")[3];
		String strArticleNo="";
		
		driver.switchTo().defaultContent();
		//Clicking on Köruppdrag tab
		anyClick("Köruppdrag Tab click", By.xpath(prop.getProperty("FLF.Home.Köruppdrag.Tab")));
		
		//Clicking on Brist tab
		anyClick("Brist tab Click", By.xpath(prop.getProperty("FLF.Home.Brist.Tab")));
		
		driver.switchTo().frame("tabIframe");
		
		//clicking on the record with status 'unplanned'[Oplanerad]
		String OplaneradStatus = prop.getProperty("FLF.UPPD.Page.Status.Click").replace("&Value", strOplaneradStatus);
		
		// Verify the Brist table is displayed
		if (verifyElementExistReturn(By.xpath(OplaneradStatus)) == true) {

				//Selecting the unplanned[OplaneradStatus] FLF Train
				driver.findElements(By.xpath(OplaneradStatus)).get(0).click();
				Thread.sleep(2000);
				
				if(verifyElementExistReturn(By.xpath(prop.getProperty("FLF.Brist.Oplanerad.Status.Plan.Window")))==true) {
					
					//Fetching ArticleNo
					strArticleNo=driver.findElements(By.xpath(prop.getProperty("FLF.Brist.Oplanerad.Status.Plan.Window.ArticleNo.Fetch"))).get(0).getText();
					reportStep("ArticleNo:" +strArticleNo, "pass", false);
					excelUtils.UpdateValuesToExcel(sheName,"ArticleNo",colName,strArticleNo);
					
					//Clicking on Plan now button
					anyClick("Plan Now click", By.xpath(prop.getProperty("FLF.Brist.Plan.Window.Plannow.Click")));
					
					
				}else {
					reportStep("Plan Window is not displayed", "fail", false);
				}
				
		}else {
					
			reportStep("Oplanerad Status is not present", "fail", false);
		}
		reportStep("#B verified planning of Kollis in Köruppdrag tab Test case Id: 3703 #C", "pass", false);
	}
	
	
	/*
	 * 05 verify Mark red button functionality in Shortage screen Test case Id: 3704
	 */
	
	public void Making_Redcolour(String Status) throws InterruptedException, SQLException{
		
		driver.switchTo().defaultContent();
		//Clicking on Köruppdrag tab
		anyClick("Köruppdrag Tab click", By.xpath(prop.getProperty("FLF.Home.Köruppdrag.Tab")));
		
		//verifying Fabrik and Välj is displayed
		verifyElementExistReturn(By.xpath(prop.getProperty("FLF.Köruppdrag.Page.Fabrik.Välj.Verify")));
		reportStep("#B Fabrik and Välj is displayed as expected #C", "pass", true);
					
		//clicking on 'C' button in Fabrik 
		driver.switchTo().frame("tabIframe");
		anyClick("C Button click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.C.Button")));
					
		//clicking on Rum in Välj
		anyClick("Rum click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.Rum")));
					
		//Getting the background color of UPPSTART room, to make it unselect and select the TCSLAST room to deliver the articles
		String bgColor = driver.findElement(By.xpath(prop.getProperty("FLF.Köruppdrag.Page.UPPSTART.BgColor.Fetch"))).getCssValue("background-color");
				
		if(bgColor.equals("rgba(255, 145, 200, 1)")||bgColor.equals("rgb(255, 145, 200)")){
					
				//clicking on UPPSTART room
				anyClick("UPPSTART click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.UPPSTART")));
					
				//clicking on TCSLAST room
				anyClick("TCSLAST click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.TCSLAST")));
					
		}else {
				//clicking on TCSLAST room
				anyClick("TCSLAST click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.TCSLAST")));
			}
		
	
		driver.switchTo().defaultContent();
		//Clicking on Brist tab
		anyClick("Brist tab Click", By.xpath(prop.getProperty("FLF.Home.Brist.Tab")));
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				
		driver.switchTo().frame("tabIframe");
				
		//Selecting the status other than unplanned  FLF Train
		//Take all of the elements under Column "Status" inside the table
		List<WebElement> columVal =  driver.findElements(By.xpath(prop.getProperty("FLF.Brist.Status.Rows.Fetch")));
			    
		// count the size of the rows to match with the size of the column state
		System.out.println("Size of the contents in the column state is : " +columVal.size());

		//Now for matching one of the content and then performing some action 
		//Start a for loop
		String oneVal = Status.split(",")[3];
		for(int i=0;i<=columVal.size();i++){
			   
			System.out.println("Content text is : " + columVal.get(i).getText());
			// match the content here in the if loop
			        if(!(columVal.get(i).getText().equals(oneVal))){
			            // perform action
			            columVal.get(i).click();
			            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			            
			            //Fetching Object Id from the make red confirmation window
			            String objectId=driver.findElements(By.xpath(prop.getProperty("FLF.Brist.Make.Red.Page.ObjectId.Fetch"))).get(0).getText();
			            reportStep("ObjectId:" +objectId, "pass", false);
			            
			            //Clicking on make red button after the confirmation window opens
			            anyClick("Make red button click", By.xpath(prop.getProperty("FLF.BRIST.Page.Click.Red")));
			            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			            
			            driver.switchTo().defaultContent();
			    		//Clicking on Uppd tab
			    		anyClick("Uppd tab clcik", By.xpath(prop.getProperty("FLF.Home.Uppd.Tab")));
			    		driver.switchTo().frame("tabIframe");
			    		driver.findElements(By.xpath(prop.getProperty("FLF.Brist.UPPD.Page.Mark.Red.ObjectID.Find").replace("&Value", objectId))).get(0).click();
			            
			            
			            break;
			        }
			    }
		
		reportStep("#B verified Mark red button functionality in Shortage screen Test case Id: 3704 #C", "pass", false);
					
	}

	
	/*
	 * 06 Verify the Sok Larm and Larm page in Köruppdrag tab Test Case ID:3705
	 */
	public void Sok_Larm() {
		
		driver.switchTo().defaultContent();
		//Clicking on Köruppdrag tab
		anyClick("Köruppdrag Tab click", By.xpath(prop.getProperty("FLF.Home.Köruppdrag.Tab")));
		
		//Clicking on Sok Larm tab
		anyClick("Sok Larm Tab click", By.xpath(prop.getProperty("FLF.Home.Sok.Larm.Tab")));
		
		driver.switchTo().frame("tabIframe");
		//clicking on 'C' button in Fabrik 
		anyClick("C Button click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.C.Button")));
		
		//Clicking on Causes in Sok Larm page
		anyClick("Causes click in sok larm page", By.xpath(prop.getProperty("FLF.Sok.Larm.Causes.Click")));
		
		//Clicking on Larm Tab
		driver.switchTo().defaultContent();
		anyClick("Larm Tab click", By.xpath(prop.getProperty("FLF.Home.Larm.Tab")));
		
		reportStep("#B Verified the Sok Larm and Larm page in Köruppdrag tab Test Case ID:3705 #C", "pass", false);
	}
	
	
	/*
	 * 07 verify Release functionality in spec tab Test Case ID:3706
	 */
	public void Release_Functionality(String Status) throws InterruptedException {
		
		strLEDIGStatus = Status.split(",")[0];
		strHAMTADStatus = Status.split(",")[1];
		String HAMTADStatus="";
		String strobjectId="";
		
		driver.switchTo().defaultContent();
		//Clicking on Köruppdrag tab
		anyClick("Köruppdrag Tab click", By.xpath(prop.getProperty("FLF.Home.Köruppdrag.Tab")));
		
		//clicking on Sok Uppd tab
		anyClick("Sok UPPD Tab click", By.xpath(prop.getProperty("FLF.Home.Sok.Uppd.Tab")));
				
		//verifying Fabrik and Välj is displayed
		verifyElementExistReturn(By.xpath(prop.getProperty("FLF.Köruppdrag.Page.Fabrik.Välj.Verify")));
		reportStep("#B Fabrik and Välj is displayed as expected #C", "pass", true);
					
		//clicking on 'C' button in Fabrik 
		driver.switchTo().frame("tabIframe");
		anyClick("C Button click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.C.Button")));
					
		//clicking on Rum in Välj
		anyClick("Rum click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.Rum")));
		
		//Getting the background color of UPPSTART room, to make it unselect and select the TCSLAST room 
		String bgColor = driver.findElement(By.xpath(prop.getProperty("FLF.Köruppdrag.Page.UPPSTART.BgColor.Fetch"))).getCssValue("background-color");
						
		if(bgColor.equals("rgba(255, 145, 200, 1)")||bgColor.equals("rgb(255, 145, 200)")){
							
				//clicking on UPPSTART room
				anyClick("UPPSTART click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.UPPSTART")));
							
				//clicking on TCSLAST room
				anyClick("TCSLAST click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.TCSLAST")));
							
		}else {
				//clicking on TCSLAST room
				anyClick("TCSLAST click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.TCSLAST")));
			}
					
		driver.switchTo().defaultContent();
		//Clicking on Uppd tab
		anyClick("Uppd tab clcik", By.xpath(prop.getProperty("FLF.Home.Uppd.Tab")));
		driver.switchTo().frame("tabIframe");
		String LEDIGStatus = prop.getProperty("FLF.UPPD.Page.Status.Click").replace("&Value", strLEDIGStatus);
		
		// Verify the UPPD table is displayed
		if (verifyElementExistReturn(By.xpath(LEDIGStatus)) == true) {

				//Selecting the Free[LEDIG] FLF Train
				driver.findElements(By.xpath(LEDIGStatus)).get(0).click();
				Thread.sleep(2000);
				
				HAMTADStatus = prop.getProperty("FLF.UPPD.Page.Status.Click").replace("&Value", strHAMTADStatus);
				verifyElementExistReturn(By.xpath(HAMTADStatus));
				
				strobjectId=driver.findElements(By.xpath(HAMTADStatus)).get(0).getText();
				reportStep("objectId:" +strobjectId, "pass", false);
		}else {
					reportStep("LEDIG status is not displayed in the page", "fail", true);
		}
		
		driver.switchTo().defaultContent();
		//Clicking on spec tab
		anyClick("Spec tab clcik", By.xpath(prop.getProperty("FLF.Home.Spec.Tab")));
		
		driver.switchTo().frame("tabIframe");
		//clicking on the Taken[Hamtad] status in spec tab, which is already picked in UPPD screen
		driver.findElements(By.xpath(prop.getProperty("FLF.Spec.Hamtad.Status.Click").replace("&Value", strHAMTADStatus).replace("&1Value", strobjectId))).get(0).click();
		
		if(verifyElementExistReturn(By.xpath(prop.getProperty("FLF.Spec.Confirmation.Window")))==true) {
			
			//clicking on Retrieve/Release button
			anyClick("Retrieve button click", By.xpath(prop.getProperty("FLF.Spec.Confirmation.Window.Frislapp.Click")));
			reportStep("Release button is clicked successfully", "pass", false);
			
		}else {
			reportStep("Confirmation window is not displayed", "fail", false);
		}
		reportStep("#B Verified Release functionality in spec tab Test Case ID:3706 #C", "pass", false);
	}
	
	/*
	 * 08 verify Change of driver in spec tab Test Case ID:3707
	 */
	public void Change_Driver(String Status) throws InterruptedException {
		
		strHAMTADStatus = Status.split(",")[1];
		
		driver.switchTo().defaultContent();
		//Clicking on Köruppdrag tab
		anyClick("Köruppdrag Tab click", By.xpath(prop.getProperty("FLF.Home.Köruppdrag.Tab")));
		
		//clicking on Sok Uppd tab
		anyClick("Sok UPPD Tab click", By.xpath(prop.getProperty("FLF.Home.Sok.Uppd.Tab")));
		
		//clicking on 'C' button in Fabrik 
		driver.switchTo().frame("tabIframe");
		anyClick("C Button click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.C.Button")));
							
		//clicking on Rum in Välj
		anyClick("Rum click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.Rum")));
							
		//Getting the background color of UPPSTART room, to make it unselect and select the TCSLAST room 
		String bgColor = driver.findElement(By.xpath(prop.getProperty("FLF.Köruppdrag.Page.UPPSTART.BgColor.Fetch"))).getCssValue("background-color");
						
		if(bgColor.equals("rgba(255, 145, 200, 1)")||bgColor.equals("rgb(255, 145, 200)")){
							
				//clicking on UPPSTART room
				anyClick("UPPSTART click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.UPPSTART")));
							
				//clicking on TCSLAST room
				anyClick("TCSLAST click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.TCSLAST")));
							
		}else {
				//clicking on TCSLAST room
				anyClick("TCSLAST click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.TCSLAST")));
			}
		
		driver.switchTo().defaultContent();
		//Clicking on spec tab
		anyClick("Spec tab clcik", By.xpath(prop.getProperty("FLF.Home.Spec.Tab")));
		
		driver.switchTo().frame("tabIframe");
		
		String strDriver=driver.findElements(By.xpath(prop.getProperty("FLF.Spec.Driver.Name.Fetch").replace("&Value", strHAMTADStatus))).get(0).getText();
		
		//Selecting the Driver that are allocated for FLF Train and Changing the driver
		driver.findElements(By.xpath(prop.getProperty("FLF.Spec.Driver.Name.Fetch.Click").replace("&Value", strHAMTADStatus).replace("&1Value", strDriver))).get(0).click();
		Thread.sleep(2000);
			
			
		if(verifyElementExistReturn(By.xpath(prop.getProperty("FLF.Spec.Confirmation.Window")))==true) {
				
			//clicking on Retrieve/Release button
			anyClick("Change Driver button click", By.xpath(prop.getProperty("FLF.Spec.Confirmation.Window.Ta.Click")));
			reportStep("Change Driver button is clicked successfully", "pass", false);
				
			}else {
				reportStep("Confirmation window is not displayed", "fail", false);
			}
		reportStep("#B Verified Change of driver in spec tab Test Case ID:3707 #C", "pass", false);	
	}
	
	
	/*
	 * 09 verify Fold out and Fold in functionality in spec tab Test Case ID:3708
	 */
	public void Fold_out_Fold_in_Functionality() {
		
		driver.switchTo().defaultContent();
		//Clicking on Köruppdrag tab
		anyClick("Köruppdrag Tab click", By.xpath(prop.getProperty("FLF.Home.Köruppdrag.Tab")));
				
		//clicking on Sok Uppd tab
		anyClick("Sok UPPD Tab click", By.xpath(prop.getProperty("FLF.Home.Sok.Uppd.Tab")));
				
		//clicking on 'C' button in Fabrik 
		driver.switchTo().frame("tabIframe");
		anyClick("C Button click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.C.Button")));
									
		//clicking on Rum in Välj
		anyClick("Rum click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.Rum")));
		
		//Getting the background color of TCSLAST room, to make it unselect and select the UPPSTART room 
		String bgColor = driver.findElement(By.xpath(prop.getProperty("FLF.Köruppdrag.Page.TCSLAST.BgColor.Fetch"))).getCssValue("background-color");
				
		if(bgColor.equals("rgba(255, 145, 200, 1)")||bgColor.equals("rgb(255, 145, 200)")){
					
			//clicking on TCSLAST room
			anyClick("TCSLAST click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.TCSLAST")));
					
			//clicking on UPPSTART room
			anyClick("UPPSTART click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.UPPSTART")));
					
		}else {
			//clicking on UPPSTART room
			anyClick("UPPSTART click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.UPPSTART")));
		}
		
		driver.switchTo().defaultContent();
		//Clicking on spec tab
		anyClick("Spec tab clcik", By.xpath(prop.getProperty("FLF.Home.Spec.Tab")));
		
		driver.switchTo().frame("tabIframe");
		for(int i=0;i<=1;i++) {
		driver.findElements(By.xpath(prop.getProperty("FLF.Spec.FirstRow.Click"))).get(0).click();
		
		if(verifyElementExistReturn(By.xpath(prop.getProperty("FLF.Spec.Confirmation.Window")))==true) {
			
			//Clicking on Fold out option
			anyClick("Fold out button clcik", By.xpath(prop.getProperty("FLF.Spec.Confirmation.Window.Fold.Click")));
			
		}else {
			reportStep("Confirmation window is not displayed", "fail", false);
		}
		}
		reportStep("#B Verified Fold out and Fold in functionality in spec tab Test Case ID:3708 #C", "pass", false);
	}
	
	
	/*
	 * 10 To change the status of record from Release to taken in uppd tab Test Case ID:3709
	 */
	public void Release_to_Taken(String Status) throws InterruptedException {
		
		strHAMTADStatus = Status.split(",")[1];
		strFRISLAPPT = Status.split(",")[2];
		
		driver.switchTo().defaultContent();
		//Clicking on Köruppdrag tab
		anyClick("Köruppdrag Tab click", By.xpath(prop.getProperty("FLF.Home.Köruppdrag.Tab")));
				
		//clicking on Sok Uppd tab
		anyClick("Sok UPPD Tab click", By.xpath(prop.getProperty("FLF.Home.Sok.Uppd.Tab")));
				
		//clicking on 'C' button in Fabrik 
		driver.switchTo().frame("tabIframe");
		anyClick("C Button click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.C.Button")));
					
		//clicking on Rum in Välj
		anyClick("Rum click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.Rum")));
		
		//Getting the background color of UPPSTART room, to make it unselect and select the TCSLAST room 
		String bgColor = driver.findElement(By.xpath(prop.getProperty("FLF.Köruppdrag.Page.UPPSTART.BgColor.Fetch"))).getCssValue("background-color");
						
		if(bgColor.equals("rgba(255, 145, 200, 1)")||bgColor.equals("rgb(255, 145, 200)")){
							
				//clicking on UPPSTART room
				anyClick("UPPSTART click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.UPPSTART")));
							
				//clicking on TCSLAST room
				anyClick("TCSLAST click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.TCSLAST")));
							
		}else {
				//clicking on TCSLAST room
				anyClick("TCSLAST click", By.xpath(prop.getProperty("FLF.Köruppdrag.Page.TCSLAST")));
			}
					
		driver.switchTo().defaultContent();
		//Clicking on Uppd tab
		anyClick("Uppd tab clcik", By.xpath(prop.getProperty("FLF.Home.Uppd.Tab")));
		driver.switchTo().frame("tabIframe");
		
		String FRISLAPPTStatus = prop.getProperty("FLF.UPPD.Page.Status.Click").replace("&Value", strFRISLAPPT);
		
		if (verifyElementExistReturn(By.xpath(FRISLAPPTStatus)) == true) {
		//Selecting the Free[LEDIG] FLF Train
		driver.findElements(By.xpath(FRISLAPPTStatus)).get(0).click();
		Thread.sleep(2000);
		
		//Verifying the picked train with status HAMTAD
		String HAMTADStatus = prop.getProperty("FLF.UPPD.Page.Status.Click").replace("&Value", strHAMTADStatus);
		
		}else {
			reportStep("FRISLAPPT Status is not displayed", "fail", false);
		}
		reportStep("#B Verified the status of record from Release to taken in uppd tab Test Case ID:3709 #C", "pass", false);
	}
	
	/*
	 * 11 To verify the search functionality in Mad artiklar screen Test Case ID:3710
	 */
	public void  Mad_Article(String StartAvMad,String FromMad,String ToMad) {
		
		driver.switchTo().defaultContent();
		//clicking on MAD/Artikel button
		anyClick("Mad/Article click", By.xpath(prop.getProperty("FLF.Home.Mad.Tab")));
		
		//clicking on Search button
		anyClick("Search click", By.xpath(prop.getProperty("FLF.Mad.Search.Button")));
		
		//Entering value in Start av mad Textbod
		sendKeys("Start Av Mad:", By.xpath(prop.getProperty("FLF.Mad.Start.Av.Mad.Textbox")), StartAvMad);
		
		//Entering value in From Mad
		sendKeys("From Mad:", By.xpath(prop.getProperty("FLF.Mad.From.Mad.Textbox")), FromMad);
		
		//Entering value in To Mad
		sendKeys("To Mad:", By.xpath(prop.getProperty("FLF.Mad.To.Mad.Textbox")), ToMad);
		
		//clicking on Create empty food[Skapa Tomma Madar] button
		anyClick("Search click", By.xpath(prop.getProperty("FLF.Mad.Skapa.Tomma.Madar.Button")));
		
		//Fetching the message once creating mad
		String Message=driver.findElements(By.xpath(prop.getProperty("FLF.Mad.Message.Fetch"))).get(0).getText();
		reportStep("#B Message: #C" +Message, "pass", true);
		reportStep("#B verified the search functionality in Mad artiklar screen Test Case ID:3710 #C", "pass", false);
	}
	
	/*
	 *12 To edit functionality in Food or articles screen Test Case ID:3711 
	 */
	public void Mad_Edit_Functionality(String MadChk) throws InterruptedException {
		
		driver.switchTo().defaultContent();
		//clicking on MAD/Artikel button
		anyClick("Mad/Article click", By.xpath(prop.getProperty("FLF.Home.Mad.Tab")));
		
		//clicking on Search button
		anyClick("Search click", By.xpath(prop.getProperty("FLF.Mad.Search.Button")));
		
		if(verifyElementExistReturn(By.xpath(prop.getProperty("FLF.Mad.Table.Verify")))==true) {
			
			//Clicking on edit button in the table
			anyClick("Edit button click", By.xpath(prop.getProperty("FLF.Mad.Table.Edit.Button.Click")));
			
			//Entering value in Mad chk textbox to save the changes
			sendKeys("Mad Chk:", By.xpath(prop.getProperty("FLF.Mad.Chk.Textbox")), MadChk);
			
			//Clicking on save button
			anyClick("Save button click", By.xpath(prop.getProperty("FLF.Mad.Save.Button")));
			
			//Accepting alert pop up window
			driver.switchTo().alert().accept();
			Thread.sleep(3000);
			
			//Clicking on edit button in the table
			anyClick("Edit button click", By.xpath(prop.getProperty("FLF.Mad.Table.Edit.Button.Click")));
			
			//Clearing the value that has been updated in mad chk textbox
			clearByLocator(By.xpath(prop.getProperty("FLF.Mad.Chk.Textbox")));
			
			//Clicking on save button
			anyClick("Save button click", By.xpath(prop.getProperty("FLF.Mad.Save.Button")));
			
			//Accepting alert pop up window
			driver.switchTo().alert().accept();
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			
			//Clicking on edit button in the table
			Thread.sleep(3000);
			anyClick("Edit button click", By.xpath(prop.getProperty("FLF.Mad.Table.Edit.Button.Click")));
			
			//Clicking on Return button in the table
			anyClick("Return button click", By.xpath(prop.getProperty("FLF.Mad.Return.Button")));
			
		}else {
			reportStep("Table not displayed", "fail", false);
		}
		reportStep("#B verified edit functionality in Food or articles screen Test Case ID:3711 #C", "pass", false);
	}
	
	
	/*
	 * 13 To verify search functionality in Rooms screen Test Case ID:3712
	 */
	public void Room(String Fabrik,String Rum,String Rumbeskrivning) {
		
		String selectFabrik=Fabrik.split(",")[0];
		
		driver.switchTo().defaultContent();
		//Clicking on Room button 
		anyClick("Room button click", By.xpath(prop.getProperty("FLF.Home.Rum.Tab")));
		
		//clicking on Search button
		anyClick("Search click", By.xpath(prop.getProperty("FLF.Mad.Search.Button")));
		reportStep("Rooms displayed successfully", "pass", true);
		
		//Entering Fabrik,Rum and Rumberskrivning
		selectDropDownValue("Fabrik", By.xpath(prop.getProperty("FLF.Rum.Fabrik.Dropdown")), selectFabrik);
		sendKeys("Rum", By.xpath(prop.getProperty("FLF.Rum.Textbox")), Rum);
		sendKeys("Rumbeskrivning", By.xpath(prop.getProperty("FLF.Rum.Rumbeskrivning.Textbox")), Rumbeskrivning);
		
		//clicking on Search button
		anyClick("Search click", By.xpath(prop.getProperty("FLF.Mad.Search.Button")));
		
		reportStep("#B verified search functionality in Rooms screen Test Case ID:3712 #C", "pass", false);
		
	}
	
	
	/*
	 * 14 To create new room in Rooms screen Test Case ID:3713
	 */
	public void Create_Room() {
		
		driver.switchTo().defaultContent();
		//Clicking on Room button 
		anyClick("Room button click", By.xpath(prop.getProperty("FLF.Home.Rum.Tab")));
		
		//Clicking on Create Room button 
		anyClick("Create Room button click", By.xpath(prop.getProperty("FLF.Rum.Create.Room.Button")));
		
		//verifying create new room page displayed
		verifyElementExistReturn(By.xpath(prop.getProperty("FLF.Rum.Create.Room.Page.Verify")));
		reportStep("#B Create new room Page displayed successfully", "pass", true);
		
		reportStep("#B verified create new room in Rooms screen Test Case ID:3713 #C", "pass", false);
	}
	
	
	/*
	 * 15 To edit room details in Rooms screen Test Case ID:3714
	 */
	public void Edit_Room(String Fabrik) throws InterruptedException {
		
		String changeFabrik=Fabrik.split(",")[1];
		String updateFabrik=Fabrik.split(",")[0];
		
		driver.switchTo().defaultContent();
		//Clicking on Room button 
		anyClick("Room button click", By.xpath(prop.getProperty("FLF.Home.Rum.Tab")));
		
		//clicking on Search button
		anyClick("Search button click", By.xpath(prop.getProperty("FLF.Mad.Search.Button")));
		
		//Clicking on edit button
		anyClick("Edit button click", By.xpath(prop.getProperty("FLF.Rum.TCSLAST.Edit.Button")));
		
		//Selecting another plant to edit and save
		selectDropDownValue("Fabrik", By.xpath(prop.getProperty("FLF.Rum.Edit.Fabrik.Dropdown")), changeFabrik);
		
		//Clicking on save button
		anyClick("Save button click", By.xpath(prop.getProperty("FLF.Mad.Save.Button")));
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		reportStep("Changes made", "pass", true);
		
		//Clicking on edit button
		anyClick("Edit button click", By.xpath(prop.getProperty("FLF.Rum.TCSLAST.Edit.Button")));
				
		//Selecting another plant to edit and save
		selectDropDownValue("Fabrik", By.xpath(prop.getProperty("FLF.Rum.Edit.Fabrik.Dropdown")), updateFabrik);
				
		//Clicking on save button
		anyClick("Save button click", By.xpath(prop.getProperty("FLF.Mad.Save.Button")));
		Thread.sleep(3000);
		reportStep("Reverted back to the old value", "pass", true);
		
		reportStep("#B verified edit room details in Rooms screen Test Case ID:3714 #C", "pass", false);
		
	}
	
	
	/*
	 * 16 To remove room in Rooms screen Test Case ID:3715
	 */
	public void Remove_Room() {
		
		driver.switchTo().defaultContent();
		//Clicking on Room button 
		anyClick("Room button click", By.xpath(prop.getProperty("FLF.Home.Rum.Tab")));
				
		//clicking on Search button
		anyClick("Search button click", By.xpath(prop.getProperty("FLF.Mad.Search.Button")));
		
		reportStep("Rooms displayed", "pass", true);
		reportStep("#B verified remove room in Rooms screen Test Case ID:3715 #C", "pass", false);
	}
	
	
	/*
	 * 17 To verify search functionality in Tour screen Test Case ID:3716
	 */
	public void Tur_Search(String Tur,String LeadTime,String ChangeTime) throws InterruptedException {
		
		String searchTur=Tur.split(",")[0];
		String newTur=Tur.split(",")[1];
		
		driver.switchTo().defaultContent();
		//Clicking on Tur tab
		anyClick("Tur tab click", By.xpath(prop.getProperty("FLF.Home.Tur.Tab")));
		
		//clicking on Search button
		anyClick("Search button click", By.xpath(prop.getProperty("FLF.Mad.Search.Button")));
		
		//Sending Tur in Tur textbox
		sendKeys("Tur", By.xpath(prop.getProperty("FLF.Tur.Textbox")), searchTur);
		
		//clicking on Search button
		anyClick("Search button click", By.xpath(prop.getProperty("FLF.Mad.Search.Button")));
		
		//clicking on new tur button
		anyClick("New tur button click", By.xpath(prop.getProperty("FLF.Tur.New.Tur.Button")));
		
		if(verifyElementExist("New Tur Page Verify", By.xpath(prop.getProperty("FLF.Tur.New.Tur.Button.Page.Verify")))){
			
			//Entering new tur name, to create new tur
			sendKeys("New Tur", By.xpath(prop.getProperty("FLF.Tur.Create.Tur.Textbox")),newTur);
			
			//Entering framkortid[Lead Time] 
			sendKeys("framkortid[Lead Time]", By.xpath(prop.getProperty("FLF.Tur.LeadTime.Textbox")), LeadTime);
			
			//Entering bytestid[Change time]
			sendKeys("Change Time", By.xpath(prop.getProperty("FLF.Tur.Change.Time.Textbox")), ChangeTime);
			
			//Clicking on Save button
			anyClick("Save button click", By.xpath(prop.getProperty("FLF.Tur.New.Tur.Page.Save.Button")));
			
			//Clicking on Revert button in new tur page
			anyClick("Revert button click", By.xpath(prop.getProperty("FLF.Tur.New.Tur.Page.Revert.Button")));
			
		}
		
		//Finding the data that has been created now 
		//Note:Pass the value in Test data using CAPS letters in Tur column
		driver.findElements(By.xpath(prop.getProperty("FLF.Tur.Edit.Button").replace("&Value", newTur))).get(0).click();
		Thread.sleep(3000);
		
		//Clicking on Remove this version button
		anyClick("Remove button click", By.xpath(prop.getProperty("FLF.Tur.New.Tur.Page.Remove.Button")));
		driver.switchTo().alert().accept();
		
		reportStep("#B verified search functionality in Tour screen Test Case ID:3716 #C", "pass", false);
		
	}
	
	/*
	 * 18 To verify send all MAD to MAS button functionality in Tour screen Test Case ID:3717
	 */
	public void MAD_to_MAS_Functionality() {
		
		driver.switchTo().defaultContent();
		//Clicking on Tur tab
		anyClick("Tur tab click", By.xpath(prop.getProperty("FLF.Home.Tur.Tab")));
		
		//Clicking on send all MAD's to MAS button
		anyClick("Send all MAD to MAS button click", By.xpath(prop.getProperty("FLF.Tur.Send.MAD.To.MAS.Button")));
		
		//Accepting the window pop-up as Okay
		driver.switchTo().alert().accept();
		
		reportStep("#B verified send all MAD to MAS button functionality in Tour screen Test Case ID:3717 #C", "pass", false);
	}
	
	/*
	 * 19 To verify search button functionality in Link Tour screen Test Case ID:3718
	 */
	public void  Link_Tur(String Tur) {
		
		String searchTur=Tur.split(",")[2];
		
		driver.switchTo().defaultContent();
		//Clicking on Link Tur Tab [Länkad Tur]
		anyClick("Link Tur tab click", By.xpath(prop.getProperty("FLF.Home.Link.Tur.Tab")));
		
		//Entering Tur name 
		sendKeys("Tur", By.xpath(prop.getProperty("FLF.Tur.Textbox")), searchTur);
		
		//clicking on Search button
		anyClick("Search button click", By.xpath(prop.getProperty("FLF.Mad.Search.Button")));
		
		reportStep("#B verified search button functionality in Link Tour screen Test Case ID:3718 #C", "pass", false);
	}
	
	/*
	 * 20 To verify edit button functionality in Link Tour screen Test Case ID:3719
	 */
	public void Link_Tur_Edit_Functionality() throws InterruptedException {
		
		driver.switchTo().defaultContent();
		//Clicking on Link Tur Tab [Länkad Tur]
		anyClick("Link Tur tab click", By.xpath(prop.getProperty("FLF.Home.Link.Tur.Tab")));
		
		//Clicking on edit button
		anyClick("Edit button click", By.xpath(prop.getProperty("FLF.Link.Tur.Edit.Button.Click")));
		
		//Clicking on save button
		anyClick("Save button click", By.xpath(prop.getProperty("FLF.Mad.Save.Button")));
		
		String fetchMessage=driver.findElements(By.xpath(prop.getProperty("FLF.Link.Tur.Edit.Page.Message.Fetch"))).get(0).getText();
		reportStep("#B Message: #C" +fetchMessage, "pass", true);
		
		//Clicking on Revert button in new tur page
		anyClick("Revert button click", By.xpath(prop.getProperty("FLF.Tur.New.Tur.Page.Revert.Button")));
		
		//Accepting alert pop up window
		driver.switchTo().alert().accept();
		Thread.sleep(3000);
		
		//Clicking on New Link button
		anyClick("New Link button click", By.xpath(prop.getProperty("FLF.Link.Tur.New.Link.Button")));
		
		//Clicking on left arrow button in new link page
		driver.findElements(By.xpath(prop.getProperty("FLF.Link.Tur.New.Link.Page.Left.Arrow1.Button"))).get(0).click();
		
		//Clicking on save button
		anyClick("Save button click", By.xpath(prop.getProperty("FLF.Mad.Save.Button")));
		String fetchMessage1=driver.findElements(By.xpath("FLF.Link.Tur.New.Link.Page.Message.Fetch")).get(0).getText();
		System.out.println("Message1:" + fetchMessage1);
		reportStep("#B Message1: #C" +fetchMessage1, "pass", true);
		Thread.sleep(3000);
		
		//Clicking on Revert button in new tur page
		anyClick("Revert button click", By.xpath(prop.getProperty("FLF.Tur.New.Tur.Page.Revert.Button")));
		
		//Accepting alert pop up window
		driver.switchTo().alert().accept();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		
		reportStep("#B verified edit button functionality in Link Tour screen Test Case ID:3719 #C", "pass", false);
	}
	
	
	/*
     * 21 To Verify Change/move the postion of the turs in lInked Tour screen Test Case ID:3720
     */
     
     public void Move_Link_Tur(String Fabrik,String Tur) throws InterruptedException, SQLException{
    	 
    	 	String strFabrik=Fabrik.split(",")[0];
    	 	String strName=Tur.split(",")[1];
    	 	
    	 	driver.switchTo().defaultContent();
            //Clicking on Linked Tour screen Tab
            anyClick("Linked Tour Tab click", By.xpath(prop.getProperty("FLF.Home.Link.Tur.Tab")));
            
            //Clicking on New Link Button
            anyClick("New Link Button click", By.xpath(prop.getProperty("FLF.Link.Tur.New.Link.Button")));
            
            
            //Selecting the value in drop-down
            selectDropDownValue("Fabrik Drop down value is selected",By.xpath(prop.getProperty("FLF.Link.Tur.New.Link.Page.Fabrik.dropdown")),strFabrik);
            
            //Entering value in name Column
            sendKeys("Name is entered", By.xpath(prop.getProperty("FLF.Link.Tur.New.Link.Page.Name.Textbox")),strName);
            
            //Clicking on Save button
            anyClick("Save Button Click", By.xpath(prop.getProperty("FLF.Mad.Save.Button")));
            
            //clicking on tur to move left
            driver.findElements(By.xpath(prop.getProperty("FLF.Link.Tur.New.Link.Page.Left.Arrow1.Button"))).get(0).click();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            driver.findElements(By.xpath(prop.getProperty("FLF.Link.Tur.New.Link.Page.Left.Arrow2.Button"))).get(0).click();
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            reportStep("Two nodes are selected and moved to left table", "pass", true);
            
            //Clicking on down arrow button on the first row of left table
            driver.findElements(By.xpath(prop.getProperty("FLF.Link.Tur.New.Link.Page.Down.Arrow.Button"))).get(0).click();
            reportStep("Down arrow button clicked successfully", "pass", true);
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            
            //Clicking on Up arrow button on the 2nd row of left table
            driver.findElements(By.xpath(prop.getProperty("FLF.Link.Tur.New.Link.Page.Up.Arrow.Button"))).get(0).click();
            reportStep("Up arrow button clicked successfully", "pass", true);
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            
            //Clicking on Revert button in new tur page
			anyClick("Revert button click", By.xpath(prop.getProperty("FLF.Tur.New.Tur.Page.Revert.Button")));
			
			//Accepting alert pop up window
			driver.switchTo().alert().accept();
			reportStep("#B verified edit button functionality in Link Tour screen Test Case ID:3720 #C", "pass", false);
     }
	
	
	/*
	 * 25 To verify Administration tab in FLF Test Case ID:3724
	 */
	public void Administration(String userDropdown,String Cdsid) throws InterruptedException {
		try {
		
		driver.switchTo().defaultContent();
		//Clicking on Administration Tab
		anyClick("Administration Tab click", By.xpath(prop.getProperty("FLF.Home.Administration.Tab")));
		
		//Selecting the value in drop-down
		new Select(driver.findElement(By.xpath(prop.getProperty("FLF.Administration.User.Dropdown")))).selectByVisibleText(userDropdown);
		//driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		
		String Message=driver.switchTo().alert().getText();
		
		//Accepting Ok in alert pop-up
		driver.switchTo().alert().accept();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		
		//Sending value in cdsid text box
		sendKeys("CDSID", By.xpath(prop.getProperty("FLF.Administration.Cdsid.Textbox")), Cdsid);
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		
		//Clicking on plus button
		anyClick("Plus button click", By.xpath(prop.getProperty("FLF.Administration.Plus.Button")));
		
		//Selecting the value in drop-down
		new Select(driver.findElement(By.xpath(prop.getProperty("FLF.Administration.User.Dropdown")))).selectByVisibleText(userDropdown);
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		
		//Clicking on Ok button
		anyClick("ok button click", By.xpath(prop.getProperty("FLF.Administration.Ok.Button")));
		
		reportStep("#B verified Administration tab in FLF Test Case ID:3724 #C", "pass", false);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	
}

	
	
	

