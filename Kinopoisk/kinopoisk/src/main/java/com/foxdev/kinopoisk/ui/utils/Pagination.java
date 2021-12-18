package com.foxdev.kinopoisk.ui.utils;

import androidx.annotation.NonNull;

public interface Pagination
{
    int previousVisibility();

    @NonNull
    String pagesCounter();

    int nextVisibility();
}
