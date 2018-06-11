package indre.chimoutite.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FilmDataModel.class}, version = 1)
public abstract class FilmDatabase extends RoomDatabase{

    public abstract FilmDataModelDao filmDao();
    private static FilmDatabase INSTANCE;

    public static FilmDatabase getDatabase(final Context context) {
        synchronized (FilmDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FilmDatabase.class,
                        "film_db").build();
            }
        }
        return INSTANCE;
    }


}
