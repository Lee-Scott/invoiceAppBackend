package com.familyfirstsoftware.invoiceApplication.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserForm {

    // these messages get put into our handleException.java class, for example, handleMethodArgumentNotValid exception.getBingResult().getFieldError();
    @NotEmpty(message = "Current password cannot be empty")
    private String currentPassword;
    @NotEmpty(message = "New password cannot be empty")
    private String newPassword;
    @NotEmpty(message = "Confirm new password cannot be empty")
    private String confirmNewPassword;

}
