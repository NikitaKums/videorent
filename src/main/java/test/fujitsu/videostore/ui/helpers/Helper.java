package test.fujitsu.videostore.ui.helpers;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

public class Helper {

    public static Button CreateButtonWithText(String id, String text){
        Button button = new Button(text);
        button.setId(id);

        return button;
    }

    public static DatePicker CreateDatePicker(String id, String label, String width, boolean readOnly, boolean visible){
        DatePicker datePicker = new DatePicker(label);
        datePicker.setId(id);
        datePicker.setWidth(width);
        datePicker.setReadOnly(readOnly);
        datePicker.setVisible(visible);

        return datePicker;
    }

    public static TextField CreateTextFieldWithPlaceholder(String id, String placeholder, ValueChangeMode valueChangeMode){
        TextField textField = new TextField();
        textField.setId(id);
        textField.setPlaceholder(placeholder);
        if (valueChangeMode != null) textField.setValueChangeMode(valueChangeMode);

        return textField;
    }

    public static TextField CreateTextFieldWithLabel(String id, String label, ValueChangeMode valueChangeMode){
        TextField textField = new TextField(label);
        textField.setId(id);
        if (valueChangeMode != null) textField.setValueChangeMode(valueChangeMode);

        return textField;
    }

    public static <T> ComboBox<T> CreateComboBox(String id, String label, String width, boolean required){
        ComboBox<T> comboBox = new ComboBox<T>(label);
        comboBox.setId(id);
        SetComboBoxWidthAndRequired(comboBox, width, required);

        return comboBox;
    }

    public static TextField CreateTextField(String id, String label, String width, boolean required, ValueChangeMode valueChangeMode){
        TextField textField = CreateTextFieldWithLabel(id, label, valueChangeMode);
        SetTextFieldWidthAndRequired(textField, width, required);
        return textField;
    }

    public static void AddBinderStatusChangeListener(Binder binder, Button button){
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            button.setEnabled(hasChanges && isValid);
        });
    }

    public static Button CreateButtonWithTextAndWidth(String id, String text, String width){
        Button button = new Button(text);
        button.setId(id);
        button.setWidth(width);

        return button;
    }

    public static void SetTextFieldWidthAndRequired(TextField textField, String width, boolean required){
        textField.setWidth(width);
        textField.setRequired(required);
    }

    public static void SetComboBoxWidthAndRequired(ComboBox comboBox, String width, boolean required){
        comboBox.setWidth(width);
        comboBox.setRequired(required);
    }

    public static HorizontalLayout CreateHorizontalLayout(Button button, TextField textField){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.add(textField);
        horizontalLayout.add(button);
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.START, textField);
        horizontalLayout.expand(textField);

        return horizontalLayout;
    }

    public static <T> VerticalLayout CreateVerticalLayout(HorizontalLayout horizontalLayout, Grid<T> grid){
        VerticalLayout verticalLayout = CreateVerticalLayout();
        verticalLayout.add(horizontalLayout);
        verticalLayout.add(grid);
        verticalLayout.setFlexGrow(1, grid);
        verticalLayout.setFlexGrow(0, horizontalLayout);
        verticalLayout.setSizeFull();
        verticalLayout.expand(grid);

        return verticalLayout;
    }

    public static VerticalLayout CreateVerticalLayout(){
        return new VerticalLayout();
    }

    public static boolean IsStringEmptyOrWhitespace(String value) {
        return value.isEmpty() || value.trim().length() == 0;
    }
}
