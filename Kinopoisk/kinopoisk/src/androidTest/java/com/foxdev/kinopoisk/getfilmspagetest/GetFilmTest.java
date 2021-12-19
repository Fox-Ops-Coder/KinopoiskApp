package com.foxdev.kinopoisk.getfilmspagetest;

import android.app.Application;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.foxdev.kinopoisk.data.net.KinopoiskServer;
import com.foxdev.kinopoisk.data.net.ServerInterface;
import com.foxdev.kinopoisk.data.objects.FilmPage;
import com.foxdev.kinopoisk.data.objects.FilmShortInfo;
import com.foxdev.kinopoisk.data.objects.FilmWatchData;
import com.foxdev.kinopoisk.data.objects.Watch;
import com.foxdev.kinopoisk.data.sql.KinopoiskDao;
import com.foxdev.kinopoisk.data.sql.KinopoiskDatabase;
import com.foxdev.kinopoisk.viewmodel.FilmViewModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Future;

@RunWith(AndroidJUnit4.class)
public class GetFilmTest
{
    private final KinopoiskDao kinopoiskDao;

    public GetFilmTest()
    {
        Application application = ApplicationProvider.getApplicationContext();

        kinopoiskDao = Room
                .inMemoryDatabaseBuilder(application, KinopoiskDatabase.class)
                .allowMainThreadQueries()
                .build().GetKinopoiskDao();

        for (int index = 1; index <= 50; ++index)
        {
            Watch watch = new Watch();
            watch.FilmId = index;

            long watchId = kinopoiskDao.addToWatchList(watch);

            FilmWatchData filmWatchData = new FilmWatchData();
            filmWatchData.filmId = index;
            filmWatchData.watchId = (int) watchId;

            kinopoiskDao.addFilmToWatchList(filmWatchData);
        }
    }

    @Test
    public void pageIsZero()
    {
        FilmPage filmPage = kinopoiskDao.getFilms(0);

        Assert.assertEquals(0, filmPage.films.size());
    }

    @Test
    public void pageIsMore()
    {
        FilmPage filmPage = kinopoiskDao.getFilms(50);

        Assert.assertEquals(0, filmPage.films.size());
    }

    @Test
    public void normalPage()
    {
        FilmPage filmPage = kinopoiskDao.getFilms(1);

        Assert.assertEquals(20, filmPage.films.size());
    }

    @Test
    public void countryNull()
    {
        FilmPage filmPage = kinopoiskDao.getFilms(1);

        Assert.assertEquals(0, filmPage.films.get(0).countries.size());
    }

    @Test
    public void genreNull()
    {
        FilmPage filmPage = kinopoiskDao.getFilms(1);

        Assert.assertEquals(0, filmPage.films.get(0).genres.size());
    }

    @Test
    public void databaseEmpty()
    {
        Application application = ApplicationProvider.getApplicationContext();

        KinopoiskDao dao = Room
                .inMemoryDatabaseBuilder(application, KinopoiskDatabase.class)
                .allowMainThreadQueries()
                .build().GetKinopoiskDao();

        FilmPage filmPage = dao.getFilms(1);

        Assert.assertEquals(0, filmPage.films.size());
    }

    @Test
    public void countyNotNull()
    {
        Watch watch = new Watch();
        watch.FilmId = 1;

        FilmWatchData filmWatchData = new FilmWatchData();
        filmWatchData.country = "Russia";

        Application application = ApplicationProvider.getApplicationContext();

        KinopoiskDao dao = Room
                .inMemoryDatabaseBuilder(application, KinopoiskDatabase.class)
                .allowMainThreadQueries()
                .build().GetKinopoiskDao();

        long watchId = dao.addToWatchList(watch);
        filmWatchData.watchId = (int) watchId;

        dao.addFilmToWatchList(filmWatchData);

        FilmPage filmPage = dao.getFilms(1);

        Assert.assertEquals("Russia", filmPage.films.get(0).countries.get(0).FilmCountry);
    }

    @Test
    public void genreNotNull()
    {
        Watch watch = new Watch();
        watch.FilmId = 1;

        FilmWatchData filmWatchData = new FilmWatchData();
        filmWatchData.genre = "Боевик";

        Application application = ApplicationProvider.getApplicationContext();

        KinopoiskDao dao = Room
                .inMemoryDatabaseBuilder(application, KinopoiskDatabase.class)
                .allowMainThreadQueries()
                .build().GetKinopoiskDao();

        long watchId = dao.addToWatchList(watch);
        filmWatchData.watchId = (int) watchId;

        dao.addFilmToWatchList(filmWatchData);

        FilmPage filmPage = dao.getFilms(1);

        Assert.assertEquals("Боевик", filmPage.films.get(0).genres.get(0).FilmGenre);
    }
}
