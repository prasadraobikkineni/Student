
package com.studentparty.mediachooser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.studentparty.R;
import com.studentparty.mediachooser.Utilities.BucketEntry;
import com.studentparty.mediachooser.Utilities.MediaChooserConstants;
import com.studentparty.mediachooser.async.ImageLoadAsync;
import com.studentparty.mediachooser.async.MediaAsync;
import com.studentparty.mediachooser.async.VideoLoadAsync;
import com.studentparty.mediachooser.fragment.BucketVideoFragment;

import java.util.ArrayList;


public class BucketGridAdapter extends ArrayAdapter<BucketEntry> {

    public BucketVideoFragment bucketVideoFragment;

    private Context mContext;
    private ArrayList<BucketEntry> mBucketEntryList;
    private boolean mIsFromVideo;
    private int mWidth;
    private LayoutInflater mViewInflater;


    public BucketGridAdapter(Context context, ArrayList<BucketEntry> categories, boolean isFromVideo) {

        super(context, 0, categories);
        mBucketEntryList = categories;
        mContext = context;
        mIsFromVideo = isFromVideo;
        mViewInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return mBucketEntryList.size();
    }

    @Override
    public BucketEntry getItem(int position) {
        return mBucketEntryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addLatestEntry(String url) {
        int count = mBucketEntryList.size();
        boolean success = false;
        for (int i = 0; i < count; i++) {
            if (mBucketEntryList.get(i).bucketName.equals(MediaChooserConstants.folderName)) {
                mBucketEntryList.get(i).bucketUrl = url;
                success = true;
                break;
            }
        }

        if (!success) {
            BucketEntry latestBucketEntry = new BucketEntry(0, MediaChooserConstants.folderName, url);
            mBucketEntryList.add(0, latestBucketEntry);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            mWidth = mContext.getResources().getDisplayMetrics().widthPixels;

            convertView = mViewInflater.inflate(R.layout.view_grid_bucket_item_media_chooser, parent, false);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageViewFromMediaChooserBucketRowView);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextViewFromMediaChooserBucketRowView);

            FrameLayout.LayoutParams imageParams = (FrameLayout.LayoutParams) holder.imageView.getLayoutParams();
            imageParams.width = mWidth / 2;
            imageParams.height = mWidth / 2;

            holder.imageView.setLayoutParams(imageParams);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (mIsFromVideo) {
            new VideoLoadAsync(bucketVideoFragment, holder.imageView, false, mWidth / 2).executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, mBucketEntryList.get(position).bucketUrl);

        } else {
            ImageLoadAsync loadAsync = new ImageLoadAsync(mContext, holder.imageView, mWidth / 2);
            loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, mBucketEntryList.get(position).bucketUrl);
        }

        holder.nameTextView.setText(mBucketEntryList.get(position).bucketName);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
    }
}


