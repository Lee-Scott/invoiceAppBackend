package com.familyfirstsoftware.invoiceApplication.service.implementation;

import com.familyfirstsoftware.invoiceApplication.enumeration.VerificationType;
import com.familyfirstsoftware.invoiceApplication.exception.ApiException;
import com.familyfirstsoftware.invoiceApplication.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String firstName, String email, String verificationUrl, VerificationType verificationType) {
        try{
            // send email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("FamilyFirstSoftwareSolutions@gmail.com");
            message.setTo(email);
            message.setText(getEmailMessage(firstName, verificationUrl, verificationType));
            message.setSubject(String.format("Family First Software - %s Verification", StringUtils.capitalize(verificationType.getType())));
            mailSender.send(message);
            log.info("Email sent successfully to: " + email + " for " + verificationType.getType() + " verification");
        } catch (Exception e) {
            log.error("Error sending email: " + e.getMessage());
        }
    }

    // TODO - add a flag to lock their account if they did not request the password reset

    private String getEmailMessage(String firstName, String verificationUrl, VerificationType verificationType) {
        switch(verificationType){
            case PASSWORD -> { return "Hello " + firstName + ",\n\n" +
                    "Please click the link below to reset your password:\n" +
                    verificationUrl + "\n\n" +
                    "If you did not request a password reset, please ignore this email.\n\n" +
                    "Thank you,\n" +
                    "Family First Software Solutions"; }

            case ACCOUNT -> { return "Hello " + firstName + ",\n\n" +
                    "Your new account has been create. Please Click this link the verify your account:\n" +
                    verificationUrl + "\n\n" +
                    "If you did not request a password reset, please ignore this email.\n\n" +
                    "Thank you,\n" +
                    "Family First Software Solutions"; }

            default -> throw new ApiException("Unable to send email. Email type not recognized.");
        }
    }
}
