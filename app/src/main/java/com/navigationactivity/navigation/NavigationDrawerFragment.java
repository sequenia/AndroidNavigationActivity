package com.navigationactivity.navigation;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navigationactivity.R;

import java.util.HashMap;

/**
 * Created by chybakut2004 on 13.07.15.
 *
 * Боковое меню.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Запоминаем позицию выбранного элемента меню
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * В первый запуск приложения дровер должен быть открыт
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * Указатель на текущий экземпляр колбэка (активити)
     */
    private NavigationDrawerCallbacks mCallbacks;

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ViewGroup mDrawerContainer;
    private View mFragmentContainerView;

    public static final int NO_SELECTED = -1;

    private int mCurrentSelectedPosition = NO_SELECTED;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    // Список вьюшек для элементов меню. К ним будут привязываться клики
    private HashMap<Integer, View> items;

    public NavigationDrawerFragment() {
        items = new HashMap<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDrawerContainer = (ViewGroup) inflater.inflate(
                getNavigationDrawerLayoutId(), container, false);

        initDrawerItems(items, mDrawerContainer);

        for(Integer key : items.keySet()) {
            final int localKey = key;
            items.get(key).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItem(localKey);
                }
            });
        }

        showData();

        setUp(getNavigationDrawerFragmentId(),
                (DrawerLayout) getActivity().findViewById(getNavigationDrawerLayoutWidgetId()));

        setSelectedDrawerItem(mCurrentSelectedPosition);

        return mDrawerContainer;
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its fragment's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                //getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                //getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        NavigationActivity activity = (NavigationActivity) getActivity();
        PlaceholderFragment lastFragment = activity.getLastFragment();
        if(lastFragment != null) {
            setDrawerIndicatorEnabled(lastFragment.needsShowMainMenuButton());
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     * Выбирает элемент меню
     */
    private void selectItem(int position) {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    public void setSelectedDrawerItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerContainer != null && mCallbacks != null) {
            for(Integer key : items.keySet()) {
                if(key == position) {
                    mCallbacks.selectDrawerItem(items.get(key));
                } else {
                    mCallbacks.deselectDrawerItem(items.get(key));
                }
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        /*if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(getGlobalMenuId(), menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        TextView title = getToolbarTitle();
        if(title != null) {
            title.setText(R.string.app_name);
        }
    }

    private ActionBar getActionBar() {
        return ((NavigationActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
        void showNavigationDrawerData(ViewGroup drawerContainer);
        int getNavigationDrawerLayoutId();
        int getNavigationDrawerFragmentId();
        int getNavigationDrawerLayoutWidgetId();
        void selectDrawerItem(View menuItem);
        void deselectDrawerItem(View menuItem);
        void initDrawerItems(HashMap<Integer, View> items, ViewGroup drawerContainer);
        TextView getToolbarTitle();
    }

    public void setDrawerIndicatorEnabled(boolean enabled) {
        mDrawerToggle.setDrawerIndicatorEnabled(enabled);
    }

    public ViewGroup getDrawerContainer() {
        return mDrawerContainer;
    }

    /**
     * Здесь можно модифицировать дровер под разные нужды,
     * например выводить аватар и имя пользователя.
     */
    public void showData() {
        mCallbacks.showNavigationDrawerData(getDrawerContainer());
    }

    /**
     * Здесь нужно вернуть id разметки для дровера
     * @return
     */
    public int getNavigationDrawerLayoutId() {
        return mCallbacks.getNavigationDrawerLayoutId();
    }

    /**
     * Здесь нужно вернуть id фрагмента для дровера в разметке
     * @return
     */
    public int getNavigationDrawerFragmentId() {
        return mCallbacks.getNavigationDrawerFragmentId();
    }

    /**
     * id виджета Drawer Layout в разметке активити
     * @return
     */
    public int getNavigationDrawerLayoutWidgetId() {
        return mCallbacks.getNavigationDrawerLayoutWidgetId();
    }

    /**
     * Здесь нужно добавить все элементы меню в массив items
     * для привязки кликов по ним.
     * @param drawerContainer - уже созданная разметка дровера
     * @param items
     */
    public void initDrawerItems(HashMap<Integer, View> items, ViewGroup drawerContainer) {
        mCallbacks.initDrawerItems(items, drawerContainer);
    }

    /**
     * Здесь нужно вернуть TextView, являющийся заголовком тулбара.
     * @return
     */
    public TextView getToolbarTitle() {
        return mCallbacks.getToolbarTitle();
    }
}