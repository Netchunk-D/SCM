package com.scm.helper;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class Helper {
    static Logger logger = LoggerFactory.getLogger(Helper.class);
    public static String getEmail(Authentication authentication) {


        var token = (OAuth2AuthenticationToken) authentication;
        var x = token.getAuthorizedClientRegistrationId();
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        if(x.equalsIgnoreCase("google")) {
            logger.info("Google");
            return ((String) ((OAuth2AuthenticationToken) authentication).getPrincipal().getAttributes().get("email"));
        } else if(x.equalsIgnoreCase("github")) {
            logger.info("Github");
            String email = user.getAttribute("email")!=null ? user.getAttribute("email").toString():user.getAttribute("login").toString()+"@gmail.com";
            logger.info("Email: "+email);
            return email;
        }else{
            return authentication.getName();
        }

    }
}
