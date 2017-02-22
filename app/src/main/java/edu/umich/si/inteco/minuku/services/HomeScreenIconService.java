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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.model.User;
import edu.umich.si.inteco.minuku.model.Views.UserIcon;
import edu.umich.si.inteco.minuku.util.AnimUtilities;

public class HomeScreenIconService extends Service {
    // Views
    private RelativeLayout removeView, homescreenServiceView;
    private GridLayout gridLayout;
    private ImageView chatheadImg, removeImg;
    private TextView tvSave;
    private FrameLayout mainLayout;
    //    private Spinner spinner;
//    private Button btnSave;
    private ImageButton ibClose;
    private Button btnOpen;
    private int initCoordX, initCoordY, initMarginX, initMarginY;
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
    private AnimUtilities animationUtils;

    @Override
    public void onCreate() {
        super.onCreate();

        animationUtils = new AnimUtilities(this);
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
        initServiceViews();

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
            long timeStart = 0, timeEnd = 0;
            boolean isLongclick = false, inBounded = false;
            int removeImgWidth = 0, removeImgHeight = 0;

            Handler handlerLongClick = new Handler();
            Runnable runnableLongClick = new Runnable() {

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

                int coordX = (int) event.getRawX();
                int coordY = (int) event.getRawY();
                int xCoordDestination, yCoordDestination;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setUserIcon();

                        timeStart = System.currentTimeMillis();
                        handlerLongClick.postDelayed(runnableLongClick, 600);

                        removeImgWidth = removeImg.getLayoutParams().width;
                        removeImgHeight = removeImg.getLayoutParams().height;

                        initCoordX = coordX;
                        initCoordY = coordY;

                        initMarginX = layoutParams.x;
                        initMarginY = layoutParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int DiffMoveX = coordX - initCoordX;
                        int DiffMoveY = coordY - initCoordY;

                        if (DiffMoveX >= MainActivity.screenWidth / 4) {
                            ifUserIconOpen = true;
                            setUserIcon();
                        }

                        xCoordDestination = initMarginX + DiffMoveX;
                        yCoordDestination = initMarginY + DiffMoveY;

                        if (isLongclick) {
                            int xBoundLeft = szWindow.x / 2 - (int) (removeImgWidth * 1.5);
                            int xBoundRight = szWindow.x / 2 + (int) (removeImgWidth * 1.5);
                            int yBoundTop = szWindow.y - (int) (removeImgHeight * 1.5);

                            if ((coordX >= xBoundLeft && coordX <= xBoundRight) && coordY >= yBoundTop) {
                                inBounded = true;

                                int xCoordRemove = (int) ((szWindow.x - (removeImgHeight * 1.5)) / 2);
                                int yCoordRemove = (int) (szWindow.y - ((removeImgWidth * 1.5) + getStatusBarHeight()));

                                if (removeImg.getLayoutParams().height == removeImgHeight) {
                                    removeImg.getLayoutParams().height = (int) (removeImgHeight * 1.5);
                                    removeImg.getLayoutParams().width = (int) (removeImgWidth * 1.5);

                                    WindowManager.LayoutParams paramRemove = (WindowManager.LayoutParams) removeView.getLayoutParams();
                                    paramRemove.x = xCoordRemove;
                                    paramRemove.y = yCoordRemove;

                                    windowManager.updateViewLayout(removeView, paramRemove);
                                }

                                layoutParams.x = xCoordRemove + (Math.abs(removeView.getWidth() - homescreenServiceView.getWidth())) / 2;
                                layoutParams.y = yCoordRemove + (Math.abs(removeView.getHeight() - homescreenServiceView.getHeight())) / 2;

                                windowManager.updateViewLayout(homescreenServiceView, layoutParams);
                                break;
                            } else {
                                inBounded = false;
                                removeImg.getLayoutParams().height = removeImgHeight;
                                removeImg.getLayoutParams().width = removeImgWidth;

                                WindowManager.LayoutParams paramRemove = (WindowManager.LayoutParams) removeView.getLayoutParams();
                                int xCoordRemove = (szWindow.x - removeView.getWidth()) / 2;
                                int yCoordRemove = szWindow.y - (removeView.getHeight() + getStatusBarHeight());

                                paramRemove.x = xCoordRemove;
                                paramRemove.y = yCoordRemove;

                                windowManager.updateViewLayout(removeView, paramRemove);
                            }
                        }


                        layoutParams.x = xCoordDestination;
                        layoutParams.y = yCoordDestination;

                        windowManager.updateViewLayout(homescreenServiceView, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        isLongclick = false;
                        removeView.setVisibility(View.GONE);
                        removeImg.getLayoutParams().height = removeImgHeight;
                        removeImg.getLayoutParams().width = removeImgWidth;
                        handlerLongClick.removeCallbacks(runnableLongClick);

                        if (inBounded) {
                            stopService(new Intent(HomeScreenIconService.this, HomeScreenIconService.class));
                            inBounded = false;
                            break;
                        }

                        int xDiff = coordX - initCoordX;
                        int yDiff = coordY - initCoordY;

                        if (Math.abs(xDiff) < 5 && Math.abs(yDiff) < 5) {
                            timeEnd = System.currentTimeMillis();
//                            if ((timeEnd - timeStart) < 300) {
//                            }
                        }

                        yCoordDestination = initMarginY + yDiff;

                        int BarHeight = getStatusBarHeight();
                        if (yCoordDestination < 0) {
                            yCoordDestination = 0;
                        } else if (yCoordDestination + (homescreenServiceView.getHeight() + BarHeight) > szWindow.y) {
                            yCoordDestination = szWindow.y - (homescreenServiceView.getHeight() + BarHeight);
                        }
                        layoutParams.y = yCoordDestination;
                        inBounded = false;
                        resetPosition(coordX);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void initServiceViews() {
        gridLayout = (GridLayout) homescreenServiceView.findViewById(R.id.home_screen_service_gridLayout);
        mainLayout = (FrameLayout) homescreenServiceView.findViewById(R.id.home_screen_service_main_layout);
//        spinner = (Spinner) homescreenServiceView.findViewById(R.id.home_screen_service_spinner);
//        btnSave = (Button) homescreenServiceView.findViewById(R.id.home_screen_service_btnSave);
        chatheadImg = (ImageView) homescreenServiceView.findViewById(R.id.home_screen_service_chathead_img);
        tvSave = (TextView) homescreenServiceView.findViewById(R.id.home_screen_service_tvSave);
        btnOpen = (Button) homescreenServiceView.findViewById(R.id.home_screen_service_btnOpen);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenIconService.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ibClose = (ImageButton) homescreenServiceView.findViewById(R.id.home_screen_service_btn_close);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.GONE);
                ifUserIconOpen = false;
            }
        });
    }

    private void setUserIcon() {
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
                    if (userSettingsDBHelper.getIfUserSelectedById(id))
                        setSelectedTabColor(id, user, false);
                    else {
                        setSelectedTabColor(id, user, true);
                    }
                    animationUtils.setTvAnimToVisible(tvSave);
                }
            });
            gridLayout.addView(userIcon.getView());
        }
    }

    private void setSelectedTabColor(String id, User user, boolean setIfSelected) {
//        if (currentSelected >= numOfPeopleUsing) {
//            userSettingsDBHelper.setAllUserUnSelected();
//            currentSelected = 1;
//        } else {
//            currentSelected++;
//        }
        user.setIfSelected(setIfSelected);
        userSettingsDBHelper.updateDB(id, user);
        setUserIconView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
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

    private void resetPosition(int xCoordNow) {
        if (xCoordNow <= szWindow.x / 2) {
            isLeft = true;
            moveToLeft(xCoordNow);
        } else {
            isLeft = false;
            moveToRight(xCoordNow);
        }
    }

    private void moveToLeft(final int xCoordNow) {
        final int x = szWindow.x - xCoordNow;

        new CountDownTimer(300, 5) {
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) homescreenServiceView.getLayoutParams();

            public void onTick(long t) {
                long step = (300 - t) / 5;
                mParams.x = 0 - (int) (double) bounceValue(step, x);
                windowManager.updateViewLayout(homescreenServiceView, mParams);
            }

            public void onFinish() {
                mParams.x = 0;
                windowManager.updateViewLayout(homescreenServiceView, mParams);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) chatheadImg.getLayoutParams();
                params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                chatheadImg.setLayoutParams(params);
            }
        }.start();
    }

    private void moveToRight(final int xCoordNow) {
        new CountDownTimer(300, 5) {
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) homescreenServiceView.getLayoutParams();

            // Final Destination
            int finalDest = (int) (chatheadImg.getWidth() + getResources().getDimension(R.dimen.view_space));

            public void onTick(long t) {
                long step = (300 - t) / 5;
                mParams.x = szWindow.x + (int) (double) bounceValue(step, xCoordNow) - finalDest;
                windowManager.updateViewLayout(homescreenServiceView, mParams);
            }

            public void onFinish() {
//                mParams.x = szWindow.x - finalDest;
                mParams.x = 0;
                windowManager.updateViewLayout(homescreenServiceView, mParams);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) chatheadImg.getLayoutParams();
                params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                chatheadImg.setLayoutParams(params);
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