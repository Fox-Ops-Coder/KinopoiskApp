package com.foxdev.kinopoisk.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foxdev.kinopoisk.R;
import com.foxdev.kinopoisk.databinding.FragmentPaginatorBinding;
import com.foxdev.kinopoisk.viewmodel.FilmViewModel;

public final class PaginationFragment extends Fragment {

    public PaginationFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        FragmentPaginatorBinding fragmentPaginatorBinding = FragmentPaginatorBinding
                .inflate(inflater);

        FilmViewModel filmViewModel = new ViewModelProvider(requireActivity())
                .get(FilmViewModel.class);

        filmViewModel.getFilmPageLiveData().observe(getViewLifecycleOwner(), filmPage ->
        {
            if (filmPage != null)
            {
                fragmentPaginatorBinding.setFilmPage(filmPage);
                fragmentPaginatorBinding.executePendingBindings();

                fragmentPaginatorBinding.leftButton.setOnClickListener(v ->
                        filmViewModel.loadTopFilms(filmPage.currentPage - 1));

                fragmentPaginatorBinding.rightButton.setOnClickListener(v ->
                        filmViewModel.loadTopFilms(filmPage.currentPage + 1));
            }
        });

        return fragmentPaginatorBinding.getRoot();
    }
}