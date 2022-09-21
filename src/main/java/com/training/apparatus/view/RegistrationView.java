package com.training.apparatus.view;

import com.training.apparatus.data.entity.Role;
import com.training.apparatus.data.entity.User;
import com.training.apparatus.data.repo.UserRepository;
import com.training.apparatus.data.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@AnonymousAllowed
@Route("registration")
@PageTitle("Registration | Simulator")
@Getter
@Setter
public class RegistrationView extends VerticalLayout  {
    private TextField pseudonym = new TextField("Pseudonym");
    private TextField email = new TextField("Email");
    private PasswordField password = new PasswordField("Password");
    private PasswordField confirmPassword = new PasswordField("Confirm password");
    private Button registration = new Button("Registration");

    Binder<User> binder = new Binder<>(User.class);

    //Binder<User> binder = new BeanValidationBinder<>(User.class);

    private User user = new User();

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    //private FormLayout formLayout = new FormLayout();

    public RegistrationView(){
        //binder.bindInstanceFields(this);
        view();
        validate();
        registration.addClickListener(e -> {
            save();
//            registration.getUI().ifPresent(ui ->
//                    ui.navigate(""));
        });
        //binder.addStatusChangeListener(e -> registration.setEnabled(binder.isValid()));
    }

    public void view() {
        pseudonym.setMinWidth("450px");
        email.setMinWidth("450px");
        password.setMinWidth("450px");
        confirmPassword.setMinWidth("450px");
        registration.setMinWidth("450px");
        add(
                pseudonym, email,
                password, confirmPassword,
                registration
        );
        setAlignItems(Alignment.CENTER);
//        formLayout.setResponsiveSteps(
//                // Use one column by default
//                new FormLayout.ResponsiveStep("0", 1)
//                // Use two columns, if layout's width exceeds 500px
//                //new FormLayout.ResponsiveStep("500px", 2)
//        );
        // Stretch the username field over 2 columns
        //formLayout.setColspan(username, 2);

        //add(formLayout);
    }

    private void save() {
        try {
            if(binder.validate().isOk() ) {
                binder.writeBean(user);
                user.setRole(Role.ROLE_WORKER);
                userService.save(user);
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
        password.setValueChangeMode(ValueChangeMode.EAGER);
        confirmPassword.setValueChangeMode(ValueChangeMode.EAGER);

        //binderPseudonym.bind(pseudonym, User::getPseudonym, User::setPseudonym);
        binder.forField(pseudonym)
                // Explicit validator instance
                .asRequired("This field must not be empty")
                .bind(User::getPseudonym, User::setPseudonym);

        binder.forField(email)
                // Explicit validator instance
                .withValidator(new EmailValidator(
                        "This doesn't look like a valid email address"))
                .bind(User::getEmail, User::setEmail);

        binder.forField(password)
                // Validator defined based on a lambda
                // and an error message
                .withValidator(
                        pass -> pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{4,15}$"),
                        "Password does not meet the requirements")
                .bind(User::getPassword, User::setPassword);

        binder.forField(confirmPassword)
                // Validator defined based on a lambda
                // and an error message
                .withValidator(
                        pass -> password.getValue().equals(confirmPassword.getValue()),
                        "The spanks are not the same");
    }


}

