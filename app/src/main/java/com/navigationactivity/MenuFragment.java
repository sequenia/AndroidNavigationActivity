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
    public void onCreateViewCustom(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View view) {
        Button button = (Button) view.findViewById(R.id.button);

        // Пример открытия фрагмента по нажатию кнопки
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceholderFragment subFragment = ((NavigationActivity) getActivity()).newFragmentInstance(MainActivity.SUB_FRAGMENT);

                Bundle args = subFragment.getArguments();
                args.putString("key", "value");

                ((NavigationActivity) getActivity()).addSubFragment(subFragment);
            }
        });
    }

    @Override
    public String getTitle() {
        return String.format("SECTION %d", getNumber());
    }

    @Override
    public void setupToolbar(Menu menu) {

    }

    // Является фрагментом в меню дровера. При открытии нужно очистить стек фрагментов
    @Override
    public boolean clearStackBeforeOpen() {
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
}
