package com.studentparty.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.studentparty.R;
import com.studentparty.base.BaseActivity;
import com.studentparty.controller.listner.PermissionCallBack;
import com.studentparty.controller.utils.FirebaseUtil;
import com.studentparty.controller.utils.Utils;
import com.studentparty.fragment.NewPostFragmentView;
import com.studentparty.mediachooser.MediaChooser;
import com.studentparty.mediachooser.Utilities.MediaChooserConstants;
import com.studentparty.mediachooser.activity.HomeScreenMediaChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PostUpload extends BaseActivity implements View.OnClickListener,NewPostFragmentView.TaskCallbacks  {
    public static final String POST_CATEGORY = "category";
    public static final String TAG_TASK_FRAGMENT = "NewPostFragmentView";
    private NewPostFragmentView mTaskFragment;
    PercentRelativeLayout mPercentageLayout,donebtn;
    StringBuffer imageUploadData=null;
    String videoImageUrl=null;
    private Uri mDownloadUrl = null;
    List<String> imageUru=new ArrayList<>();
    private StringBuffer mImageUrlData=null;
    private String videoUrl="";
    StorageReference storageRef;
    UploadTask uploadTask=null;
    ImageView image,image1,image2,image3,image4;

    List<Bitmap> arrayBitmap=new ArrayList<>();
    ImageView[] imageArray;

    int screenHeight=0;
    int postUploadScreenHeight=0;
    int screenWidth=0;
    int postUploadScreenWidth=0;
    EditText postText,postTittle;
    ImageView uploadImage;
    TextView uploadContent;
    String cateGory;
    ImageView videoimage,backBtn;
    Bitmap videoBitmap=null;

    String[] permissionRequired={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    ProgressBar pb;
    boolean isImageVidePicke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);
        isImageVidePicke=false;
        pb=(ProgressBar) findViewById(R.id.pb);
        pb.bringToFront();
         cateGory = getIntent().getStringExtra(POST_CATEGORY);
        if (cateGory == null) {
            finish();
        }
        backBtn=(ImageView) findViewById(R.id.btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        FragmentManager fm = getSupportFragmentManager();
        storageRef = FirebaseStorage.getInstance().getReference();
        mImageUrlData=new StringBuffer();
        if (mTaskFragment == null) {
            // add the fragment
            mTaskFragment = new NewPostFragmentView();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

        screenHeight= Utils.getHeight(this);
        screenWidth=Utils.getWidth(this);
        postUploadScreenWidth=94*screenWidth/100;
        postUploadScreenHeight=35*screenHeight/100;
        postText=(EditText)findViewById(R.id.login_ed_username);
        postTittle=(EditText)findViewById(R.id.tittleedit);
        mPercentageLayout=(PercentRelativeLayout) findViewById(R.id.imageupload);
        mPercentageLayout.setOnClickListener(this);
        donebtn=(PercentRelativeLayout) findViewById(R.id.donebtn);
        donebtn.setOnClickListener(this);

        imageUploadData=new StringBuffer();
        videoimage=(ImageView) findViewById(R.id.videoimage);
        image=(ImageView)findViewById(R.id.mulpleimage);
        image1=(ImageView)findViewById(R.id.mulpleimagesecond);
        image2=(ImageView)findViewById(R.id.mulpleimagethree);
        image3=(ImageView)findViewById(R.id.mulpleimagefour);
        image4=(ImageView)findViewById(R.id.mulpleimagefive);


        imageArray= new ImageView[]{image, image1,image2,image3,image4};
        uploadContent=(TextView)findViewById(R.id.uploadcontent);
        uploadImage=(ImageView)findViewById(R.id.imagupload);


        selectRegisterView();



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
               MediaChooser.setSelectionLimit(5);
               HomeScreenMediaChooser.startMediaChooser(PostUpload.this, false);
           }

           @Override
           public void permissionDenied() {

               Toast.makeText(getApplicationContext(),"Permission is denied by you", Toast.LENGTH_LONG).show();

           }
       });
   }
    private void selectRegisterView() {

        IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(videoBroadcastReceiver, videoIntentFilter);

        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);
    }


    BroadcastReceiver videoBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            arrayBitmap.clear();
            for(int i=0;i<imageArray.length;i++)
            {
                imageArray[i].setVisibility(View.INVISIBLE);
            }
            isImageVidePicke=true;
            imageUru=intent.getStringArrayListExtra("list");
            loadBitmap(imageUru.get(0));


        }
    };


    BroadcastReceiver imageBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            arrayBitmap.clear();
            isImageVidePicke=true;
          String[] array = intent.getStringArrayListExtra("list").toArray(new String[0]);
            int width=postUploadScreenWidth*50/100;
            int height=postUploadScreenHeight*50/100;
            mTaskFragment.resizeBitmap(array, width,image,height);

            imageUru=intent.getStringArrayListExtra("list");



        }
    };


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.donebtn) {

            if(!isImageVidePicke)
            {
                Toast.makeText(this,"Please upload Image/Video", Toast.LENGTH_LONG).show();
            }else if(postTittle.getText().length()<1)
            {
                Toast.makeText(this,"Please enter tittle", Toast.LENGTH_LONG).show();
            }else {
                pb.setVisibility(View.VISIBLE);
                Long timestamp = System.currentTimeMillis();
                if (videoBitmap != null) {
                    String bitmapPath = "/" + FirebaseUtil.getCurrentUserId() + "/full/" + timestamp.toString() + "/";
                    String thumbnailPath = "/" + FirebaseUtil.getCurrentUserId() + "/thumb/" + timestamp.toString() + "/";

                    Log.d("bitmapPath", bitmapPath);
                    mTaskFragment.uploadVideoPost(videoBitmap, bitmapPath, thumbnailPath, "video");


                } else
                    dataUpload();
            }



        }else {

            callPermission();
        }
    }

    private void dataUpload() {
        for(int i=0;i<imageUru.size();i++) {
            Uri uri = Uri.fromFile(new File(imageUru.get(i)));

            uploadFromUri(uri, "image",imageUru.size(),i);

        }


    }

    private void loadBitmap(String img_url)
    {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            Log.d("img_url",img_url);

            Uri uri = Uri.parse(img_url);
            String scheme = uri.getScheme();
            Log.d("uri",""+uri+" "+scheme);
            retriever.setDataSource(this,uri);
            videoBitmap = retriever.getFrameAtTime(100);
            videoimage.setImageBitmap(videoBitmap);
            videoimage.setVisibility(View.VISIBLE);

            Log.d("bitmappp", " "+videoBitmap.getWidth()+" "+videoBitmap.getHeight());
            uploadContent.setVisibility(View.INVISIBLE);
            uploadImage.setImageResource(R.drawable.video);
        } catch (IllegalArgumentException ex) {
// Assume this is a corrupt video file
            Log.d("ill",""+ex.getMessage());
        } catch (RuntimeException ex) {
// Assume this is a corrupt video file.
            Log.d("RuntimeException",""+ex.getMessage());
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
// Ignore failures while cleaning up.
                Log.d("RuntimeException",""+ex.getMessage());
            }
        }
    }

    private void uploadFromUri(Uri fileUri, final String type, final int size, final int i) {
      //  Toast.makeText(getApplicationContext(), fileUri.toString(), Toast.LENGTH_SHORT).show();

        final StorageReference photoRef = storageRef.child(type)
                .child(fileUri.getLastPathSegment());


        uploadTask= photoRef.putFile(fileUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                mDownloadUrl = null;


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size,
                if(null!=taskSnapshot.getMetadata().getDownloadUrl())
                mDownloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                if(type.equalsIgnoreCase("image"))
                    mImageUrlData.append(mDownloadUrl).append(",").toString();
                else
                    videoUrl=mDownloadUrl.toString();

                if(type.equalsIgnoreCase("image")) {
                    if (size - 1 == i) {
                        mTaskFragment.uploadPost(mImageUrlData.toString(), "image", mImageUrlData.toString(), postText.getText().toString(), cateGory, type, "",postTittle.getText().toString());
                    }
                }else{
                    mTaskFragment.uploadPost(videoImageUrl, "video", videoImageUrl, postText.getText().toString(), cateGory, type, videoUrl,postTittle.getText().toString());
                }



            }
        });
    }



    @Override
    public void onBitmapResized(Bitmap resizedBitmap, int mMaxDimension) {
        if (resizedBitmap == null) {

            Toast.makeText(getApplicationContext(), "Couldn't resize bitmap.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

    }

    @Override
    public void onBitmapResized(Bitmap resizedBitmap, int mMaxDimension, ImageView image1) {

        arrayBitmap.add(resizedBitmap);


        image1.setVisibility(View.VISIBLE);
        image1.setImageBitmap(resizedBitmap);


    }

    @Override
    public void onArraylistBitmap(ArrayList<Bitmap> resizedBitmap, int mMaxDimension) {

       for (int i=0;i<resizedBitmap.size();i++)
        { PercentRelativeLayout.LayoutParams layoutParams;
            int height = 2;
            int width = 2;
            if (resizedBitmap.size() == 1) {
                width = 1;
            }
            if (resizedBitmap.size() == 2 || resizedBitmap.size() == 1) {
                height = 1;
            }
            if (resizedBitmap.size() == 3 && i == 2) {
                layoutParams = new PercentRelativeLayout.LayoutParams(this.mPercentageLayout.getWidth(), this.mPercentageLayout.getHeight() / 1);
            } else if ((resizedBitmap.size() == 5 && i == 2) || ((resizedBitmap.size() == 5 && i == 3) || (resizedBitmap.size() == 5 && i == 4))) {
                layoutParams = new PercentRelativeLayout.LayoutParams(this.mPercentageLayout.getWidth() / 3, this.mPercentageLayout.getHeight() / 2);
            } else {
                layoutParams = new PercentRelativeLayout.LayoutParams(this.mPercentageLayout.getWidth() / width, this.mPercentageLayout.getHeight() / height);
            }
            if (i != 0) {
                if (i == 1) {
                    layoutParams.addRule(1, this.imageArray[0].getId());
                } else if (i == 2) {
                    layoutParams.addRule(3, this.imageArray[0].getId());
                } else if (i == 3) {
                    layoutParams.addRule(3, this.imageArray[1].getId());
                    layoutParams.addRule(1, this.imageArray[2].getId());
                } else if (i == 4) {
                    layoutParams.addRule(3, this.imageArray[1].getId());
                    layoutParams.addRule(1, this.imageArray[3].getId());
                }
            }
            this.imageArray[i].setLayoutParams(layoutParams);
            imageArray[i].setVisibility(View.VISIBLE);
            imageArray[i].setImageBitmap(resizedBitmap.get(i));
        }

        uploadImage.setVisibility(View.INVISIBLE);
        uploadContent.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onPostUploaded(final String error) {
        PostUpload.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                if (error == null) {
                    Toast.makeText(PostUpload.this, "EventPost Upload Successfully!", Toast.LENGTH_SHORT).show();



                    pb.setVisibility(View.INVISIBLE);
                    finish();
                } else {
                    Toast.makeText(PostUpload.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPostUploadImageAndVideo(String url, String type) {

        Log.d(TAG_TASK_FRAGMENT,url);
        imageUploadData.append(url.toString());

    }



    @Override
    public void onPostVideoUpload(String url) {

        videoImageUrl=url;
        for(int i=0;i<imageUru.size();i++) {
            Uri uri = Uri.fromFile(new File(imageUru.get(i)));
            //uploadFromUri(uri,"Video");
            uploadFromUri(uri, "video",imageUru.size(),i);

        }

    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(imageBroadcastReceiver);
        unregisterReceiver(videoBroadcastReceiver);
        super.onDestroy();
    }
}
