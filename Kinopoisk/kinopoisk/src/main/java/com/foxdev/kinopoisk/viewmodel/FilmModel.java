package com.foxdev.kinopoisk.viewmodel;

import androidx.annotation.NonNull;

import java.util.concurrent.Future;

public interface FilmModel
{
    @NonNull
    Future<?> loadTopFilms(final int pageNumber);

    @NonNull
    Future<?> loadTopFilms();

    @NonNull
    Future<?> searchFilms(@NonNull final String keyword, final int pageNumber);

    @NonNull
    Future<?> searchFilms(@NonNull final String keyword);

    @NonNull
    Future<?> getFavoriteFilms();
}
