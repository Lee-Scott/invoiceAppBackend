package com.familyfirstsoftware.invoiceApplication.service;

import com.familyfirstsoftware.invoiceApplication.enumeration.VerificationType;

public interface EmailService {
    void sendVerificationEmail(String firstName, String email, String verificationUrl, VerificationType verificationType);
}
