package com.foxdev.kinopoisk.data.sql;

import android.app.Application;

import androidx.room.Room;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public final class DatabaseModule
{
    @Provides
    public static KinopoiskDao getDatabase(Application application)
    {
        return Room.databaseBuilder(application.getApplicationContext(),
                KinopoiskDatabase.class,
                "FilmWatchList")
                .build()
                .GetKinopoiskDao();
    }
}
