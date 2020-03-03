package test.fujitsu.videostore.ui.helpers;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class Helper {

    public static Button CreateButtonWithText(String text){
        Button button = new Button(text);
        button.setId("new-item");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setIcon(VaadinIcon.PLUS_CIRCLE.create());

        return button;
    }

    public static TextField CreateTextFieldWithPlaceholder(String placeholder){
        TextField textField = new TextField();
        textField.setId("filter");
        textField.setPlaceholder("Filter by name");
        textField.setValueChangeMode(ValueChangeMode.EAGER);

        return textField;
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
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(horizontalLayout);
        verticalLayout.add(grid);
        verticalLayout.setFlexGrow(1, grid);
        verticalLayout.setFlexGrow(0, horizontalLayout);
        verticalLayout.setSizeFull();
        verticalLayout.expand(grid);

        return verticalLayout;
    }
}
