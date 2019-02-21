package com.volvo.mfg.test;



import java.sql.ResultSet;
import java.sql.SQLException;

import com.volvo.mfg.commonutilis.DB_Connectivity;



public class DBConnect {

	public static void main(String[] args) throws InterruptedException, SQLException {
		// TODO Auto-generated method stub
		/* Parameter_differentracksize parameter = new Parameter_differentracksize();
         int thersholdValue = (int) parameter.planning();*/
		DB_Connectivity db= new DB_Connectivity();
	ResultSet rs=	db.Connect_DB("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@flfqdb.got.volvocars.net:49313/flfq", "FLF", "FLF111", "select * from korning");
	if(rs.next()) {
		System.out.println("True");
	}
	}

}



