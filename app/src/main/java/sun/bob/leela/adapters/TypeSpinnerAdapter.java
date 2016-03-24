package sun.bob.leela.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import sun.bob.leela.R;
import sun.bob.leela.db.AcctType;
import sun.bob.leela.db.TypeHelper;

/**
 * Created by bob.sun on 16/3/24.
 */
public class TypeSpinnerAdapter extends ArrayAdapter {

    private ArrayList<AcctType> types;

    public TypeSpinnerAdapter(Context context, int resource) {
        super(context, resource);
        types = TypeHelper.getInstance(context).getAllTypes();
    }

    public TypeSpinnerAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        types = TypeHelper.getInstance(context).getAllTypes();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AcctType type = this.types.get(position);
        TypeViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_category, parent, false);
            viewHolder = new TypeViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TypeViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new TypeViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
            }
        }
        viewHolder.textView.setText(type.getName());
        try {
            viewHolder.imageView.setImageBitmap(BitmapFactory.decodeStream(getContext().getAssets().open(type.getIcon())));
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
        return types.size();
    }

    class TypeViewHolder {
        public ImageView imageView;
        public TextView textView;
    }
}
