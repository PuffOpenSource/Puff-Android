package sun.bob.leela.adapters;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

    private static final int HEIGHT_SHORT  = 50;
    private static final int HEIGHT_EXPAND = 150;

    private float dp;

    public AcctListViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.itemView.setOnClickListener(this);
        EventBus.getDefault().register(this);
        dp = itemView.getContext().getResources().getDisplayMetrics().density;
    }

    public void configureWithAccount(Account account) {
        ((TextView) itemView.findViewById(R.id.list_account_name)).setText(account.getName());
    }

    public void expand(){
        ValueAnimator valueAnimator;
        int toHeight;
        if (!isExpanded) {
            this.itemView.setVisibility(View.VISIBLE);
            this.itemView.setEnabled(true);
            isExpanded = true;
            toHeight = (int) (HEIGHT_EXPAND * dp);
            valueAnimator = ValueAnimator.ofInt(this.itemView.getHeight(), toHeight);
        } else {
            isExpanded = false;
            toHeight = HEIGHT_SHORT;
            valueAnimator = ValueAnimator.ofInt(this.itemView.getHeight(), toHeight);
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

    public void onEventMainThread(ItemUIEvent event) {
        int height = this.itemView.getHeight();
        if (this.getLayoutPosition() == 0) {
            if (event.getDy() < 0) {
                height += event.getDy();
            } else if (height < 50){
                height += event.getDy();
                if (height > 50)
                height = 50;
            }
        } else {
            height = 100;
        }

//        setHeight(height);
    }
}
