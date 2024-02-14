package com.familyfirstsoftware.invoiceApplication.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateForm {

    @NotNull(message = "ID cannot be empty")
    private Long id;
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email. Please enter a valid email address")
//    @Pattern(regexp = "^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[\\w-]{2,}$",
//            message = "Invalid email. Please enter a valid email address")
    private String email;
   /* @Pattern(regexp = "^(\\d{3}-\\d{3}-\\d{4})$",
            message = "Invalid phone number. Please enter a valid phone number. Format: 123-456-7890")*/
    //@Pattern(regexp = "^\\d{11}$",
     //       message = "Invalid phone number. Please enter a valid phone number. Format: 1234567890")
    private String phone;
    private String address;
    private String title;
    private String bio;
}
