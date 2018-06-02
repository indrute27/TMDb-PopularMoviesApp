package indre.chimoutite.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by indre on 6/2/18. Custom loader.
 */

public class ReviewLoader extends AsyncTaskLoader<List<Review>> {

    /** Tag for log messages */
    private static final String LOG_TAG = ReviewLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link ReviewLoader}.
     * @param context of the activity
     * @param url to load data from
     */
    public ReviewLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Load review data from the internet on a background thread
     */
    @Override
    public List<Review> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of trailers
        return ReviewUtils.fetchReviews(mUrl);
    }
}

