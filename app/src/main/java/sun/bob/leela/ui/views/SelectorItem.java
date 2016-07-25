package sun.bob.leela.ui.views;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;

import com.kenumir.materialsettings.MaterialSettingsItem;

import sun.bob.leela.R;

/**
 * Created by bob.sun on 16/7/25.
 */
public class SelectorItem extends MaterialSettingsItem {

    private SpinnerAdapter adapter;
    private int selectedPos = -1;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;

    public SelectorItem(Context ctx, String name) {
        super(ctx, name);
    }

    @Override
    public int getViewResource() {
        return R.layout.layout_selector_item;
    }

    @Override
    public void setupView(View view) {
        AppCompatSpinner spinner = (AppCompatSpinner)view.findViewById(R.id.settings_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
        if (selectedPos != -1)
            spinner.setSelection(selectedPos);
    }

    @Override
    public void save() {

    }

    public SpinnerAdapter getAdapter() {
        return adapter;
    }

    public SelectorItem setAdapter(SpinnerAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public AdapterView.OnItemSelectedListener getOnItemClickListener() {
        return onItemSelectedListener;
    }

    public SelectorItem setOnItemClickListener(AdapterView.OnItemSelectedListener onItemClickListener) {
        this.onItemSelectedListener = onItemClickListener;
        return this;
    }

    public void setSelectedPos(int pos) {
        selectedPos = pos;
    }
}
