package indre.chimoutite.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import indre.chimoutite.popularmovies.database.FilmDataModel;
import indre.chimoutite.popularmovies.database.FilmDatabase;

public class FilmDataViewModel extends AndroidViewModel {

    private FilmDatabase filmDatabase;
    private final LiveData<List<FilmDataModel>> mAllFilms;

    public FilmDataViewModel(Application application) {
        super(application);

        filmDatabase = FilmDatabase.getDatabase(this.getApplication());
        mAllFilms = filmDatabase.filmDetailsList().getAllDataItems();
    }

    public LiveData<List<FilmDataModel>> getFilmData() {
        return mAllFilms;
    }

    // Insert item into the database in the background
    public void insertItem(FilmDataModel filmDataModel) {
        new insertAsyncTask(filmDatabase).execute(filmDataModel);
    }

    private static class insertAsyncTask extends AsyncTask<FilmDataModel, Void, Void> {
        private FilmDatabase db;

        insertAsyncTask(FilmDatabase filmDatabase) {
            db = filmDatabase;
        }

        @Override
        protected Void doInBackground(final FilmDataModel... params) {
            db.filmDetailsList().addData(params[0]);
            return null;
        }
    }

    // Delete item from the database in the background
    public void deleteItem(FilmDataModel filmDataModel) {
        new deleteAsyncTask(filmDatabase).execute(filmDataModel);
    }

    private static class deleteAsyncTask extends AsyncTask<FilmDataModel, Void, Void> {
        private FilmDatabase db;

        deleteAsyncTask(FilmDatabase filmDatabase) {
            db = filmDatabase;
        }

        @Override
        protected Void doInBackground(final FilmDataModel... params) {
            db.filmDetailsList().deleteData(params[0]);
            return null;
        }
    }
}
