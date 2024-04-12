package com.familyfirstsoftware.invoiceApplication.utils;

import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import static com.familyfirstsoftware.invoiceApplication.constant.Constants.USER_AGENT_HEADER;
import static com.familyfirstsoftware.invoiceApplication.constant.Constants.X_FORWARDED_FOR;
import static nl.basjes.parse.useragent.UserAgent.AGENT_NAME;
import static nl.basjes.parse.useragent.UserAgent.DEVICE_NAME;

// https://try.yauaa.basjes.nl/

// TODO create my own parser, because the current one is too slow
// the RequestEventUtils parser takes forever to parse the request

public class RequestEventUtils {

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = "Unknown IP";
        if (request != null) {
            ipAddress = request.getHeader(X_FORWARDED_FOR);
            if (ipAddress == null || "".equals(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }

        }
        return ipAddress;
    }

    public static String getDevice(HttpServletRequest request) {
        UserAgentAnalyzer userAgentAnalyzer = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(10000).build();
        UserAgent agent = userAgentAnalyzer.parse(request.getHeader(USER_AGENT_HEADER));
        //System.out.println(AGENT_NAME + " : " + agent.getValue(UserAgent.DEVICE_NAME)); // AGENT_NAME is browser ex: Chrome
        //System.out.println(agent); // see all
        //return agent.getValue(UserAgent.OPERATING_SYSTEM_NAME) + " - " + agent.getValue(AGENT_NAME) + " - " + agent.getValue(UserAgent.DEVICE_NAME);
        return agent.getValue(AGENT_NAME) + " - " + agent.getValue(DEVICE_NAME);
    }
}
