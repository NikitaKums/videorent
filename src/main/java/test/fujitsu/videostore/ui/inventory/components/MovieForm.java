package test.fujitsu.videostore.ui.inventory.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.MovieType;
import test.fujitsu.videostore.ui.base.BaseForm;
import test.fujitsu.videostore.ui.base.BaseFormImpl;
import test.fujitsu.videostore.ui.customer.Converter.StockCountConverter;
import test.fujitsu.videostore.ui.helpers.Helper;
import test.fujitsu.videostore.ui.inventory.VideoStoreInventoryLogic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Movie form
 */
public class MovieForm extends BaseFormImpl<Movie, VideoStoreInventoryLogic> implements BaseForm<Movie> {

    private VerticalLayout content;

    private TextField name;
    private TextField stockCount;
    private ComboBox<MovieType> type;
    private Binder<Movie> binder;

    public MovieForm(VideoStoreInventoryLogic videoStoreInventoryLogic) {
        super(new Movie(), videoStoreInventoryLogic);
        setId("edit-form");

        content = Helper.CreateVerticalLayout();
        content.setSizeUndefined();
        add(content);

        name = Helper.CreateTextField("movie-name", "Movie name", "100%", true,  ValueChangeMode.EAGER);
        content.add(name);

        type = Helper.CreateComboBox("movie-type", "Movie type", "100%", true);
        type.setItems(MovieType.values());
        type.setItemLabelGenerator(MovieType::getTextualRepresentation);
        content.add(type);

        stockCount = Helper.CreateTextField("stock-count", "In stock", "100%", true,  ValueChangeMode.EAGER);
        stockCount.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        content.add(stockCount);

        createSaveButton();
        createCancelButton();
        createDeleteButton();

        addListenerToSaveButton();
        binder = createBinder();

        content.add(save, delete, cancel);
    }

    private Binder<Movie> createBinder(){
        Binder<Movie> tempBinder = new Binder<>(Movie.class);
        tempBinder.forField(name)
                .bind("name");
        tempBinder.forField(type)
                .bind("type");
        tempBinder.forField(stockCount).withConverter(new StockCountConverter())
                .bind("stockCount");
        Helper.AddBinderStatusChangeListener(tempBinder, save);

        return tempBinder;
    }

    private void addListenerToSaveButton(){
        save.addClickListener(event -> {
            if (entity != null) {
                // TODO: Validation for movie name, validate that movie type is selected
                binder.writeBeanIfValid(entity);
                viewLogic.saveEntity(entity);
            }
        });
    }

    @Override
    public void editEntity(Movie movie) {
        if (movie == null) {
            movie = new Movie();
        }
        entity = movie;
        binder.readBean(movie);

        // TODO: Delete movie button should be inactive if it’s new movie creation or it was rented at least one time.
        delete.setEnabled(true);
    }
}
