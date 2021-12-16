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
}
