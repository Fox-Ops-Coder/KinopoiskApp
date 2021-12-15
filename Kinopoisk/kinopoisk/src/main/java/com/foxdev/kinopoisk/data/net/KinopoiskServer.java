package com.foxdev.kinopoisk.data.net;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.foxdev.kinopoisk.data.objects.Film;
import com.foxdev.kinopoisk.data.objects.FilmPage;
import com.foxdev.kinopoisk.data.objects.FilmSearch;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class KinopoiskServer implements ServerInterface
{
    private static final String BASE_URL = "https://kinopoiskapiunofficial.tech/api/";

    @NonNull
    private final KinopoiskApi kinopoiskApi;

    @Inject
    public KinopoiskServer()
    {
        kinopoiskApi = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient().newBuilder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(KinopoiskApi.class);
    }

    @NonNull
    @Override
    public Call<Film> getFilmById(final int filmId)
    {
        return kinopoiskApi.getFilmById(filmId);
    }

    @NonNull
    @Override
    public Call<FilmPage> getTopFilm(final int pageNumber)
    {
        return kinopoiskApi.getTopFilm(pageNumber);
    }

    @NonNull
    @Override
    public Call<FilmSearch> searchFilm(@NonNull final String keyword, final int pageNumber)
    {
        return kinopoiskApi.searchFilm(keyword, pageNumber);
    }

    private static final class FilmCallback implements Callback<Film>
    {
        @NonNull
        private final MutableLiveData<Film> filmData;

        public FilmCallback(@NonNull final MutableLiveData<Film> filmData)
        {
            this.filmData = filmData;
        }

        @Override
        public void onResponse(@NonNull Call<Film> call, @NonNull Response<Film> response)
        {
            if (response.isSuccessful() && response.body() != null)
                filmData.postValue(response.body());
            else
                filmData.postValue(null);
        }

        @Override
        public void onFailure(@NonNull Call<Film> call, @NonNull Throwable t)
        {
            filmData.postValue(null);
        }
    }

    private static final class FilmPageCallback implements Callback<FilmPage>
    {
        @NonNull
        private final MutableLiveData<FilmPage> filmData;

        public FilmPageCallback(@NonNull final MutableLiveData<FilmPage> filmData)
        {
            this.filmData = filmData;
        }

        @Override
        public void onResponse(@NonNull Call<FilmPage> call,
                               @NonNull Response<FilmPage> response)
        {
            if (response.isSuccessful() && response.body() != null)
                filmData.postValue(response.body());
            else
                filmData.postValue(null);
        }

        @Override
        public void onFailure(@NonNull Call<FilmPage> call, @NonNull Throwable t)
        {
            filmData.postValue(null);
        }
    }
}
