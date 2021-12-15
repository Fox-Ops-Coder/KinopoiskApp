package com.foxdev.kinopoisk.data.objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.security.PublicKey;
import java.util.ArrayList;

public final class FilmShortInfo
{
    @SerializedName("filmId")
    public int filmId;

    @NonNull
    @SerializedName("nameRu")
    public String nameRu = "";

    @NonNull
    @SerializedName("nameEn")
    public String nameEn = "";

    @NonNull
    @SerializedName("year")
    public String year = "";

    @NonNull
    @SerializedName("filmLength")
    public String filmLength = "";

    @NonNull
    @SerializedName("countries")
    public ArrayList<Country> countries = new ArrayList<>();

    @NonNull
    @SerializedName("genres")
    public ArrayList<Genre> genres = new ArrayList<>();

    @NonNull
    @SerializedName("rating")
    public String rating = "";

    @SerializedName("ratingVoteCount")
    public float ratingVoteCount;

    @NonNull
    @SerializedName("posterUrl")
    public String posterUrl = "";

    @NonNull
    @SerializedName("posterUrlPreview")
    public String posterUrlPreview = "";

    @Nullable
    @SerializedName("ratingChange")
    public String ratingChange = null;

    @NonNull
    public String OtherName()
    {
        if (nameEn.isEmpty())
            return year;
        else
            return nameEn + ", " + year;
    }

    @NonNull
    public String FilmInfo()
    {
        return countries.get(0).FilmCountry + " • " + genres.get(0).FilmGenre;
    }

    @NonNull
    public String Rating()
    {
        if (rating.isEmpty() || rating.equals("null"))
            return "Нет";
        else
            return rating;
    }

    public boolean inWatchList = false;
}
