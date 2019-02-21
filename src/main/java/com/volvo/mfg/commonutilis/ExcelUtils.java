package com.volvo.mfg.commonutilis;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//POI Headers
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelUtils extends CommonWrapperMethods {
	
	HashMap<String, String> testData = null;
	InputStream testFile = null;
	OutputStream outFile = null;
	XSSFWorkbook testWorkBook = null;
	DataFormatter formatter = new DataFormatter();
	HSSFWorkbook workbook = null;
	
	public HashMap<String, String> testCaseRetrieve(String testCaseID, String sheetName)
	{
		
		
		try {
			
			testFile = new FileInputStream(Test_Sheet_Path);
			testWorkBook = new XSSFWorkbook(testFile);
			Sheet testSheet = testWorkBook.getSheet(sheetName);
			
			int testCaseColumn = getColumnNumber(testSheet, "Scenario_ID");
			int testCaseExecute = getColumnNumber(testSheet, "To_Be_Executed");
			
			if (testCaseColumn == -1)
			{
				System.err.println("Scenario_ID column not exist, Check the Data Sheet: "+ Test_Sheet_Path);
				reportStep("Scenario_ID column not exist, Check the Data Sheet: "+ Test_Sheet_Path, "FAIL", false);
				
			}
			
			int rowsCount = testSheet.getLastRowNum();
			
			for (int i = 1; i <= rowsCount; i ++)
			{
				//Navigate each row
				Row row = testSheet.getRow(i);
				Row rowHeader = testSheet.getRow(0);
				//Get the Test Case Id value
				Cell testCellValue = row.getCell(testCaseColumn);
				Cell testCellExecute = row.getCell(testCaseExecute);
				//Verify the test Id
				if (formatter.formatCellValue(testCellValue).equalsIgnoreCase(testCaseID) && testCellExecute.getStringCellValue().equalsIgnoreCase("Y"))
				{
					int colsCount = row.getLastCellNum();
					testData = new HashMap<String, String>();
					for (int j = 0; j < colsCount; j ++)
					{
						Cell cellHeader = rowHeader.getCell(j);
						Cell cellValue = row.getCell(j);
						testData.put(cellHeader.getStringCellValue(), formatter.formatCellValue(cellValue));
					}
					testData.entrySet();
					
					return testData;
				}
			}
			
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Error in Test Data Sheet access");


		}
		finally 
		{
			try 
			{
				testFile.close();
				testWorkBook.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return testData;	
	}
	
	private int getColumnNumber(Sheet getSheetName, String colName) {
		// TODO Auto-generated method stub
		Row row = getSheetName.getRow(0);
		
		int colCounts = row.getLastCellNum();
		
		for (int i=0; i< colCounts; i ++)
		{
			Cell cell = row.getCell(i);
			if (cell.getStringCellValue().equalsIgnoreCase(colName))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	//Write the data in the cell
	public boolean excelUpdateValue(String sheetName, String colName, String ScenarioName, String strValue) {
		boolean bReturn = false;
		
		try {
			
				outFile = new FileOutputStream(Test_Sheet_Path);
				testWorkBook = new XSSFWorkbook(testFile);
				Sheet testSheet = testWorkBook.getSheet(sheetName);
				
				int testCaseColumn = getColumnNumber(testSheet, "Scenario_ID");
				int columnEdit = getColumnNumber(testSheet, colName);
				
				if (testCaseColumn == -1 || columnEdit == -1) 
				{
					return bReturn;
				}
				//Get the Row Count
				int rowsCount = testSheet.getLastRowNum();
				
				for (int i = 1; i <= rowsCount; i ++)
				{
					//Navigate each row
					Row row = testSheet.getRow(i);
					//Get the Test Case Id value
					Cell testCellValue = row.getCell(testCaseColumn);
					//Verify the Scenario Name
					if(formatter.formatCellValue(testCellValue).equalsIgnoreCase(ScenarioName))
					{
						Cell cell = row.createCell(columnEdit);
						cell.setCellValue(strValue);
						bReturn = true;
						break;
					}
					
				}
				testWorkBook.write(outFile);
				outFile.flush();
				outFile.close();
					
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				testWorkBook.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
				
	return bReturn;
	}
	
	
	/*public boolean UpdateValuesToExcel(String sheetName,String colName,String tName,String strValue) throws IOException {
		
		boolean bReturn = false;
		try {

        	FileInputStream testFile = new FileInputStream(Test_Sheet_Path);
             workbook = new HSSFWorkbook(testFile);
             Sheet testSheet = workbook.getSheet(sheetName);

            Cell cell = null;  
            int seqNamePos = getColumnNumber(testSheet, "TC_No");
            //System.out.println("seqNamePos"+seqNamePos);
            int columnEdit = getColumnNumber(testSheet, colName);
//            System.out.println("columnEdit"+columnEdit);          
            //Retrieve the row and check for null
            int rowsCount = testSheet.getLastRowNum();
            for (int j = 1; j <= rowsCount; j++) {
            	  Row row = testSheet.getRow(j);
           
            Cell testCellExecute = row.getCell(seqNamePos);
            String cValue=testCellExecute.getStringCellValue();
            if(testCellExecute.getStringCellValue().equalsIgnoreCase(cValue))
            {
           
            cell = row.createCell(columnEdit);
			cell.setCellValue(strValue);
			System.out.println("cell: "+cell.getStringCellValue());
            System.out.println("strValue: "+strValue);
			bReturn = true;
		}
            
           }
            FileOutputStream outFile =new FileOutputStream(new File(Test_Sheet_Path));
            workbook.write(outFile);
            outFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bReturn;
    }*/

	// for excel update to executing status yes and WRT testname - values passed as parameter are sheetname,columname value to be updat,testname,value to be updated
			public boolean UpdateValuesToExcel(String sheetName,String colName,String tName,String strValue) throws IOException {
	        
		        boolean bReturn = false;
		        try {
		
		        FileInputStream testFile = new FileInputStream(Test_Sheet_Path);
		       workbook = new HSSFWorkbook(testFile);
		       Sheet testSheet = workbook.getSheet(sheetName);
		
		      Cell cell,cell2,cell1 = null;  
	      
		      int seqNamePos = getColumnNumber(testSheet,"TC_No");
		      //System.out.println("seqNamePos"+seqNamePos);
		      int columnEdit = getColumnNumber(testSheet, colName);
		//      System.out.println("columnEdit"+columnEdit);          
		      //Retrieve the row and check for null
		      int rowsCount = testSheet.getLastRowNum();
		      for (int j = 1; j <= rowsCount; j++) {
		          Row row = testSheet.getRow(j);
		     
		      Cell testCellExecute = row.getCell(seqNamePos);
		      
		      cell1= row.getCell(2);
		      String eStatus=cell1.getStringCellValue();
		      System.out.println("eStatus: "+eStatus);
		      cell2= row.getCell(1);
		      String testName=cell2.getStringCellValue();
		      System.out.println("testName: "+testName);
		      System.out.println("testCellExecute: "+testCellExecute);
		      String cValue=testCellExecute.getStringCellValue();
		      System.out.println("cValue: "+cValue);
		      if(testCellExecute.getStringCellValue().equalsIgnoreCase(cValue) && eStatus.equalsIgnoreCase("Yes") && testName.equalsIgnoreCase(testName))
		      {
		     
		      cell = row.createCell(columnEdit);
		               cell.setCellValue(strValue);
		               
		               bReturn = true;
		        }
		      
		     }
		      FileOutputStream outFile =new FileOutputStream(new File(Test_Sheet_Path));
		      workbook.write(outFile);
		      outFile.close();
		
		  } catch (FileNotFoundException e) {
		      e.printStackTrace();
		  } catch (IOException e) {
		      e.printStackTrace();
		  }
		  return bReturn;
		}
		
			// excel update method based on execution status
			
			public boolean UpdateValuesToExcel1(String sheetName,String colName,String tName,String strValue) throws IOException {
		        
		        boolean bReturn = false;
		        try {
		
		        FileInputStream testFile = new FileInputStream(Test_Sheet_Path);
		       workbook = new HSSFWorkbook(testFile);
		       Sheet testSheet = workbook.getSheet(sheetName);
		
		      Cell cell = null;  
		      Cell cell1= null;
		      int seqNamePos = getColumnNumber(testSheet,"TC_No");
		      //System.out.println("seqNamePos"+seqNamePos);
		      int columnEdit = getColumnNumber(testSheet, colName);
		//      System.out.println("columnEdit"+columnEdit);          
		      //Retrieve the row and check for null
		      int rowsCount = testSheet.getLastRowNum();
		      for (int j = 1; j <= rowsCount; j++) {
		          Row row = testSheet.getRow(j);
		     
		      Cell testCellExecute = row.getCell(seqNamePos);
		      
		      cell1= row.getCell(2);
		      String eValue=cell1.getStringCellValue();
		      System.out.println("testCellExecute: "+testCellExecute);
		      String cValue=testCellExecute.getStringCellValue();
		     
		      if(testCellExecute.getStringCellValue().equalsIgnoreCase(cValue) && eValue.equalsIgnoreCase("Yes"))
		      {
		     
		      cell = row.createCell(columnEdit);
		               cell.setCellValue(strValue);
		               /*System.out.println("cell: "+cell.getStringCellValue());
		      System.out.println("strValue: "+strValue);*/
		               bReturn = true;
		        }
		      
		     }
		      FileOutputStream outFile =new FileOutputStream(new File(Test_Sheet_Path));
		      workbook.write(outFile);
		      outFile.close();
		
		  } catch (FileNotFoundException e) {
		      e.printStackTrace();
		  } catch (IOException e) {
		      e.printStackTrace();
		  }
		  return bReturn;
		}
	
	public long takeSnap(){
		return 0;
	}

	
}
