package com.foxdev.kinopoisk.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.foxdev.kinopoisk.data.objects.FilmSearch;
import com.foxdev.kinopoisk.viewmodel.FilmViewModel;

public final class FilmSearchPaginationFragment extends PaginationFragment
{
    public FilmSearchPaginationFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        FilmViewModel filmViewModel = new ViewModelProvider(requireActivity())
                .get(FilmViewModel.class);

        filmViewModel.getFilmPageLiveData().observe(getViewLifecycleOwner(), filmPage ->
        {
            if (filmPage != null)
            {
                fragmentPaginatorBinding.setFilmPage(filmPage);
                fragmentPaginatorBinding.executePendingBindings();

                if (filmPage instanceof FilmSearch)
                {
                    FilmSearch filmSearch = (FilmSearch) filmPage;

                    fragmentPaginatorBinding.leftButton.setOnClickListener(v ->
                            filmViewModel.searchFilms(filmSearch.keyword,
                                    filmSearch.currentPage - 1));

                    fragmentPaginatorBinding.rightButton.setOnClickListener(v ->
                            filmViewModel.searchFilms(filmSearch.keyword,
                                    filmSearch.currentPage + 1));
                }
            }
        });

        return root;
    }
}
