package com.studentparty.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.studentparty.R;
import com.studentparty.fragment.ChatFragment;
import com.studentparty.fragment.CheckInFragment;
import com.studentparty.fragment.FriendsFragment;
import com.studentparty.fragment.SettingFragment;

public class MenuScreen extends AppCompatActivity implements View.OnClickListener{
    BottomNavigationView bnve_no_animation_shifting_mode;
    ImageView postevent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);
        postevent=(ImageView) findViewById(R.id.postevent);
        postevent.setOnClickListener(this);
        bnve_no_animation_shifting_mode=(BottomNavigationView) findViewById(R.id.bottom_navigation);
        bnve_no_animation_shifting_mode.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new CheckInFragment());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_favorites:
                     fragment = new ChatFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_schedules:
                     fragment = new CheckInFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.action_music:
                    fragment = new FriendsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.setting:
                    fragment = new SettingFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.rootLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onClick(View view) {
        Intent main=new Intent(MenuScreen.this,PostUpload.class);
        main.putExtra(PostUpload.POST_CATEGORY,"TSTS");
        startActivity(main);


    }
}
