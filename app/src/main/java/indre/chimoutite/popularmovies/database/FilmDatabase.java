package indre.chimoutite.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {FilmDataModel.class}, version = 1)
public abstract class FilmDatabase extends RoomDatabase{

    private static final String LOG_TAG = FilmDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "film_db";
    private static FilmDatabase sInstance;

    public static FilmDatabase getDatabase(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), FilmDatabase.class,
                        FilmDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract FilmDataModelDao filmDao();
}
