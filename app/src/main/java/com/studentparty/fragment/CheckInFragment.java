package com.studentparty.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.studentparty.R;
import com.studentparty.adapter.MyPostAdapter;
import com.studentparty.controller.utils.FirebaseUtil;
import com.studentparty.holder.MyPostHolder;
import com.studentparty.model.EventPost;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link CheckInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    LinearLayoutManager linearLayoutManager;
    RecyclerView mRecyclerView;
    private ValueEventListener mValueEventListner;

    MyPostAdapter queryAdapter;
    MyPostAdapter.OnSetupViewListener  mListPostner=null;

    public CheckInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckInFragment newInstance(String param1, String param2) {
        CheckInFragment fragment = new CheckInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_check_in, container, false);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);








        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        setValueEventListNer();
        Query allPostsQuery = FirebaseUtil.getPostsRef();



        allPostsQuery.limitToLast(10);
        allPostsQuery.addValueEventListener(mValueEventListner);
        allPostsQuery.keepSynced(true);
        return rootView;
    }

    private void setValueEventListNer() {


        mValueEventListner=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<EventPost> postValue=new ArrayList<>();
                List<String > keyPostID=new ArrayList<>();

                for (DataSnapshot pos : dataSnapshot.getChildren()) {

                    EventPost post = pos.getValue(EventPost.class);

                    postValue.add(post);
                    keyPostID.add(pos.getKey());


                }



                queryAdapter=new MyPostAdapter(postValue, new MyPostAdapter.OnSetupViewListener() {
                    @Override
                    public void onSetupView(MyPostHolder holder, EventPost post, int position, String postKey) {
                        setupPost(holder, post, position, postKey);
                    }
                }, keyPostID);
              //  queryAdapter.setPaths(postValue);
                queryAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);

                    }
                });


                mRecyclerView.setAdapter(queryAdapter);

                queryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    private void setupPost(final MyPostHolder postViewHolder, final EventPost post, final int position, final String inPostKey) {
        postViewHolder.setPhotoDataView(post.getEventImage(),post,getActivity().getApplicationContext());

        postViewHolder.setIcon(post.getAuthor().getImageUrl().toString(),"asfsf",getActivity().getApplicationContext());

        postViewHolder.setTittle(post.getEventTittle());





    }

}
