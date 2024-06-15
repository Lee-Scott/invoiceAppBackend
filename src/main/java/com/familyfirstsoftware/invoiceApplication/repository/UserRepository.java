package com.familyfirstsoftware.invoiceApplication.repository;

import com.familyfirstsoftware.invoiceApplication.domain.User;
import com.familyfirstsoftware.invoiceApplication.dto.UserDTO;
import com.familyfirstsoftware.invoiceApplication.event.Event;
import com.familyfirstsoftware.invoiceApplication.form.UpdateForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository<T extends User> {
    /* Basic CRUD Operations */
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More Complex Operations */
    User getUserByEmail(String email);
    T findById(Long id);
    void sendVerificationCode(UserDTO user);
    User verifyCode(String email, String code);
    void resetPassword(String email);
    T verifyPasswordKey(String key);
    void renewPassword(String key, String password, String confirmPassword);
    void renewPassword(Long userId, String password, String confirmPassword);

    T verifyAccountKey(String key);

    T updateUserDetails(UpdateForm user);


    void updatePassword(Long userId, String currentPassword, String newPassword, String confirmNewPassword);

    void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked);

    T toggleMfa(String email);

    void updateProfileImage(UserDTO user, MultipartFile image);




}