package com.studentparty.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.studentparty.R;
import com.studentparty.base.BaseActivity;
import com.studentparty.controller.listner.PermissionCallBack;
import com.studentparty.controller.utils.FirebaseUtil;
import com.studentparty.mediachooser.MediaChooser;
import com.studentparty.mediachooser.Utilities.MediaChooserConstants;
import com.studentparty.mediachooser.activity.HomeScreenMediaChooser;
import com.studentparty.model.People;
import com.studentparty.uicomponents.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class ProfileScreen extends BaseActivity implements View.OnClickListener{
    CircleImageView circleimage;
    EditText mFirst,mSur;
    Button mSubmit;
    List<Bitmap> arrayBitmap=new ArrayList<>();
    boolean isImageVidePicke;
    List<String> imageUru=new ArrayList<>();
    String mImageUrl="";
    private DatabaseReference mDatabase;
    String[] permissionRequired={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        mFirst=(EditText)findViewById(R.id.firstname);
        mSur=(EditText)findViewById(R.id.surname);
        circleimage=(CircleImageView) findViewById(R.id.circleimage);
        circleimage.setOnClickListener(this);
        mSubmit=(Button)findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);
        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);
    }

    private  void callPermission()
    {
        requestPermissions(permissionRequired, new PermissionCallBack() {
            @Override
            public void permissionGranted() {
                MediaChooserConstants.VIDEO_ALREADY_SELCTED = false;
                MediaChooserConstants.IMAGE_ALREADY_SELCTED = false;
                MediaChooserConstants.SELECTED_MEDIA_COUNT = 0;
                MediaChooserConstants.MAX_MEDIA_LIMIT = 5;
                MediaChooser.setSelectionLimit(1);
                HomeScreenMediaChooser.startMediaChooser(ProfileScreen.this, false);
            }

            @Override
            public void permissionDenied() {

                Toast.makeText(getApplicationContext(),"Permission is denied by you",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.circleimage)
            callPermission();
        else
            updateProfileData();

    }
    BroadcastReceiver imageBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            arrayBitmap.clear();
            isImageVidePicke=true;
            String[] array = intent.getStringArrayListExtra("list").toArray(new String[0]);


            imageUru=intent.getStringArrayListExtra("list");



        }
    };

    private void updateProfileData() {

        People mPeople=new People(mFirst.getText().toString(),mSur.getText().toString(),"https://yt3.ggpht.com/-DOF9MYU0TQU/AAAAAAAAAAI/AAAAAAAAAAA/Lp08ujNTHTY/s48-c-k-no-mo-rj-c0xffffff/photo.jpg",FirebaseUtil.getCurrentUserId());
        mDatabase= FirebaseUtil.getPeopleRef();
        mDatabase.child(FirebaseUtil.getCurrentUserId()).setValue(mPeople);
        Intent i = new Intent(ProfileScreen.this, MenuScreen.class);
        startActivity(i);

        // close this activity
        finish();


    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(imageBroadcastReceiver);
        super.onDestroy();
    }
}
