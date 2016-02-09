package com.navigationactivity.navigation;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navigationactivity.R;

import java.util.HashMap;

/**
 * Created by chybakut2004 on 12.07.15.
 *
 * Активити с нафигацией с помощью фрагментов
 */
public abstract class NavigationActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    // Дровер
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutId());

        Toolbar toolbar = (Toolbar) findViewById(getToolbarId());
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Настройка навигационного меню
        mNavigationDrawerFragment = createNavigationDrawer();
        fragmentManager.beginTransaction()
                .replace(getNavigationDrawerFragmentId(), mNavigationDrawerFragment)
                .commit();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                PlaceholderFragment last = getLastFragment();
                if(last != null) {
                    setupScreen(last);
                }
            }
        });

        // Помещаем главный фрагмент в стек
        if(savedInstanceState == null) {
            PlaceholderFragment mainFragment = createMainFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.content, mainFragment, mainFragment.getName())
                    .addToBackStack(mainFragment.getName())
                    .commit();
        } else {
            setupScreen(getLastFragment());
        }
    }

    /**
     * Настраивает экран под переданный фрагмент (заголовок, кнопки и т.д.)
     */
    public void setupScreen(PlaceholderFragment fragment) {
        updateTitle(fragment);
        hideDefaultTitle();
        supportInvalidateOptionsMenu();
    }

    private NavigationDrawerFragment createNavigationDrawer() {
        return new NavigationDrawerFragment();
    }

    @Override
    public void onNavigationDrawerItemSelected(int number) {
        // Выбор элемента меню по умолчанию добавляет фрагмент на экран,
        // в соответствие с его настройками.
        // (Может заменить все текущие фрагменты на новый,
        // или добавить новый фрагмент поверх остальных)
        addSubFragment(newFragmentInstance(number));
    }

    /**
     * Добавляет фрагмент в стек поверх текущих фрагментов.
     * Если новый фрагмент является главной секцией, то перед этим очищает стек.
     */
    public void addSubFragment(PlaceholderFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Если фрагмент является секцией меню, то очищаем стек, перед тем как добавить его.
        if(fragment.clearStackBeforeOpen()) {
            while (fragmentManager.getBackStackEntryCount() > 1) {
                fragmentManager.popBackStackImmediate();
            }
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();

        // Ставим верхний фрагмент на паузу
        PlaceholderFragment last = getLastFragment();
        last.onPause(); // Останавливаем предыдущий фрагмент
        if (fragment.hidePrevFragment()) {
            ft.hide(last);  // Скрываем предыдущий фрагмент с экрана
        } else {
            ft.show(last);
        }

        ft.add(getContentFragmentId(), fragment, fragment.getName())
                .addToBackStack(fragment.getName())
                .commit();

        updateBackItem(fragment); // Обновление вида кнопки меню
    }

    /**
     * Показывает меню или стрелку назад в зависимости от значения,
     * возвращенного методом needsShowBackItem();
     */
    private void updateBackItem(PlaceholderFragment fragment) {
        mNavigationDrawerFragment.setDrawerIndicatorEnabled(fragment.needsShowMainMenuButton());
    }

    /**
     * Задание вида тулбара в зависимости от секции меню
     */
    public void hideDefaultTitle() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        PlaceholderFragment last = getLastFragment();
        if(last != null) {
            getMenuInflater().inflate(last.getMenuId(), menu);
            restoreMenu(last, menu);
        }

        return true;
    }

    /**
     * Задает вид для меню айтемов в тулбаре для текущего фрагмента.
     *
     * Логика задания вида описывается в методе setupToolbar у фрагмента.
     */
    public void restoreMenu(PlaceholderFragment fragment, Menu menu) {
        if (fragment != null) {
            fragment.setupToolbar(menu);
        }
    }

    public PlaceholderFragment getLastFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        int fragmentsCount = fragmentManager.getBackStackEntryCount();

        if(fragmentsCount == 0) {
            return null;
        }

        FragmentManager.BackStackEntry lastEntry = fragmentManager.getBackStackEntryAt(fragmentsCount - 1);
        String lastFragmentName = lastEntry.getName();
        return (PlaceholderFragment) fragmentManager.findFragmentByTag(lastFragmentName);
    }

    /**
     * Вызывается при нажатии кнопки назад.
     *
     * По умолчинию это действие закрывает самый верхний фрагмент из стека,
     * и восстанавливает предыдущий фрагмент.
     */
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.getBackStackEntryCount() > 1) {
            super.onBackPressed();

            PlaceholderFragment fragment = getLastFragment();

            if(fragment != null) {
                fragment.resumeFragment();
                updateBackItem(fragment);
            }
        } else {
            finish();
        }
    }

    /**
     * Закрывает верхний фрагмент
     */
    public void closeLastFragment() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            finish();
        }
    }

    /**
     * Выставляет заголовок экрана в зависимости от текущего экрана
     */
    public void updateTitle(PlaceholderFragment fragment) {
        String title = null;

        if(fragment == null) {
            title = getString(R.string.app_name);
        } else {
            title = fragment.getTitle();
        }

        if(title == null) {
            title = getString(R.string.app_name);
        }

        TextView titleTextView = getToolbarTitle();
        if(titleTextView != null) {
            titleTextView.setText(title);
        }
    }

    /**
     * Обновляет навигационное меню.
     */
    public void updateDrawer() {
        mNavigationDrawerFragment.showData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home && !getLastFragment().needsShowMainMenuButton()) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Этот метод должен вернуть фрагмент в зависимости от переданного индекса.
     * (Индекс возвращается по номеру элемента меню).
     */
    public PlaceholderFragment newFragmentInstance(int number) {
        return PlaceholderFragment.newInstance(number, createFragmentByNumber(number));
    }

    /**
     * Здесь нужно добавить все элементы дровера в массив items
     * для привязки кликов по ним.
     * @param drawerContainer - уже созданная разметка дровера
     */
    public abstract void initDrawerItems(HashMap<Integer, View> items, ViewGroup drawerContainer);

    /**
     * Здесь нужно вернуть id разметки активити
     */
    public int getActivityLayoutId() {
        return R.layout.activity_navigation;
    }

    /**
     * Создать фрагмент в зависимости от номера
     */
    public abstract PlaceholderFragment createFragmentByNumber(int number);

    /**
     * Этот метод должен вернуть главный фрагмент активити, который будет показан,
     * если не выбрано ни одного элемента меню.
     */
    public abstract PlaceholderFragment createMainFragment();

    /**
     * id фрагмента с дровером в разметке
     */
    public int getNavigationDrawerFragmentId() {
        return R.id.navigation_drawer;
    }

    /**
     * id виджета для активити с дровером в разметке
     */
    public int getNavigationDrawerLayoutWidgetId() {
        return R.id.drawer_layout;
    }

    /**
     * id фрагмента, куда будет помещаться контент
     */
    public int getContentFragmentId() {
        return R.id.content;
    }

    /**
     * id тулбара в разметке
     */
    public int getToolbarId() {
        return R.id.toolbar;
    }

    /**
     * Вернуть id файла с разметкой дровера
     */
    public abstract int getNavigationDrawerLayoutId();

    /**
     * Вернуть здесь TextView, в которой отображается заголовок тулбара
     */
    public TextView getToolbarTitle() {
        return (TextView) findViewById(R.id.toolbar_title);
    }
}
