package com.foxdev.kinopoisk.ui.listeners;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.foxdev.kinopoisk.R;
import com.foxdev.kinopoisk.data.objects.FilmShortInfo;
import com.foxdev.kinopoisk.viewmodel.WatchDatabase;

import java.util.concurrent.Future;

public final class WatchListener implements View.OnClickListener
{
    @NonNull
    private final WatchDatabase watchDatabase;

    @NonNull
    private final FilmShortInfo filmShortInfo;

    private boolean isAdd;

    private static Drawable getDrawable(@NonNull Resources resources,
                                        final int resourceId,
                                        @NonNull Resources.Theme theme)
    {
        return ResourcesCompat
                .getDrawable(resources,
                        resourceId,
                        theme);
    }

    public WatchListener(@NonNull final WatchDatabase watchDatabase,
                         @NonNull final FilmShortInfo filmShortInfo,
                         final boolean isAdd)
    {
        this.watchDatabase = watchDatabase;
        this.filmShortInfo = filmShortInfo;
        this.isAdd = isAdd;
    }


    @Override
    public void onClick(View v)
    {
        Drawable drawable = null;

        if (isAdd)
        {
            Future<?> future = watchDatabase
                    .addToWatchList(filmShortInfo);

            while (!future.isDone());

            drawable = getDrawable(v.getResources(),
                    R.drawable.ic_baseline_check_box_24,
                    v.getContext().getTheme());

            isAdd = false;
        }
        else
        {
            Future<?> future = watchDatabase
                    .removeFromWatchList(filmShortInfo);

            while (!future.isDone())

                drawable = getDrawable(v.getResources(),
                        R.drawable.ic_baseline_bookmark_24,
                        v.getContext().getTheme());

            isAdd = true;
        }

        ((ImageButton)v).setImageDrawable(drawable);

        filmShortInfo.inWatchList = true;
    }
}
