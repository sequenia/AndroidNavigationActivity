package com.navigationactivity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by chybakut2004 on 12.07.15.
 *
 * Активити с нафигацией с помощью фрагментов
 */
public abstract class NavigationActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    // Дровер
    private NavigationDrawerFragment mNavigationDrawerFragment;

    // Заголовок
    private CharSequence mTitle;

    // Стек фрагментов. Используется для навигации между фрагментами.
    private Stack<PlaceholderFragment> fragmentStack;

    private Menu menu; // Меню с элементами управления на тулбаре.

    // Заголовки экранов
    private HashMap<Integer, String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutId());

        Toolbar toolbar = (Toolbar) findViewById(getToolbarId());
        setSupportActionBar(toolbar);

        // Заполнение списка заголовков
        titles = new HashMap<Integer, String>();
        initTitles(titles);

        // Инициализация стека фрагментов
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentStack = new Stack<PlaceholderFragment>();

        // Помещаем главный фрагмент в стек
        PlaceholderFragment mapFragment = createMainFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.content, mapFragment);
        fragmentStack.push(mapFragment);
        ft.commit();

        // Настройка навигационного меню
        mNavigationDrawerFragment = createNavigationDrawer();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(getNavigationDrawerFragmentId(), mNavigationDrawerFragment)
                .commit();

        mTitle = getTitle();
    }

    private NavigationDrawerFragment createNavigationDrawer() {

        final NavigationActivity activity = this;

        return new NavigationDrawerFragment() {

            @Override
            public void showData() {
                activity.showNavigationDrawerData(mNavigationDrawerFragment.getDrawerContainer());
            }

            @Override
            public int getNavigationDrawerLayoutId() {
                return activity.getNavigationDrawerLayoutId();
            }

            @Override
            public int getNavigationDrawerFragmentId() {
                return activity.getNavigationDrawerFragmentId();
            }

            @Override
            public int getNavigationDrawerLayoutWidgetId() {
                return activity.getNavigationDrawerLayoutWidgetId();
            }

            @Override
            public void initDrawerItems(HashMap<Integer, View> items, ViewGroup drawerContainer) {
                activity.initDrawerItems(items, drawerContainer);
            }

            @Override
            public TextView getToolbarTitle() {
                return activity.getToolbarTitle();
            }

            @Override
            public int getGlobalMenuId() {
                return activity.getGlobalMenuId();
            }
        };
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
     *
     * @param fragment
     */
    public void addSubFragment(PlaceholderFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.add(getContentFragmentId(), fragment); // Добавляем фрагмент на экран

        // Если фрагмент является секцией меню, то очищаем стек, перед тем как добавить его.
        if(fragment.isDrawerElement()) {
            while (fragmentStack.size() > 1) {
                fragmentStack.lastElement().onPause();  // Останавливаем верхний фрагмент
                ft.remove(fragmentStack.pop());         // Удаляем его с экрана и из стека
            }
        }

        // Ставим верхний фрагмент на паузу
        PlaceholderFragment last = fragmentStack.lastElement();
        last.onPause(); // Останавливаем предыдущий фрагмент
        if (fragment.hidePrevFragment()) {
            ft.hide(last);  // Скрываем предыдущий фрагмент с экрана
        } else {
            ft.show(last);
        }

        fragmentStack.push(fragment); // Добавляем новый фрагмент в стек

        ft.commit();

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
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        TextView title = getToolbarTitle();
        if(title != null) {
            getToolbarTitle().setText(mTitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(getMenuId(), menu);
        restoreActionBar();
        this.menu = menu;
        restoreMenu();

        return true;
    }

    /**
     * Задает вид для меню айтемов в тулбаре для текущего фрагмента.
     *
     * Логика задания вида описывается в методе restoreMenu у фрагмента.
     */
    public void restoreMenu() {
        PlaceholderFragment fragment = fragmentStack.lastElement();
        if (fragment != null) {
            fragment.restoreMenu(menu);
        }
    }

    /**
     * Вызывается при нажатии кнопки назад.
     *
     * По умолчинию это действие закрывает самый верхний фрагмент из стека,
     * и восстанавливает предыдущий фрагмент.
     */
    @Override
    public void onBackPressed() {
        if (fragmentStack.size() > 1) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            fragmentStack.lastElement().onPause();  // Останавливаем текущий фрагмент
            ft.remove(fragmentStack.pop());         // Удаляем его с экрана и из стека
            fragmentStack.lastElement().onResume(); // Возобновляем предыдущий фрагмент
            ft.show(fragmentStack.lastElement());   // Показываем предыдущий фрагмент на экране

            ft.commit();

            // Настройка тулбара для предыдущего фрагмента
            PlaceholderFragment currentFragment = fragmentStack.lastElement();
            currentFragment.resumeFragment();
            onSectionAttached(currentFragment.getNumber());
            updateBackItem(currentFragment);
            restoreActionBar();
            restoreMenu();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Показывает меню айтем в тулбаре с id = itemId
     * @param itemId
     */
    public void showMenuItem(int itemId) {
        setMenuItemVisibility(itemId, true);
    }

    /**
     * Скрывает меню айтем с тулбара
     * @param itemId
     */
    public void hideMenuItem(int itemId) {
        setMenuItemVisibility(itemId, false);
    }

    /**
     * Задает видимость меню айтема
     * @param itemId
     * @param isVisible
     */
    public void setMenuItemVisibility(int itemId, boolean isVisible) {
        if(menu != null) {
            MenuItem item = menu.findItem(itemId);
            if(item != null) {
                item.setVisible(isVisible);
            }
        }
    }

    /**
     * Вызывается после того, как фрагмент будет помещен в активити.
     *
     * По умолчанию после этого в тулбар выставляется заголовок для текущего фрагмента.
     *
     * @param number
     */
    public void onSectionAttached(int number) {
        mTitle = titles.get(number);

        if(mTitle == null) {
            mTitle = getString(R.string.app_name);
        }

        TextView title = getToolbarTitle();
        if(title != null) {
            getToolbarTitle().setText(mTitle);
        }
        fragmentStack.lastElement().restoreMenu(menu);
    }

    /**
     * Обновляет навигационное меню.
     */
    public void updateDrawer() {
        mNavigationDrawerFragment.showData();
    }

    public Menu getMenu() {
        return menu;
    }

    public Stack<PlaceholderFragment> getFragmentStack() {
        return fragmentStack;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home && !fragmentStack.lastElement().needsShowMainMenuButton()) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Этот метод должен вернуть фрагмент в зависимости от переданного индекса.
     * (Индекс возвращается по номеру элемента меню).
     *
     * @param number
     * @return
     */
    public PlaceholderFragment newFragmentInstance(int number) {
        return PlaceholderFragment.newInstance(number, createFragmentByNumber(number));
    }

    /**
     * Нужно заполнить контейнер titles заголовками,
     * привязав каждый к значению Integer.
     *
     * Эти значения будут так же использоваться для создания экземпляров фрагментов.
     * @param titles
     */
    public abstract void initTitles(HashMap<Integer, String> titles);

    /**
     * Здесь нужно добавить все элементы дровера в массив items
     * для привязки кликов по ним.
     * @param drawerContainer - уже созданная разметка дровера
     * @param items
     */
    public abstract void initDrawerItems(HashMap<Integer, View> items, ViewGroup drawerContainer);

    /**
     * Здесь нужно вернуть id разметки активити
     * @return
     */
    public abstract int getActivityLayoutId();

    /**
     * Создать фрагмент в зависимости от номера
     * @param number
     * @return
     */
    public abstract PlaceholderFragment createFragmentByNumber(int number);

    /**
     * Этот метод должен вернуть главный фрагмент активити, который будет показан,
     * если не выбрано ни одного элемента меню.
     * @return
     */
    public abstract PlaceholderFragment createMainFragment();

    /**
     * id фрагмента с дровером в разметке
     * @return
     */
    public abstract int getNavigationDrawerFragmentId();

    /**
     * id виджета для активити с дровером в разметке
     * @return
     */
    public abstract int getNavigationDrawerLayoutWidgetId();

    /**
     * id фрагмента, куда будет помещаться контент
     * @return
     */
    public abstract int getContentFragmentId();

    /**
     * id тулбара в разметке
     * @return
     */
    public abstract int getToolbarId();

    /**
     * Вернуть id разметки дровера
     * @return
     */
    public abstract int getNavigationDrawerLayoutId();

    /**
     * id глобального меню
     * @return
     */
    public abstract int getGlobalMenuId();

    /**
     * id меню
     * @return
     */
    public abstract int getMenuId();

    /**
     * Вернуть здесь TextView, в которой отображается заголовок тулбара
     * @return
     */
    public abstract TextView getToolbarTitle();

    /**
     * Здесь можно показать данные в дровере (Имя пользователя, аватар и т.д.)
     */
    public abstract void showNavigationDrawerData(ViewGroup drawerContainer);
}
