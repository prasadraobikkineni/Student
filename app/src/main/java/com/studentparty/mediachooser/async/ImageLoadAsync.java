package com.studentparty.mediachooser.async;

import android.content.Context;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.BitmapAjaxCallback;

import java.io.File;


public class ImageLoadAsync extends MediaAsync<String, String, String> {

    private ImageView mImageView;
    private Context mContext;
    private int mWidth;

    public ImageLoadAsync(Context context, ImageView imageView, int width) {
        mImageView = imageView;
        mContext = context;
        mWidth = width;
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        return url;
    }

    @Override
    protected void onPostExecute(String result) {

        AQuery aQuery = new AQuery(mContext);
        aQuery.id(mImageView);
        aQuery.image(new File(result),true, mWidth, new BitmapAjaxCallback());
    }

}
