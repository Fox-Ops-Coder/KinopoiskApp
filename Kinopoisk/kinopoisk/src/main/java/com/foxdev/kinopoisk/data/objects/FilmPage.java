package com.foxdev.kinopoisk.data.objects;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FilmPage
{
    @SerializedName("pagesCount")
    public int pagesCount;

    public boolean favorite = false;

    @NonNull
    @SerializedName("films")
    public ArrayList<FilmShortInfo> films = new ArrayList<>();
}
