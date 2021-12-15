package com.foxdev.kinopoisk.viewmodel;

import androidx.annotation.NonNull;

import com.foxdev.kinopoisk.data.objects.Film;
import com.foxdev.kinopoisk.data.objects.FilmShortInfo;
import com.foxdev.kinopoisk.data.objects.Watch;

import java.util.concurrent.Future;

public interface WatchDatabase
{
    @NonNull
    Future<?> addToWatchList(@NonNull final FilmShortInfo filmShortInfo);

    @NonNull
    Future<?> removeFromWatchList(@NonNull final FilmShortInfo filmShortInfo);
}
