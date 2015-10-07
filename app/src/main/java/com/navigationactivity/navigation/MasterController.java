package com.navigationactivity.navigation;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chybakut2004 on 04.08.15.
 *
 * Контроллер для мастер фрагмента
 */
public abstract class MasterController {

    private RecyclerView recyclerView;

    public MasterController(View fragmentView) {

    }

    public abstract int getRecyclerViewId();
}
