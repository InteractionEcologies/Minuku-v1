package edu.umich.si.inteco.minuku.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

import edu.umich.si.inteco.minuku.LoginActivity;
import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.constants.UserIconReference;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.model.User;
import edu.umich.si.inteco.minuku.model.Views.DialogUserIcon;

/**
 * Created by tsung on 2017/2/10.
 */

public class UserIconAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<User> allUserList;
    private UserIconReference userIconReference;
    private UserSettingsDBHelper userSettingsDBHelper;
    private Dialog dialog;
    private GridLayout gridLayout;
    private String iconImgNum;

    public UserIconAdapter(Context context, ArrayList<User> allUserList) {
        this.context = context;
        this.allUserList = allUserList;
    }

    @Override
    public int getCount() {
        return allUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return allUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(context);
        userIconReference = new UserIconReference(context);

        if (convertView == null) {
            convertView = li.inflate(R.layout.activity_login_list_item, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.ivUserIcon = (ImageView) convertView.findViewById(R.id.activity_login_list_item_iv);
            viewHolder.edUserName = (EditText) convertView.findViewById(R.id.activity_login_list_item_tv);
            viewHolder.ibRemove = (ImageButton) convertView.findViewById(R.id.activity_login_list_item_btn_remove);
            viewHolder.edUserName.setText(allUserList.get(getCount() - position - 1).getUserName());
            viewHolder.ivUserIcon.setImageDrawable(userIconReference.getIcon(allUserList.get(getCount() - position - 1).getImgNumber()));
            viewHolder.ivUserIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDialog(position, allUserList.get(getCount() - position - 1));
                }
            });
            viewHolder.ibRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null == userSettingsDBHelper) {
                        userSettingsDBHelper = new UserSettingsDBHelper(context);
                    }
                    userSettingsDBHelper.deleteUserById(userSettingsDBHelper.getAllIdList().get(getCount() - position - 1));
                    ((LoginActivity) LoginActivity.getContext()).setListView();
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView ivUserIcon;
        EditText edUserName;
        ImageButton ibRemove;
    }

    private void setDialog(final int position, final User user) {
        dialog = new Dialog(context);
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.activity_login_user_icon_dialog, null);
        dialog.setContentView(dialogView);
        dialog.setTitle("Select an icon");
        gridLayout = (GridLayout) dialogView.findViewById(R.id.activity_login_user_icon_dialog_gridLayout);
        Button btnSave = (Button) dialogView.findViewById(R.id.activity_login_user_icon_dialog_btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == userSettingsDBHelper) {
                    userSettingsDBHelper = new UserSettingsDBHelper(context);
                }
                user.setImgNumber(iconImgNum);
                userSettingsDBHelper.updateDB(userSettingsDBHelper.getAllIdList().get(getCount() - position - 1) + "", user);
                ((LoginActivity) LoginActivity.getContext()).setListView();
                dialog.dismiss();
            }
        });
        UserIconReference userIconReference = new UserIconReference(context);
        for (int i = 0; i < userIconReference.getIconsReference().size(); i++) {
            DialogUserIcon dialogUserIcon = new DialogUserIcon(context, i + "", this);
            gridLayout.addView(dialogUserIcon.getView());
        }
        dialog.show();
    }

    public void reloadIcons(String iconImgNum) {
        this.iconImgNum = iconImgNum;
        gridLayout.removeAllViews();
        for (int i = 0; i < userIconReference.getIconsReference().size(); i++) {
            DialogUserIcon dialogUserIcon = new DialogUserIcon(context, i + "", this);
            if (String.valueOf(i).equalsIgnoreCase(iconImgNum)) {
                dialogUserIcon.setSelectedView();
            }
            gridLayout.addView(dialogUserIcon.getView());
        }
    }
}
