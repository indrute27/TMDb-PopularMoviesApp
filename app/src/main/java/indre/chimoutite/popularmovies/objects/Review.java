package indre.chimoutite.popularmovies.objects;

/**
 * Created by indre on 6/2/18. Creates the film object.
 */

public class Review {

    private String mAuthor;
    private String mContent;

    /** Create a new Review object
     * @param author is the review author name
     * @param content is the content of the review
     */

    public Review(String author, String content){

        mAuthor = author;
        mContent = content;
    }

    public String getmAuthor() { return mAuthor;}
    public String getmContent() { return mContent;}
}

