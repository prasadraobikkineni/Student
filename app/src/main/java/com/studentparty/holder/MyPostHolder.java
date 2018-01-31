package com.studentparty.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.studentparty.R;
import com.studentparty.activity.GlideUtil;
import com.studentparty.model.EventPost;


/**
 * Created by Admin on 18-10-2017.
 */

public class MyPostHolder extends RecyclerView.ViewHolder{
    private final View mView;
    ImageView mPostImage,mVideoIcon,mDeleteIcon;
    TextView mProfileName,mDate,mCounterText;
    ImageView mProfileIcon;
    MyPostClickListener mListner;
    Button checkInBtn;
    Context mContext;
    public MyPostHolder(View itemView) {
        super(itemView);
        mView=itemView;
        mProfileIcon = (ImageView) itemView.findViewById(R.id.user_profile_photo);
        mProfileName=(TextView)itemView.findViewById(R.id.eventTittle);

        mPostImage = (ImageView) itemView.findViewById(R.id.eventImage);
        checkInBtn=(Button) itemView.findViewById(R.id.btn);
    }

    public void setPhotoDataView(String url, final EventPost post, Context mContext) {

        String[] stringArray=url.split(",");



        GlideUtil.loadImage(url.replace(",",""), mPostImage,mContext);
        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListner.showPostDetailActivity(post);
            }
        });
    }

    public void setIcon(String url, final String authorId, Context mContext) {
        //GlideUtil.loadProfileIcon(url, mProfileIcon,mContext);
        GlideUtil.loadProfileIcon("https://wallpapers.filmibeat.com/ph-1024x768/2017/07/ajith-kumar_149968155480.jpg", mProfileIcon,mContext);
    }

    public void setTittle(String author) {

        mProfileName.setText(author);

    }
    private void showUserDetail(String authorId) {
        Context context = mView.getContext();
      /*  Intent userDetailIntent = new Intent(context, UserDetailActivity.class);
        userDetailIntent.putExtra(UserDetailActivity.USER_ID_EXTRA_NAME, authorId);
        context.startActivity(userDetailIntent);*/
    }
    public void setVideoPlayCion(boolean status) {
        if(status)
            mVideoIcon.setVisibility(View.VISIBLE);
        else
            mVideoIcon.setVisibility(View.INVISIBLE);

    }

    public void setDeleteIcon(final String postKey) {

        Log.d("postKey---",postKey);
     mDeleteIcon.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
               mListner.deletePostActivity(postKey);

         }
     });

    }

    public void setPostClickListener(MyPostClickListener listener) {
        mListner = listener;
    }
    public void setTimestamp(String timestamp) {
        mDate.setText(timestamp);
    }
    public interface MyPostClickListener {

        void showPostDetailActivity(EventPost post);
        void deletePostActivity(String post);

    }
}
