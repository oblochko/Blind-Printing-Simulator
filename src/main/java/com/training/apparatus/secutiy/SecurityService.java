package com.training.apparatus.secutiy;

import com.training.apparatus.data.entity.User;
import com.training.apparatus.data.repo.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
//@Component
public class SecurityService {

    @Autowired
    private UserRepository userRepository;

    private static final String LOGOUT_SUCCESS_URL = "/";

//    public User getAuthenticatedUser() {
//        SecurityContext context = SecurityContextHolder.getContext();
//        Object principal = context.getAuthentication().getPrincipal();
//        if (principal instanceof User) {
//            return (User) principal;
//        }
//        // Anonymous or no authentication.
//        return null;
//    }

    public UserDetails getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }
        // Anonymous or no authentication.
        return null;
    }

    public User getAuthUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return userRepository.findByEmail(((UserDetails) principal).getUsername());
        }
        // Anonymous or no authentication.
        return null;
    }

    public String getAuthenticatedUserRole() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (!(principal instanceof User)) {
            // Anonymous or no authentication.
            return null;
        }
        UserDetails user = (UserDetails) principal;
        GrantedAuthority authority = user.getAuthorities().stream().findFirst().get();
        return authority.getAuthority();
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }
}
