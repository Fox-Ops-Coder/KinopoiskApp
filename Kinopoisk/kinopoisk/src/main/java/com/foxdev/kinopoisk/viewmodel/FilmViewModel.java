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
    private final MutableLiveData<FilmSearch> filmSearchMutableLiveData;

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
        filmSearchMutableLiveData = new MutableLiveData<>();

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

    @NonNull
    public LiveData<FilmSearch> getFilmSearchLiveData()
    {
        return filmSearchMutableLiveData;
    }

    private static void inWatchListFilms(@NonNull List<FilmShortInfo> films,
                                         @NonNull List<Watch> watchList)
    {
        for (int index = 0; index < films.size(); ++index)
        {
            boolean found = false;
            int jndex = 0;

            while (!found && jndex < watchList.size())
            {
                if ((int) films.get(index).filmId == watchList.get(jndex).FilmId)
                {
                    found = true;
                    watchList.remove(jndex);
                    films.get(index).inWatchList = true;
                }
                else
                {
                    ++jndex;
                }
            }
        }
    }

    @Override
    public void loadTopFilms(int pageNumber)
    {
        serverInterface.getTopFilm(pageNumber).enqueue(new Callback<FilmPage>()
        {
            @Override
            public void onResponse(@NonNull Call<FilmPage> call,
                                   @NonNull Response<FilmPage> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    List<FilmShortInfo> films = response.body().Films;

                    executor.submit(() ->
                    {
                        List<Watch> watchList = kinopoiskDao.getWatchList();

                        inWatchListFilms(films, watchList);

                        filmPageMutableLiveData.postValue(response.body());
                    });
                }
                else
                {
                    filmPageMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<FilmPage> call, @NonNull Throwable t)
            {
                filmPageMutableLiveData.postValue(null);
            }
        });
    }

    @Override
    public void loadTopFilms()
    {
        loadTopFilms(1);
    }

    @Override
    public void searchFilms(@NonNull String keyword, int pageNumber)
    {
        serverInterface.searchFilm(keyword, pageNumber).enqueue(new Callback<FilmSearch>()
        {
            @Override
            public void onResponse(@NonNull Call<FilmSearch> call,
                                   @NonNull Response<FilmSearch> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    List<FilmShortInfo> films = response.body().films;

                    executor.submit(() ->
                    {
                        List<Watch> watchList = kinopoiskDao.getWatchList();

                        inWatchListFilms(films, watchList);

                        filmSearchMutableLiveData.postValue(response.body());
                    });
                }
                else
                {
                    filmSearchMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<FilmSearch> call, @NonNull Throwable t)
            {
                filmSearchMutableLiveData.postValue(null);
            }
        });
    }

    @Override
    public void searchFilms(@NonNull String keyword)
    {
        searchFilms(keyword, 1);
    }

    @Override
    public void getFavoriteFilms()
    {
        executor.submit(() ->
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
            filmPage.Pages = 1;
            filmPage.Favorite = true;
            filmPage.Films = filmShortInfos;

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

            if (filmPage.Favorite)
            {
                filmPage.Films.remove(filmShortInfo);
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
