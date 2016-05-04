package com.navigationactivity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.navigationactivity.activities.MainActivity;
import com.navigationactivity.R;
import com.navigationactivity.navigation.NavigationActivity;
import com.navigationactivity.navigation.PlaceholderFragment;

/**
 * Created by chybakut2004 on 04.08.15.
 *
 * Пример главного фрагмента
 */
public class MainFragment extends AppFragment {

    // Задание вида меню (скрытие и показ кнопок на тулбаре)
    @Override
    public void setupToolbar(Menu menu) {

    }

    // Фрагмент не является списком меню в дровере
    @Override
    public boolean clearStackBeforeOpen() {
        return false;
    }

    // Показывать кнопку меню в тулбаре
    @Override
    public boolean needsShowMainMenuButton() {
        return true;
    }

    // Вызывается при возвращении на этот фрагмент
    @Override
    public void resumeFragment() {

    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onCreateViewCustom(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View view) {

        view.findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceholderFragment subFragment = ((NavigationActivity) getActivity()).newFragmentInstance(MainActivity.FRAGMENT_1);
                ((NavigationActivity) getActivity()).addSubFragment(subFragment);
            }
        });
    }

    @Override
    public String getTitle() {
        return "Главный экран";
    }

    @Override
    public int getMenuId() {
        return R.menu.main;
    }
}
