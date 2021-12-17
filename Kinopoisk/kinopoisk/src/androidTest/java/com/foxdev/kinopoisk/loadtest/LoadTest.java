package com.foxdev.kinopoisk.loadtest;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.foxdev.kinopoisk.data.net.ServerInterface;
import com.foxdev.kinopoisk.data.objects.FilmPage;
import com.foxdev.kinopoisk.data.sql.KinopoiskDao;
import com.foxdev.kinopoisk.viewmodel.FilmViewModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
    public void notSuccessful() throws InterruptedException, ExecutionException {
        KinopoiskDao kinopoiskDao = Mockito.mock(KinopoiskDao.class);
        Mockito.when(kinopoiskDao.getWatchList()).thenReturn(new ArrayList<>());

        ServerInterface serverInterface = Mockito.mock(ServerInterface.class);
        Mockito.when(serverInterface.getTopFilm(1)).thenReturn(new Call<FilmPage>() {
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
        filmViewModel.loadTopFilms().get();

        Assert.assertNull(filmViewModel.getFilmPageLiveData().getValue());
    }

    @Test
    public void bodyNull() throws InterruptedException, ExecutionException {
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
        filmViewModel.loadTopFilms().get();

        Assert.assertNull(filmViewModel.getFilmPageLiveData().getValue());
    }

    @Test
    public void success() throws InterruptedException, ExecutionException {
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
        filmViewModel.loadTopFilms().get();

        Assert.assertNotNull(filmViewModel.getFilmPageLiveData().getValue());
    }
}
