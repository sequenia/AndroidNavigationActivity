package com.navigationactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.navigationactivity.navigation.PlaceholderFragment;

/**
 * Created by chybakut2004 on 04.08.15.
 *
 * Пример фрагмента из навигации
 */
public class SubFragment extends PlaceholderFragment {

    @Override
    public void onCreateViewCustom(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View view) {

    }

    @Override
    public String getTitle() {
        return "SUB FRAGMENT";
    }

    @Override
    public void setupToolbar(Menu menu) {

    }

    // Не нужно очищать стек при открытии - это фрагмент далекой навигации
    @Override
    public boolean clearStackBeforeOpen() {
        return false;
    }

    @Override
    public boolean needsShowMainMenuButton() {
        return false;
    }

    @Override
    public void resumeFragment() {

    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_sub;
    }
}
