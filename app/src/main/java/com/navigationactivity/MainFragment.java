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
 * Пример главного фрагмента
 */
public class MainFragment extends PlaceholderFragment {

    // Задание вида меню (скрытие и показ кнопок на тулбаре)
    @Override
    public void setupToolbar(Menu menu) {

    }

    // Фрагмент не является списком меню в дровере
    @Override
    public boolean clearStackBeforeOpen() {
        return false;
    }

    // Скрыть предыдущий фрагмент
    @Override
    public boolean hidePrevFragment() {
        return true;
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

    }

    @Override
    public String getTitle() {
        return "MAIN";
    }
}
