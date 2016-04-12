package sun.bob.leela.adapters;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.events.ItemUIEvent;
import sun.bob.leela.utils.CryptoUtil;

/**
 * Created by bob.sun on 16/3/19.
 */
public class AcctListViewHolder extends RecyclerView.ViewHolder{
    private View itemView;

    private float dp;

    private TextView name, accountName;

    public AcctListViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        dp = itemView.getContext().getResources().getDisplayMetrics().density;
    }

    public void configureWithAccount(final Account account) {
        ((TextView) itemView.findViewById(R.id.list_name)).setText(account.getName());
        ((TextView) itemView.findViewById(R.id.list_account_name)).setText(account.getMasked_account());

        this.itemView.findViewById(R.id.view_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CryptoUtil(v.getContext(), new CryptoUtil.OnDecryptedListener() {
                    @Override
                    public void onDecrypted(String account, String passwd, String addt) {
                        Log.e("LEELA", account + "|" + passwd + "|" + addt);
                    }
                }).runDecrypt(account.getAccount(), account.getHash(), account.getAdditional(),
                        account.getAccount_salt(), account.getSalt(), account.getAdditional_salt());
            }
        });
//        ((TextView) itemView.findViewById(R.id.list_account_category)).setText(account.getCategory());
//        ((CardView) this.itemView.findViewById(R.id.list_item_card_view)).setCardBackgroundColor(Color.parseColor("#CDDC39"));
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
