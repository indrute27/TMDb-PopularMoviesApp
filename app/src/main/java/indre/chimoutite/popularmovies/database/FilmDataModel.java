package indre.chimoutite.popularmovies.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
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
    @ColumnInfo(name = "filmID")
    private String filmId;
    @ColumnInfo(name = "favorite")
    private boolean favourite;

    public FilmDataModel(String posterURL, String releaseDate, String voterAvg, String description,
                         String title, String filmId, boolean favourite) {
        this.posterURL = posterURL;
        this.releaseDate = releaseDate;
        this.voterAvg = voterAvg;
        this.description = description;
        this.title = title;
        this.filmId = filmId;
        this.favourite = favourite;
    }

    public String getPosterURL() {
        return this.posterURL;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public String getFilmId() {
        return filmId;
    }

    public String getTitle() {
        return title;
    }

    public String getVoterAvg() {
        return voterAvg;
    }
}
