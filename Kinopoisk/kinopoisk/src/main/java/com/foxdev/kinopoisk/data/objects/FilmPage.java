package com.foxdev.kinopoisk.data.objects;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public final class FilmPage
{
    @SerializedName("pagesCount")
    public int Pages;

    public boolean Favorite = false;

    @NonNull
    @SerializedName("films")
    public ArrayList<FilmShortInfo> Films = new ArrayList<>();
}
