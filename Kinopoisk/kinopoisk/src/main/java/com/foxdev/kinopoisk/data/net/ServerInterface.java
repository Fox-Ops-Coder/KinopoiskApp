package com.foxdev.kinopoisk.data.net;

import androidx.annotation.NonNull;

import com.foxdev.kinopoisk.data.objects.Film;
import com.foxdev.kinopoisk.data.objects.FilmPage;
import com.foxdev.kinopoisk.data.objects.FilmSearch;

import retrofit2.Call;

public interface ServerInterface
{
    @NonNull
    Call<Film> getFilmById(final int filmId);

    @NonNull
    Call<FilmPage> getTopFilm(final int pageNumber);

    @NonNull
    Call<FilmSearch> searchFilm(@NonNull final String keyword, final int pageNumber);
}
