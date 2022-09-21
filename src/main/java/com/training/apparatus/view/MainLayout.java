package com.training.apparatus.view;

import com.training.apparatus.data.entity.User;
import com.training.apparatus.secutiy.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

import java.util.Optional;


public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Training Apparatus");
        logo.addClassNames("text-l", "m-m");

        Button logout = new Button("Log out", e -> securityService.logout());

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo,
                logout
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.expand(logo);
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Training Apparatus", Simulator.class);
        RouterLink course = new RouterLink("Course", CourseAdminView.class);
        RouterLink theoretical = new RouterLink("Theoretical", TheoreticalBackgroundView.class);
        //RouterLink dashboard = new RouterLink("Dashboard", DashboardView.class);
        RouterLink profile = new RouterLink("Profile", ProfileView.class);
        RouterLink worker = null;
        Optional<User> user = Optional.ofNullable(securityService.getAuthUser());
        if(user.isPresent()) {
            if(user.get().getRole().name().equals("ROLE_BOSS")) {
                 worker = new RouterLink("Worker", WorkerListView.class);
            }
        }
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        if(worker == null) {
            addToDrawer(new VerticalLayout(
                    listLink, course, theoretical, /*dashboard,*/ profile
            ));
        } else {
            addToDrawer(new VerticalLayout(
                    listLink, course, theoretical, /*dashboard,*/ profile, worker
            ));
        }

    }
}
