package indre.chimoutite.popularmovies;

/**
 * Created by indre on 4/28/18. Creates the film object.
 */

public class Film {

    private int mVoteCount;
    private long mVoteAverage;
    private String mTitle;
    private long mPopularity;
    private String mPosterUrl;
    private String mOverview;
    private String mReleaseDate;
    private String mId;

    /** Create a new Film object
     * @param voteCount is the total number of votes from the public
     * @param voteAverage is the average film rating on a scale of 1 to 10
     * @param title is the film title
     * @param popularity is the current popularity of the film
     * @param posterUrl is the url for the poster image
     * @param overview is the film description
     * @param releaseDate is the film release date in theaters
     * @param id is the film id to be used to identify movie for trailer and reviews
     */

    public Film(int voteCount, long voteAverage, String title, long popularity, String posterUrl,
                String overview, String releaseDate, String id){

        mVoteCount = voteCount;
        mVoteAverage = voteAverage;
        mTitle = title;
        mPopularity = popularity;
        mPosterUrl = posterUrl;
        mOverview = overview;
        mReleaseDate = releaseDate.substring(0, 4);
        mId = id;
    }

    public String getmVoteCount() { return Integer.toString(mVoteCount);}
    public String getmVoteAverage() { return (Long.toString(mVoteAverage) + "/10");}
    public String getmTitle() { return mTitle;}
    public String getmPopularity() { return Long.toString(mPopularity);}
    public String getmPosterUrl() { return mPosterUrl;}
    public String getmOverview() { return mOverview;}
    public String getmReleaseDate() {return mReleaseDate;}
    public String getmId() {return mId;}
}

