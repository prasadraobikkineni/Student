package com.studentparty.controller.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @Sathyaventhan
 */

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    private static String CATEGORY_URL = "";

    public static String getDate(String timeStampStr){


        try{
            DateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

            Date netDate = (new Date(Long.parseLong(timeStampStr)));
            return sdf.format(netDate);
        } catch (Exception ignored) {
            return "xx";
        }
    }


    public static Drawable recolorDrawable(Context context, Drawable icon, int toColor) {

        Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(bitmap);

        icon.setColorFilter(toColor, PorterDuff.Mode.SRC_IN);
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        icon.draw(myCanvas);

        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static String stringToDate(String dateString) {
        String date = "";
        try {
            DateFormat inputFormatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date da = inputFormatter.parse(dateString);
            System.out.println("==Date is ==" + da);

            DateFormat outputFormatter = new SimpleDateFormat("dd MMM yyyy");
            date = outputFormatter.format(da);
            System.out.println("==String date is : " + date);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }

        return date;
    }

    public static GridLayoutManager setGridLayoutManager(final Context context, boolean isHomeAdapter) {
        GridLayoutManager gridLayoutManager = null;
        if (isTablet(context)) {
            gridLayoutManager = new GridLayoutManager(context, 2);
        } else {
            gridLayoutManager = new GridLayoutManager(context, 1);
        }
        if (isHomeAdapter) {
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                                    @Override
                                                    public int getSpanSize(int position) {
                                                        //set the 4th row span to 2... its only for tablet..
                                                        if (position == 3 && isTablet(context))
                                                            return 2;
                                                        else
                                                            return 1;

                                                    }
                                                }
            );
        }
        return gridLayoutManager;
    }

    public static GridLayoutManager getDefaultGridLayoutManager(final Context context) {
        GridLayoutManager gridLayoutManager = null;

        gridLayoutManager = new GridLayoutManager(context, 1);
        return gridLayoutManager;
    }


    public static GridLayoutManager getGridLayoutMgrFirstItemSpan(final Context context) {
        GridLayoutManager gridLayoutManager = null;
        gridLayoutManager = new GridLayoutManager(context, 2);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                                @Override
                                                public int getSpanSize(int position) {
                                                    //set the 4th row span to 2... its only for tablet..
                                                    if (position == 0)
                                                        return 2;
                                                    else
                                                        return 1;

                                                }
                                            }
        );


        return gridLayoutManager;
    }







    public static void setFullScreen(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public static boolean isTablet(Context context) {
        try {
            return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
        return false;
    }

    public static String getYoutubeVideoId(String youtubeUrl) {
        String video_id = "";

        if (!youtubeUrl.contains("https"))
            youtubeUrl = "https:" + youtubeUrl;

        if (youtubeUrl.trim().length() > 0) {

            String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static boolean isNullOrEmpty(String data)
    { boolean isEmpty=false;
        if(null==data || data.equals("") || data.equals("null"))
            isEmpty=true;

            return  isEmpty;

    }


    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }
    public static float margintop(float px, int dcountp) {
        final float scale =px/dcountp;
        return scale/2-2;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static ViewGroup.LayoutParams getLayoutParamsBasedOnParent(View view, int width, int height)
            throws IllegalArgumentException {

        // Get the parent of the given view.
        // Determine what is the parent's type and return the appropriate type of LayoutParams.
        ViewParent parent = view.getParent();
        if (parent instanceof FrameLayout) {
            return new FrameLayout.LayoutParams(width, height);
        }
        if(parent instanceof PercentRelativeLayout)
        {
            return new PercentRelativeLayout.LayoutParams(width, height);
        }
        if (parent instanceof RelativeLayout) {

            return new RelativeLayout.LayoutParams(width, height);
        }

        if (parent instanceof LinearLayout) {
            return new LinearLayout.LayoutParams(width, height);
        }

        // Throw this exception if the parent is not the correct type.
        IllegalArgumentException exception = new IllegalArgumentException("The PARENT of a " +
                "FrameLayout container used by the GoogleMediaFramework must be a LinearLayout, " +
                "FrameLayout, or RelativeLayout. Please ensure that the container is inside one of these " +
                "three supported view groups.");

        // If the parent is not one of the supported types, throw our exception.
        throw exception;
    }

    public static int getHeight(Activity context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }
    public static int getWidth(Activity context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }




    public static String getCategoryUrl() {
        return CATEGORY_URL;
    }

    public static void setCategoryUrl(String categoryUrl) {
        CATEGORY_URL = categoryUrl;
    }


}