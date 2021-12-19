package com.foxdev.kinopoisk.data.sql;

import static androidx.room.OnConflictStrategy.ABORT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.foxdev.kinopoisk.data.objects.Country;
import com.foxdev.kinopoisk.data.objects.FilmPage;
import com.foxdev.kinopoisk.data.objects.FilmShortInfo;
import com.foxdev.kinopoisk.data.objects.FilmWatchData;
import com.foxdev.kinopoisk.data.objects.Genre;
import com.foxdev.kinopoisk.data.objects.Watch;

import java.util.List;

@Dao
public abstract class KinopoiskDao
{
    @Insert(onConflict = ABORT)
    public abstract long addToWatchList(@NonNull Watch filmWatch);

    @Insert(onConflict = ABORT)
    public abstract void addFilmToWatchList(@NonNull FilmWatchData filmWatchData);

    @Delete
    public abstract void removeFromWatchList(@NonNull Watch filmWatch);

    @NonNull
    @Query("SELECT * FROM WatchList")
    public abstract List<Watch> getWatchList();

    @NonNull
    @Query("SELECT * FROM WatchList WHERE FilmId = :filmId")
    public abstract Watch getWatch(final int filmId);

    @Query("SELECT COUNT(*) FROM WatchList")
    protected abstract int filmsCount();

    @Query("SELECT * FROM Films LIMIT 20 OFFSET :offset")
    protected abstract List<FilmWatchData> getFilmsPage(int offset);

    @NonNull
    public final FilmPage getFilms(int page)
    {
        FilmPage filmPage = new FilmPage();
        int filmsCount = filmsCount();
        int pagesCount = filmsCount / 20 + 1;

        filmPage.currentPage = page;
        filmPage.pagesCount = pagesCount;

        if (page <= pagesCount && page > 0) //1
        {
            List<FilmWatchData> filmWatchDataList = getFilmsPage((page - 1) * 20);

            for (int index = 0; index < filmWatchDataList.size(); ++index)  //2
            {
                FilmShortInfo filmShortInfo = new FilmShortInfo();
                FilmWatchData filmWatchData = filmWatchDataList.get(index);

                filmShortInfo.filmId = filmWatchData.filmId;
                filmShortInfo.nameRu = filmWatchData.nameRu;
                filmShortInfo.nameEn = filmWatchData.nameEn;
                filmShortInfo.year = filmWatchData.year;
                filmShortInfo.rating = filmWatchData.rating;
                filmShortInfo.posterUrlPreview = filmWatchData.poster;

                filmShortInfo.inWatchList = true;

                if (filmWatchData.genre != null)    //3
                {
                    Genre genre = new Genre();
                    genre.FilmGenre = filmWatchData.genre;

                    filmShortInfo.genres.add(genre);    //4
                }

                if (filmWatchData.country != null)  //5
                {
                    Country country = new Country();
                    country.FilmCountry = filmWatchData.country;

                    filmShortInfo.countries.add(country);   //6
                }

                filmPage.films.add(filmShortInfo);
            }
        }

        return filmPage;    //7
    }
}
