package indre.chimoutite.popularmovies.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class FilmDataModel {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    private String posterURL;

    public FilmDataModel(String posterURL) {
        this.posterURL = posterURL;
    }

    public String getPosterURL() {
        return this.posterURL;
    }
}
