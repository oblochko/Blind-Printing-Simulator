package com.training.apparatus.view;

import com.training.apparatus.data.entity.User;
import com.training.apparatus.data.repo.ResultRepository;
import com.training.apparatus.data.repo.UserRepository;
import com.training.apparatus.secutiy.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Vaadin CRM")
public class DashboardView extends VerticalLayout {
    private UserRepository userRepository;
    private SecurityService securityService;
    private ResultRepository resultRepository;

    private User user;

    public DashboardView(UserRepository userRepository, SecurityService securityService, ResultRepository resultRepository) {

        this.userRepository = userRepository;
        this.securityService = securityService;
        this.resultRepository = resultRepository;

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        addUser();
        add(getContactStats());
        setAlignItems(Alignment.CENTER);
    }

    public void addUser() {
        UserDetails userAuth = securityService.getAuthenticatedUser();
        user = userRepository.findByEmail(userAuth.getUsername());
    }
    private Component getContactStats() {
        Span stats = new Span(resultRepository.countResult(user.getId()) + " attempts");
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }


}
