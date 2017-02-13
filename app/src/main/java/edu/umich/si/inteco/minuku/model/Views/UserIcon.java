package edu.umich.si.inteco.minuku.model.Views;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.constants.UserIconReference;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.fragments.ProfileFragment;
import edu.umich.si.inteco.minuku.model.User;

/**
 * Created by tsung on 2017/2/11.
 */

public class UserIcon extends View {

    private View view;
    private Context context;
    // View widgets
    private View selectedView;
    private TextView tvName;
    private ImageButton ibUser;
    // Function
    private UserIconReference userIconReference;

    public UserIcon(Context context, User user) {
        super(context);
        this.context = context;
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
    }

    private void setUserViews(User user) {
        tvName.setText(user.getUserName());
        ibUser.setImageDrawable(userIconReference.getIcon(user.getImgNumber()));
        Log.d("asfasfasf", user.getIfSelected());
        if ("1".equalsIgnoreCase(user.getIfSelected())) {
            selectedView.setBackgroundColor(getResources().getColor(R.color.brightblue));
        } else {
            selectedView.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    public ImageButton getIbUser() {
        return ibUser;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
