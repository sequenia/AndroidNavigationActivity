package com.navigationactivity;

import android.Manifest;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navigationactivity.navigation.NavigationActivity;
import com.navigationactivity.navigation.PlaceholderFragment;

import java.util.HashMap;

/**
 * Created by chybakut2004 on 13.07.15.
 *
 * Пример активити с навигацией
 */
public class MainActivity extends NavigationActivity {

    // Коды экранов (фрагментов)
    public static final int FRAGMENT_MAIN = 0;
    public static final int FRAGMENT_1 = 1;
    public static final int FRAGMENT_2 = 3;
    public static final int FRAGMENT_3 = 5;
    public static final int SUB_FRAGMENT = 111;

    // Менеджер проверки разрешений
    private PermissionChecker checker;
    // Разрешения, необходимые приложению
    private String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void initTitles(HashMap<Integer, String> titles) {
        // Заполнить заголовки
        titles.put(FRAGMENT_MAIN, "MAIN");
        titles.put(FRAGMENT_1, "FRAGMENT 1");
        titles.put(FRAGMENT_2, "FRAGMENT 2");
        titles.put(FRAGMENT_3, "FRAGMENT 3");
        titles.put(SUB_FRAGMENT, "FRAGMENT 4");
    }

    // Id файла с разметкой дровера
    @Override
    public int getNavigationDrawerLayoutId() {
        return R.layout.fragment_navigation_drawer;
    }

    @Override
    public void showNavigationDrawerData(ViewGroup drawerContainer) {
        // Забить личные данные в дровер
    }

    @Override
    public void initDrawerItems(HashMap<Integer, View> items, ViewGroup drawerContainer) {
        // Передать в дровер ссылки на элементы меню, по которым можно кликать
        items.put(FRAGMENT_1, drawerContainer.findViewById(R.id.section1));
        items.put(FRAGMENT_2, drawerContainer.findViewById(R.id.section2));
        items.put(FRAGMENT_3, drawerContainer.findViewById(R.id.section3));
    }

    @Override
    public PlaceholderFragment createMainFragment() {
        // Создать главный фрагмент
        return newFragmentInstance(FRAGMENT_MAIN);
    }

    @Override
    public PlaceholderFragment createFragmentByNumber(int number) {
        PlaceholderFragment fragment = null;

        switch (number) {

            // Главный фрагмент (Который показывается при заходе в приложение)
            case FRAGMENT_MAIN:
                fragment = new MainFragment();
                break;

            // Фрагменты, являющиеся элементами меню в дровере
            case FRAGMENT_1:case FRAGMENT_2:case FRAGMENT_3:
                fragment = new MenuFragment();
                break;

            // Подфрагмент (Что-то далекое в навигации)
            case SUB_FRAGMENT:
                fragment = new SubFragment();
                break;
        }

        return fragment;
    }

    // Вернуть id меню во время открытого дровера
    @Override
    public int getGlobalMenuId() {
        return R.menu.global;
    }

    // Вернуть id меню
    @Override
    public int getMenuId() {
        return R.menu.main;
    }
}
