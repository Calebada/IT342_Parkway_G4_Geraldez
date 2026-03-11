package com.parkway.demo.config;

import com.parkway.demo.model.User;
import com.parkway.demo.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        
        // Get user from database to get the user ID
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // Build redirect URL with user info as URL parameters
            String redirectUrl = String.format(
                "http://localhost:3000/oauth-callback?userId=%d&email=%s&firstname=%s&lastname=%s&role=%s",
                user.getUserID(),
                URLEncoder.encode(user.getEmail() != null ? user.getEmail() : "", StandardCharsets.UTF_8),
                URLEncoder.encode(user.getFirstname() != null ? user.getFirstname() : "", StandardCharsets.UTF_8),
                URLEncoder.encode(user.getLastname() != null ? user.getLastname() : "", StandardCharsets.UTF_8),
                URLEncoder.encode(user.getRole() != null ? user.getRole() : "user", StandardCharsets.UTF_8)
            );
            
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } else {
            // Fallback redirect if user not found (shouldn't happen)
            getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/login?error=user_not_found");
        }
    }
}
