/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.studentparty.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.studentparty.R;
import com.studentparty.uicomponents.CircleImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;

public class GlideUtil {
    Context ctContext;
    static ProgressBar progressBarView;
    public static void loadImage(String url, ImageView imageView, Context mContext) {

        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.img_bg)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }

    public static void loadImageCir(String url, CircleImageView imageView, Context mContext) {

        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.img_bg)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }
    public static Bitmap getBitmap(String url, Context ctContext) throws ExecutionException, InterruptedException {
    return Glide.with(ctContext)
            .load(url)
            .asBitmap()
            .into(-1,-1).get();


    }
    public static void shareView(String uri, int maxDimension, Context ctContext, String shareText, ProgressBar progressBar) {
        progressBarView=progressBar;
        progressBarView.bringToFront();
        progressBarView.setVisibility(View.VISIBLE);
        LoadResizedBitmapTask task = new LoadResizedBitmapTask(maxDimension,ctContext,shareText);
        task.execute(uri);
    }
     static Uri getLocalBitmapUri(Context context, Bitmap bmp, String id) {
        Uri bmpUri = null;
        try {
            if(id==null){
                id= String.valueOf(System.currentTimeMillis());

            }
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + id + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (Exception e) {
            Log.e("", "getLocalBitmapUri: ",e );
        }
        return bmpUri;
    }
    static class LoadResizedBitmapTask extends AsyncTask<String, Void, Bitmap> {
        private int mMaxDimension;
        private Context mMaxContext;
        private String mShareText;

        public LoadResizedBitmapTask(int maxDimension, Context ctContext, String shareText) {

            mMaxDimension = maxDimension;
            mMaxContext=ctContext;
            mShareText=shareText;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {

            String uri = params[0];
            if (uri != null) {

                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(mMaxContext)
                            .load(uri)
                            .asBitmap()
                            .centerCrop()
                            .into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                            .get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressBarView.setVisibility(View.INVISIBLE);
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("image/*");
            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(mMaxContext, bitmap,"3"));
            i.putExtra(Intent.EXTRA_TEXT, mShareText);
            mMaxContext.startActivity(Intent.createChooser(i, "Share News"));
        }
    }
    public static void loadProfileIcon(String url, ImageView imageView, Context mContext) {

        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.img_bg)
                .dontAnimate()
                .fitCenter()
                .into(imageView);
    }
}