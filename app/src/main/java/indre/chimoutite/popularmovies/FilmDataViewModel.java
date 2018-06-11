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
        mAllFilms = filmDatabase.filmDao().getAllDataItems();
    }

    public LiveData<List<FilmDataModel>> getFilmData() {
        return mAllFilms;
    }

    // Find if the movie is already in the database
    public boolean findFilm(String ID) {
        if (filmDatabase.filmDao().inDatabase(ID)) {return true; };
        return false;
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
            db.filmDao().addData(params[0]);
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
            db.filmDao().deleteData(params[0]);
            return null;
        }
    }
}
