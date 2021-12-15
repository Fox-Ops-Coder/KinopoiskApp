package com.foxdev.kinopoisk.data.net;

import androidx.annotation.NonNull;

import com.foxdev.kinopoisk.data.objects.Film;
import com.foxdev.kinopoisk.data.objects.FilmPage;
import com.foxdev.kinopoisk.data.objects.FilmSearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface KinopoiskApi {
    @GET("v2.2/films/{id}")
    @Headers({"X-API-KEY: e9eff289-49ab-4f71-9741-a22faafac399"})
    @NonNull
    Call<Film> getFilmById(@Path("id") final int filmId);

    @GET("v2.2/films/top?type=TOP_250_BEST_FILMS")
    @Headers({"X-API-KEY: e9eff289-49ab-4f71-9741-a22faafac399"})
    @NonNull
    Call<FilmPage> getTopFilm(@Query("page") final int pageNumber);

    @GET("v2.1/films/search-by-keyword")
    @Headers({"X-API-KEY: e9eff289-49ab-4f71-9741-a22faafac399"})
    @NonNull
    Call<FilmSearch> searchFilm(@Query(value = "keyword", encoded = true) @NonNull String keyword,
                                @Query("page") final int page);
}
