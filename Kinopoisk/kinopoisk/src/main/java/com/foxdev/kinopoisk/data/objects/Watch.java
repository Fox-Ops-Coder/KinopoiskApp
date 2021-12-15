package com.foxdev.kinopoisk.data.objects;

import static androidx.room.ColumnInfo.INTEGER;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "WatchList")
public final class Watch
{
    @ColumnInfo(typeAffinity = INTEGER)
    @PrimaryKey(autoGenerate = true)
    public int WatchId;

    @ColumnInfo(typeAffinity = INTEGER)
    public int FilmId;
}
