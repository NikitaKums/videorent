package test.fujitsu.videostore.ui.inventory;

import com.vaadin.flow.component.UI;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.ui.base.BaseListLogicImpl;
import test.fujitsu.videostore.ui.database.CurrentDatabase;

public class VideoStoreInventoryLogic extends BaseListLogicImpl<Movie, VideoStoreInventory> {

    public VideoStoreInventoryLogic(VideoStoreInventory videoStoreInventory) {
        super(videoStoreInventory);
    }

    @Override
    public void init() {
        if (CurrentDatabase.get() == null) {
            return;
        }
        dBTableRepository = CurrentDatabase.get().getMovieTable();

        view.setNewEntityEnabled(true);
        view.setEntities(dBTableRepository.getAll());
    }

    @Override
    public void setFragmentParameter(String movieId) {
        String fragmentParameter;
        if (movieId == null || movieId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = movieId;
        }

        UI.getCurrent().navigate(VideoStoreInventory.class, fragmentParameter);
    }

    @Override
    public void saveEntity(Movie movie) {
        boolean isNew = movie.isNewObject();

        Movie updatedMovieObject = dBTableRepository.createOrUpdate(movie);

        if (isNew) {
            view.addEntity(updatedMovieObject);
        } else {
            view.updateEntity(movie);
        }

        view.clearSelection();
        setFragmentParameter("");
        view.showSaveNotification(movie.getName() + (isNew ? " created" : " updated"));
    }

    @Override
    public void deleteEntity(Movie movie) {
        dBTableRepository.remove(movie);

        view.clearSelection();
        view.removeEntity(movie);

        setFragmentParameter("");
        view.showSaveNotification(movie.getName() + " removed");
    }

    /**
     * Method fired when user selects movie which he want to edit.
     *
     * @param movie Movie object
     */
    @Override
    public void editEntity(Movie movie) {
        if (movie == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(movie.getId() + "");
        }
        view.editEntity(movie);
    }

    @Override
    public void newEntity() {
        view.editEntity(new Movie());
        view.clearSelection();
        setFragmentParameter("new");
    }
}
