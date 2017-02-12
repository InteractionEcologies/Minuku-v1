package edu.umich.si.inteco.minuku.model.Views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.adapters.UserIconAdapter;
import edu.umich.si.inteco.minuku.constants.UserIconReference;

/**
 * Created by tsung on 2017/2/11.
 */

public class DialogUserIcon extends View {
    private View view;
    private Context context;
    private ImageView imageView;
    private View selectedView;
    private String imgNum;
    private UserIconAdapter userIconAdapter;

    public DialogUserIcon(Context context, String imgNum, UserIconAdapter userIconAdapter) {
        super(context);
        this.context = context;
        this.imgNum = imgNum;
        this.userIconAdapter = userIconAdapter;
        LayoutInflater li = LayoutInflater.from(context);
        view = li.inflate(R.layout.obj_dialog_user_icon, null);
        initVIew();
    }

    private void initVIew() {
        imageView = (ImageView) view.findViewById(R.id.obj_user_dialog_icon_iv);
        selectedView = (View) view.findViewById(R.id.obj_user_dialog_icon_selectedView);
        imageView.setImageDrawable(new UserIconReference(context).getIcon(imgNum));
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                userIconAdapter.reloadIcons(imgNum);
            }
        });
    }

    public void setSelectedView(){
        selectedView.setBackgroundColor(getResources().getColor(R.color.brightblue));
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
