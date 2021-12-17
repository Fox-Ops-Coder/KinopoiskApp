package com.foxdev.kinopoisk.data.objects;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public final class FilmSearch extends FilmPage
{
    @NonNull
    @SerializedName("keyword")
    public String keyword = "";
}
