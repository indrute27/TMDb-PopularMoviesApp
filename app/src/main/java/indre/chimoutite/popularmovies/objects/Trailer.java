package indre.chimoutite.popularmovies.objects;

/**
 * Created by indre on 6/2/18. Creates the film object.
 */

public class Trailer {

    private String mId;
    private String mName;

    /** Create a new Trailer object
     * @param id is trailer ID
     * @param name is the name of the trailer
     */

    public Trailer(String id, String name){

        mId = id;
        mName = name;
    }

    public String getmId() { return mId;}
    public String getmName() { return mName;}
}

