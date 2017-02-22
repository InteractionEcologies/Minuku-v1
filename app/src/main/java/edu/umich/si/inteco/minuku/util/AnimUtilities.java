package edu.umich.si.inteco.minuku.util;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import edu.umich.si.inteco.minuku.R;

/**
 * Created by Tsung Wei Ho on 2/3/17.
 */

public class AnimUtilities {
    private static Context context;

    // Animation duration
    private int ANIM_DURATION = 800;

    public AnimUtilities(Context context) {
        this.context = context;
    }

    public void setTvAnimToVisible(final TextView textView) {
        textView.setVisibility(View.VISIBLE);
        Animation am = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        am.setDuration(ANIM_DURATION);
        textView.setAnimation(am);
        am.startNow();
    }
}
