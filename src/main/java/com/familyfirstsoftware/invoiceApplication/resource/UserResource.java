package com.familyfirstsoftware.invoiceApplication.resource;


import com.familyfirstsoftware.invoiceApplication.domain.HttpResponse;
import com.familyfirstsoftware.invoiceApplication.domain.User;
import com.familyfirstsoftware.invoiceApplication.domain.UserPrincipal;
import com.familyfirstsoftware.invoiceApplication.dto.UserDTO;
import com.familyfirstsoftware.invoiceApplication.event.Event;
import com.familyfirstsoftware.invoiceApplication.event.NewUserEvent;
import com.familyfirstsoftware.invoiceApplication.exception.ApiException;
import com.familyfirstsoftware.invoiceApplication.form.LoginForm;
import com.familyfirstsoftware.invoiceApplication.form.UpdateForm;
import com.familyfirstsoftware.invoiceApplication.form.SettingsForm;
import com.familyfirstsoftware.invoiceApplication.form.UpdateUserForm;
import com.familyfirstsoftware.invoiceApplication.provider.TokenProvider;
import com.familyfirstsoftware.invoiceApplication.service.EventService;
import com.familyfirstsoftware.invoiceApplication.service.RoleService;
import com.familyfirstsoftware.invoiceApplication.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.familyfirstsoftware.invoiceApplication.dtoMapper.UserDTOMapper.toUser;
import static com.familyfirstsoftware.invoiceApplication.enumeration.EventType.*;
import static com.familyfirstsoftware.invoiceApplication.utils.ExceptionUtils.processError;
import static com.familyfirstsoftware.invoiceApplication.utils.UserAthUtils.getAuthenticatedUser;
import static com.familyfirstsoftware.invoiceApplication.utils.UserAthUtils.getLoggedInUser;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

/*
 *  Called a resource because we are just making an API
 * TODO implement @EnableMethodSecurity in the controller.
 */
@Slf4j
@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final long DELAY = 0; // delay in seconds shows the spinner can delete to make it faster

    private final UserService userService;
    private final RoleService roleService;
    private final EventService eventService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ApplicationEventPublisher publisher;


    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
        UserDTO user = authenticate(loginForm.getEmail(), loginForm.getPassword());
        return user.isUsingMfa() ? sendVerificationCode(user) : sendResponse(user);
    }


    @PostMapping(path = "/register")
    public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user) {
        UserDTO userDto = userService.createUser(user);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userDto)) // Map.of - static import
                        .message("User created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build());
    }

    /*@GetMapping(path = "/profile")
    public ResponseEntity<HttpResponse> profile(Authentication authentication) {
        try {
            *//*if (authentication == null) {
                log.warn("Authentication is null");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        HttpResponse.builder()
                                .timeStamp(now().toString())
                                .reason("Unauthorized")
                                .status(HttpStatus.UNAUTHORIZED)
                                .statusCode(HttpStatus.UNAUTHORIZED.value())
                                .build());
            }*//*

            log.info("profile endpoint authentication: " + authentication);
            UserDTO user = userService.getUserByEmail(authentication.getName());

            log.info("User principle: {}", authentication);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .data(Map.of("user", user))
                            .message("Profile retrieved")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build());
        } catch (UsernameNotFoundException ex) {
            // Handle the case where the user is not found by email
            log.error("*** User not found by email: {}", authentication.getName(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .reason("User not found")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build());
        } catch (Exception e) {
            // Handle generic exceptions
            log.error("Error in profile method: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .reason("Internal Server Error")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }*/


    @GetMapping("/profile")
    public ResponseEntity<HttpResponse> profile(Authentication authentication) {
        UserDTO user = userService.getUserByEmail(getAuthenticatedUser(authentication).getEmail());
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user, "events", eventService.getEventsByUserId(user.getId()), "roles", roleService.getRoles()))
                        .message("Profile Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    /*@GetMapping("/profile")
    public ResponseEntity<HttpResponse> profile(Authentication authentication) {
        //UserDTO userTwo = userService.getUserByEmail(getAuthenticatedUser(authentication).getEmail());
        soutOnAllAuthenticationVarForTesting(authentication);
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .reason("Unauthorized")
                                .status(HttpStatus.UNAUTHORIZED)
                                .statusCode(HttpStatus.UNAUTHORIZED.value())
                                .build());
            }


            String principal = authentication.getPrincipal().toString();
            String email = extractEmail(principal);
            if (email == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .reason("Invalid principal format")
                                .status(HttpStatus.BAD_REQUEST)
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .build());
            }

            UserDTO user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .reason("User not found")
                                .status(HttpStatus.NOT_FOUND)
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .build());
            }

            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(LocalDateTime.now().toString())
                            .data(of("user", user))
                            .message("Profile Retrieved")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    HttpResponse.builder()
                            .timeStamp(LocalDateTime.now().toString())
                            .reason("Internal Server Error")
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }*/

    // patch is used to update a resource partially not updating the password or is using mfa or their roll
    @PatchMapping("/update")
    public ResponseEntity<HttpResponse> updateUser(@RequestBody @Valid UpdateForm user) {
        UserDTO updatedUser = userService.updateUserDetails(user);
        publisher.publishEvent(new NewUserEvent(updatedUser.getEmail(), PROFILE_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", updatedUser, "events", eventService.getEventsByUserId(user.getId()), "roles", roleService.getRoles()))
                        .message("User updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }


    // Start - to reset password when user is not logged

    @GetMapping(path = "/verify/code/{email}/{code}")
    public ResponseEntity<HttpResponse> verifycode(@PathVariable("email") String email, @PathVariable("code") String code) {
        UserDTO user = userService.verifyCode(email, code);
        publisher.publishEvent(new NewUserEvent(user.getEmail(), LOGIN_ATTEMPT_SUCCESS));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("user", user, "access_token", tokenProvider.createAccessToken(getUserPrincipal(user))
                                , "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user))))
                        .message("Login Success")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) {
        userService.resetPassword(email);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Email sent. Please check your email to reset your password")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    // Mid - to reset password when user is not logged in
    @GetMapping("/verify/password/{key}")
    public ResponseEntity<HttpResponse> verifyPasswordUrl(@PathVariable("key") String key) {
        UserDTO user = userService.verifyPasswordKey(key);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("user", user))
                        .message("Please enter a new password")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/resetpassword/{key}/{password}/{confirmPassword}")
    public ResponseEntity<HttpResponse> resetPasswordWithKey(@PathVariable("key") String key,
                                                             @PathVariable("password") String password,
                                                             @PathVariable("confirmPassword") String confirmPassword) {
        userService.renewPassword(key, password, confirmPassword);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Password reset successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
    // END - to reset password when a user is not logged in

    //@PreAuthorize("#authentication.principal.id == @tokenProvider.getUserIdFromToken(#request.getHeader('Authorization').substring(7))")
    // @Valid makes sure the exceptions are thrown in the updateUserForm such as @NotEmpty(message = "Current password cannot be empty")
    // these messages get put into our handleException.java class for example handleMethodArgumentNotValid exception.getBingResult().getFieldError();
    @PatchMapping("/update/password")
    public ResponseEntity<HttpResponse> updatePassword(Authentication authentication, @RequestBody @Valid UpdateUserForm form) {
       // you could check that the user id = the user id in the token using @PreAuthorize, but it is not necessary
        // because we are gettingAuthenticatedUser in userUtils
        UserDTO userDto = getAuthenticatedUser(authentication);
        userService.updatePassword(userDto.getId(), form.getCurrentPassword(), form.getNewPassword(), form.getConfirmNewPassword());
        publisher.publishEvent(new NewUserEvent(userDto.getEmail(), PASSWORD_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("user", userService.getUserById(userDto.getId()), "events", eventService.getEventsByUserId(userDto.getId()),  "roles", roleService.getRoles()))
                        .timeStamp(now().toString())
                        .message("Password updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update/settings")
    public ResponseEntity<HttpResponse> updateAccountSettings(Authentication authentication, @RequestBody @Valid SettingsForm form) {
        UserDTO userDto = getAuthenticatedUser(authentication);
        userService.updateAccoutSettings(userDto.getId(), form.getEnabled(), form.getNotLocked());
        publisher.publishEvent(new NewUserEvent(userDto.getEmail(), ACCOUNT_SETTINGS_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("user", userService.getUserById(userDto.getId()), "events", eventService.getEventsByUserId(userDto.getId()),  "roles", roleService.getRoles()))
                        .timeStamp(now().toString())
                        .message("Account settings updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PatchMapping("/togglemfa")
    public ResponseEntity<HttpResponse> toggleMfa(Authentication authentication) throws InterruptedException {
        TimeUnit.SECONDS.sleep(DELAY); // shows the spinner can remove to speed it up
        UserDTO user = userService.toggleMfa(getAuthenticatedUser(authentication).getEmail());
        publisher.publishEvent(new NewUserEvent(user.getEmail(), MFA_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("user", user, "events", eventService.getEventsByUserId(user.getId()),  "roles", roleService.getRoles()))
                        .timeStamp(now().toString())
                        .message("Multi-Factor Authentication settings updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update/image")
    public ResponseEntity<HttpResponse> updateProfileImage(Authentication authentication, @RequestParam("image") MultipartFile image) throws InterruptedException {TimeUnit.SECONDS.sleep(DELAY); // shows the spinner can remove to speed it up
        UserDTO user = getAuthenticatedUser(authentication); // this user will not have these changes until the next request
        userService.updateProfileImage(user, image); // this user will not have these changes caz its void
        publisher.publishEvent(new NewUserEvent(user.getEmail(), PROFILE_PICTURE_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("user", userService.getUserById(user.getId()), "events", eventService.getEventsByUserId(user.getId()),  "roles", roleService.getRoles()))// gets fresh data from db
                        .timeStamp(now().toString())
                        .message("Profile image updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping(value = "/event/report")
    public ResponseEntity<HttpResponse> reportEvent(@RequestBody Event event) {
        System.out.println(event.toString());
        eventService.reportEvent(event);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("event", event))
                        .timeStamp(now().toString())
                        .message("Event reported successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping(value = "/image/{imgName}", produces = IMAGE_PNG_VALUE)
    public ResponseEntity<?> getProfileImage(@PathVariable("imgName") String fileName) {
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/downloads/images/" + fileName));
            return ResponseEntity.ok().body(imageBytes);
        } catch (NoSuchFileException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading image: " + fileName);
        }
    }

    @PatchMapping("/update/role/{roleName}")
    public ResponseEntity<HttpResponse> updateUserRole(Authentication authentication, @PathVariable("roleName") String roleName) {
        // you could check that the user id = the user id in the token using @PreAuthorize, but it is not necessary
        // because we are gettingAuthenticatedUser in userUtils
        UserDTO userDto = getAuthenticatedUser(authentication);
        userService.updateUserRole(userDto.getId(), roleName);
        publisher.publishEvent(new NewUserEvent(userDto.getEmail(), ROLE_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("user", userService.getUserById(userDto.getId()), "events", eventService.getEventsByUserId(userDto.getId()),  "roles", roleService.getRoles()))
                        .timeStamp(now().toString())
                        .message("Role updated successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping(path = "/verify/account/{key}")
    public ResponseEntity<HttpResponse> verifyAccount(@PathVariable("key") String key) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message(userService.verifyAccountKey(key).isEnabled() ? "Account already verified" : "Account verified")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping(path = "/refresh/token")
    public ResponseEntity<HttpResponse> refreshToken(HttpServletRequest request) {
        if (isHeaderAndTokenValid(request)) {
            String token = request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length());
            UserDTO user = userService.getUserById(tokenProvider.getSubject(token, request));
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .data(Map.of("user", user, "access_token", tokenProvider.createAccessToken(getUserPrincipal(user))
                                    , "refresh_token", token)) // TODO you can check the refresh token  everytime the user sends a request. check to see when it expires and renew it
                            .message("Token refreshed")
                            .status(OK)
                            .statusCode(OK.value())
                            .build());
        } else {
            // Not throwing an exception
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .reason("Refresh missing or invalid")
                            .developerMessage("Refresh missing or invalid")
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build());
        }
    }

    private boolean isHeaderAndTokenValid(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION) != null // is valid
                && request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX) // starts with bear
                && tokenProvider.isTokenValid(
                tokenProvider.getSubject(request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()), request),// the email
                request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()) // the request
        );
    }


    /*@RequestMapping(path = "/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
        //log.info("User principle: {}", authentication);
        return ResponseEntity.badRequest().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .reason("There is no mapping for a " + request.getMethod() + " request for this path on the server")
                        .status(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .build());
    }*/

    @RequestMapping(path = "/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
        //log.info("User principle: {}", authentication);
        return new ResponseEntity<>(HttpResponse.builder()
                .timeStamp(now().toString())
                .reason("There is no mapping for a " + request.getMethod() + " request for this path on the server")
                .status(NOT_FOUND)
                .statusCode(NOT_FOUND.value())
                .build(), NOT_FOUND);
    }

    // TODO - try using a Long id instead of email. test the service then repository then Event query
    private UserDTO authenticate(String email, String password) {
        //
        try {
            if(null != userService.getUserByEmail(email)) {
                publisher.publishEvent(new NewUserEvent(email, LOGIN_ATTEMPT));
                System.out.println("NewUserEvent(email, LOGIN_ATTEMPT)");
            }
            Authentication authentication = authenticationManager.authenticate(unauthenticated(email, password));

            UserDTO loggedInUser = getLoggedInUser(authentication);
            if(!loggedInUser.isUsingMfa()) {
                publisher.publishEvent(new NewUserEvent(email, LOGIN_ATTEMPT_SUCCESS));
                System.out.println("NewUserEvent(email, LOGIN_ATTEMPT_SUCCESS)");
            }
            return loggedInUser;
        } catch (Exception exception) {
            publisher.publishEvent(new NewUserEvent(email, LOGIN_ATTEMPT_FAILURE));
            System.out.println("NewUserEvent(email, LOGIN_ATTEMPT_FAILURE)");
            processError(request, response, exception);
            throw new ApiException(exception.getMessage());
        }
    }



    private URI getUri() {
        return URI.create(fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }

    private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO user) {
        userService.sendVerificationCode(user);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("user", user))
                        .message("Verification Code Sent")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());

    }

    private ResponseEntity<HttpResponse> sendResponse(UserDTO user) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("user", user, "access_token", tokenProvider.createAccessToken(getUserPrincipal(user))
                                , "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(user))))
                        .message("Login Success")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());

    }

    private UserPrincipal getUserPrincipal(UserDTO user) {
        return new UserPrincipal(toUser(userService.getUserByEmail(user.getEmail())),
                roleService.getRoleById(user.getId()));
        //return new UserPrincipal(userService.getUser(user.getEmail()), roleService.getRoleById(user.getId()));
    }

}
