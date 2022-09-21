package com.training.apparatus.view;

import com.training.apparatus.data.repo.ResultRepository;
import com.training.apparatus.data.repo.TaskRepository;
import com.training.apparatus.data.service.ResultService;
import com.training.apparatus.data.service.UserService;
import com.training.apparatus.data.widget.Stopwatch;
import com.training.apparatus.secutiy.SecurityService;
import com.training.apparatus.data.entity.*;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PermitAll
@Route(value = "course", layout =  MainLayout.class)
@PageTitle("Course apparatus")
public class CourseAdminView extends VerticalLayout {

    TaskRepository taskRepository;
    SecurityService securityService;
    ResultService resultService;
    ResultRepository resultRepository;
    UserService userService;
    Tabs columnTabs;
    Tabs headerTabs;
    User auth;

    TextField textField;
    TextArea textArea;
    Label title;

    Stopwatch stopwatch = new Stopwatch();
    Boolean flag = false;
    Integer count = 0;

    public CourseAdminView(TaskRepository taskRepository, SecurityService securityService,
                           ResultService resultService, ResultRepository resultRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.securityService = securityService;
        this.resultService = resultService;
        this.resultRepository = resultRepository;
        this.userService = userService;
        addClassName("course-admin-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setAuth();
        add( addHorizTables() );
        HorizontalLayout layout = new HorizontalLayout(addVerticalTables(), addSimulator());
        layout.setSizeFull();
        add(layout);

        setAlignItems(Alignment.CENTER);
    }

    public void setAuth() {
        auth = securityService.getAuthUser();
    }

    public Div addHorizTables() {
        Div div = new Div();
        Tab simple = new Tab("Simple");
        Tab hard = new Tab("Hard");
        Tab english = new Tab("English");

        headerTabs = new Tabs(simple, hard, english);
        columnTabs = new Tabs();
        headerTabs.setMaxWidth("100%");
        headerTabs.setWidth("400px");
        div.add(headerTabs);
        headerTabs.addSelectedChangeListener(event ->
                updateVerticalTables()
        );
        return div;
    }

    public VerticalLayout addVerticalTables() {
        updateVerticalTables();
        VerticalLayout layout = new VerticalLayout();

        if(auth.getRole().equals(Role.ROLE_ADMIN)) {
            Button button = new Button("add Element");
            layout.add(button);
            button.addClickListener(
                    b -> addElement()
            );
        }
        layout.add(columnTabs);
        columnTabs.setWidth("240px");
        columnTabs.setOrientation(Tabs.Orientation.VERTICAL);
        layout.setWidth("250px");
        columnTabs.addSelectedChangeListener(event ->
                updateSimulator()
        );
        return layout;
    }

    public VerticalLayout addSimulator() {
        VerticalLayout layout = new VerticalLayout();
        // при старте тут вылетает ошибка, так как может не быть выбранных объектов

        title = new Label("You haven't completed this quest yet");
        layout.add(title);

        textArea = new TextArea();
        textArea.setWidth("85%");
        textArea.setHeight("75%");
        textArea.setValueChangeMode(ValueChangeMode.EAGER);

        textField = new TextField();
        textField.setWidth("85%");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        layout.add(textArea);

        textField.addKeyPressListener(e -> {
            startTimer();
            incCount();
            if(textField.getValue().length() != 0)
                checkForCorrection(1);
            stopTimer();
        });

        if(auth.getRole().equals(Role.ROLE_ADMIN)) {
            Button button = new Button("Save");
            layout.add(button);
            button.addClickListener(
                    b -> save()
            );
        } else {
            textArea.setReadOnly(true);
        }

        layout.add(textField);
        updateSimulator();
        layout.setHeight("100%");
        layout.setSizeFull();
        return layout;
    }


    public void updateVerticalTables() {
        columnTabs.removeAll();
        String name = headerTabs.getSelectedTab().getLabel();
        Optional<Long> size = taskRepository.getSizeByType(name);
        List<String> namesTab = userService.getMapResultInTask(auth);
//        List<String> namesTab = results.stream()
//                .map(r -> r.getTask().getType() + " " + r.getTask().getNumber()).collect(Collectors.toList());
        if(size.isPresent()) {
            for (int i = 0; i < size.get(); i++) {
                String nameTab = name + " " + String.valueOf(i + 1);
                Tab tab;
                if(namesTab.contains(nameTab)) {
                    tab = new Tab(
                            VaadinIcon.CHECK_CIRCLE.create(),
                            new Span(nameTab)
                    );
                } else {
                    tab = new Tab(
                            VaadinIcon.CLOSE_CIRCLE.create(),
                            new Span(nameTab)
                    );
                }
                columnTabs.add(tab);
            }
        } else {
            addElement();
        }
    }

    public void updateSimulator() {
        if(columnTabs.getChildren().collect(Collectors.toList()).size() == 0) {
            title.setText("You haven't completed this quest yet");
            textArea.setValue("The text for typing is not ready yet");
            return;
        }
        Span span = columnTabs.getSelectedTab().getChildren().filter(tab -> tab instanceof Span)
                .map(tab -> (Span)tab).findFirst().get();
        String str[] = span.getText().split(" ");
        String type = str[0];
        long number = Long.valueOf(str[1]);
        Optional<Result> result = resultRepository.findResultByTaskAndUser(type, number, auth.getEmail());
        if(result.isPresent()) {
            title.setText("You have already completed this task\n" +
                    "Your speed " + result.get().getSpeed() + "\n" +
                    "Your error rate " + result.get().getMistakes());
        } else {
            title.setText("You haven't completed this quest yet");
        }
        Optional<Task> task = taskRepository.findByNumberAndType(headerTabs.getSelectedTab().getLabel(), number);
        if (task.isPresent()) {
            textArea.setValue(task.get().getText());
        } else {
            textArea.setValue("Enter text");
        }
    }

    public void save() {
        Span span = columnTabs.getSelectedTab().getChildren().filter(tab -> tab instanceof Span)
                .map(tab -> (Span)tab).findFirst().get();
        String str[] = span.getText().split(" ");
        String type = str[0];
        long number = Long.valueOf(str[1]);
        Optional<Task> task = taskRepository.findByNumberAndType(type, number);
        if (task.isPresent()) {
            task.get().setText(textArea.getValue());
            taskRepository.save(task.get());
        } else {
            Task t = new Task();
            t.setText(textArea.getValue());
            t.setType(getType(headerTabs.getSelectedTab().getLabel()));
            long size = taskRepository.getSizeByType(headerTabs.getSelectedTab().getLabel()).orElse(Long.valueOf(0)) + 1;
            t.setNumber(size);
            taskRepository.save(t);
            updateVerticalTables();
            updateSimulator();
        }

    }

    public Type getType(String type) {
        if(Type.Simple.name().equals(type)) {
            return Type.Simple;
        } else if(Type.Hard.name().equals(type)) {
            return Type.Hard;
        } else if(Type.English.name().equals(type)) {
            return Type.English;
        } else {
            return Type.Simple;
        }
    }

    public void addElement() {
        String name = headerTabs.getSelectedTab().getLabel();
        long size = taskRepository.getSizeByType(name).orElse(Long.valueOf(0));
        Tab tab = new Tab(
                VaadinIcon.CLOSE_CIRCLE.create(),
                new Span(name + " " + String.valueOf(size + 1))
        );
        columnTabs.add(tab);
        //columnTabs.setSelectedTab(tab);
        updateSimulator();
    }

    public void checkForCorrection(int ind) {
        String filed = textField.getValue();
        char chInput = filed.charAt(filed.length()-ind);
        char chComp = textArea.getValue().charAt(filed.length()-ind);
        if(chInput == chComp) {
            String sub = filed.substring(0, filed.length() - ind + 1);
            textField.setValue(sub);
        } else {
            if(filed.length()-ind == 0) {
                String sub = filed.substring(0, filed.length() - ind);
                textField.setValue(sub);
                return;
            }
            checkForCorrection(ind + 1);
        }
    }

    public void startTimer() {
        if(flag == false) {
            flag = true;
            stopwatch.start();
        }
    }

    public void stopTimer() {
        String str = textField.getValue();
        if(textArea.getValue().length() == str.length() &&
                textArea.getValue().charAt(textArea.getValue().length() - 1) == str.charAt(str.length() - 1)) {
            stopwatch.stop();
            flag = false;
            textField.setValue("");
            generateResult();
            count = 0;
        }
    }

    public void generateResult() {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        Span span = columnTabs.getSelectedTab().getChildren().filter(tab -> tab instanceof Span)
                .map(tab -> (Span)tab).findFirst().get();
        String str_number[] = span.getText().split(" ");
        String type = str_number[0];
        long number = Long.valueOf(str_number[1]);

        Result result = resultService.save(count, textArea.getValue().length(), stopwatch.getResultSec(), headerTabs.getSelectedTab().getLabel(), number);

        String str = "Тренировка прошла успешно!\n" + "Ваша скорость: "
                + result.getSpeed() + " зн/мин\n" +
                "Ваша точность: " + result.getMistakes() + " %";
        Div div = new Div(new Text(str));

        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> {
            notification.close();
        });

        HorizontalLayout layout = new HorizontalLayout(div, closeButton);
        layout.setAlignItems(Alignment.CENTER);
        notification.setPosition(Notification.Position.MIDDLE);

        notification.add(layout);
        notification.open();

    }

    public void incCount() {
        count++;
    }
}
