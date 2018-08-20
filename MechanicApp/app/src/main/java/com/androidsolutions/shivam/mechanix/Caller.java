package com.androidsolutions.shivam.mechanix;

import org.json.JSONObject;


public class Caller extends Thread{
	public CallSoap cs;
	  public  JSONObject json;


	    public void run(){
	        try{
	            cs=new CallSoap();
	            String resp=cs.Call(json);
	            UserRegistration.rslt=resp;
	        }catch(Exception ex)
	        {
	        	UserRegistration.rslt=ex.toString();

				}
	        
	    }
}
