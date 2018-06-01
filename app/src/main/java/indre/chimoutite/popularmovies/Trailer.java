package indre.chimoutite.popularmovies;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by indre on 5/11/18.
 */

public class Trailer {

    private Trailer() {
    }

    /** Tag for log messages */
    private static final String LOG_TAG = Trailer.class.getName();

    public static ArrayList<String> fetchTrailerID(String requestUrl) {
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
        return extractFeatureFromJsonTrailer(jsonResponse);
    }


    /**
     * Return a list of {@link String} trailers that has been built up from
     * parsing the given JSON response
     */
    private static ArrayList<String> extractFeatureFromJsonTrailer(String filmJSON) {
        // If the JSON string is empty or null, then return
        if (TextUtils.isEmpty(filmJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding films to
        ArrayList<String> trailerIDs = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(filmJSON);

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of films
            JSONArray filmArray = baseJsonResponse.getJSONArray("results");

            // For each movie in the filmArray, create an {@link Film} object
            for (int i = 0; i < filmArray.length(); i++) {

                // Get a single movie at position i within the list of movies
                JSONObject currentFilm = filmArray.getJSONObject(i);

                // Extract the value for individual keys from JSONObject results
                String trailerID = currentFilm.getString("key");

                // Add the new {@link trailerID} to the list of trailers for that movie
                trailerIDs.add(trailerID);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception
            Log.e("Trailer", "Problem parsing the TMDb JSON results", e);
        }

        // Return the list of trailers
        return trailerIDs;
    }
}
