package com.navigationactivity.fragments;

import android.widget.Toast;

import com.navigationactivity.MainApplication;
import com.navigationactivity.RequestCodes;
import com.navigationactivity.mvc.MVC;
import com.navigationactivity.navigation.NavigationActivity;
import com.navigationactivity.navigation.PlaceholderFragment;

/**
 * Фрагмент, от которого нужно наследовать все фрагменты приложения.
 * В нем реализованы старнартные методы для View в паттерне MVC
 * Created by chybakut2004 on 28.03.16.
 */
public abstract class AppFragment extends PlaceholderFragment implements MVC.ViewOperations {

    @Override
    public void showMessage(String text, int requestCode) {
        Toast.makeText(MainApplication.getContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String text) {
        showMessage(text, RequestCodes.CODE_MESSAGE);
    }

    @Override
    public void close() {
        ((NavigationActivity) getActivity()).closeLastFragment();
    }
}
