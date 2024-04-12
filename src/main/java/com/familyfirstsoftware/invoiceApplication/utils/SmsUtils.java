package com.familyfirstsoftware.invoiceApplication.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.concurrent.CompletableFuture;

import static com.twilio.rest.api.v2010.account.Message.creator;

// TODO - get the Twilio working
public class SmsUtils {
    public static final String FROM_NUMBER = "+16592182831";
    public static final String SID_KEY = "AC3c7ac1588c0c699944d1c02a8207682e";
    public static final String TOKEN_KEY = "c34a66861b999e0c54b877ea05b6ced6"; // TODO - pass in as environment variables

    public static void sendSMS(String to, String messageBody) {
        CompletableFuture.runAsync(() -> {
            try {
                Twilio.init(SID_KEY, TOKEN_KEY);
                Message message = creator(new PhoneNumber("+" + to), new PhoneNumber(FROM_NUMBER), messageBody).create();
                System.out.println(message);
            } catch (Exception e) {
                System.err.println("Failed to send SMS: " + e.getMessage());
            }
        });
    }
}