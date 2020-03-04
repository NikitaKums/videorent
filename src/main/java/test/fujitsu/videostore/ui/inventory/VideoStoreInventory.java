package test.fujitsu.videostore.ui.inventory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.ui.MainLayout;
import test.fujitsu.videostore.ui.base.BaseListViewImpl;
import test.fujitsu.videostore.ui.helpers.Helper;
import test.fujitsu.videostore.ui.inventory.components.MovieForm;
import test.fujitsu.videostore.ui.inventory.components.MovieGrid;

import java.util.ArrayList;
import java.util.List;

@Route(value = VideoStoreInventory.VIEW_NAME, layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class VideoStoreInventory extends BaseListViewImpl<Movie, MovieGrid, MovieForm> implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Inventory";
    private VideoStoreInventoryLogic viewLogic = new VideoStoreInventoryLogic(this);
    private Button newMovie;

    public VideoStoreInventory() {
        super(new ListDataProvider<>(new ArrayList<>()));
        setId(VIEW_NAME);
        setSizeFull();

        HorizontalLayout topLayout = createTopBar();
        initializeGrid();
        form = new MovieForm(viewLogic);

        add(Helper.CreateVerticalLayout(topLayout, grid));
        add(form);

        viewLogic.init();
    }

    private HorizontalLayout createTopBar() {
        createFilterTextField();
        createNewMovieButton();
        return Helper.CreateHorizontalLayout(newMovie, filter);
    }

    private void initializeGrid(){
        grid = new MovieGrid();
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        grid.setDataProvider(dataProvider);
    }

    private void createNewMovieButton(){
        newMovie = Helper.CreateButtonWithText("New Movie");
        newMovie.addClickListener(click -> viewLogic.newEntity());
        newEntityButton = newMovie;
    }

    private void createFilterTextField(){
        filter = Helper.CreateTextFieldWithPlaceholder("Filter by name");
        filter.addValueChangeListener(event -> {
            // TODO: Implement filtering by movie name
        });
    }

    @Override
    public void editEntity(Movie entity) {
        showForm(entity != null);
        form.editMovie(entity);
    }

    @Override
    public void showForm(boolean show) {
        form.setVisible(show);
    }

    @Override
    public void setEntities(List<Movie> entities) {
        dataProvider.getItems().clear();
        dataProvider.getItems().addAll(entities);
        dataProvider.refreshAll();
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}
