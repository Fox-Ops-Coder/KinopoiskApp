package com.foxdev.kinopoisk.data.objects;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public final class FilmSearch
{
    @NonNull
    @SerializedName("keyword")
    public String keyword = "";

    @SerializedName("pagesCount")
    public int pagesCount;

    @SerializedName("films")
    public ArrayList<FilmShortInfo> films = new ArrayList<>();
}
