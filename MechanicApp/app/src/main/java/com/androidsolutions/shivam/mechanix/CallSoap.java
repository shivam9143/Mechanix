package com.androidsolutions.shivam.mechanix;

import android.util.Log;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class CallSoap 
{
	public final String SOAP_ACTION = "http://tempuri.org/EmployeeRegistration";

	public  final String OPERATION_NAME = "EmployeeRegistration";

	public  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

	url u=new url();
	public  final String SOAP_ADDRESS = u.url;
	public CallSoap() 
	{ 
	}
	public String Call(JSONObject json)
	{
		Log.e("SoapStringgg",json.toString());
	SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
	PropertyInfo pi=new PropertyInfo();
	        pi.setName("str");
	        pi.setValue(json.toString());
	        pi.setType(String.class);
	        request.addProperty(pi);
	       
	        
	       

	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
	SoapEnvelope.VER11);
	envelope.dotNet = true;

	envelope.setOutputSoapObject(request);

	HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
	Object response=null;
	try
	{
	httpTransport.call(SOAP_ACTION, envelope);
	response = envelope.getResponse();
	}
	catch (Exception exception)
	{
	response=exception.toString();
	Log.e("SoapResponse",exception.toString());
	}
	return response.toString();
	}
}
