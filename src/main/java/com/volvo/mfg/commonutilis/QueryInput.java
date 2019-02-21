package com.volvo.mfg.commonutilis;

public interface QueryInput {

/*
    * Query:FLF Levno,Kollino,Checkcode fetching
    * Query Updated: Preethi Madheshwaran
	* Description: Fetching Levno,Kollino,checkcode and Picking Kolli's in FLF Application 
*/
   public String FLF_Picking_Kolli ="select * from korning where ARTIKEL=#objectId#";
   
   /*
    * Query:FLF LASTPLATS fetching
    * Query Updated: Preethi Madheshwaran
	* Description: Fetching LastPlats code to park the train in FLF Application 
*/
   public String FLF_Parking ="select * from LASTPLATS ";
   

}
