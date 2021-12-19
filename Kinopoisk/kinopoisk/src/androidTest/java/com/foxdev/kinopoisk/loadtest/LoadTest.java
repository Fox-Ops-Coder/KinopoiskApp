package com.foxdev.kinopoisk.loadtest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.foxdev.kinopoisk.data.net.KinopoiskServer;
import com.foxdev.kinopoisk.data.net.ServerInterface;
import com.foxdev.kinopoisk.data.objects.FilmPage;
import com.foxdev.kinopoisk.data.sql.KinopoiskDao;
import com.foxdev.kinopoisk.data.sql.KinopoiskDatabase;
import com.foxdev.kinopoisk.viewmodel.FilmViewModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(AndroidJUnit4.class)
public class LoadTest
{
    @Test
    public void exception() throws InterruptedException
    {
        KinopoiskDao kinopoiskDao = Mockito.mock(KinopoiskDao.class);
        Mockito.when(kinopoiskDao.getWatchList()).thenReturn(new ArrayList<>());

        ServerInterface serverInterface = Mockito.mock(ServerInterface.class);
        Mockito.when(serverInterface.getTopFilm(1)).thenReturn(new Call<FilmPage>() {
            @NonNull
            @Override
            public Response<FilmPage> execute() throws IOException
            {
                throw new IOException();
            }

            @Override
            public void enqueue(Callback<FilmPage> callback)
            {
            }

            @Override
            public boolean isExecuted()
            {
                return false;
            }

            @Override
            public void cancel()
            {

            }

            @Override
            public boolean isCanceled()
            {
                return false;
            }

            @Override
            public Call<FilmPage> clone()
            {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }

            @Override
            public Timeout timeout() {
                return null;
            }
        });

        FilmViewModel filmViewModel = new FilmViewModel(kinopoiskDao, serverInterface);

        Future<?> loadTask = filmViewModel.loadTopFilms();
        synchronized (this)
        {
            while (!loadTask.isDone())
                wait(1000);
        }

        Assert.assertNull(filmViewModel.getFilmPageLiveData().getValue());
    }

    @Test
    public void notSuccessful() throws InterruptedException
    {
        KinopoiskDao kinopoiskDao = Mockito.mock(KinopoiskDao.class);
        Mockito.when(kinopoiskDao.getWatchList()).thenReturn(new ArrayList<>());

        ServerInterface serverInterface = Mockito.mock(ServerInterface.class);
        Mockito.when(serverInterface.getTopFilm(1)).thenReturn(new Call<FilmPage>() {
            @NonNull
            @Override
            public Response<FilmPage> execute()
            {
                return Response
                        .error(404, ResponseBody.create(null, new byte[]{}));
            }

            @Override
            public void enqueue(Callback<FilmPage> callback)
            {
            }

            @Override
            public boolean isExecuted()
            {
                return false;
            }

            @Override
            public void cancel()
            {

            }

            @Override
            public boolean isCanceled()
            {
                return false;
            }

            @Override
            public Call<FilmPage> clone()
            {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }

            @Override
            public Timeout timeout() {
                return null;
            }
        });

        FilmViewModel filmViewModel = new FilmViewModel(kinopoiskDao, serverInterface);

        Future<?> loadTask = filmViewModel.loadTopFilms();
        synchronized (this)
        {
            while (!loadTask.isDone())
                wait(1000);
        }

        Assert.assertNull(filmViewModel.getFilmPageLiveData().getValue());
    }

    @Test
    public void bodyNull() throws InterruptedException
    {
        KinopoiskDao kinopoiskDao = Mockito.mock(KinopoiskDao.class);
        Mockito.when(kinopoiskDao.getWatchList())
                .thenReturn(new ArrayList<>());

        ServerInterface serverInterface = Mockito.mock(ServerInterface.class);
        Mockito.when(serverInterface.getTopFilm(1))
                .thenReturn(new Call<FilmPage>() {
            @Override
            public Response<FilmPage> execute()
            {
                return Response
                        .success(200, null);
            }

            @Override
            public void enqueue(Callback<FilmPage> callback)
            {
            }

            @Override
            public boolean isExecuted()
            {
                return false;
            }

            @Override
            public void cancel()
            {

            }

            @Override
            public boolean isCanceled()
            {
                return false;
            }

            @Override
            public Call<FilmPage> clone()
            {
                return null;
            }

            @Override
            public Request request()
            {
                return null;
            }

            @Override
            public Timeout timeout()
            {
                return null;
            }
        });

        FilmViewModel filmViewModel = new FilmViewModel(kinopoiskDao, serverInterface);

        Future<?> loadTask = filmViewModel.loadTopFilms();
        synchronized (this)
        {
            while (!loadTask.isDone())
                wait(1000);
        }

        Assert.assertNull(filmViewModel.getFilmPageLiveData().getValue());
    }

    @Test
    public void success() throws InterruptedException
    {
        KinopoiskDao kinopoiskDao = Mockito.mock(KinopoiskDao.class);
        Mockito.when(kinopoiskDao.getWatchList())
                .thenReturn(new ArrayList<>());

        ServerInterface serverInterface = Mockito.mock(ServerInterface.class);
        Mockito.when(serverInterface.getTopFilm(1))
                .thenReturn(new Call<FilmPage>() {
                    @Override
                    public Response<FilmPage> execute()
                    {
                        return Response
                                .success(200, new FilmPage());
                    }

                    @Override
                    public void enqueue(Callback<FilmPage> callback)
                    {
                    }

                    @Override
                    public boolean isExecuted()
                    {
                        return false;
                    }

                    @Override
                    public void cancel()
                    {
                    }

                    @Override
                    public boolean isCanceled()
                    {
                        return false;
                    }

                    @Override
                    public Call<FilmPage> clone()
                    {
                        return null;
                    }

                    @Override
                    public Request request()
                    {
                        return null;
                    }

                    @Override
                    public Timeout timeout()
                    {
                        return null;
                    }
                });

        FilmViewModel filmViewModel = new FilmViewModel(kinopoiskDao, serverInterface);

        Future<?> loadTask = filmViewModel.loadTopFilms();

        synchronized (this)
        {
            while (!loadTask.isDone())
                wait(1000);
        }

        Assert.assertNotNull(filmViewModel.getFilmPageLiveData().getValue());
    }

}
