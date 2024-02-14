package com.familyfirstsoftware.invoiceApplication.service;

import com.familyfirstsoftware.invoiceApplication.domain.User;
import com.familyfirstsoftware.invoiceApplication.dto.UserDTO;
import com.familyfirstsoftware.invoiceApplication.form.UpdateForm;

public interface UserService {
    UserDTO createUser(User user);
    UserDTO getUserByEmail(String email);
    void sendVerificationCode(UserDTO user);
    UserDTO verifyCode(String email, String code);
    void resetPassword(String email);
    UserDTO verifyPasswordKey(String key);
    void renewPassword(String key, String password, String confirmPassword);

    UserDTO getUserById(Long userId);

    UserDTO verifyAccountKey(String key);

    UserDTO updateUserDetails(UpdateForm user);

    // should only be dto so not like:
    // User getUser(String email);
}