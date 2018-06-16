package indre.chimoutite.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface FilmDataModelDao {
    @Query("select * from film ORDER BY title")
    LiveData<List<FilmDataModel>> loadAllFilms();

    @Insert(onConflict = REPLACE)
    void addFilm(FilmDataModel filmDataModel);

    @Delete
    void deleteFilm(FilmDataModel filmDataModel);

//    @Update(onConflict = onConflictStrategy.REPLACE)
//    void updateFilm(FilmDataModel filmDataModel);

    @Query("SELECT * FROM film WHERE filmId = :filmId")
    LiveData<FilmDataModel> loadFilmById(int filmId);
}
