package edu.umich.si.inteco.minuku.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by tsung on 2017/1/24.
 */

public class HomeScreenIconService extends Service {

    private WindowManager windowManager;
    private RelativeLayout chatheadView, removeView;
    private LinearLayout txtView, txt_linearlayout;
    private ImageView chatheadImg, removeImg;
    private TextView txt1;
    private int x_init_cord, y_init_cord, x_init_margin, y_init_margin;
    private Point szWindow = new Point();
    private boolean isLeft = true;
    private String sMsg = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
