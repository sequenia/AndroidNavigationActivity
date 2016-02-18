package com.navigationactivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.navigationactivity.navigation.NavigationActivity;
import com.navigationactivity.navigation.PlaceholderFragment;
import com.navigationactivity.permissions.PermissionsActivity;
import com.navigationactivity.permissions.PermissionsChecker;

import java.util.ArrayList;
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
    public static final int FRAGMENT_2 = 2;
    public static final int FRAGMENT_3 = 3;
    public static final int SUB_FRAGMENT = 111;

    // Менеджер проверки разрешений
    private PermissionsChecker checker;
    // Разрешения, необходимые приложению
    private String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private boolean permissionsShown = false; // Если уже показали, то не показывать

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Создаем менеджер проверки разрешений
        checker = new PermissionsChecker(getApplicationContext());
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
    public void selectDrawerItem(View menuItem) {
        menuItem.setBackgroundResource(R.color.material_grey_300);
    }

    @Override
    public void deselectDrawerItem(View menuItem) {
        menuItem.setBackgroundResource(R.color.white);
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

    @Override
    protected void onResume() {
        super.onResume();

        // Если не все разрешения выданы, то нужно их попросить
        if(checker.permissionsDenied(PERMISSIONS)) {
            // Но только если мы их еще не показывали или не недоели пользователю
            if(!checker.clickedNeverAskAgain(this, PERMISSIONS) && !permissionsShown) {
                permissionsShown = true;
                PermissionsActivity.startActivityForResult(this, RequestCodes.CODE_PERMISSIONS, PERMISSIONS);
            }
        }
    }
}
