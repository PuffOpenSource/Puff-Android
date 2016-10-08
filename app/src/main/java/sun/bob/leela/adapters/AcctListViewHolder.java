package sun.bob.leela.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.db.AcctType;
import sun.bob.leela.db.Category;
import sun.bob.leela.db.CategoryHelper;
import sun.bob.leela.db.TypeHelper;
import sun.bob.leela.services.NotificationService;
import sun.bob.leela.ui.activities.DetailActivity;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.ClipboardUtil;
import sun.bob.leela.utils.CryptoUtil;
import sun.bob.leela.utils.ResUtil;
import sun.bob.leela.utils.StringUtil;

/**
 * Created by bob.sun on 16/3/19.
 */
public class AcctListViewHolder extends RecyclerView.ViewHolder{
    private View itemView;

    private float dp;

    private TextView name, accountName;
    private ImageView image;
    private Account account;
    private String iconStr;

    public AcctListViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        dp = itemView.getContext().getResources().getDisplayMetrics().density;
    }

    public void configureWithAccount(final Account account) {
        this.account = account;
        ((TextView) itemView.findViewById(R.id.list_name)).setText(account.getName());
        ((TextView) itemView.findViewById(R.id.list_account_name)).setText(account.getMasked_account());
        AcctType type = TypeHelper.getInstance(null).getTypeById(account.getType());
        ((TextView) itemView.findViewById(R.id.list_account_category)).setText(type.getName());

        iconStr = account.getIcon();
        if (!StringUtil.isNullOrEmpty(iconStr)) {
            Picasso.with(this.itemView.getContext())
                    .load(ResUtil.getInstance(null).getBmpUri(account.getIcon()))
                    .fit()
                    .config(Bitmap.Config.RGB_565)
                    .into((ImageView) itemView.findViewById(R.id.list_account_image));
        } else {
            iconStr = TypeHelper.getInstance(null).getTypeById(account.getType()).getIcon();
            Uri icon = ResUtil.getInstance(null)
                .getBmpUri(iconStr);
            Picasso.with(this.itemView.getContext())
                    .load(icon)
                    .fit()
                    .config(Bitmap.Config.RGB_565)
                    .into((ImageView) itemView.findViewById(R.id.list_account_image));
        }


        this.itemView.findViewById(R.id.view_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CryptoUtil(itemView.getContext(), new CryptoUtil.OnDecryptedListener() {
                    @Override
                    public void onDecrypted(String account, String passwd, String addt) {
                        Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                        ArrayList<String> list = new ArrayList();
                        list.add(account);
                        list.add(passwd);
                        list.add(addt);
                        intent.putStringArrayListExtra("credentials", list);
                        intent.putExtra("account", AcctListViewHolder.this.account.getId());
                        itemView.getContext().startActivity(intent);
                        updateAccess();
                    }
                }).runDecrypt(account.getAccount(), account.getHash(), account.getAdditional(),
                        account.getAccount_salt(), account.getSalt(), account.getAdditional_salt());
            }
        });

        this.itemView.findViewById(R.id.pin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CryptoUtil(itemView.getContext(), new CryptoUtil.OnDecryptedListener() {
                    @Override
                    public void onDecrypted(String account, String passwd, String addt) {
                        Intent intent = new Intent(itemView.getContext(), NotificationService.class);

                        itemView.getContext().stopService(intent);

                        intent.setAction(AppConstants.SERVICE_CMD_START);

                        intent.putExtra("name", AcctListViewHolder.this.account.getName());
                        intent.putExtra("account", account);
                        intent.putExtra("password", passwd);
                        intent.putExtra("additional", addt);
                        intent.putExtra("icon", iconStr);

                        itemView.getContext().startService(intent);
                        Snackbar.make(itemView,"Pinned!", Snackbar.LENGTH_SHORT).show();
                        updateAccess();
                    }
                }).runDecrypt(account.getAccount(), account.getHash(), account.getAdditional(),
                        account.getAccount_salt(), account.getSalt(), account.getAdditional_salt());
            }
        });

        this.itemView.findViewById(R.id.copy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CryptoUtil(itemView.getContext(), new CryptoUtil.OnDecryptedListener() {
                    @Override
                    public void onDecrypted(String account, String passwd, String addt) {
                        updateAccess();
                        ClipboardUtil.getInstance(itemView.getContext()).setText(passwd);
                        Snackbar.make(itemView,"Copied!", Snackbar.LENGTH_SHORT).show();
                    }
                }).runDecrypt(account.getAccount(), account.getHash(), account.getAdditional(),
                        account.getAccount_salt(), account.getSalt(), account.getAdditional_salt());
            }
        });
//        ((TextView) itemView.findViewById(R.id.list_account_category)).setText(account.getCategory());
//        ((CardView) this.itemView.findViewById(R.id.list_item_card_view)).setCardBackgroundColor(Color.parseColor("#CDDC39"));
    }

    private void updateAccess() {
        account.setLast_access(Calendar.getInstance().getTimeInMillis());
        AccountHelper.getInstance(null).saveAccount(account);
    }

//    public void setHeight(int height) {
//        ViewGroup.LayoutParams layoutParams = this.itemView.getLayoutParams();
//        if (layoutParams == null) {
//            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            this.itemView.setLayoutParams(layoutParams);
//        }
//        layoutParams.height = height;
//    }

//    public void setHeightWithAnimate(int height) {
//        ValueAnimator valueAnimator;
//        int originHeight = this.itemView.getHeight();
//        valueAnimator = ValueAnimator.ofInt(originHeight, height);
//        valueAnimator.setDuration(200);
//        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            public void onAnimationUpdate(ValueAnimator animation) {
//                Integer value = (Integer) animation.getAnimatedValue();
//                AcctListViewHolder.this.itemView.getLayoutParams().height = value.intValue();
//                AcctListViewHolder.this.itemView.requestLayout();
//            }
//        });
//        valueAnimator.start();
//
//    }
}
