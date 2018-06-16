package indre.chimoutite.popularmovies.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "film")
public class FilmDataModel {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "posterURL")
    private String posterURL;
    private String releaseDate;
    @ColumnInfo(name = "voterAvg")
    private String voterAvg;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "filmId")
    private int filmId;

    public FilmDataModel(String posterURL, String releaseDate, String voterAvg, String description,
                         String title, int filmId) {
        this.posterURL = posterURL;
        this.releaseDate = releaseDate;
        this.voterAvg = voterAvg;
        this.description = description;
        this.title = title;
        this.filmId = filmId;
    }

    public String getPosterURL() {
        return this.posterURL;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTitle() {
        return this.title;
    }

    public String getVoterAvg() {
        return this.voterAvg;
    }

    public int getFilmId() {
        return filmId;
    }
}
