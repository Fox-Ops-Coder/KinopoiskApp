package com.foxdev.kinopoisk.data.sql;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.foxdev.kinopoisk.data.objects.FilmWatchData;
import com.foxdev.kinopoisk.data.objects.Watch;

@Database(entities = {Watch.class, FilmWatchData.class}, version = 1)
public abstract class KinopoiskDatabase extends RoomDatabase
{
    public abstract KinopoiskDao GetKinopoiskDao();
}
