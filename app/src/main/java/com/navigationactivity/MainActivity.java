package com.navigationactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by chybakut2004 on 13.07.15.
 *
 * Пример активити с навигацией
 */
public class MainActivity extends NavigationActivity {

    private static final int FRAGMENT_MAIN = 0;
    private static final int FRAGMENT_1 = 1;
    private static final int FRAGMENT_2 = 3;
    private static final int FRAGMENT_3 = 5;
    private static final int SUB_FRAGMENT = 111;

    @Override
    public void initTitles(HashMap<Integer, String> titles) {
        // Заполнить заголовки
        titles.put(FRAGMENT_MAIN, "MAIN");
        titles.put(FRAGMENT_1, "FRAGMENT 1");
        titles.put(FRAGMENT_2, "FRAGMENT 2");
        titles.put(FRAGMENT_3, "FRAGMENT 3");
        titles.put(SUB_FRAGMENT, "FRAGMENT 4");
    }

    /**
     * Вернуть здесь TextView, в которой отображается заголовок тулбара
     */
    @Override
    public TextView getToolbarTitle() {
        return (TextView) findViewById(R.id.toolbar_title);
    }

    @Override
    public int getNavigationDrawerLayoutId() {
        // Вернуть id разметки для дровера
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

                    @Override
                    public int getFragmentLayoutId() {
                        return R.layout.fragment_main;
                    }

                    @Override
                    public void onCreateViewCustom(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View view) {

                    }
                };
                break;

            // Фрагменты, являющиеся элементами меню в дровере
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

                    @Override
                    public int getFragmentLayoutId() {
                        return R.layout.fragment_main;
                    }

                    @Override
                    public void onCreateViewCustom(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View view) {
                        Button button = new Button(getActivity());
                        button.setText("Нажми меня");
                        ((ViewGroup) view).addView(button);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PlaceholderFragment subFragment = newFragmentInstance(SUB_FRAGMENT);

                                Bundle args = subFragment.getArguments();
                                args.putString("key", "value");

                                addSubFragment(subFragment);
                            }
                        });
                    }
                };
                break;

            // Подфрагмент (Что-то далекое в навигации)
            case SUB_FRAGMENT:
                fragment = new PlaceholderFragment() {
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
                        return R.layout.fragment_main;
                    }

                    @Override
                    public void onCreateViewCustom(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View view) {

                    }
                };
                break;
        }

        return fragment;
    }

    // Вернуть id дровер фрагмента в разметке
    @Override
    public int getNavigationDrawerFragmentId() {
        return R.id.navigation_drawer;
    }

    // Вернуть id виджета дровера в разметке
    @Override
    public int getNavigationDrawerLayoutWidgetId() {
        return R.id.drawer_layout;
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

    // Вернуть id фрагмента в разметке, куда помещается контент
    @Override
    public int getContentFragmentId() {
        return R.id.content;
    }

    // Вернуть id тулбара в разметке
    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }
}
