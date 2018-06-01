package indre.chimoutite.popularmovies;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by indre on 4/28/18. Activity shows the detail moview view.
 */

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ItemClickListener, LoaderManager.LoaderCallbacks<ArrayList<String>> {

    private URL trailerURL;

    // Get a reference to the LoaderManager to interact with loaders
    private LoaderManager loaderManager = getLoaderManager();

    // Initialize a new instance of the film adapter
    private TrailerAdapter mAdapter;

    // Set a constant value for the film loader ID
    private static final int TRAILER_LOADER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        // Add a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String posterURL = intent.getExtras().getString("PosterURL");
        String releaseDate = intent.getExtras().getString("ReleaseDate");
        String voterAvg = intent.getExtras().getString("VoterAvg");
        String overview = intent.getExtras().getString("Overview");
        String title = intent.getExtras().getString("Title");
        String id = intent.getExtras().getString("id");

        // Update UI with selected film data
        ImageView posterImage = findViewById(R.id.detailPosterimageView);
        TextView releaseDateText = findViewById(R.id.detailReleaseDate);
        TextView voterAvgText = findViewById(R.id.detailVoteAverage);
        TextView overviewText = findViewById(R.id.detailDescription);
        TextView titleText = findViewById(R.id.detailMovieTitle);

        Picasso.with(this)
                .load(posterURL)
                .into(posterImage);
        releaseDateText.setText(releaseDate);
        voterAvgText.setText(voterAvg);
        overviewText.setText(overview);
        titleText.setText(title);

        // Load in trailers
        trailerURL = QueryUtils.createUrl(QueryUtils.universalVariables.URLMain + id
                + "/videos" + QueryUtils.universalVariables.APIKey);
        Log.v("DetailActivity", trailerURL.toString());


        // Create a new adapter that takes an empty list of trailers as input
        mAdapter = new TrailerAdapter(this, new ArrayList<String>());

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter.
        loaderManager.initLoader(TRAILER_LOADER_ID, null, this);

        // Get the trailer recycler view
        RecyclerView recyclerView = findViewById(R.id.trailer_recycler_view);

        // Implement recycler view
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        // Create a new adapter that takes an empty list of trailers as input
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int i, Bundle bundle) {
        Log.v("DetailActivity", "Loader Started!");
        return new TrailerLoader(this, trailerURL.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> newTrailers) {
        // Clear the adapter of previous film data
        mAdapter.swap(newTrailers);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader) {
    }
}
