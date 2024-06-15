package com.familyfirstsoftware.invoiceApplication.service;

import com.familyfirstsoftware.invoiceApplication.domain.Role;
import com.familyfirstsoftware.invoiceApplication.domain.User;
import com.familyfirstsoftware.invoiceApplication.dto.UserDTO;
import com.familyfirstsoftware.invoiceApplication.repository.RoleRepository;
import com.familyfirstsoftware.invoiceApplication.repository.UserRepository;
import com.familyfirstsoftware.invoiceApplication.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * This class contains unit tests for the UserServiceImpl class.
 * Each method in the UserServiceImpl class has a corresponding test method in this class.
 * The tests are designed to verify the correct behavior of the UserServiceImpl methods,
 * by using mock implementations of the UserRepository and RoleRepository interfaces.
 * The Mockito framework is used to create the mock implementations and to verify that the methods are called with the correct parameters.
 * The tests use the JUnit 5 framework.
 */

@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository<User> userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void testCreateUser() {
        User expectedUser = new User();
        expectedUser.setEmail("test@example.com");
        Role expectedRole = new Role();
        expectedRole.setName("testRole");
        when(userRepository.create(expectedUser)).thenReturn(expectedUser);
        when(roleRepository.getRoleByUserId(expectedUser.getId())).thenReturn(expectedRole);

        UserDTO actualUser = userService.createUser(expectedUser);

        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedRole.getName(), actualUser.getRoleName());
    }

    @Test
    public void testGetUserByEmail() {
        User expectedUser = new User();
        expectedUser.setEmail("test@example.com");
        Role expectedRole = new Role();
        expectedRole.setName("testRole");
        when(userRepository.getUserByEmail("test@example.com")).thenReturn(expectedUser);
        when(roleRepository.getRoleByUserId(expectedUser.getId())).thenReturn(expectedRole);

        UserDTO actualUser = userService.getUserByEmail("test@example.com");

        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedRole.getName(), actualUser.getRoleName());
    }

    @Test
    public void testVerifyCode() {
        User expectedUser = new User();
        expectedUser.setEmail("test@example.com");
        Role expectedRole = new Role();
        expectedRole.setName("testRole");
        when(userRepository.verifyCode("test@example.com", "1234")).thenReturn(expectedUser);
        when(roleRepository.getRoleByUserId(expectedUser.getId())).thenReturn(expectedRole);

        UserDTO actualUser = userService.verifyCode("test@example.com", "1234");

        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedRole.getName(), actualUser.getRoleName());
    }

    @Test
    public void testGetUserById() {
        User expectedUser = new User();
        expectedUser.setEmail("test@example.com");
        Role expectedRole = new Role();
        expectedRole.setName("testRole");
        when(userRepository.get(1L)).thenReturn(expectedUser);
        when(roleRepository.getRoleByUserId(expectedUser.getId())).thenReturn(expectedRole);

        UserDTO actualUser = userService.getUserById(1L);

        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedRole.getName(), actualUser.getRoleName());
    }

    @Test
    public void testUpdateUserRole() {
        Long userId = 1L;
        String roleName = "testRole";
        doNothing().when(roleRepository).updateUserRole(userId, roleName);

        userService.updateUserRole(userId, roleName);

        verify(roleRepository, times(1)).updateUserRole(userId, roleName);
    }

    @Test
    public void testSendVerificationCode() {
        UserDTO expectedUser = new UserDTO();
        expectedUser.setEmail("test@example.com");
        doNothing().when(userRepository).sendVerificationCode(expectedUser);

        userService.sendVerificationCode(expectedUser);

        verify(userRepository, times(1)).sendVerificationCode(expectedUser);
    }

    @Test
    public void testResetPassword() {
        String email = "test@example.com";
        doNothing().when(userRepository).resetPassword(email);

        userService.resetPassword(email);

        verify(userRepository, times(1)).resetPassword(email);
    }

    @Test
    public void testUpdatePassword() {
        Long userId = 1L;
        String password = "newPassword";
        String confirmPassword = "newPassword";
        doNothing().when(userRepository).renewPassword(userId, password, confirmPassword);

        userService.updatePassword(userId, password, confirmPassword);

        verify(userRepository, times(1)).renewPassword(userId, password, confirmPassword);
    }

    @Test
    public void testUpdateAccountSettings() {
        Long userId = 1L;
        Boolean enabled = true;
        Boolean notLocked = true;
        doNothing().when(userRepository).updateAccountSettings(userId, enabled, notLocked);

        userService.updateAccountSettings(userId, enabled, notLocked);

        verify(userRepository, times(1)).updateAccountSettings(userId, enabled, notLocked);
    }

    @Test
    public void testToggleMfa() {
        String email = "test@example.com";
        User expectedUser = new User();
        expectedUser.setEmail(email);
        Role expectedRole = new Role();
        expectedRole.setName("testRole");
        when(userRepository.toggleMfa(email)).thenReturn(expectedUser);
        when(roleRepository.getRoleByUserId(expectedUser.getId())).thenReturn(expectedRole);

        UserDTO actualUser = userService.toggleMfa(email);

        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedRole.getName(), actualUser.getRoleName());
    }

    @Test
    public void testUpdateProfileImage() {
        UserDTO user = new UserDTO();
        user.setEmail("test@example.com");
        MultipartFile image = new MockMultipartFile("imagefile", "hello.png", "image/png", "fake image content".getBytes());
        doNothing().when(userRepository).updateProfileImage(user, image);

        userService.updateProfileImage(user, image);

        verify(userRepository, times(1)).updateProfileImage(user, image);
    }
}