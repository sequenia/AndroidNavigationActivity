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
 * Пример фрагмента из навигации
 */
public class SubFragment extends PlaceholderFragment {

    @Override
    public void onCreateViewCustom(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View view) {
        // Закрытие экрана по кнопке
        Button button = (Button) view.findViewById(R.id.back_button);
        if(button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((NavigationActivity) getActivity()).closeLastFragment();
                }
            });
        }
    }

    @Override
    public String getTitle() {
        return "Экран поверх остальных";
    }

    @Override
    public void setupToolbar(Menu menu) {

    }

    // Не нужно очищать стек при открытии - это фрагмент далекой навигации
    @Override
    public boolean clearStackBeforeOpen() {
        return false;
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
        return R.layout.fragment_sub;
    }
}
