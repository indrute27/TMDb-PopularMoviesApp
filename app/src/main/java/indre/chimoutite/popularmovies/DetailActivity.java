package indre.chimoutite.popularmovies;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by indre on 4/28/18. Activity shows the detail moview view.
 */

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ItemClickListener {

    private URL trailerURL;
    private URL reviewURL;
    private boolean favoriteSelected = false;
    private static Bundle mBundleRecyclerViewState;
    private RecyclerView recyclerViewTrailer;
    private RecyclerView recyclerViewReview;
    private final String TRAILER_KEY_RECYCLER_STATE = "TRAILER_RECYCLER_STATE";
    private final String REVIEW_KEY_RECYCLER_STATE = "REVIEW_RECYCLER_STATE";

    // Get a reference to the LoaderManager to interact with loaders
    private LoaderManager loaderManager = getLoaderManager();

    // Initialize a new instance of the trailer and review adapters
    private TrailerAdapter mAdapterTrailer;
    private ReviewAdapter mAdapterReview;

    // Set a constant value for the trailer and review loader IDs
    private static final int TRAILER_LOADER_ID = 2;
    private static final int REVIEW_LOADER_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        // Initialize the favorite button
        final ImageButton favoriteButton = (ImageButton) findViewById(R.id.favorite_icon);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favoriteSelected) {
                    favoriteSelected = false;
                    favoriteButton.setImageResource(R.drawable.heart_icon_off);
                } else {
                    favoriteSelected = true;
                    favoriteButton.setImageResource(R.drawable.heart_icon_on);
                }
            }
        });

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

        /**
         * Load in trailers and reviews
         */

        // Create the new URLs based on selected film ID
        trailerURL = QueryUtils.createUrl(QueryUtils.universalVariables.URLMain + id
                + "/videos" + QueryUtils.universalVariables.APIKey);

        reviewURL = QueryUtils.createUrl(QueryUtils.universalVariables.URLMain + id
                + "/reviews" + QueryUtils.universalVariables.APIKey);

        // Create a new adapter that takes an empty list of trailers as input
        mAdapterTrailer = new TrailerAdapter(this, new ArrayList<Trailer>());
        mAdapterReview = new ReviewAdapter(this, new ArrayList<Review>());


        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter.
        getLoaderManager().initLoader(TRAILER_LOADER_ID, null, trailerLoaderListener);
        getLoaderManager().initLoader(REVIEW_LOADER_ID, null, reviewLoaderListener);

        // Get the trailer and review recycler views
        recyclerViewTrailer = findViewById(R.id.trailer_recycler_view);
        recyclerViewReview = findViewById(R.id.review_recycler_view);

        // Implement recycler views
        recyclerViewTrailer.setHasFixedSize(true);
        recyclerViewReview.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManagerTrailer = new LinearLayoutManager(recyclerViewTrailer.getContext());
        RecyclerView.LayoutManager layoutManagerReview = new LinearLayoutManager(recyclerViewReview.getContext());

        recyclerViewTrailer.setLayoutManager(layoutManagerTrailer);
        recyclerViewReview.setLayoutManager(layoutManagerReview);

        // Create a new adapter that takes an empty list of trailers as input
        recyclerViewTrailer.setAdapter(mAdapterTrailer);
        recyclerViewReview.setAdapter(mAdapterReview);
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

    private LoaderManager.LoaderCallbacks<List<Trailer>> trailerLoaderListener =
            new LoaderManager.LoaderCallbacks<List<Trailer>>() {
        @Override
        public Loader<List<Trailer>> onCreateLoader(int i, Bundle bundle) {
            return new TrailerLoader(getApplicationContext(), trailerURL.toString());
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> newTrailers) {
            // Clear the adapter of previous trailer data
            mAdapterTrailer.swap(newTrailers);
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<List<Review>> reviewLoaderListener = new LoaderManager.LoaderCallbacks<List<Review>>() {
        @Override
        public Loader<List<Review>> onCreateLoader(int i, Bundle bundle) {
            return new ReviewLoader(getApplicationContext(), reviewURL.toString());
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> newReviews) {
            // Clear the adapter of previous review data
            mAdapterReview.swap(newReviews);
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {

        }
    };

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }
}
