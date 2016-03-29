package sun.bob.leela.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import sun.bob.leela.R;
import sun.bob.leela.db.Category;
import sun.bob.leela.db.CategoryHelper;
import sun.bob.leela.utils.ResUtil;

/**
 * Created by bob.sun on 16/3/24.
 */
public class CategorySpinnerAdapter extends ArrayAdapter {

    private ArrayList<Category> categories;

    public CategorySpinnerAdapter(Context context, int resource) {
        super(context, resource);
        this.categories = CategoryHelper.getInstance(context).getAllCategory();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == categories.size()) {
            AppCompatButton ret = new AppCompatButton(getContext());
            ret.setText("Add New Category");
            return ret;
        }
        CategoryViewHolder viewHolder;
        if (convertView == null || convertView instanceof AppCompatButton) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_category, parent, false);
            viewHolder = new CategoryViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CategoryViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new CategoryViewHolder();
            }
        }
        viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
        viewHolder.textView.setText(categories.get(position).getName());
        try {
            viewHolder.imageView.setImageBitmap(ResUtil.getInstance(null).getBmp(categories.get(position).getIcon()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount(){
        return categories.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == categories.size()){
            return null;
        }
        return categories.get(position);
    }
    class CategoryViewHolder {
        public TextView textView;
        public ImageView imageView;
    }
}
