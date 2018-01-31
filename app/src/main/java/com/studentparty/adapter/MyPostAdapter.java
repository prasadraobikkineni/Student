package com.studentparty.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studentparty.R;
import com.studentparty.holder.MyPostHolder;
import com.studentparty.model.EventPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 18-10-2017.
 */

public class MyPostAdapter extends RecyclerView.Adapter<MyPostHolder> {

    private final String TAG = "PostQueryAdapter";
    private List<EventPost> mPostPaths=new ArrayList<>();
    private OnSetupViewListener mOnSetupViewListener;
    private List<String> mKeyValue;

    public MyPostAdapter(List<EventPost> paths, OnSetupViewListener onSetupViewListener, List<String> keyValue) {
        if (paths == null || paths.isEmpty()) {
            mPostPaths = new ArrayList<>();
        } else {
            mPostPaths = paths;
        }
        mKeyValue=keyValue;
        mOnSetupViewListener = onSetupViewListener;
    }

    public void setPaths(List<EventPost> postPaths) {
        if ((postPaths != null) && (!postPaths.isEmpty())) {
            mPostPaths.clear();
            mPostPaths.addAll(postPaths);

        }

        notifyDataSetChanged();
    }

    public  void addList(List<EventPost> postListData)
    {
        int positionStart = postListData.size();
        mPostPaths.addAll(postListData);
        int itemCount = mPostPaths.size() - positionStart;

        if (positionStart == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(positionStart + 1, itemCount);
        }
    }


    public void addItem(EventPost path) {
        mPostPaths.add(path);
        notifyItemInserted(mPostPaths.size());
    }

    @Override
    public MyPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mypost, parent, false);
        return new MyPostHolder(v);
    }

    @Override
    public void onBindViewHolder(MyPostHolder holder, int position) {
        EventPost post = mPostPaths.get(position);

    //    Log.d(TAG, "post key: " + mKeyValue.get(position));

        mOnSetupViewListener.onSetupView(holder, post, holder.getAdapterPosition(),
                mKeyValue.get(position));
    }

    @Override
    public void onViewDetachedFromWindow(MyPostHolder holder) {

        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        if(mPostPaths!=null)
        return mPostPaths.size();
        return 0;
    }

    public interface OnSetupViewListener {
        void onSetupView(MyPostHolder holder, EventPost post, int position, String postKey);
    }
}
