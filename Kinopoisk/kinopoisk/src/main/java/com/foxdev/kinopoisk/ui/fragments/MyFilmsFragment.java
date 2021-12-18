package com.foxdev.kinopoisk.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foxdev.kinopoisk.adapters.FilmListAdapter;
import com.foxdev.kinopoisk.databinding.FragmentFilmListBinding;
import com.foxdev.kinopoisk.viewmodel.FilmViewModel;

public final class MyFilmsFragment extends Fragment {

    public MyFilmsFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        FragmentFilmListBinding fragmentFilmListBinding = FragmentFilmListBinding
                .inflate(inflater);

        FilmViewModel filmViewModel = new ViewModelProvider(requireActivity())
                .get(FilmViewModel.class);

        FilmListAdapter filmListAdapter = new FilmListAdapter(filmViewModel);
        fragmentFilmListBinding.filmList.setAdapter(filmListAdapter);

        getChildFragmentManager().beginTransaction()
                .add(fragmentFilmListBinding.paginatorPlace.getId(),
                        new MyFilmsPaginationFragment())
                .commit();

        filmViewModel.getFilmPageLiveData().observe(getViewLifecycleOwner(), filmPage ->
        {
            if (filmPage != null)
                filmListAdapter.setFilms(filmPage.films);
        });

        filmViewModel.getFavoriteFilms();

        return fragmentFilmListBinding.getRoot();
    }
}