package sun.bob.leela.adapters;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import sun.bob.leela.utils.UserDefault;

/**
 * Created by bob.sun on 16/7/25.
 */
public class SettingsSpinnerAdapter implements SpinnerAdapter {

    private String[] displayText = {"3x3", "4x4"};
    private long[] ids = {UserDefault.v3x3, UserDefault.v4x4};
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        AppCompatTextView ret = new AppCompatTextView(parent.getContext());
        ret.setText(displayText[position]);
        ret.setTextSize(16);
        ret.setPadding(0, 10, 0, 10);
        ret.setTextColor(Color.BLACK);
        return ret;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return displayText[position];
    }

    @Override
    public long getItemId(int position) {
        return ids[position];
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppCompatTextView ret = new AppCompatTextView(parent.getContext());
        ret.setText(displayText[position]);
        ret.setTextSize(16);
        ret.setPadding(0, 10, 0, 10);
        ret.setTextColor(Color.BLACK);
        return ret;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
