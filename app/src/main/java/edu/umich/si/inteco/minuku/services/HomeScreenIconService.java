package edu.umich.si.inteco.minuku.services;


import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.model.User;
import edu.umich.si.inteco.minuku.model.Views.UserIcon;

public class HomeScreenIconService extends Service {
    // Views
    private RelativeLayout removeView, homescreenServiceView;
    private GridLayout gridLayout;
    private ImageView chatheadImg, removeImg;
    private FrameLayout mainLayout;
    private Spinner spinner;
    private Button btnSave;
    private ImageButton ibClose;
    private int x_init_cord, y_init_cord, x_init_margin, y_init_margin;
    private Point szWindow = new Point();
    private boolean isLeft = true;

    // Function
    private boolean ifUserIconOpen = false;
    private WindowManager windowManager;
    private UserSettingsDBHelper userSettingsDBHelper;
    private ArrayList<User> userList;
    private ArrayList<Integer> userIdList;
    private int numOfPeopleUsing = 1;
    private int currentSelected = 1;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private void handleStart() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        removeView = (RelativeLayout) inflater.inflate(R.layout.service_homescreen_remove, null);
        WindowManager.LayoutParams paramRemove = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        paramRemove.gravity = Gravity.TOP | Gravity.LEFT;
        removeView.setVisibility(View.GONE);
        removeImg = (ImageView) removeView.findViewById(R.id.remove_img);
        windowManager.addView(removeView, paramRemove);

        homescreenServiceView = (RelativeLayout) inflater.inflate(R.layout.service_homescreen_mainlayout, null);
        gridLayout = (GridLayout) homescreenServiceView.findViewById(R.id.home_screen_service_gridLayout);
        mainLayout = (FrameLayout) homescreenServiceView.findViewById(R.id.home_screen_service_main_layout);
        spinner = (Spinner) homescreenServiceView.findViewById(R.id.home_screen_service_spinner);
        btnSave = (Button) homescreenServiceView.findViewById(R.id.home_screen_service_btnSave);
        chatheadImg = (ImageView) homescreenServiceView.findViewById(R.id.home_screen_service_chathead_img);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            windowManager.getDefaultDisplay().getSize(szWindow);
        } else {
            int w = windowManager.getDefaultDisplay().getWidth();
            int h = windowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        params.width = windowManager.getDefaultDisplay().getWidth();
        windowManager.addView(homescreenServiceView, params);

        chatheadImg.setOnTouchListener(new View.OnTouchListener() {
            long time_start = 0, time_end = 0;
            boolean isLongclick = false, inBounded = false;
            int remove_img_width = 0, remove_img_height = 0;

            Handler handler_longClick = new Handler();
            Runnable runnable_longClick = new Runnable() {

                @Override
                public void run() {
                    isLongclick = true;
                    removeView.setVisibility(View.VISIBLE);
                    chathead_longclick();
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) homescreenServiceView.getLayoutParams();

                int x_cord = (int) event.getRawX();
                int y_cord = (int) event.getRawY();
                int x_cord_Destination, y_cord_Destination;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        showUserIcon();
                        time_start = System.currentTimeMillis();
                        handler_longClick.postDelayed(runnable_longClick, 600);

                        remove_img_width = removeImg.getLayoutParams().width;
                        remove_img_height = removeImg.getLayoutParams().height;

                        x_init_cord = x_cord;
                        y_init_cord = y_cord;

                        x_init_margin = layoutParams.x;
                        y_init_margin = layoutParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x_diff_move = x_cord - x_init_cord;
                        int y_diff_move = y_cord - y_init_cord;

                        x_cord_Destination = x_init_margin + x_diff_move;
                        y_cord_Destination = y_init_margin + y_diff_move;

                        if (isLongclick) {
                            int x_bound_left = szWindow.x / 2 - (int) (remove_img_width * 1.5);
                            int x_bound_right = szWindow.x / 2 + (int) (remove_img_width * 1.5);
                            int y_bound_top = szWindow.y - (int) (remove_img_height * 1.5);

                            if ((x_cord >= x_bound_left && x_cord <= x_bound_right) && y_cord >= y_bound_top) {
                                inBounded = true;

                                int x_cord_remove = (int) ((szWindow.x - (remove_img_height * 1.5)) / 2);
                                int y_cord_remove = (int) (szWindow.y - ((remove_img_width * 1.5) + getStatusBarHeight()));

                                if (removeImg.getLayoutParams().height == remove_img_height) {
                                    removeImg.getLayoutParams().height = (int) (remove_img_height * 1.5);
                                    removeImg.getLayoutParams().width = (int) (remove_img_width * 1.5);

                                    WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
                                    param_remove.x = x_cord_remove;
                                    param_remove.y = y_cord_remove;

                                    windowManager.updateViewLayout(removeView, param_remove);
                                }

                                layoutParams.x = x_cord_remove + (Math.abs(removeView.getWidth() - homescreenServiceView.getWidth())) / 2;
                                layoutParams.y = y_cord_remove + (Math.abs(removeView.getHeight() - homescreenServiceView.getHeight())) / 2;

                                windowManager.updateViewLayout(homescreenServiceView, layoutParams);
                                break;
                            } else {
                                inBounded = false;
                                removeImg.getLayoutParams().height = remove_img_height;
                                removeImg.getLayoutParams().width = remove_img_width;

                                WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
                                int x_cord_remove = (szWindow.x - removeView.getWidth()) / 2;
                                int y_cord_remove = szWindow.y - (removeView.getHeight() + getStatusBarHeight());

                                param_remove.x = x_cord_remove;
                                param_remove.y = y_cord_remove;

                                windowManager.updateViewLayout(removeView, param_remove);
                            }
                        }


                        layoutParams.x = x_cord_Destination;
                        layoutParams.y = y_cord_Destination;

                        windowManager.updateViewLayout(homescreenServiceView, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        isLongclick = false;
                        removeView.setVisibility(View.GONE);
                        removeImg.getLayoutParams().height = remove_img_height;
                        removeImg.getLayoutParams().width = remove_img_width;
                        handler_longClick.removeCallbacks(runnable_longClick);

                        if (inBounded) {
                            stopService(new Intent(HomeScreenIconService.this, HomeScreenIconService.class));
                            inBounded = false;
                            break;
                        }

                        int x_diff = x_cord - x_init_cord;
                        int y_diff = y_cord - y_init_cord;

                        if (Math.abs(x_diff) < 5 && Math.abs(y_diff) < 5) {
                            time_end = System.currentTimeMillis();
//                            if ((time_end - time_start) < 300) {
//                            }
                        }

                        y_cord_Destination = y_init_margin + y_diff;

                        int BarHeight = getStatusBarHeight();
                        if (y_cord_Destination < 0) {
                            y_cord_Destination = 0;
                        } else if (y_cord_Destination + (homescreenServiceView.getHeight() + BarHeight) > szWindow.y) {
                            y_cord_Destination = szWindow.y - (homescreenServiceView.getHeight() + BarHeight);
                        }
                        layoutParams.y = y_cord_Destination;
                        inBounded = false;
                        resetPosition(x_cord);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void showUserIcon() {
        if (null == userSettingsDBHelper)
            userSettingsDBHelper = new UserSettingsDBHelper(getApplicationContext());
        if (ifUserIconOpen) {
            ifUserIconOpen = false;
            mainLayout.setVisibility(View.GONE);
        } else {
            ifUserIconOpen = true;
            setUserIconView();
            mainLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setUserIconView() {
        gridLayout.removeAllViews();
        userList = userSettingsDBHelper.getAllUserList();
        userIdList = userSettingsDBHelper.getAllIdList();
        for (int i = 0; i < userList.size(); i++) {
            final User user = userList.get(i);
            final String id = userIdList.get(i).toString();
            UserIcon userIcon = new UserIcon(getApplicationContext(), user);
            ImageButton ib = userIcon.getIbUser();
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectedTabColor(id, user);
                }
            });
            gridLayout.addView(userIcon.getView());
        }
    }

    private void setSelectedTabColor(String id, User user) {
        if (currentSelected >= numOfPeopleUsing) {
            userSettingsDBHelper.setAllUserUnSelected();
            currentSelected = 1;
        } else {
            currentSelected++;
        }
        user.setIfSelected(true);
        userSettingsDBHelper.updateDB(id, user);
        setUserIconView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            windowManager.getDefaultDisplay().getSize(szWindow);
        } else {
            int w = windowManager.getDefaultDisplay().getWidth();
            int h = windowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }

        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) homescreenServiceView.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            if (layoutParams.y + (homescreenServiceView.getHeight() + getStatusBarHeight()) > szWindow.y) {
                layoutParams.y = szWindow.y - (homescreenServiceView.getHeight() + getStatusBarHeight());
                windowManager.updateViewLayout(homescreenServiceView, layoutParams);
            }

            if (layoutParams.x != 0 && layoutParams.x < szWindow.x) {
                resetPosition(szWindow.x);
            }

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            if (layoutParams.x > szWindow.x) {
                resetPosition(szWindow.x);
            }
        }

    }

    private void resetPosition(int x_cord_now) {
        if (x_cord_now <= szWindow.x / 2) {
            isLeft = true;
            moveToLeft(x_cord_now);

        } else {
            isLeft = false;
            moveToRight(x_cord_now);

        }

    }

    private void moveToLeft(final int x_cord_now) {
        final int x = szWindow.x - x_cord_now;

        new CountDownTimer(500, 5) {
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) homescreenServiceView.getLayoutParams();

            public void onTick(long t) {
                long step = (500 - t) / 5;
                mParams.x = 0 - (int) (double) bounceValue(step, x);
                windowManager.updateViewLayout(homescreenServiceView, mParams);
            }

            public void onFinish() {
                mParams.x = 0;
                windowManager.updateViewLayout(homescreenServiceView, mParams);
            }
        }.start();
    }

    private void moveToRight(final int x_cord_now) {
        new CountDownTimer(500, 5) {
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) homescreenServiceView.getLayoutParams();

            public void onTick(long t) {
                long step = (500 - t) / 5;
                mParams.x = szWindow.x + (int) (double) bounceValue(step, x_cord_now) - homescreenServiceView.getWidth();
                windowManager.updateViewLayout(homescreenServiceView, mParams);
            }

            public void onFinish() {
                mParams.x = szWindow.x - homescreenServiceView.getWidth();
                windowManager.updateViewLayout(homescreenServiceView, mParams);
            }
        }.start();
    }

    private double bounceValue(long step, long scale) {
        double value = scale * java.lang.Math.exp(-0.055 * step) * java.lang.Math.cos(0.08 * step);
        return value;
    }

    private int getStatusBarHeight() {
        int statusBarHeight = (int) Math.ceil(25 * getApplicationContext().getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }

    private void chathead_longclick() {
        WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
        int x_cord_remove = (szWindow.x - removeView.getWidth()) / 2;
        int y_cord_remove = szWindow.y - (removeView.getHeight() + getStatusBarHeight());

        param_remove.x = x_cord_remove;
        param_remove.y = y_cord_remove;

        windowManager.updateViewLayout(removeView, param_remove);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (startId == Service.START_STICKY) {
            handleStart();
            return super.onStartCommand(intent, flags, startId);
        } else {
            return Service.START_NOT_STICKY;
        }

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if (homescreenServiceView != null) {
            windowManager.removeView(homescreenServiceView);
        }

        if (removeView != null) {
            windowManager.removeView(removeView);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}