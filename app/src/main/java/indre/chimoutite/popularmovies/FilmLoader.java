package indre.chimoutite.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by indre on 4/29/18. Custom loader.
 */

public class FilmLoader extends AsyncTaskLoader<List<Film>> {

    /** Tag for log messages */
    private static final String LOG_TAG = FilmLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link FilmLoader}.
     * @param context of the activity
     * @param url to load data from
     */
    public FilmLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        //Log.e(LOG_TAG, "onStartLoading started");
        forceLoad();
    }

    /**
     * Load film data from the internet on a background thread
     */
    @Override
    public List<Film> loadInBackground() {
        //Log.e(LOG_TAG, "load in background");
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of films
        return QueryUtils.fetchFilmData(mUrl);
    }
}
