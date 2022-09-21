package com.training.apparatus.view;

import com.training.apparatus.data.entity.Result;
import com.training.apparatus.data.service.ResultService;
import com.training.apparatus.data.widget.Stopwatch;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "", layout =  MainLayout.class)
@PageTitle("Training apparatus")
public class Simulator  extends VerticalLayout {
    @Autowired
    private ResultService resultService;

    private String text = "Миллионы людей совершали друг против друга такое бесчисленное количество злодеяний, обманов, измен, воровства, подделок и выпуска фальшивых ассигнаций, грабежей, поджогов и убийств, которого в целые века не соберет летопись всех судов мира и на которые, в этот период времени, люди, совершавшие их, не смотрели как на преступления.";
    TextField textField;
    TextArea textArea;
    Stopwatch stopwatch = new Stopwatch();
    Boolean flag = false;
    Integer count = 0;


    public Simulator() {
        setSizeFull();
        /*Button button = new Button("I'm a button");*/
        textArea = new TextArea();
        textArea.setReadOnly(true);
        textArea.setValue(text);
        textArea.setWidth("60%");
        textField = new TextField("start typing");
        textField.setWidth("60%");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        //textField.addThemeVariants();
        add(/*button, */textArea, textField);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        Key key = Key.of("f");
        textField.addKeyUpListener(key,null, KeyModifier.SHIFT);

        /*TextField valueChange = new TextField("addValueChangeListener");
        TextField inputL = new TextField("addInputListener");
        TextField attachL = new TextField("addAttachListener");*/
        //HorizontalLayout layout1 = new HorizontalLayout(valueChange, inputL, attachL);

        /*final int[] value = {0};
        textField.addValueChangeListener(e -> {
            value[0]++; valueChange.setValue(" " + value[0]);});
        final int[] input = {0};
        final int[] index = {0};

        textField.addInputListener(e -> {

            //index[0]++; textField.setValue(" " + input[0]);
            input[0]++; inputL.setValue(" " + textField.getValue().length());});

        final int[] attach = {0};
        textField.addAttachListener(e -> {
            attach[0]++; attachL.setValue(" " + attach[0]);});*/

        /*TextField keyDown = new TextField("addKeyDownListener");
        TextField keyPress = new TextField("addKeyPressListener");
        TextField keyUp = new TextField("addKeyUpListener");
        HorizontalLayout layout3 = new HorizontalLayout(keyDown, keyPress, keyUp);*/

        /*final int[] down = {0};
        textField.addKeyDownListener(e -> {
            down[0]++; keyDown.setValue(" " + textField.getValue().length());});*/
        final int[] press = {0};
        textField.addKeyPressListener(e -> {
            startTimer();
            incCount();
            if(textField.getValue().length() != 0)
                checkForCorrection(1);
            press[0]++; //  keyPress.setValue(" " + textField.getValue().length());
            stopTimer();
        });

        /*final int[] up = {0};
        Binder<TextField> binder = new Binder<>();

        textField.addKeyUpListener(e -> {
            up[0]++; keyUp.setValue(" "  + up[0]);});*/

        //add(layout1, layout3);
    }

    public void checkForCorrection(int ind) {
        String filed = textField.getValue();
        char chInput = filed.charAt(filed.length()-ind);
        char chComp = text.charAt(filed.length()-ind);
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
            stopwatch.start();
        }
    }

    public void stopTimer() {
        String str = textField.getValue();
        if(text.length() == str.length() &&
            text.charAt(text.length() - 1) == str.charAt(str.length() - 1)) {
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
        Result result = resultService.create(count, text.length(), stopwatch.getResultMin());

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
