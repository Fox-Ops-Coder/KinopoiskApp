package com.foxdev.kinopoisk.inwatchtest;

import androidx.annotation.NonNull;

import com.foxdev.kinopoisk.data.objects.FilmShortInfo;
import com.foxdev.kinopoisk.data.objects.Watch;
import com.foxdev.kinopoisk.viewmodel.FilmViewModel;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class InWatchTest
{
    @Test
    public void noDataInServerResponse()
    {
        ArrayList<FilmShortInfo> filmShortInfos = new ArrayList<>();
        ArrayList<Watch> watches = new ArrayList<>();

        FilmViewModel.inWatchListFilms(filmShortInfos, watches);
    }

    @Test
    public void hasDataInServerResponseButNotInDatabase()
    {
        ArrayList<FilmShortInfo> filmShortInfos = new ArrayList<>();
        for (int index = 1; index <= 5; ++index)
        {
            FilmShortInfo filmShortInfo = new FilmShortInfo();
            filmShortInfo.filmId = index;

            filmShortInfos.add(filmShortInfo);
        }

        ArrayList<Watch> watches = new ArrayList<>();

        FilmViewModel.inWatchListFilms(filmShortInfos, watches);
    }

    private static boolean AreEqual(@NonNull List<FilmShortInfo> expected,
                                    @NonNull List<FilmShortInfo> actual)
    {
        for (int index = 0; index < expected.size(); ++index)
        {
            if (expected.get(index).inWatchList != actual.get(index).inWatchList)
                return false;
        }

        return true;
    }

    @Test
    public void hasDataInServerResponseAndInDatabase()
    {
        ArrayList<FilmShortInfo> expectedArray = new ArrayList<>();
        ArrayList<FilmShortInfo> filmShortInfos = new ArrayList<>();
        ArrayList<Watch> watches = new ArrayList<>();

        for (int index = 1; index <= 5; ++index)
        {
            FilmShortInfo filmShortInfo = new FilmShortInfo();
            filmShortInfo.filmId = index;

            FilmShortInfo copy = new FilmShortInfo();
            copy.filmId = index;
            copy.inWatchList = true;

            filmShortInfos.add(filmShortInfo);
            expectedArray.add(filmShortInfo);

            Watch watch = new Watch();
            watch.FilmId = index;

            watches.add(watch);
        }

        FilmViewModel.inWatchListFilms(filmShortInfos, watches);

        Assert.assertTrue(AreEqual(expectedArray, filmShortInfos));
    }

    @Test
    public void databaseHasLessObjects()
    {
        ArrayList<FilmShortInfo> expectedArray = new ArrayList<>();
        ArrayList<FilmShortInfo> filmShortInfos = new ArrayList<>();
        ArrayList<Watch> watches = new ArrayList<>();

        for (int index = 1; index <= 5; ++index)
        {
            FilmShortInfo filmShortInfo = new FilmShortInfo();
            filmShortInfo.filmId = index;

            FilmShortInfo copy = new FilmShortInfo();
            copy.filmId = index;

            filmShortInfos.add(filmShortInfo);
            expectedArray.add(filmShortInfo);
        }

        for (int index = 1; index <= 5; index += 2)
        {
            Watch watch = new Watch();
            watch.FilmId = index;

            watches.add(watch);

            expectedArray.get(index - 1).inWatchList = true;
        }

        FilmViewModel.inWatchListFilms(filmShortInfos, watches);

        Assert.assertTrue(AreEqual(expectedArray, filmShortInfos));
    }

    @Test
    public void serverResponseLessObjects()
    {
        ArrayList<FilmShortInfo> expectedArray = new ArrayList<>();
        ArrayList<FilmShortInfo> filmShortInfos = new ArrayList<>();
        ArrayList<Watch> watches = new ArrayList<>();

        for (int index = 1; index <= 5; index += 2)
        {
            FilmShortInfo filmShortInfo = new FilmShortInfo();
            filmShortInfo.filmId = index;

            FilmShortInfo copy = new FilmShortInfo();
            copy.filmId = index;
            copy.inWatchList = true;

            filmShortInfos.add(filmShortInfo);
            expectedArray.add(filmShortInfo);
        }

        for (int index = 1; index <= 5; ++index)
        {
            Watch watch = new Watch();
            watch.FilmId = index;

            watches.add(watch);
        }

        FilmViewModel.inWatchListFilms(filmShortInfos, watches);

        Assert.assertTrue(AreEqual(expectedArray, filmShortInfos));
    }
}
