package edu.umich.si.inteco.minuku.constants;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Map;

import edu.umich.si.inteco.minuku.R;

/**
 * Created by tsung on 2017/2/10.
 */

public class UserIconReference {
    private Context context;
    private Map<String, Drawable> userIcons;

    public UserIconReference(Context context) {
        this.context = context;
        initIconReference();

    }

    private void initIconReference() {
        userIcons = new HashMap<String, Drawable>() {
            {
                put("0", context.getResources().getDrawable(R.drawable.fragment_profile_icon1));
                put("1", context.getResources().getDrawable(R.drawable.fragment_profile_icon2));
                put("2", context.getResources().getDrawable(R.drawable.fragment_profile_icon3));
                put("3", context.getResources().getDrawable(R.drawable.fragment_profile_icon4));
                put("4", context.getResources().getDrawable(R.drawable.fragment_profile_icon5));
                put("5", context.getResources().getDrawable(R.drawable.fragment_profile_icon6));
                put("6", context.getResources().getDrawable(R.drawable.fragment_profile_icon7));
                put("7", context.getResources().getDrawable(R.drawable.fragment_profile_icon8));
                put("8", context.getResources().getDrawable(R.drawable.fragment_profile_icon9));
                put("9", context.getResources().getDrawable(R.drawable.fragment_profile_icon10));
                put("10", context.getResources().getDrawable(R.drawable.fragment_profile_icon11));
                put("11", context.getResources().getDrawable(R.drawable.fragment_profile_icon12));
                put("12", context.getResources().getDrawable(R.drawable.fragment_profile_icon13));
                put("13", context.getResources().getDrawable(R.drawable.fragment_profile_icon14));
                put("14", context.getResources().getDrawable(R.drawable.fragment_profile_icon15));
                put("15", context.getResources().getDrawable(R.drawable.fragment_profile_icon16));
                put("16", context.getResources().getDrawable(R.drawable.fragment_profile_icon17));
                put("17", context.getResources().getDrawable(R.drawable.fragment_profile_icon18));
                put("18", context.getResources().getDrawable(R.drawable.fragment_profile_icon19));
                put("19", context.getResources().getDrawable(R.drawable.fragment_profile_icon20));
                put("20", context.getResources().getDrawable(R.drawable.fragment_profile_icon21));
                put("21", context.getResources().getDrawable(R.drawable.fragment_profile_icon22));
                put("22", context.getResources().getDrawable(R.drawable.fragment_profile_icon23));
                put("23", context.getResources().getDrawable(R.drawable.fragment_profile_icon24));
            }
        };
    }

    public Map<String, Drawable> getIconsReference() {
        return userIcons;
    }

    public Drawable getIcon(String iconRef) {
        return userIcons.get(iconRef);
    }
}
