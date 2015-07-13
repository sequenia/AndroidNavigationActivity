package com.navigationactivity;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by chybakut2004 on 13.07.15.
 */
public class MainActivity extends NavigationActivity {

    private static final int FRAGMENT_MAIN = 0;
    private static final int FRAGMENT_1 = 1;
    private static final int FRAGMENT_2 = 3;
    private static final int FRAGMENT_3 = 5;

    @Override
    public void initTitles(HashMap<Integer, String> titles) {
        // Заполнить заголовки
        titles.put(FRAGMENT_MAIN, "MAIN");
        titles.put(FRAGMENT_1, "FRAGMENT 1");
        titles.put(FRAGMENT_2, "FRAGMENT 2");
        titles.put(FRAGMENT_3, "FRAGMENT 3");
    }

    @Override
    public TextView getToolbarTitle() {
        // Вернуть вьюшку для заголовка
        return (TextView) findViewById(R.id.toolbar_title);
    }

    @Override
    public int getNavigationDrawerLayoutId() {
        // Вернуть id разметки для дровера
        return R.layout.fragment_navigation_drawer;
    }

    @Override
    public void showNavigationDrawerData() {
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
    public int getActivityLayoutId() {
        // Вернуть id разметки активити
        return R.layout.activity_main;
    }

    @Override
    public PlaceholderFragment createFragmentByNumber(int number) {
        PlaceholderFragment fragment = null;

        switch (number) {

            // Главный фрагмент (Который показывается при заходе в приложение)
            case FRAGMENT_MAIN:
                fragment = new PlaceholderFragment() {

                    // Задание вида меню (скрытие и показ кнопок на тулбаре)
                    @Override
                    public void restoreMenu(Menu menu) {

                    }

                    // Фрагмент не является списком меню в дровере
                    @Override
                    public boolean isDrawerElement() {
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
                };
                break;

            case FRAGMENT_1:case FRAGMENT_2:case FRAGMENT_3:
                fragment = new PlaceholderFragment() {
                    @Override
                    public void restoreMenu(Menu menu) {

                    }

                    // Является фрагментом в меню дровера
                    @Override
                    public boolean isDrawerElement() {
                        return true;
                    }

                    @Override
                    public boolean hidePrevFragment() {
                        return true;
                    }

                    // Нужно показать кнопку назад
                    @Override
                    public boolean needsShowMainMenuButton() {
                        return false;
                    }

                    @Override
                    public void resumeFragment() {

                    }
                };
                break;
        }

        return fragment;
    }
}
