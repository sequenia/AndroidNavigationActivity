package com.navigationactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.navigationactivity.navigation.NavigationActivity;
import com.navigationactivity.navigation.PlaceholderFragment;

/**
 * Created by chybakut2004 on 04.08.15.
 *
 * Пример секции меню
 */
public class MenuFragment extends PlaceholderFragment {
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
        return R.layout.fragment_menu;
    }

    @Override
    public void onCreateViewCustom(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View view) {
        Button button = (Button) view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceholderFragment subFragment = PlaceholderFragment.newInstance(MainActivity.SUB_FRAGMENT, new SubFragment());

                Bundle args = subFragment.getArguments();
                args.putString("key", "value");

                ((NavigationActivity) getActivity()).addSubFragment(subFragment);
            }
        });
    }
}
