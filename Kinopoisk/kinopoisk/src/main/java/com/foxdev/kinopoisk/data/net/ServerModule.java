package com.foxdev.kinopoisk.data.net;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public final class ServerModule
{
    @Provides
    public static ServerInterface getServerInterface()
    {
        return new KinopoiskServer();
    }
}
