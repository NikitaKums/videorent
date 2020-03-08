package test.fujitsu.videostore.ui.database;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.File;

@Route("DatabaseSelection")
@PageTitle("Database Selection")
@HtmlImport("css/shared-styles.html")
public class DatabaseSelectionView extends FlexLayout {

    private TextField databasePath;
    private Button selectDatabaseButton;

    public DatabaseSelectionView() {
        setSizeFull();
        setClassName("database-selection-screen");

        FlexLayout centeringLayout = new FlexLayout();
        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(buildLoginForm());

        add(centeringLayout);
    }

    private Component buildLoginForm() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth("310px");

        databasePath = new TextField("Enter database file path");
        databasePath.setId("database-path");
        databasePath.setRequired(true);

        verticalLayout.add(databasePath);

        HorizontalLayout buttons = new HorizontalLayout();
        verticalLayout.add(buttons);

        selectDatabaseButton = new Button("Select database");
        selectDatabaseButton.setId("database-select");
        selectDatabaseButton.addClickListener(event -> selectDatabase());
        selectDatabaseButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        buttons.add(selectDatabaseButton);

        return verticalLayout;
    }

    private void selectDatabase() {
        String databasePathValue = databasePath.getValue();
        selectDatabaseButton.setEnabled(false);
        try {
            if (!(databasePathValue.endsWith(".yaml") || databasePathValue.endsWith(".json"))){
                displayNotification("Database file extension must be either .yaml or .json");
                return;
            }
            else if (!new File(databasePathValue).exists()){
                displayNotification("Could not find file at relative path: " + databasePathValue);
                return;
            }

            CurrentDatabase.set(databasePathValue);

            getUI().get().navigate("");
        } finally {
            selectDatabaseButton.setEnabled(true);
        }
    }

    private void displayNotification(String message){
        Notification databaseFilePathNotification = new Notification(message, 2000);
        databaseFilePathNotification.open();
    }
}
