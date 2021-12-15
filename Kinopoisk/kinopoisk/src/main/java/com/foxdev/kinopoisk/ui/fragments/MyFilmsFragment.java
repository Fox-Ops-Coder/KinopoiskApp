package com.foxdev.kinopoisk.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foxdev.kinopoisk.R;
import com.foxdev.kinopoisk.adapters.FilmListAdapter;
import com.foxdev.kinopoisk.data.objects.FilmShortInfo;
import com.foxdev.kinopoisk.data.objects.Watch;
import com.foxdev.kinopoisk.databinding.FragmentFilmListBinding;
import com.foxdev.kinopoisk.viewmodel.FilmViewModel;

import java.util.ArrayList;
import java.util.List;

public final class MyFilmsFragment extends Fragment {

    public MyFilmsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        FragmentFilmListBinding fragmentFilmListBinding = FragmentFilmListBinding
                .inflate(inflater);

        FilmViewModel filmViewModel = new ViewModelProvider(requireActivity())
                .get(FilmViewModel.class);

        return fragmentFilmListBinding.getRoot();
    }
}