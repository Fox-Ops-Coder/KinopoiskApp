package com.foxdev.kinopoisk.data.objects;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public final class Film
{
    @SerializedName("kinopoiskId")
    public int kinopoiskId;

    @NonNull
    @SerializedName("imdbId")
    public String imdbId = "";

    @NonNull
    @SerializedName("nameRu")
    public String nameRu = "";

    @NonNull
    @SerializedName("nameEn")
    public String nameEn = "";

    @NonNull
    @SerializedName("nameOriginal")
    public String nameOriginal = "";

    @NonNull
    @SerializedName("posterUrl")
    public String posterUrl = "";

    @NonNull
    @SerializedName("posterUrlPreview")
    public String posterUrlPreview = "";

    @SerializedName("reviewsCount")
    public float reviewsCount;

    @SerializedName("ratingGoodReview")
    public float ratingGoodReview;

    @SerializedName("ratingGoodReviewVoteCount")
    public float ratingGoodReviewVoteCount;

    @SerializedName("ratingKinopoisk")
    public float ratingKinopoisk;

    @SerializedName("ratingKinopoiskVoteCount")
    public float ratingKinopoiskVoteCount;

    @SerializedName("ratingImdb")
    public float ratingImdb;

    @SerializedName("ratingImdbVoteCount")
    public float ratingImdbVoteCount;

    @SerializedName("ratingFilmCritics")
    public float ratingFilmCritics;

    @SerializedName("ratingFilmCriticsVoteCount")
    public float ratingFilmCriticsVoteCount;

    @SerializedName("ratingAwait")
    public float ratingAwait;

    @SerializedName("ratingAwaitCount")
    public float ratingAwaitCount;

    @SerializedName("ratingRfCritics")
    public float ratingRfCritics;

    @SerializedName("ratingRfCriticsVoteCount")
    public float ratingRfCriticsVoteCount;

    @NonNull
    @SerializedName("webUrl")
    public String webUrl = "";

    @SerializedName("year")
    public float year;

    @SerializedName("filmLength")
    public float filmLength;

    @NonNull
    @SerializedName("slogan")
    public String slogan = "";

    @NonNull
    @SerializedName("description")
    public String description = "";

    @NonNull
    @SerializedName("shortDescription")
    public String shortDescription = "";

    @NonNull
    @SerializedName("editorAnnotation")
    public String editorAnnotation = "";

    @SerializedName("isTicketsAvailable")
    public boolean isTicketsAvailable;

    @NonNull
    @SerializedName("productionStatus")
    public String productionStatus = "";

    @NonNull
    @SerializedName("type")
    public String type = "";

    @NonNull
    @SerializedName("ratingMpaa")
    public String ratingMpaa = "";

    @NonNull
    @SerializedName("ratingAgeLimits")
    public String ratingAgeLimits = "";

    @SerializedName("hasImax")
    public boolean hasImax;

    @SerializedName("has3D")
    public boolean has3D;

    @NonNull
    @SerializedName("lastSync")
    public String lastSync = "";

    @NonNull
    @SerializedName("countries")
    public ArrayList<Country> countries = new ArrayList<Country>();

    @NonNull
    @SerializedName("genres")
    public ArrayList <Genre> genres = new ArrayList<Genre>();

    @SerializedName("startYear")
    public float startYear;

    @SerializedName("endYear")
    public float endYear;

    @SerializedName("serial")
    public boolean serial;

    @SerializedName("shortFilm")
    public boolean shortFilm;

    @SerializedName("completed")
    public boolean completed;
}
