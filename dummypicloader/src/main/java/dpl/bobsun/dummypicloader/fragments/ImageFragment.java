package dpl.bobsun.dummypicloader.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import dpl.bobsun.dummypicloader.DummyPicLoader;
import dpl.bobsun.dummypicloader.utils.DisplayUtil;

/**
 * Created by bob.sun on 15/10/21.
 */
public class ImageFragment extends Fragment {
    private String image;
    public ImageFragment(){
        super();
    }
    public void setImage(String filePath){
        image = filePath;
    }
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle savedInstanceState){
        View ret = new ImageView(this.getActivity());
        DummyPicLoader.getInstance(this.getActivity()).resize(DisplayUtil.getStaticInstance(this.getActivity()).getScreenWidth(),DisplayUtil.getStaticInstance(null).getScaledHeight()).loadImageFromFile(image, (ImageView) ret);
        return ret;
    }
}