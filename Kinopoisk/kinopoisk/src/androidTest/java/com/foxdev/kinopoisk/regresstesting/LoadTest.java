package com.foxdev.kinopoisk.regresstesting;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;

import static org.hamcrest.core.AllOf.allOf;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.foxdev.kinopoisk.R;
import com.foxdev.kinopoisk.ui.activities.MainActivity;
import com.foxdev.kinopoisk.viewmodel.FilmViewModel;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoadTest
{
    public static class ClickOnImageView implements ViewAction
    {
        ViewAction action = click();

        @Override
        public Matcher<View> getConstraints() {
            return action.getConstraints();
        }

        @Override
        public String getDescription() {
            return " click on custom image view";
        }

        @Override
        public void perform(UiController uiController, View view) {
            action.perform(uiController, view.findViewById(R.id.bookmark_button));
        }
    }
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void regressTest()
    {
        onView(withId(R.id.film_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0, new ClickOnImageView()));

        onView(withId(R.id.my_films_page)).perform(click());

        onView(withId(R.id.film_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0, new ClickOnImageView()));
    }
}
