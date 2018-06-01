package indre.chimoutite.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by indre on 5/29/18. Custom loader.
 */

public class TrailerLoader extends AsyncTaskLoader<ArrayList<String>> {

    /** Tag for log messages */
    private static final String LOG_TAG = TrailerLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link TrailerLoader}.
     * @param context of the activity
     * @param url to load data from
     */
    public TrailerLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        //Log.e(LOG_TAG, "onStartLoading started");
        forceLoad();
    }

    /**
     * Load trailer data from the internet on a background thread
     */
    @Override
    public ArrayList<String> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of trailers
        return Trailer.fetchTrailerID(mUrl);
    }
}

