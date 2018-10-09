package com.androidsolutions.shivam.mechanix;

/**
 * Created by Shivam on 8/10/2018.
 */
import org.json.JSONObject;


public class Caller1 extends Thread{
    public CallSoap1 cs;
    public  JSONObject json;

    public void run(){
        try{
            cs=new CallSoap1();
            String resp=cs.Call(json);
            RegisterWoutScan.result=resp;
        }catch(Exception ex)
        {
            RegisterWoutScan.result="Helo Caller="+ex.getMessage().toString();
        }
    }
}