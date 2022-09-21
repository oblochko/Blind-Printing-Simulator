package com.training.apparatus.view;

import com.training.apparatus.data.dto.UserDto;
import com.training.apparatus.data.repo.ResultRepository;
import com.training.apparatus.data.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@RolesAllowed("ROLE_BOSS")
@Route(value = "list", layout =  MainLayout.class)
@PageTitle("Worker List")
public class WorkerListView extends VerticalLayout {
    Grid<UserDto> grid = new Grid<>(UserDto.class);
    TextField filterText = new TextField();

    private ResultRepository resultRepository;

    private UserService userService;

    public WorkerListView(ResultRepository resultRepository, UserService userService) {
        this.resultRepository = resultRepository;
        this.userService = userService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        add(getToolbar(), grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("pseudonym", "email", "avgMistakes", "avgSpeed", "count");
        grid.getColumnByKey("count").setHeader("Пройдено курсов");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        Button addContactButton = new Button("Add contact");

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void updateList() {
        grid.setItems(userService.findUsersDtoByGroup());
    }
}
