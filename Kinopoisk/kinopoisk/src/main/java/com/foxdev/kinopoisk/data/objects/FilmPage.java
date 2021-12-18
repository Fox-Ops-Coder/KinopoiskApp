package com.foxdev.kinopoisk.data.objects;

import android.view.View;

import androidx.annotation.NonNull;

import com.foxdev.kinopoisk.ui.utils.Pagination;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FilmPage implements Pagination
{
    @SerializedName("pagesCount")
    public int pagesCount;

    public int currentPage;

    public boolean favorite = false;

    @NonNull
    @SerializedName("films")
    public ArrayList<FilmShortInfo> films = new ArrayList<>();

    @Override
    public final int previousVisibility()
    {
        return currentPage <= 1 ? View.INVISIBLE : View.VISIBLE;
    }

    @Override
    @NonNull
    public final String pagesCounter()
    {
        return currentPage + " из " + pagesCount;
    }

    @Override
    public final int nextVisibility()
    {
        return currentPage >= pagesCount ? View.INVISIBLE : View.VISIBLE;
    }
}
