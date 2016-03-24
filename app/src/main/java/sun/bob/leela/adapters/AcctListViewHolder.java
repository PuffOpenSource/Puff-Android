package sun.bob.leela.adapters;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.events.ItemUIEvent;

/**
 * Created by bob.sun on 16/3/19.
 */
public class AcctListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private View itemView;
    private boolean isExpanded = false;

    private static final int HEIGHT_SHORT  = 100;
    private static final int HEIGHT_EXPAND = 200;

    private float dp;

    public AcctListViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.itemView.setOnClickListener(this);
        EventBus.getDefault().register(this);
        dp = itemView.getContext().getResources().getDisplayMetrics().density;
    }

    public void configureWithAccount(Account account) {
        ((TextView) itemView.findViewById(R.id.list_name)).setText(account.getName());
        ((TextView) itemView.findViewById(R.id.list_account_name)).setText(account.getAccount_name());
        ((TextView) itemView.findViewById(R.id.list_name_thumb)).setText(account.getName());

        itemView.findViewById(R.id.id_item_content_expand).setVisibility(View.GONE);
        itemView.findViewById(R.id.id_item_content_thumb).setVisibility(View.VISIBLE);
//        ((TextView) itemView.findViewById(R.id.list_account_category)).setText(account.getCategory());
//        ((CardView) this.itemView.findViewById(R.id.list_item_card_view)).setCardBackgroundColor(Color.parseColor("#CDDC39"));
    }

    public void expand(){
        ValueAnimator valueAnimator;
        int toHeight;
        if (!isExpanded) {
            //Expand
            isExpanded = true;
            toHeight = (int) (HEIGHT_EXPAND * dp);
            valueAnimator = ValueAnimator.ofInt(this.itemView.getHeight(), toHeight);
            itemView.findViewById(R.id.id_item_content_expand).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.id_item_content_thumb).setVisibility(View.GONE);
        } else {
            //Shrink
            isExpanded = false;
            toHeight = (int) (HEIGHT_SHORT * dp);
            valueAnimator = ValueAnimator.ofInt(this.itemView.getHeight(), toHeight);
            itemView.findViewById(R.id.id_item_content_expand).setVisibility(View.GONE);
            itemView.findViewById(R.id.id_item_content_thumb).setVisibility(View.VISIBLE);
        }
        valueAnimator.setDuration(200);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                AcctListViewHolder.this.itemView.getLayoutParams().height = value.intValue();
                AcctListViewHolder.this.itemView.requestLayout();
            }
        });
        valueAnimator.start();
    }

    @Override
    public void onClick(View v) {
        this.expand();
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams layoutParams = this.itemView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            this.itemView.setLayoutParams(layoutParams);
        }
        layoutParams.height = height;
    }

    public void setHeightWithAnimate(int height) {
        ValueAnimator valueAnimator;
        int originHeight = this.itemView.getHeight();
        valueAnimator = ValueAnimator.ofInt(originHeight, height);
        valueAnimator.setDuration(200);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                AcctListViewHolder.this.itemView.getLayoutParams().height = value.intValue();
                AcctListViewHolder.this.itemView.requestLayout();
            }
        });
        valueAnimator.start();

    }

    public void onEventMainThread(ItemUIEvent event) {
//        int dp = (int) ResUtil.getInstance(null).getDP();
//        int height = this.itemView.getHeight();
//        if (this.getLayoutPosition() == 0) {
//            if (event.getDy() < 0) {
//                height += event.getDy();
//                if (height < 0) {
//                    height = 0;
//                }
//            } else if (height < 100 * dp){
//                height += event.getDy();
//                if (height > 100 * dp)
//                height = 100 * dp;
//            }
//        } else {
//            height = 100 * dp;
//        }
//        setHeightWithAnimate(height);
    }
}
