package com.foxdev.kinopoisk.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foxdev.kinopoisk.data.net.ServerInterface;
import com.foxdev.kinopoisk.data.objects.Country;
import com.foxdev.kinopoisk.data.objects.Film;
import com.foxdev.kinopoisk.data.objects.FilmPage;
import com.foxdev.kinopoisk.data.objects.FilmSearch;
import com.foxdev.kinopoisk.data.objects.FilmShortInfo;
import com.foxdev.kinopoisk.data.objects.FilmWatchData;
import com.foxdev.kinopoisk.data.objects.Genre;
import com.foxdev.kinopoisk.data.objects.Watch;
import com.foxdev.kinopoisk.data.sql.KinopoiskDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public final class FilmViewModel extends ViewModel implements FilmModel, WatchDatabase
{
    @NonNull
    private final KinopoiskDao kinopoiskDao;

    @NonNull
    private final ServerInterface serverInterface;

    @NonNull
    private final MutableLiveData<Film> filmMutableLiveData;

    @NonNull
    private final MutableLiveData<FilmPage> filmPageMutableLiveData;

    @NonNull
    private final ExecutorService executor;

    @Inject
    public FilmViewModel(@NonNull final KinopoiskDao kinopoiskDao,
                         @NonNull final ServerInterface serverInterface)
    {
        this.kinopoiskDao = kinopoiskDao;
        this.serverInterface = serverInterface;

        filmMutableLiveData = new MutableLiveData<>();
        filmPageMutableLiveData = new MutableLiveData<>();

        executor = Executors.newSingleThreadExecutor();
    }

    @NonNull
    public LiveData<Film> getFilmLiveData()
    {
        return filmMutableLiveData;
    }

    @NonNull
    public LiveData<FilmPage> getFilmPageLiveData()
    {
        return filmPageMutableLiveData;
    }

    public static void inWatchListFilms(@NonNull List<FilmShortInfo> films,
                                         @NonNull List<Watch> watchList)
    {
        for (int index = 0; index < films.size(); ++index)  //1
        {
            boolean found = false;
            int jndex = 0;

            while (!found && jndex < watchList.size())  //2, 3
            {
                if (films.get(index).filmId == watchList.get(jndex).FilmId) //4
                {
                    found = true;
                    watchList.remove(jndex);    //6
                    films.get(index).inWatchList = true;
                }
                else
                {
                    ++jndex;    //5
                }
            }
        }
    }   //7

    public void handleResult(@NonNull FilmPage filmPage)
    {
        List<FilmShortInfo> films = filmPage.films; //1

        List<Watch> watchList = kinopoiskDao.getWatchList();    //2

        inWatchListFilms(films, watchList); //3

        filmPageMutableLiveData.postValue(filmPage); //4
    }

    @NonNull
    @Override
    public Future<?> loadTopFilms(int pageNumber)
    {
        return executor.submit(() ->
        {
            try
            {
                Response<FilmPage> filmPageResponse = serverInterface
                        .getTopFilm(pageNumber).execute();

                if (filmPageResponse.isSuccessful() && filmPageResponse.body() != null) //1, 2
                {
                    FilmPage filmPage = filmPageResponse.body();
                    filmPage.currentPage = pageNumber;

                    handleResult(filmPage);  //4
                }
                else
                {
                    filmPageMutableLiveData.postValue(null);
                }

            } catch (IOException e)
            {
                filmPageMutableLiveData.postValue(null);    //3
            }
        }); //5
    }

    @Override
    public Future<?> loadTopFilms()
    {
        return loadTopFilms(1);
    }

    @NonNull
    @Override
    public Future<?> searchFilms(@NonNull String keyword, int pageNumber)
    {
        return executor.submit(() ->
        {
            try
            {
                Response<FilmSearch> filmSearchResponse = serverInterface
                        .searchFilm(keyword, pageNumber).execute();

                if (filmSearchResponse.isSuccessful() && filmSearchResponse.body() != null)
                {
                    FilmSearch filmSearch = filmSearchResponse.body();
                    filmSearch.currentPage = pageNumber;

                    handleResult(filmSearch);
                }
                else
                {

                    filmPageMutableLiveData.postValue(null);
                }


            } catch (IOException e)
            {
                filmPageMutableLiveData.postValue(null);
            }
        });
    }

    @Override
    public Future<?> searchFilms(@NonNull String keyword)
    {
        return searchFilms(keyword, 1);
    }

    @Override
    @NonNull
    public Future<?> getFavoriteFilms()
    {
        return executor.submit(() ->
        {
            List<FilmWatchData> watchDataList = kinopoiskDao.getFilms();
            ArrayList<FilmShortInfo> filmShortInfos = new ArrayList<>(watchDataList.size());

            for (int index = 0; index < watchDataList.size(); ++index)
            {
                FilmShortInfo filmShortInfo = new FilmShortInfo();
                FilmWatchData filmWatchData = watchDataList.get(index);

                filmShortInfo.filmId = filmWatchData.filmId;
                filmShortInfo.nameRu = filmWatchData.nameRu;
                filmShortInfo.nameEn = filmWatchData.nameEn;
                filmShortInfo.year = filmWatchData.year;
                filmShortInfo.rating = filmWatchData.rating;
                filmShortInfo.posterUrlPreview = filmWatchData.poster;

                filmShortInfo.inWatchList = true;

                Genre genre = new Genre();
                genre.FilmGenre = filmWatchData.genre;

                Country country = new Country();
                country.FilmCountry = filmWatchData.country;

                filmShortInfo.genres.add(genre);
                filmShortInfo.countries.add(country);

                filmShortInfos.add(filmShortInfo);
            }

            FilmPage filmPage = new FilmPage();
            filmPage.pagesCount = 1;
            filmPage.favorite = true;
            filmPage.films = filmShortInfos;

            filmPageMutableLiveData.postValue(filmPage);
        });
    }

    @Override
    @NonNull
    public Future<?> addToWatchList(@NonNull FilmShortInfo filmShortInfo)
    {
        return executor.submit(() ->
        {
            Watch watch = new Watch();
            watch.FilmId = filmShortInfo.filmId;

            long watchId = kinopoiskDao.addToWatchList(watch);
            watch.WatchId = (int) watchId;

            FilmWatchData filmWatchData = new FilmWatchData();
            filmWatchData.filmId = filmShortInfo.filmId;
            filmWatchData.watchId = watch.WatchId;
            filmWatchData.country = filmShortInfo.countries.get(0).FilmCountry;
            filmWatchData.genre = filmShortInfo.genres.get(0).FilmGenre;
            filmWatchData.nameRu = filmShortInfo.nameRu;
            filmWatchData.nameEn = filmShortInfo.nameEn;
            filmWatchData.year = filmShortInfo.year;
            filmWatchData.rating = filmShortInfo.rating;
            filmWatchData.poster = filmShortInfo.posterUrlPreview;

            kinopoiskDao.addFilmToWatchList(filmWatchData);
        });
    }

    @Override
    @NonNull
    public Future<?> removeFromWatchList(@NonNull FilmShortInfo filmShortInfo)
    {
        return executor.submit(() ->
        {
            Watch watch = kinopoiskDao.getWatch(filmShortInfo.filmId);
            kinopoiskDao.removeFromWatchList(watch);

            FilmPage filmPage = filmPageMutableLiveData.getValue();
            assert filmPage != null;

            if (filmPage.favorite)
            {
                filmPage.films.remove(filmShortInfo);
                filmPageMutableLiveData.postValue(filmPage);
            }
        });
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        executor.shutdown();
    }
}
