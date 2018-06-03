package indre.chimoutite.popularmovies;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by indre on 4/29/18. Based on code for "Did you feel it app" Udacity Basics
 * completed course.
 */

public class QueryUtils {
    private QueryUtils() {
    }

    /** Tag for log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // Set global variables for URL and API key
    public interface universalVariables {
        public static final String URLMain = "http://api.themoviedb.org/3/movie/";
        public static final String APIKey = "[private_key]";
    }

    /**
     * Query TMDb and return a list of {@link Film} objects
     */
    public static List<Film> fetchFilmData(String requestUrl) {
        // Create a URL object
        URL url = createUrl(requestUrl);

        //Call HTTP request to URL and get JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request ", e);
        }

        // Extract relevant fields from the JSON response and return the list of films
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make HTTP request to the URL and return a String as the response
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        int readTimeout = 10000;
        int connectTimeout = 15000;
        int responseCode = 200;

        // If the URL is null, then return
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(readTimeout /* milliseconds */);
            urlConnection.setConnectTimeout(connectTimeout /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == responseCode) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movie JSON results ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String that contains JSON
     * response from the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Film} objects that has been built up from
     * parsing the given JSON response
     */
    private static List<Film> extractFeatureFromJson(String filmJSON) {

        // If the JSON string is empty or null, then return
        if (TextUtils.isEmpty(filmJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding films to
        List<Film> films = new ArrayList<>();

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
                int voteCount = currentFilm.getInt("vote_count");
                long voteAverage = currentFilm.getLong("vote_average");
                String title = currentFilm.getString("title");
                long popularity = currentFilm.getLong("popularity");
                String posterUrl = "http://image.tmdb.org/t/p/w185" + currentFilm.getString("poster_path");
                String overview = currentFilm.getString("overview");
                String releaseDate = currentFilm.getString("release_date");
                String id = currentFilm.getString("id");

                // Create a new {@link Film} object with the vote count, vote average, title,
                // popularity, poster path, overview, and release date from the JSON response
                Film film = new Film(voteCount, voteAverage, title, popularity, posterUrl,
                        overview, releaseDate, id);

                // Add the new {@link Film} to the list of movies
                films.add(film);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception
            Log.e("QueryUtils", "Problem parsing the TMDb JSON results", e);
        }

        // Return the list of films
        return films;
    }
}
