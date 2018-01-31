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

package com.studentparty.controller.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.studentparty.R;
import com.studentparty.model.Author;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.duration;


public class FirebaseUtil {
    private DatabaseReference mPeopleRef;
    public static DatabaseReference getBaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    public static void storedVideo(StorageReference storageRef, final ProgressBar mProgress, final Activity main, final String type)
    {



        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String folder_main = "student";
                File imagePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ File.separator + folder_main + File.separator);
                if (!imagePath.exists()) {
                    if (!imagePath.mkdirs()) {

                    }
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File mediaFile = null;
                if(type.equalsIgnoreCase("video"))
                mediaFile = new File(imagePath.getPath() + File.separator + "nucircle_video" + timeStamp + ".mp4");
                else
                    mediaFile = new File(imagePath.getPath() + File.separator + "nucircle_image" + timeStamp + ".png");

                try {
                    FileOutputStream fos=new FileOutputStream(mediaFile.getAbsolutePath());
                    fos.write(bytes);
                    fos.close();




                    // Create a media file name
                    if(type.equalsIgnoreCase("video")) {
                        ContentValues videoValues = new ContentValues();
                        videoValues.put(MediaStore.Video.Media.TITLE, "nucirecle");
                        videoValues.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                        videoValues.put(MediaStore.Video.Media.DATA, mediaFile.getAbsolutePath());

                        videoValues.put(MediaStore.Video.Media.DATE_TAKEN, String.valueOf(System.currentTimeMillis()));
                        videoValues.put(MediaStore.Video.Media.DESCRIPTION, main.getString(R.string.app_name));
                        videoValues.put(MediaStore.Video.Media.DURATION, duration);

                        main.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoValues);
                    }else{
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "nucirecle");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "nucircle");
                        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
                        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, mediaFile.toString().toLowerCase(Locale.US).hashCode());
                        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, mediaFile.getName().toLowerCase(Locale.US));
                        values.put("_data", mediaFile.getAbsolutePath());

                        ContentResolver cr = main.getContentResolver();

                        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    }

                    Toast.makeText(main, "Download Successfully!!!", Toast.LENGTH_SHORT).show();


                    mProgress.setVisibility(View.GONE);
                } catch (FileNotFoundException e) {
                    mProgress.setVisibility(View.GONE);
                    Toast.makeText(main, e.toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    mProgress.setVisibility(View.GONE);
                    Toast.makeText(main, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    public static Author getAuthor() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return null;
        return new Author(user.getDisplayName(), user.getPhotoUrl().toString(), user.getUid());
    }

    public static DatabaseReference getCurrentUserRef() {
        String uid = getCurrentUserId();
        if (uid != null) {
            return getBaseRef().child("people").child(getCurrentUserId());
        }
        return null;
    }

    public static DatabaseReference getPostsRef() {
        return getBaseRef().child("posts");
    }
    public static DatabaseReference getLocation() {
        return getBaseRef().child("userslocation");
    }
    public static DatabaseReference getPostsRefuser() {
        return getBaseRef().child("posts/userID");
    }
    public static DatabaseReference getPostNotifcation() {
        return getBaseRef().child("pushNotification");
    }
    public static DatabaseReference getPostsRefType() {
        return getBaseRef().child("posts").child(getCurrentUserId());
    }
    public static String getPostsPath() {
        return "posts/";
    }
    public static String getPushPath() {
        return "pushNotification/";
    }
    public static String getBlockPath() {
        return "Block/";
    }

    public static DatabaseReference getUsersRef() {
        return getBaseRef().child("users");
    }

    public static String getPeoplePath() {
        return "people/";
    }

    public static DatabaseReference getPeopleRef() {
        return getBaseRef().child("people");
    }

    public static DatabaseReference getCommentsRef() {
        return getBaseRef().child("comments");
    }

    public static DatabaseReference getFeedRef() {
        return getBaseRef().child("feed");
    }

    public static DatabaseReference getLikesRef() {
        return getBaseRef().child("likes");
    }

    public static DatabaseReference getFollowersRef() {
        return getBaseRef().child("followers");
    }
    public static DatabaseReference getBlockRef() {
        return getBaseRef().child("Block");
    }
}
