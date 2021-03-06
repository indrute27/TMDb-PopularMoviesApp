package indre.chimoutite.popularmovies.UI;

import android.app.LoaderManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import indre.chimoutite.popularmovies.FilmDataViewModel;
import indre.chimoutite.popularmovies.R;
import indre.chimoutite.popularmovies.database.FilmDataModel;
import indre.chimoutite.popularmovies.loadersAndAdapters.ReviewAdapter;
import indre.chimoutite.popularmovies.loadersAndAdapters.ReviewLoader;
import indre.chimoutite.popularmovies.loadersAndAdapters.TrailerAdapter;
import indre.chimoutite.popularmovies.loadersAndAdapters.TrailerLoader;
import indre.chimoutite.popularmovies.network.QueryUtils;
import indre.chimoutite.popularmovies.objects.Review;
import indre.chimoutite.popularmovies.objects.Trailer;

/**
 * Created by indre on 4/28/18. Activity shows the detail moview view.
 */

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ItemClickListener {

    private URL trailerURL;
    private URL reviewURL;
    private boolean favoriteSelected;
    private RecyclerView recyclerViewTrailer;
    private RecyclerView recyclerViewReview;
    RecyclerView.LayoutManager layoutManagerTrailer;
    RecyclerView.LayoutManager layoutManagerReview;

    private static final String TAG = "Detail Activity";

    // Get a reference to the LoaderManager to interact with loaders
    private LoaderManager loaderManager = getLoaderManager();

    // Initialize a new instance of the trailer and review adapters
    private TrailerAdapter mAdapterTrailer;
    private ReviewAdapter mAdapterReview;

    // Set a constant value for the trailer and review loader IDs
    private static final int TRAILER_LOADER_ID = 2;
    private static final int REVIEW_LOADER_ID = 3;

    // Database variables
    private FilmDataViewModel viewModel;
    private LiveData<FilmDataModel> film;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        Log.d(TAG, "OnCreate started.");

        // Add a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String posterURL = intent.getExtras().getString("PosterURL");
        final String releaseDate = intent.getExtras().getString("ReleaseDate");
        final String voterAvg = intent.getExtras().getString("VoterAvg");
        final String overview = intent.getExtras().getString("Overview");
        final String title = intent.getExtras().getString("Title");
        final int filmId = intent.getExtras().getInt("filmId");

        // Update UI with selected film data
        final ImageView posterImage = findViewById(R.id.detailPosterimageView);
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

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            Log.d(TAG, "Verified that there is a network connection.");

            // Create the new URLs based on selected film ID
            trailerURL = QueryUtils.createUrl(QueryUtils.universalVariables.URLMain + filmId
                    + "/videos" + QueryUtils.universalVariables.APIKey);

            reviewURL = QueryUtils.createUrl(QueryUtils.universalVariables.URLMain + filmId
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

            layoutManagerTrailer = new LinearLayoutManager(recyclerViewTrailer.getContext());
            layoutManagerReview = new LinearLayoutManager(recyclerViewReview.getContext());

            recyclerViewTrailer.setLayoutManager(layoutManagerTrailer);
            recyclerViewReview.setLayoutManager(layoutManagerReview);

            // Create a new adapter that takes an empty list of trailers as input
            recyclerViewTrailer.setAdapter(mAdapterTrailer);
            recyclerViewReview.setAdapter(mAdapterReview);
        }

        //Setup the FilmViewModel
        viewModel = ViewModelProviders.of(this).get(FilmDataViewModel.class);
        viewModel.loadAllFilms().observe(DetailActivity.this, new Observer<List<FilmDataModel>>() {
            @Override
            public void onChanged(@Nullable List<FilmDataModel> filmDataModelList) {
                if (viewModel.loadFilmById(filmId).getValue() != null){
                    film = viewModel.loadFilmById(filmId);
                    Log.d(TAG, "onChanged: " + film.getValue().getFilmId());
                } else {
                    Log.d(TAG, "onChanged: Null");
                }
            }
        });

        final ImageButton favoriteButton = findViewById(R.id.favorite_icon);

        if (film != null) {
            favoriteSelected = true;
            favoriteButton.setImageResource(R.drawable.heart_icon_on);
            //mDb.filmDao().updateFilm(film);
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Favorite button pressed.");
                FilmDataModel filmDataModel = new FilmDataModel(posterURL, releaseDate, voterAvg,
                        overview, title, filmId);
                if (favoriteSelected) {
                    Log.d(TAG, "Delete Item.");
                    favoriteSelected = false;
                    favoriteButton.setImageResource(R.drawable.heart_icon_off);
                    viewModel.deleteFilm(filmDataModel);
                } else {
                    Log.d(TAG, "Insert Item.");
                    favoriteSelected = true;
                    favoriteButton.setImageResource(R.drawable.heart_icon_on);
                    viewModel.insertFilm(filmDataModel);
                }
            }
        });
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
            Log.d(TAG, "Trailer onCreateLoader called.");
            return new TrailerLoader(getApplicationContext(), trailerURL.toString());
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> newTrailers) {
            Log.d(TAG, "Trailer onLoadFinished called.");
            // Clear the adapter of previous trailer data
            mAdapterTrailer.swap(newTrailers);
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {
            Log.d(TAG, "Trailer onLoaderReset called.");
        }
    };

    private LoaderManager.LoaderCallbacks<List<Review>> reviewLoaderListener = new LoaderManager
            .LoaderCallbacks<List<Review>>() {
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
}
