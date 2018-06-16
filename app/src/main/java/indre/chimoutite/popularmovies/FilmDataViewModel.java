package indre.chimoutite.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import indre.chimoutite.popularmovies.database.FilmDataModel;
import indre.chimoutite.popularmovies.database.FilmDatabase;

public class FilmDataViewModel extends AndroidViewModel {

    private FilmDatabase filmDatabase;
    private final LiveData<List<FilmDataModel>> mAllFilms;

    // Constant for logging
    private static final String TAG = FilmDataViewModel.class.getSimpleName();

    public FilmDataViewModel(Application application) {
        super(application);

        filmDatabase = FilmDatabase.getDatabase(this.getApplication());
        mAllFilms = filmDatabase.filmDao().loadAllFilms();
        Log.d(TAG, "FilmDataViewModel: Room Db loaded");
    }

    public LiveData<List<FilmDataModel>> loadAllFilms() {
        return mAllFilms;
    }

    public LiveData<FilmDataModel> loadFilmById (int filmId) {
        return filmDatabase.filmDao().loadFilmById(filmId);
    }

    // Insert item into the database in the background
    public void insertFilm(FilmDataModel filmDataModel) {
        Log.d(TAG, "insertFilm being accessed.");
        new insertAsyncTask(filmDatabase).execute(filmDataModel);
    }

    private static class insertAsyncTask extends AsyncTask<FilmDataModel, Void, Void> {
        private FilmDatabase db;

        insertAsyncTask(FilmDatabase filmDatabase) {
            db = filmDatabase;
        }

        @Override
        protected Void doInBackground(final FilmDataModel... params) {
            db.filmDao().addFilm(params[0]);
            return null;
        }
    }

    // Delete item from the database in the background
    public void deleteFilm(FilmDataModel filmDataModel) {
        Log.d(TAG, "deleteFilm being accessed.");
        new deleteAsyncTask(filmDatabase).execute(filmDataModel);
    }

    private static class deleteAsyncTask extends AsyncTask<FilmDataModel, Void, Void> {
        private FilmDatabase db;

        deleteAsyncTask(FilmDatabase filmDatabase) {
            db = filmDatabase;
        }

        @Override
        protected Void doInBackground(final FilmDataModel... params) {
            db.filmDao().deleteFilm(params[0]);
            return null;
        }
    }
}
