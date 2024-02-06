package com.familyfirstsoftware.invoiceApplication.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import static com.twilio.rest.api.v2010.account.Message.creator;


public class SmsUtils {
    public static final String FROM_NUMBER = "+16592182831";
    public static final String SID_KEY = "AC3c7ac1588c0c699944d1c02a8207682e";
    public static final String TOKEN_KEY = "c34a66861b999e0c54b877ea05b6ced6";

    public static void sendSMS(String to, String messageBody) {
        Twilio.init(SID_KEY, TOKEN_KEY);
        Message message = creator(new PhoneNumber("+" + to), new PhoneNumber(FROM_NUMBER), messageBody).create();
        System.out.println(message);
    }
}