package dpl.bobsun.dummypicloader.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import dpl.bobsun.dummypicloader.DummyPicLoader;
import dpl.bobsun.dummypicloader.R;
import dpl.bobsun.dummypicloader.utils.DisplayUtil;
import dpl.bobsun.dummypicloader.utils.ImageProvider;


/**
 * Created by bob.sun on 15/8/5.
 */
public class ImageAdapter extends ArrayAdapter {
    private ArrayList images;
    private boolean imagesStatus[];
    FrameLayout.LayoutParams cellParams;
    private ArrayList<String> selectedImages;
    public ImageAdapter(Context context, int resource) {
        super(context, resource);
        images = new ImageProvider(context).getAll();
        cellParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cellParams.width = DisplayUtil.getStaticInstance(null).getScreenWidth() / 3;;
        cellParams.height = DisplayUtil.getStaticInstance(null).getScreenWidth() / 3;;
        selectedImages = new ArrayList<>();
        imagesStatus = new boolean[images.size()];
    }

    @Override
    public int getCount(){
        return images.size();
    }

    @Override
    public String getItem(int position){
        return (String) images.get(position);
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent){
        View ret = convertView == null ? ((Activity) getContext()).getLayoutInflater().inflate(R.layout.layout_image_cell,parent,false) : convertView;
        ImageViewHolder imageViewHolder;
        if (convertView == null) {
            ImageView imageView = (ImageView) ret.findViewById(R.id.id_image);
            imageView.setLayoutParams(cellParams);
            imageViewHolder = new ImageViewHolder();
            imageViewHolder.checkBox = (CheckBox) ret.findViewById(R.id.id_image_selected);
            imageViewHolder.checkBox.setChecked(imagesStatus[index]);
            ret.setTag(imageViewHolder);
        } else {
            imageViewHolder = (ImageViewHolder) ret.getTag();
            imageViewHolder.checkBox.setChecked(imagesStatus[index]);
        }
        DummyPicLoader.getInstance(getContext()).resize(cellParams.width, cellParams.height).loadImageFromFile((String) images.get(index), (ImageView) ret.findViewById(R.id.id_image));
        return ret;
    }

    public ArrayList<String> getSelectedImages() {
        return selectedImages;
    }

    public void addSelectedImage(int position){
        this.selectedImages.add((String) images.get(position));
        imagesStatus[position] = true;
    }

    public void removeSelectedImage(int position){
        this.selectedImages.remove(images.get(position));
        imagesStatus[position] = false;
    }

    class ImageViewHolder{
        CheckBox checkBox;
    }
}
