package com.volvo.mfg.commonutilis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class DB_Connectivity extends CommonWrapperMethods {
	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pre = null;
	ResultSet rs = null;
	
	
	//**************************************************************************************************
	//@Method Name: Connect_DB
	//@Description: To retrieve the data in result set depends on Query
	//@Input Parameters: Class Name, Connection String, User name, Pwd, SqlQuery(Select)
	//@Output Parameters: Resultset
	//@Created by: Senthil Kumar Sivanandam
	//@Date Created: 20-Mar-2017
	//@Last Modified: 
	//**************************************************************************************************
	public ResultSet Connect_DB(String className,String connectionString, String userName, String password, String sqlQuery)
	{
		
		try {
			
			//Class.forName("com.mysql.jdbc.Driver").newInstance();
			Class.forName(className).newInstance();
			
			/*String connectionUrl = "jdbc:mysql://localhost:3306/testdatabase";
			String connectionUser = "testuser";
			String connectionPassword = "testpassword";*/
			
			String connectionUrl = connectionString;
			String connectionUser = userName;
			String connectionPassword = password;
			
			conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlQuery);
					
			return rs;

		}
		catch (Exception e)
		{
			e.printStackTrace();
			reportStep("Error in Connecting data base - Exception", "Fail", false);
			return rs;
		}
	}
	
	public boolean Update_DB(String className,String connectionString, String userName, String password, String sqlQuery)
	{
		boolean bReturn = false;
		try {
			
			//Class.forName("com.mysql.jdbc.Driver").newInstance();
			Class.forName(className).newInstance();
			
			/*String connectionUrl = "jdbc:mysql://localhost:3306/testdatabase";
			String connectionUser = "testuser";
			String connectionPassword = "testpassword";*/
			
			String connectionUrl = connectionString;
			String connectionUser = userName;
			String connectionPassword = password;
			
			conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlQuery);
			
			conn.close();
			bReturn = true;

		}
		catch (Exception e)
		{
			e.printStackTrace();
			reportStep("Error in Connecting data base - Exception", "Fail", false);
			
		}
		return bReturn;
	}
	
	public Recordset Connect_Excel(String sqlQuery, String excelPath)
	{
		Recordset recordset = null;
		try {
			
			//Fillo object to access excel file
			Fillo fillo=new Fillo();
			
			//Connection to Excel File
			com.codoid.products.fillo.Connection connection=fillo.getConnection(excelPath);
			
			//Executing the Query
			recordset=connection.executeQuery(sqlQuery);
			
			//return
			return recordset;
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			reportStep("Error in Connecting data base - Exception", "Fail", false);
			return recordset;
		}
	}
	
	
	public boolean Update_Excel(String sqlQuery, String excelPath)
	{
		boolean bReturn = false;
		try {
			
			//Fillo object to access excel file
			Fillo fillo=new Fillo();
			
			//Connection to Excel File
			com.codoid.products.fillo.Connection connection=fillo.getConnection(excelPath);
			
			//Executing the Query
			connection.executeUpdate(sqlQuery);
			
			//Close the Connection
			connection.close();
			//Return
			bReturn = true;

		}
		catch (Exception e)
		{
			e.printStackTrace();
			reportStep("Error in Connecting data base - Exception", "Fail", false);
		}
		return bReturn;
	}
	
}
