package com.training.apparatus.view;

import com.training.apparatus.data.entity.Group;
import com.training.apparatus.data.repo.UserRepository;
import com.training.apparatus.data.service.GroupService;
import com.training.apparatus.data.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

@PermitAll
@Route("registrationgroup")
@PageTitle("Registration Group | Simulator")
@Getter
@Setter
public class RegistrationGroupView extends VerticalLayout {
    private TextField name = new TextField("Name");
    private TextField link = new TextField("Link");
    private Button generate = new Button();
    private Button registration = new Button("Registration");

    Binder<Group> binder = new Binder<>(Group.class);

    private Group group = new Group();

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupService groupService;

    //private FormLayout formLayout = new FormLayout();

    public RegistrationGroupView(){
        //binder.bindInstanceFields(this);
        view();
        validate();
        registration.addClickListener(e -> {
            save();
            registration.getUI().ifPresent(ui ->
                    ui.navigate(""));
        });
        setAlignItems(Alignment.CENTER);
        //binder.addStatusChangeListener(e -> registration.setEnabled(binder.isValid()));
    }

    public void view() {
        Span span = new Span("Registration group");
        HorizontalLayout horiz = new HorizontalLayout();
        name.setMinWidth("450px");
        link.setMinWidth("450px");
        link.setReadOnly(true);
        link.setValue(String.valueOf(generateCode()));
        generate.setIcon(new Icon(VaadinIcon.REFRESH));
        horiz.add(link, generate);
        add(
                span, name, horiz, registration
        );
        setAlignItems(FlexComponent.Alignment.CENTER);
        generate.addClickListener(
                e -> link.setValue(String.valueOf(generateCode()))
        );
        registration.addClickListener(
                e -> save()
        );
    }

    private int generateCode() {
        double max = 800000;
        double min = 200000;
        return (int)((Math.random() * ((max - min) + 1)) + min);
    }

    private void save() {
        try {
            if(binder.validate().isOk() ) {
                binder.writeBean(group);
                group.setLink(link.getValue());
                groupService.save(group);
                registration.getUI().ifPresent(ui ->
                        ui.navigate(""));
            }
        } catch (ValidationException e) {
            e.printStackTrace();
        } finally {
            System.out.println();
        }

    }

    private void validate() {
        name.setValueChangeMode(ValueChangeMode.EAGER);

        //binderPseudonym.bind(pseudonym, User::getPseudonym, User::setPseudonym);
        binder.forField(name)
                // Explicit validator instance
                .asRequired("This field must not be empty")
                .bind(Group::getName, Group::setName);

    }
}
