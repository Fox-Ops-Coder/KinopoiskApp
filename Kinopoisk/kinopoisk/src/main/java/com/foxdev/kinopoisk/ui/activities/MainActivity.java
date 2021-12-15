package com.foxdev.kinopoisk.ui.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.foxdev.kinopoisk.R;
import com.foxdev.kinopoisk.databinding.ActivityMainBinding;
import com.foxdev.kinopoisk.ui.fragments.FilmListFragment;
import com.foxdev.kinopoisk.ui.fragments.SearchFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public final class MainActivity extends AppCompatActivity
{
    private static final int hideSystemUIValue = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        getSupportFragmentManager()
                .beginTransaction()
                .add(activityMainBinding.appContent.getId(), new FilmListFragment())
                .commit();

        activityMainBinding.bottomAppNav.setSelectedItemId(R.id.top_page);
        activityMainBinding.bottomAppNav.setOnItemSelectedListener(item ->
        {
            if (item.getItemId() == R.id.search_page)
                getSupportFragmentManager().beginTransaction()
                .replace(activityMainBinding.appContent.getId(), new SearchFragment())
                .commit();
            else if (item.getItemId() == R.id.top_page)
                getSupportFragmentManager().beginTransaction()
                .replace(activityMainBinding.appContent.getId(), new FilmListFragment())
                .commit();

            return true;
        });

        getWindow().getDecorView().setSystemUiVisibility(hideSystemUIValue);
    }
}