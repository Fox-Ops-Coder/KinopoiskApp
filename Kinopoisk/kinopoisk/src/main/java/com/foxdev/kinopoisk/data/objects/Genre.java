package com.foxdev.kinopoisk.data.objects;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public final class Genre
{
    @NonNull
    @SerializedName("genre")
    public String FilmGenre = "";
}
