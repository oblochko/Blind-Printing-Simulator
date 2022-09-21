package com.training.apparatus.view;

import com.training.apparatus.data.entity.User;
import com.training.apparatus.data.repo.GroupRepository;
import com.training.apparatus.data.repo.ResultRepository;
import com.training.apparatus.data.repo.UserRepository;
import com.training.apparatus.data.service.GroupService;
import com.training.apparatus.data.service.UserService;
import com.training.apparatus.secutiy.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "profile", layout =  MainLayout.class)
@PageTitle("Worker List")
@Getter
@Setter
public class ProfileView extends VerticalLayout {
    private SecurityService securityService;

    private UserService userService;

    private UserRepository userRepository;

    private ResultRepository resultRepository;

    private GroupService groupService;

    private GroupRepository groupRepository;

    User user;

    Button reset;

    public ProfileView(SecurityService securityService, UserRepository userRepository, ResultRepository resultRepository,
                       GroupService groupService, GroupRepository groupRepository, UserService userService) {
        this.securityService = securityService;
        this.userRepository = userRepository;
        this.resultRepository = resultRepository;
        this.groupService = groupService;
        this.groupRepository = groupRepository;
        this.userService = userService;
        addClassName("profile");
        setSizeFull();
        addUser();
        add(getToolBox(), getReset(), getSettingGroup());
        setAlignItems(Alignment.CENTER);
    }

    public void addUser() {
        UserDetails auth = securityService.getAuthenticatedUser();
        user = userRepository.findByEmail(auth.getUsername());
    }

    public VerticalLayout getToolBox() {
        VerticalLayout vert = new VerticalLayout();
        Span hello = new Span("Hello, " +  user.getPseudonym());
        Span speed = new Span("Your average typing speed " + resultRepository.avgSpeed(user.getId()).orElse(0.0));
        Span mistakes = new Span("Your average typing mistakes " + resultRepository.avgMistakes(user.getId()).orElse(0.0));
        Span number = new Span("Your number of attempts " + resultRepository.countResult(user.getId()));
        vert.add(hello, speed, mistakes, number);
        vert.setAlignItems(Alignment.CENTER);
        return vert;
    }

    public Button getReset() {
        reset = new Button("Reset results");
        reset.addClickListener(e -> {
            userService.resetResult(user.getEmail());
        });
        return reset;
    }

    public Component getSettingGroup() {
        if(user.getRole().name().equals("ROLE_BOSS")){
            HorizontalLayout horiz = new HorizontalLayout();
            TextField text = new TextField();
            text.setPlaceholder(user.getGroup().getLink());
            Button button = new Button("Generated");
            button.addClickListener(
                    e -> text.setValue(groupService.generateCode(user.getEmail()))
            );
            horiz.add(text, button);
            horiz.setAlignItems(Alignment.CENTER);
            return horiz;
        }
        if(user.getRole().name().equals("ROLE_WORKER")) {
            if(user.getGroup() == null) {
                VerticalLayout vert = new VerticalLayout();
                Button reg = new Button("Registration Group");
                reg.addClickListener(e ->
                        reg.getUI().ifPresent(ui ->
                                ui.navigate("registrationgroup"))
                );
                HorizontalLayout horiz = new HorizontalLayout();
                TextField text = new TextField();
                text.setPlaceholder("Enter group code");
                Button button = new Button("Enter");
                button.addClickListener(
                    e -> {
                        groupService.addUser(user.getEmail(), text.getValue());
                        button.getUI().ifPresent(ui ->
                                ui.navigate("profile"));
                    }
                );
                horiz.add(text, button);
                vert.add(reg, horiz);
                horiz.setAlignItems(Alignment.CENTER);
                vert.setAlignItems(Alignment.CENTER);
                return vert;
            } else {
                VerticalLayout vert = new VerticalLayout();
                Button reg = new Button("Registration Group");
                reg.addClickListener(e ->
                        reg.getUI().ifPresent(ui ->
                                ui.navigate("registrationgroup"))
                );
                Button button = new Button("Leave the group");
                button.addClickListener(
                        e -> groupService.removeUser(user)
                );
                vert.add(reg, button);
                vert.setAlignItems(Alignment.CENTER);
                return vert;
            }
        }
        return null;
    }

    private int generateCode() {
        double max = 800000;
        double min = 200000;
        return (int)((Math.random() * ((max - min) + 1)) + min);
    }
}
