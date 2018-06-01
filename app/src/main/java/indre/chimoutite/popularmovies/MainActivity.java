package indre.chimoutite.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilmAdapter.ItemClickListener,LoaderManager.LoaderCallbacks<List<Film>> {

    // Define the two search urls based on either popularity or user rating
    private static final String FILM_REQUEST_URL_POPULARITY =
            QueryUtils.universalVariables.URLMain + "popular" + QueryUtils.universalVariables.APIKey;

    private static final String FILM_REQUEST_URL_RANK =
            QueryUtils.universalVariables.URLMain +  "top_rated" + QueryUtils.universalVariables.APIKey;;

    // Inititalise the url to do the initial load to be based on popularity
    private String url = FILM_REQUEST_URL_POPULARITY;

    // Get a reference to the LoaderManager to interact with loaders
    private LoaderManager loaderManager = getLoaderManager();

    // Set a constant value for the film loader ID
    private static final int FILM_LOADER_ID = 1;

    // Initialize a new instance of the film adapter
    private FilmAdapter mAdapter;

    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    // ProgressBar that is displayed when initially loading
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        progressBar = findViewById(R.id.progressBar);

        // Get the main recycler view
        RecyclerView recyclerView = findViewById(R.id.card_recycler_view);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        mEmptyStateTextView.setVisibility(View.GONE);

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Create a new adapter that takes an empty list of films as input
            mAdapter = new FilmAdapter(this, new ArrayList<Film>());

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter.
            loaderManager.initLoader(FILM_LOADER_ID, null, this);

            // Implement recycler view
            recyclerView.setHasFixedSize(true);

            // Initialize columns in recycler view
            int columns;

            // Change the grid layout if the phone changes orientation
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                columns = 2;
            } else {
                columns = 4;
            }

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),
                    columns);

            recyclerView.setLayoutManager(layoutManager);

            // Create a new adapter that takes an empty list of films as input
            recyclerView.setAdapter(mAdapter);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            progressBar.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public Loader<List<Film>> onCreateLoader (int i, Bundle args) {
        return new FilmLoader(this, url);
    }

    @Override
    public void onLoadFinished (Loader<List<Film>> loader, List<Film> films){
        // Hide loading indicator
        progressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setVisibility(View.GONE);

        // Clear the adapter of previous film data
        mAdapter.swap(films);
    }

    @Override
    public void onLoaderReset(Loader<List<Film>> loader) {
    }

    // Inflate the menu
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    // Respond to user selection
    public boolean onOptionsItemSelected(MenuItem item) {
        url = item.getItemId() == R.id.sortRating ?
                FILM_REQUEST_URL_RANK : FILM_REQUEST_URL_POPULARITY;

        loaderManager.restartLoader(FILM_LOADER_ID, null, this);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("URL", url);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        url = savedInstanceState.getString("URL");
        loaderManager.restartLoader(FILM_LOADER_ID, null, this);

    }
}
