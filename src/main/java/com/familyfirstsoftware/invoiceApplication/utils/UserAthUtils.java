package com.familyfirstsoftware.invoiceApplication.utils;

import com.familyfirstsoftware.invoiceApplication.domain.UserPrincipal;
import com.familyfirstsoftware.invoiceApplication.dto.UserDTO;
import org.springframework.security.core.Authentication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAthUtils {

    public static UserDTO getAuthenticatedUser(Authentication authentication) {
        //System.out.println("*** AUTHENTICATION in UserUtils.getAuthenticatedUser: " + authentication);
        //System.out.println("*** AUTHENTICATION in UserUtils.getAuthenticatedUser.getPrincipal: " + authentication.getPrincipal());
        return ((UserDTO) authentication.getPrincipal());
    }

    public static UserDTO getLoggedInUser(Authentication authentication){
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
    }

    public static void soutOnAllAuthenticationVarForTesting(Authentication authentication) {
        System.out.println("\n" + "-------------------------------------------------------");
        System.out.println("authentication: " + authentication);
        System.out.println("authentication.getPrincipal().toString(): " + authentication.getPrincipal().toString());
        System.out.println("authentication.getName(): " + (authentication.getName()));
        System.out.println("authentication.toString(): " + authentication);
        System.out.println("authentication.getAuthorities(): " + authentication.getAuthorities());
        System.out.println("authentication.getDetails(): " + authentication.getDetails());
        System.out.println("authentication.getCredentials(): " + authentication.getCredentials());
        System.out.println("-------------------------------------------------------\n");
    }

    public static String extractEmail(String principal) {
        String regex = "email=([^,]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(principal);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
