package sun.bob.leela.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.Category;
import sun.bob.leela.utils.ResUtil;

/**
 * Created by bob.sun on 16/5/11.
 */
public class SimpleListViewHolder extends RecyclerView.ViewHolder {

    public enum SimpleListViewType {
        SimpleListViewTypeCategory,
        SimpleListViewTypeAccount,
    }

    private SimpleListViewType type;
    private View itemView;
    private int index;
    public SimpleListDelegate delegate;

    public SimpleListViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void configureWithAccount(Account account, int index) {
        this.type = SimpleListViewType.SimpleListViewTypeAccount;
        ((AppCompatTextView) itemView.findViewById(R.id.account_name)).setText(account.getName());
        Picasso.with(itemView.getContext()).load(ResUtil.getInstance(itemView.getContext().getApplicationContext()).getBmpUri(account.getIcon()))
                .fit()
                .config(Bitmap.Config.RGB_565)
                .into((ImageView) itemView.findViewById(R.id.account_image));
        this.index = index;
    }

    public void configureWithCategory(Category category, int index) {
        this.type = SimpleListViewType.SimpleListViewTypeCategory;
        ((AppCompatTextView) itemView.findViewById(R.id.account_name)).setText(category.getName());
        Picasso.with(itemView.getContext()).load(ResUtil.getInstance(itemView.getContext().getApplicationContext()).getBmpUri(category.getIcon()))
                .fit()
                .config(Bitmap.Config.RGB_565)
                .into((ImageView) itemView.findViewById(R.id.account_image));
        this.index = index;
    }

    public SimpleListViewType getType() {
        return type;
    }

    public void onClick() {
        this.delegate.onClick(type, index);
    }

    interface SimpleListDelegate {
        void onClick(SimpleListViewType type, int index);
    }
}
