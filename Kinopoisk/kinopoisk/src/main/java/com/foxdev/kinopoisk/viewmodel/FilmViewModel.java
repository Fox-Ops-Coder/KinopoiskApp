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

    public static void inWatchListFilms(@NonNull List<FilmShortInfo> films,
                                         @NonNull List<Watch> watchList)
    {
        for (int index = 0; index < films.size(); ++index)  //1
        {
            boolean found = false;  //2
            int jndex = 0;  //3

            while (!found && jndex < watchList.size())  //4, 5
            {
                if (films.get(index).filmId == watchList.get(jndex).FilmId) //6
                {
                    found = true;   //8
                    watchList.remove(jndex);    //9
                    films.get(index).inWatchList = true;    //10
                }
                else
                {
                    ++jndex;    //7
                }
            }
        }
    }   //11

    public void handleResult(@NonNull FilmPage filmPage)
    {
        List<FilmShortInfo> films = filmPage.films; //1

        executor.submit(() ->
        {
            List<Watch> watchList = kinopoiskDao.getWatchList();    //2

            inWatchListFilms(films, watchList); //3

            filmPageMutableLiveData.postValue(filmPage); //4
        });
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
                if (response.isSuccessful() && response.body() != null) //1, 2
                {
                    handleResult(response.body()); //4
                }
                else
                {
                    filmPageMutableLiveData.postValue(null);    //3
                }
            }   //5

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
                    handleResult(response.body());
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
