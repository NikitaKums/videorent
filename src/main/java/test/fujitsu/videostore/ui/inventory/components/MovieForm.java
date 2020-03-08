package test.fujitsu.videostore.ui.inventory.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.database.Database;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.MovieType;
import test.fujitsu.videostore.backend.domain.RentOrder;
import test.fujitsu.videostore.ui.base.BaseForm;
import test.fujitsu.videostore.ui.base.BaseFormImpl;
import test.fujitsu.videostore.ui.customer.Converter.StockCountConverter;
import test.fujitsu.videostore.ui.database.CurrentDatabase;
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
        tempBinder.forField(stockCount)
                .withValidator(item -> item != null && !item.trim().isEmpty() && !item.contains(","), "Invalid movie stock count")
                .withConverter(new StockCountConverter())
                .bind("stockCount");
        Helper.AddBinderStatusChangeListener(tempBinder, save);

        return tempBinder;
    }

    private void addListenerToSaveButton(){
        save.addClickListener(event -> {
            if (entity != null) {
                binder.writeBeanIfValid(entity);
                if (entity.getName() == null || entity.getName().trim().isEmpty()){
                    Notification.show("Please fill movie name field.");
                    return;
                }
                if (entity.getStockCount() < 0 ) {
                    Notification.show("Please enter positive stock count");
                    return;
                }
                if (entity.getType() == null){
                    Notification.show("Please select movie type");
                    return;
                }
                if (entity.getId() == -1 && doesMovieWithNameExist(entity.getName())){
                    Notification.show("Movie with given name already exists");
                    return;
                }
                viewLogic.saveEntity(entity);
            }
        });
    }

    @Override
    public void editEntity(Movie movie) {
        if (movie == null) {
            movie = new Movie();
        }
        if (movie.getId() == -1){
            delete.setEnabled(false);
        }
        else {
            delete.setEnabled(!wasMovieRented(movie.getId()));
        }
        entity = movie;
        binder.readBean(movie);
    }

    // As stated that database interfaces should not be changed, i am not going to change Database/DBTableRepository<Movie> to MovieTableRepository.
    // Thus i am unable to define methods in MovieTableRepository interface to access them via CurrentDatabase.get().getMovieTable().
    // Which means that i have to find if movie was already rented through getOrderTable().getAll() iteration.
    // e.g. wasMovieRented(int id) in MovieTableRepository and access it via CurrentDatabase.get().getMovieTable().wasMovieRented(movieId);
    private boolean wasMovieRented(int movieId){
        DBTableRepository<RentOrder> orderTable = CurrentDatabase.get().getOrderTable();
        for (RentOrder rentOrder: orderTable.getAll()) {
            if (rentOrder.getItems() != null){
                for (RentOrder.Item item: rentOrder.getItems()) {
                    if (item.getMovie().getId() == movieId){
                        return true; // movie was rented
                    }
                }
            }
        }
        return false; // movie hasn't been rented
    }

    private boolean doesMovieWithNameExist(String movieName){
        return CurrentDatabase.get().getMovieTable().getAll().stream().anyMatch(movie -> movie.getName().equals(movieName));
    }
}
