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
    @Query("select * from FilmDataModel")
    LiveData<List<FilmDataModel>> getAllDataItems();

    @Query("select * from FilmDataModel where id = :id")
    FilmDataModel getItemById(String id);

    @Insert(onConflict = REPLACE)
    void addData(FilmDataModel filmDataModel);

    @Delete
    void deleteData(FilmDataModel filmDataModel);
}
