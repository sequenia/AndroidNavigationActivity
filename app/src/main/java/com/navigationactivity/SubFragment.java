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
    public void restoreMenu(Menu menu) {

    }

    @Override
    public boolean isDrawerElement() {
        return false;
    }

    @Override
    public boolean hidePrevFragment() {
        return true;
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

    @Override
    public void onCreateViewCustom(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View view) {

    }
}
