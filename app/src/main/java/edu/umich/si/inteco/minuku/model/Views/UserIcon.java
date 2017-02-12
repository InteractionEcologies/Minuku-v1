package edu.umich.si.inteco.minuku.model.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.constants.UserIconReference;
import edu.umich.si.inteco.minuku.model.User;

/**
 * Created by tsung on 2017/2/11.
 */

public class UserIcon extends View {

    private View view;
    private LinearLayout userIconLayout;
    // View widgets
    private View selectedView;
    private TextView tvName;
    private ImageButton ibUser;
    // Function
    private UserIconReference userIconReference;

    public UserIcon(Context context, User user) {
        super(context);
        LayoutInflater li = LayoutInflater.from(context);
        view = li.inflate(R.layout.obj_user_icon, null);
        userIconReference = new UserIconReference(context);
        initVIew();
        setUserViews(user);
    }

    private void initVIew() {
        selectedView = (View) view.findViewById(R.id.obj_user_icon_selectedView);
        tvName = (TextView) view.findViewById(R.id.obj_user_icon_tv);
        ibUser = (ImageButton) view.findViewById(R.id.obj_user_icon_ib);
        userIconLayout = (LinearLayout) view.findViewById(R.id.obj_user_icon_layout);
        userIconLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setUserViews(User user) {
        tvName.setText(user.getUserName());
        ibUser.setImageDrawable(userIconReference.getIcon(user.getImgNumber()));
        if ("1".equalsIgnoreCase(user.getIfSelected())) {
            view.setBackgroundColor(getResources().getColor(R.color.brightblue));
        } else {
            view.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }
}
