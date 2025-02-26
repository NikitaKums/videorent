package test.fujitsu.videostore.ui.inventory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
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
        return Helper.CreateHorizontalLayout(newEntityButton, filter);
    }

    private void initializeGrid(){
        grid = new MovieGrid();
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        grid.setDataProvider(dataProvider);
    }

    private void createNewMovieButton(){
        newEntityButton = Helper.CreateButtonWithText("new-item", "New Movie");
        newEntityButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newEntityButton.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newEntityButton.addClickListener(click -> viewLogic.newEntity());
    }

    private void createFilterTextField(){
        filter = Helper.CreateTextFieldWithPlaceholder("filter", "Filter by name", ValueChangeMode.EAGER);
        filter.addValueChangeListener(event -> {
            clearFilters();
            String value = event.getValue();
            if (!Helper.IsStringEmptyOrWhitespace(value)) {
                dataProvider.addFilter(item -> item.getName().toLowerCase().contains(value.toLowerCase()));
            } else {
                clearFilters();
            }
        });
    }

    @Override
    public void editEntity(Movie entity) {
        showForm(entity != null);
        form.editEntity(entity);
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
