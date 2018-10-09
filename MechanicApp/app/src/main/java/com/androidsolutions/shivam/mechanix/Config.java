package com.androidsolutions.shivam.mechanix;

/**
 * Created by Shivam on 9/11/2018.
 */
public class Config {
    // server URL configuration
    public static final String URL_VERIFY_OTP = "http://www.mechanix.somee.com/default.asmx/pc";

    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "IM-MECHNX";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";
}