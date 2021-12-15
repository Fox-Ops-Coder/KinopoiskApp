package com.foxdev.kinopoisk.viewmodel;

import androidx.annotation.NonNull;

public interface FilmModel
{
    void loadTopFilms(final int pageNumber);

    void loadTopFilms();

    void searchFilms(@NonNull final String keyword, final int pageNumber);

    void searchFilms(@NonNull final String keyword);
    
    void getFavoriteFilms();
}
