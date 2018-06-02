package indre.chimoutite.popularmovies;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by indre on 6/2/18.
 */

public class ReviewUtils {
    private ReviewUtils() {
    }

    /** Tag for log messages */
    private static final String LOG_TAG = ReviewUtils.class.getSimpleName();

    /**
     * Query TMDb and return a list of {@link Review} objects
     */
    public static List<Review> fetchReviews(String requestUrl) {
        // Create a URL object
        URL url = QueryUtils.createUrl(requestUrl);

        //Call HTTP request to URL and get JSON response
        String jsonResponse = null;
        try {
            jsonResponse = QueryUtils.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request ", e);
        }

        // Extract relevant fields from the JSON response and return the list of films
        return extractFeatureFromJsonReview(jsonResponse);
    }

    /**
     * Return a list of {@link Review} objects that has been built up from
     * parsing the given JSON response
     */
    private static List<Review> extractFeatureFromJsonReview(String filmJSON) {

        // If the JSON string is empty or null, then return
        if (TextUtils.isEmpty(filmJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding reviews to
        List<Review> reviews = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(filmJSON);

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of reviews
            JSONArray reviewArray = baseJsonResponse.getJSONArray("results");

            // For each movie in the reviewArray, create an {@link Review} object
            for (int i = 0; i < reviewArray.length(); i++) {

                // Get a single movie at position i within the list of movies
                JSONObject currentReview = reviewArray.getJSONObject(i);

                // Extract the value for individual keys from JSONObject results
                String author = currentReview.getString("author");
                String content = currentReview.getString("content");

                // Create a new {@link Review} object with the author and content from the JSON response
                Review review = new Review(author, content);

                // Add the new {@link Film} to the list of movies
                reviews.add(review);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception
            Log.e("ReviewUtils", "Problem parsing the TMDb JSON results", e);
        }

        // Return the list of reviews
        return reviews;
    }
}
