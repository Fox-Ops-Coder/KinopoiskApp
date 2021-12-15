package com.foxdev.kinopoisk.data.sql;

import static androidx.room.OnConflictStrategy.ABORT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.foxdev.kinopoisk.data.objects.FilmWatchData;
import com.foxdev.kinopoisk.data.objects.Watch;

import java.util.List;

@Dao
public interface KinopoiskDao
{
    @Insert(onConflict = ABORT)
    long addToWatchList(@NonNull Watch filmWatch);

    @Insert(onConflict = ABORT)
    void addFilmToWatchList(@NonNull FilmWatchData filmWatchData);

    @Delete
    void removeFromWatchList(@NonNull Watch filmWatch);

    @NonNull
    @Query("SELECT * FROM WatchList")
    List<Watch> getWatchList();

    @NonNull
    @Query("SELECT * FROM Films")
    List<FilmWatchData> getFilms();

    @NonNull
    @Query("SELECT * FROM WatchList WHERE FilmId = :filmId")
    Watch getWatch(final int filmId);
}
