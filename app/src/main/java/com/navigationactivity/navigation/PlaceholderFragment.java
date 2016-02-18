package com.navigationactivity.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by chybakut2004 on 13.07.15.
 *
 * Базовый фрагмент, использующийся в навигации
 */
public abstract class PlaceholderFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    // Данные, поступающие от других фрагментов
    private HashMap<String, Object> messages;

    public PlaceholderFragment() {

    }

    /**
     * Задает фрагменту обязательные аргументы (Номер).
     */
    public static PlaceholderFragment newInstance(int sectionNumber, PlaceholderFragment fragment) {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Отправляет сообщение в другой фрагмент
     * @param fragmentNumber  номер фрагмента, в который передать сообщение
     * @param key ключ, под которым передается сообщение
     * @param message сообщение
     */
    public void sendMessage(int fragmentNumber, String key, Object message) {
        FragmentActivity activity = getActivity();
        if(activity != null) {
            Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(String.valueOf(fragmentNumber));
            if(fragment != null && fragment instanceof PlaceholderFragment) {
                ((PlaceholderFragment) fragment).addMessage(key, message);
            }
        }
    }

    // Принимает сообщения
    public void acceptMessages() {
        if(hasMessages()) {
            onMessagesReceived(messages);
            clearMessages();
        }
    }

    public boolean hasMessages() {
        return messages != null;
    }

    /**
     * Добавляет сообщение
     */
    public void addMessage(String key, Object message) {
        if(messages == null) {
            messages = new HashMap<>();
        }

        messages.put(key, message);
    }

    /**
     * Очищает сообщения
     */
    public void clearMessages() {
        messages = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayoutId(), container, false);
        onCreateViewCustom(inflater, container, savedInstanceState, view);
        return view;
    }

    public String getName() {
        return String.valueOf(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public boolean isInNavigation() {
        return true;
    }

    public int getNumber() {
        return getArguments().getInt(ARG_SECTION_NUMBER);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Срабатывает при приеме сообщения фрагментом во время его повторного появления на экране
     * @param messages
     */
    public void onMessagesReceived(HashMap<String, Object> messages) {

    }

    /**
     * Настройка иконок тулбара в зависимости от открытого фрагмента.
     *
     * Здесь нужно настроить видимость кнопок в тулбаре
     */
    public abstract void setupToolbar(Menu menu);

    /**
     * Вернуть true, если при открытии фрагмента нужно очистить стек фрагментов
     * @return
     */
    public abstract boolean clearStackBeforeOpen();

    /**
     * Скрывать предыдущий фрагмент или нет.
     * (Не нужно скрывать, если фрагмент является диалогом)
     * @return
     */
    public boolean hidePrevFragment() {
        return true;
    }

    /**
     * true, если нужно показать кнопку главного меню.
     * false, если нужно показать кнопку назад.
     * @return
     */
    public abstract boolean needsShowMainMenuButton();

    /**
     * Вызывается при нажатии кнопки НАЗАД при возвращении к фрагменту.
     */
    public abstract void resumeFragment();

    /**
     * id разметки для фрагмента
     * @return
     */
    public abstract int getFragmentLayoutId();

    /**
     * вызывается при создании вьюшки фрагмента
     * @param inflater
     * @param container
     * @param savedInstanceState
     */
    public abstract void onCreateViewCustom(LayoutInflater inflater, ViewGroup container,
                                            Bundle savedInstanceState, View view);

    /**
     * @return Возвращает заголовок, который отобразитс
     */
    public abstract String getTitle();

    public abstract int getMenuId();
}