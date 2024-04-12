package com.familyfirstsoftware.invoiceApplication.constant;

import com.familyfirstsoftware.invoiceApplication.provider.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    // Security configurations
    public static final String[] PUBLIC_URLS = {"/user/verify/password/**", "/user/login/**", "/user/verify/code/**",
            "/user/register/**", "/user/resetpassword/**", "/user/profile/**", "/user/verify/account/**", "/user/refresh/token/**", "/user/image/**", "/user/new/password/**"};

    // Security Authorization
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String[] PUBLIC_ROUTES = { "/user/new/password", "/user/login", "/user/verify/code", "/user/register", "/user/refresh/token", "/user/image", "/user/image", "/user/new/password" }; // has to be exact
    public static final String HTTP_OPTIONS_METHOD = "OPTIONS";

    // Security TokenProvider
    public static final String AUTHORITIES = "authorities";
    public static final String FAMILY_FIRST_SOFTWARE = "FAMILY_FIRST_SOFTWARE";
    public static final String CUSTOMER_MANAGEMENT_SERVICE = "CUSTOMER_MANAGEMENT_SERVICE";
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 1_800_000;
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 432_000_000;
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    // Request
    public static final String USER_AGENT_HEADER = "User-Agent";
    public static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";

    // Date
    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

}
