package com.foxdev.kinopoisk.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.foxdev.kinopoisk.R;
import com.foxdev.kinopoisk.data.objects.FilmShortInfo;
import com.foxdev.kinopoisk.databinding.TopFilmItemBinding;
import com.foxdev.kinopoisk.ui.listeners.WatchListener;
import com.foxdev.kinopoisk.viewmodel.WatchDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public final class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.FilmViewHolder>
{
    @NonNull
    private List<FilmShortInfo> filmList;

    @NonNull
    private final WatchDatabase watchDatabase;

    public FilmListAdapter(@NonNull WatchDatabase watchDatabase)
    {
        this.filmList = new ArrayList<>();
        this.watchDatabase = watchDatabase;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new FilmViewHolder(TopFilmItemBinding
                .inflate(LayoutInflater
                        .from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position)
    {
        FilmShortInfo filmShortInfo = filmList.get(position);
        WatchListener watchListener = null;

        if (!filmShortInfo.inWatchList)
        {
            watchListener = new WatchListener(watchDatabase,
                    filmShortInfo, true);

            holder.filmItemBinding.bookmarkButton.setOnClickListener(watchListener);
        }
        else
        {
            holder.filmItemBinding.bookmarkButton.setImageDrawable(
                    ResourcesCompat.getDrawable(holder.filmItemBinding.getRoot().getContext().getResources(),
                    R.drawable.ic_baseline_check_box_24,
                    holder.filmItemBinding.getRoot().getContext().getTheme()));

            watchListener = new WatchListener(watchDatabase,
                    filmShortInfo, false);

        }

        holder.filmItemBinding.bookmarkButton.setOnClickListener(watchListener);

        holder.Bind(filmShortInfo);
        Picasso.get()
                .load(filmShortInfo.posterUrlPreview)
                .into(holder.filmItemBinding.filmPoster);
    }

    public void setFilms(@NonNull List<FilmShortInfo> filmList)
    {
        this.filmList = filmList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return filmList.size();
    }

    protected static class FilmViewHolder extends RecyclerView.ViewHolder
    {
        private final TopFilmItemBinding filmItemBinding;

        public FilmViewHolder(@NonNull TopFilmItemBinding filmItemBinding)
        {
            super(filmItemBinding.filmPoster.getRootView());
            this.filmItemBinding = filmItemBinding;
        }

        public void Bind(@NonNull FilmShortInfo info)
        {
            filmItemBinding.setFilm(info);
            filmItemBinding.executePendingBindings();
        }
    }
}
