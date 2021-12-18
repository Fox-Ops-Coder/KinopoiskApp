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
import java.util.function.Function;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public final class FilmViewModel extends ViewModel implements WatchDatabase
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

    public Future<?> loadTopFilms()
    {
        return loadTopFilms(1);
    }

    @NonNull
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

    public Future<?> searchFilms(@NonNull String keyword)
    {
        return searchFilms(keyword, 1);
    }

    @NonNull
    public Future<?> getFavoriteFilms(int page)
    {
        return executor.submit(() ->
                filmPageMutableLiveData.postValue(kinopoiskDao.getFilms(page)));
    }

    @NonNull
    public Future<?> getFavoriteFilms()
    {
        return getFavoriteFilms(1);
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

            if (filmShortInfo.countries.size() != 0)
                filmWatchData.country = filmShortInfo.countries.get(0).FilmCountry;
            else
                filmWatchData.country = null;

            if (filmShortInfo.genres.size() != 0)
                filmWatchData.genre = filmShortInfo.genres.get(0).FilmGenre;
            else
                filmWatchData.genre = null;

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
