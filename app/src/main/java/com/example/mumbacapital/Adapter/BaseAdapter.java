package com.example.mumbacapital.Adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chetu on 6/3/2018.
 */
public abstract class BaseAdapter {
    public abstract int getCount();

    public abstract Object getItem(int i);

    public abstract long getItemId(int i);

    public abstract View getView(int i, View view, ViewGroup viewGroup);
}
