package com.foxdev.kinopoisk.data.objects;

import static androidx.room.ColumnInfo.INTEGER;
import static androidx.room.ColumnInfo.TEXT;
import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Films", foreignKeys = {@ForeignKey(entity = Watch.class,
        parentColumns = {"WatchId"},
        childColumns = {"watchId"},
        onDelete = CASCADE)})
public final class FilmWatchData
{
    @ColumnInfo(typeAffinity = INTEGER)
    public int watchId;

    @PrimaryKey()
    @ColumnInfo(typeAffinity = INTEGER)
    public int filmId;

    @NonNull
    @ColumnInfo(typeAffinity = TEXT)
    public String nameRu = "";

    @Nullable
    @ColumnInfo(typeAffinity = TEXT)
    public String nameEn = null;

    @NonNull
    @ColumnInfo(typeAffinity = TEXT)
    public String year = "";

    @NonNull
    @ColumnInfo(typeAffinity = TEXT)
    public String rating = "";

    @Nullable
    @ColumnInfo(typeAffinity = TEXT)
    public String country = null;

    @Nullable
    @ColumnInfo(typeAffinity = TEXT)
    public String genre = null;

    @NonNull
    @ColumnInfo(typeAffinity = TEXT)
    public String poster = "";
}
