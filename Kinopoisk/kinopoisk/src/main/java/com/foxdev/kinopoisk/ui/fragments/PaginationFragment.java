package com.foxdev.kinopoisk.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.foxdev.kinopoisk.databinding.FragmentPaginatorBinding;

public class PaginationFragment extends Fragment
{
    protected FragmentPaginatorBinding fragmentPaginatorBinding;

    public PaginationFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        fragmentPaginatorBinding = FragmentPaginatorBinding
                .inflate(inflater);

        return fragmentPaginatorBinding.getRoot();
    }
}