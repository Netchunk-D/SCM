package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSucessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserRepo userRepo;

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSucessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // TODO Auto-generated method stub

        var token = (OAuth2AuthenticationToken) authentication;

        String x = token.getAuthorizedClientRegistrationId();

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        User u = new User();
        u.setEmailVerified(true);
        u.setRoleList(List.of(AppConstants.USER_ROLE));
        u.setUserId(UUID.randomUUID().toString());
        u.setEnabled(true);
        if (x.equalsIgnoreCase("google")) {
            u.setProvider(Providers.GOOGLE);
            u.setAbout("this account is created using google oauth");
            u.setEmail(user.getAttribute("email").toString());
            u.setProfilePic(user.getAttribute("picture").toString());
            u.setName(user.getAttribute("name").toString());
            u.setProviderUserId(user.getName());

        } else if (x.equalsIgnoreCase("github")) {
            String email = user.getAttribute("email")!=null ? user.getAttribute("email").toString():user.getAttribute("login").toString()+"@gmail.com";
            u.setProvider(Providers.GITHUB);
            u.setAbout("this account is created using github oauth");
            u.setEmail(email);
            u.setProfilePic(user.getAttribute("avatar_url").toString());
            u.setName(user.getAttribute("login").toString());
            u.setProviderUserId(user.getName());

        }
        User u1 = userRepo.findByEmail(u.getEmail()).orElse(null);
        if (u1 == null) {
            userRepo.save(u);
            logger.info("user created using oauth" + u.getEmail());
        }

        // DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        // String email = user.getAttribute("email").toString();
        // String name = user.getAttribute("name").toString();
        // String picture = user.getAttribute("picture").toString();

        // User u = new User();
        // u.setEmail(email);
        // u.setName(name);
        // u.setProfilePic(picture);
        // u.setPassword("1234");
        // u.setUserId(UUID.randomUUID().toString());
        // u.setEnabled(true);
        // u.setProvider(Providers.GOOGLE);
        // u.setEmailVerified(true);
        // u.setProviderUserId(user.getName());
        // u.setRoleList(List.of(AppConstants.USER_ROLE));
        // u.setAbout("this account is created using google oauth");

        // User u1 = userRepo.findByEmail(email).orElse(null);
        // if(u1 == null){
        // userRepo.save(u);
        // logger.info("user created using google oauth" + u.getEmail());
        // }

        response.sendRedirect("/user/dashboard");
    }
}
