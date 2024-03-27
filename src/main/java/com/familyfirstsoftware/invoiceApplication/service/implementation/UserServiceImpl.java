package com.familyfirstsoftware.invoiceApplication.service.implementation;

import com.familyfirstsoftware.invoiceApplication.domain.Role;
import com.familyfirstsoftware.invoiceApplication.domain.User;
import com.familyfirstsoftware.invoiceApplication.dto.UserDTO;
import com.familyfirstsoftware.invoiceApplication.event.Event;
import com.familyfirstsoftware.invoiceApplication.form.UpdateForm;
import com.familyfirstsoftware.invoiceApplication.repository.EventRepository;
import com.familyfirstsoftware.invoiceApplication.repository.RoleRepository;
import com.familyfirstsoftware.invoiceApplication.repository.UserRepository;
import com.familyfirstsoftware.invoiceApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.familyfirstsoftware.invoiceApplication.dtoMapper.UserDTOMapper.fromUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;
    private final RoleRepository<Role> roleRoleRepository;
    private final EventRepository<Event> eventRepository;

    @Override
    public UserDTO createUser(User user) {
        //return fromUser(userRepository.create(user));
        return mapToUserDTO(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return mapToUserDTO(userRepository.getUserByEmail(email));
    }

    @Override
    public void sendVerificationCode(UserDTO user) {
        userRepository.sendVerificationCode(user);
    }

    @Override
    public UserDTO verifyCode(String email, String code) {
        return mapToUserDTO(userRepository.verifyCode(email, code));
    }

    @Override
    public void resetPassword(String email) {
        userRepository.resetPassword(email);
    }

    @Override
    public UserDTO verifyPasswordKey(String key) {
        return mapToUserDTO(userRepository.verifyPasswordKey(key));
    }

    @Override
    public void updatePassword(Long userId, String password, String confirmPassword) {
        userRepository.renewPassword(userId, password, confirmPassword);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return mapToUserDTO(userRepository.get(userId));
    }

    @Override
    public UserDTO verifyAccountKey(String key) {
        return mapToUserDTO(userRepository.verifyAccountKey(key));
    }

    @Override
    public UserDTO updateUserDetails(UpdateForm user) {
        return mapToUserDTO(userRepository.updateUserDetails(user));

    }

    @Override
    public void updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword) {
        userRepository.updatePassword(id, currentPassword, newPassword, confirmNewPassword);
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {
        roleRoleRepository.updateUserRole(userId, roleName);
    }

    @Override
    public void updateAccoutSettings(Long id, Boolean enabled, Boolean notLocked) {
        userRepository.updateAccountSettings(id, enabled, notLocked);
    }

    @Override
    public UserDTO toggleMfa(String email) {
        return mapToUserDTO(userRepository.toggleMfa(email));
    }

    @Override
    public void updateProfileImage(UserDTO user, MultipartFile image) {
        userRepository.updateProfileImage(user, image);
    }



    /* never should return the user object, but instead the dto or void. this is how we are separating these concerns
    @Override
    public User getUser(String email) {
        return userRepository.getUserByEmail(email);
    }*/

    private UserDTO mapToUserDTO(User user) {
        return fromUser(user, roleRoleRepository.getRoleByUserId(user.getId()));
    }
}












