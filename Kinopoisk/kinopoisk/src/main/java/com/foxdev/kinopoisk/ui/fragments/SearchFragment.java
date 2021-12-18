package com.foxdev.kinopoisk.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foxdev.kinopoisk.adapters.FilmListAdapter;
import com.foxdev.kinopoisk.databinding.FragmentSearchBinding;
import com.foxdev.kinopoisk.viewmodel.FilmViewModel;

public final class SearchFragment extends Fragment
{
    public SearchFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        FragmentSearchBinding fragmentSearchBinding = FragmentSearchBinding.inflate(inflater);

        FilmViewModel filmViewModel = new ViewModelProvider(requireActivity())
                .get(FilmViewModel.class);

        FilmListAdapter filmListAdapter = new FilmListAdapter(filmViewModel);
        fragmentSearchBinding.searchList.setAdapter(filmListAdapter);

        getChildFragmentManager().beginTransaction()
                .add(fragmentSearchBinding.searchPagination.getId(),
                        new FilmSearchPaginationFragment())
                .commit();

        filmViewModel.getFilmPageLiveData().observe(getViewLifecycleOwner(), filmSearch ->
        {
            if (filmSearch != null && fragmentSearchBinding.searchBox.getQuery().length() != 0)
            {
                filmListAdapter.setFilms(filmSearch.films);

                if (fragmentSearchBinding.searchPagination.getVisibility() == View.GONE)
                    fragmentSearchBinding.searchPagination.setVisibility(View.VISIBLE);
            }
            else
            {
                fragmentSearchBinding.searchPagination.setVisibility(View.GONE);
            }
        });

        fragmentSearchBinding.searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                filmViewModel.searchFilms(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                filmViewModel.searchFilms(newText);

                return false;
            }
        });

        return fragmentSearchBinding.getRoot();
    }
}