package com.studentparty.mediachooser.async;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Created by Admin on 10-09-2017.
 */

public class VideoThumbNailFrame extends AsyncTask<String,Void,Bitmap> {
    private ImageView mImageView;
    Bitmap bmp = null;
    public VideoThumbNailFrame(ImageView imageView) {
        mImageView=imageView;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        MediaMetadataRetriever retriever = new  MediaMetadataRetriever();

        retriever.setDataSource(params[0], new HashMap<String, String>());

        bmp = retriever.getFrameAtTime();
        return bmp;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {

            mImageView.setImageBitmap(getResizedBitmap(bitmap,mImageView.getWidth(),mImageView.getHeight()));

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        if(width==0) {
            width = 300;
            height=300;

        }

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
