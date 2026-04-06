package com.parkway.demo.service;

import com.parkway.demo.model.User;
import com.parkway.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        // Check if user already exists
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // Update auth provider if needed
            if (user.getAuthProvider() == null) {
                user.setAuthProvider("google");
                userRepository.save(user);
            }
        } else {
            // Create new user if not exists
            user = new User();
            user.setEmail(email);
            user.setFirstname(firstName != null ? firstName : "");
            user.setLastname(lastName != null ? lastName : "");
            user.setRole("user"); // default role
            user.setAuthProvider("google");
            userRepository.save(user);
        }

        return oAuth2User;
    }
}