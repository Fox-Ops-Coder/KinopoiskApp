package com.foxdev.kinopoisk.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.foxdev.kinopoisk.viewmodel.FilmViewModel;

public class MyFilmsPaginationFragment extends PaginationFragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
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

                fragmentPaginatorBinding.leftButton.setOnClickListener(v ->
                        filmViewModel.getFavoriteFilms(filmPage.currentPage - 1));

                fragmentPaginatorBinding.rightButton.setOnClickListener(v ->
                        filmViewModel.getFavoriteFilms(filmPage.currentPage + 1));
            }
        });

        return root;
    }
}
