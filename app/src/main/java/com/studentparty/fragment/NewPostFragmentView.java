package com.studentparty.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.studentparty.R;
import com.studentparty.controller.utils.FirebaseUtil;
import com.studentparty.controller.utils.SharePref;
import com.studentparty.model.EventPost;
import com.studentparty.model.People;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewPostFragmentView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewPostFragmentView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPostFragmentView extends Fragment {
    private static final String TAG = "NewPostTaskFragment";
    public interface TaskCallbacks {
        void onBitmapResized(Bitmap resizedBitmap, int mMaxDimension);
        void onBitmapResized(Bitmap resizedBitmap, int mMaxDimension, ImageView image);
        void onArraylistBitmap(ArrayList<Bitmap> resizedBitmap, int mMaxDimension);
        void onPostUploaded(String error);
        void onPostUploadImageAndVideo(String url, String type);
        void onPostVideoUpload(String url);
    }

    private Context mApplicationContext;
    private TaskCallbacks mCallbacks;


    private ImageView imageData;

    private OnFragmentInteractionListener mListener;
    StorageReference storageRef;

    public NewPostFragmentView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment NewPostFragmentView.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPostFragmentView newInstance() {
                return new NewPostFragmentView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        SharePref.init(getContext());
        storageRef = FirebaseStorage.getInstance().getReference();

    }



    public void resizeBitmap(String[] uri, int maxDimension, ImageView image, int height) {
        //imageData=image;
        GetXmlBitmap task = new GetXmlBitmap(maxDimension,height);
        task.execute(uri);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_post_fragment_view, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TaskCallbacks) {
            mCallbacks = (TaskCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TaskCallbacks");
        }
        mApplicationContext = context.getApplicationContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




    class GetXmlBitmap extends AsyncTask<String, Void, ArrayList<Bitmap>>

    {
        private int mMaxDimension;
        private int mHeight;


        public GetXmlBitmap(int maxDimension,int height) {
            mMaxDimension = maxDimension;
            mHeight=height;
        }
        // Decode image in background.
        @Override
        protected ArrayList<Bitmap> doInBackground(String... params) {
           //
            if (params != null) {
                // TODO: Currently making these very small to investigate modulefood bug.
                // Implement thumbnail + fullsize later.
                ArrayList<Bitmap> map = new ArrayList<Bitmap>();
                try {
                    for (String url : params) {

                        Uri uri = Uri.fromFile(new File(url));

                        map.add(decodeSampledBitmapFromUri(uri, mMaxDimension, mHeight));
                    }
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "Can't find file to resize: " + e.getMessage());

                } catch (IOException e) {
                    Log.e(TAG, "Error occurred during resize: " + e.getMessage());

                }
                return map;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmap) {
                 mCallbacks.onArraylistBitmap(bitmap,mMaxDimension);
        }
    }

    public void uploadVideoPost(Bitmap bitmap, String inBitmapPath, String inThumbnailPath,
                                String inFileName) {
        UploadPostTask uploadTask = new UploadPostTask(bitmap, inBitmapPath, inThumbnailPath, inFileName);
        uploadTask.execute();
    }
    class UploadPostTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Bitmap> bitmapReference;


        private String fileName;
        private String bitmapPath;
        private String thumbnailPath;

        public UploadPostTask(Bitmap bitmap, String inBitmapPath, String inThumbnailPath,
                              String inFileName) {
            bitmapReference = new WeakReference<Bitmap>(bitmap);


            fileName = inFileName;
            bitmapPath = inBitmapPath;
            thumbnailPath = inThumbnailPath;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            Bitmap fullSize = bitmapReference.get();

            if (fullSize == null ) {
                return null;
            }
            FirebaseStorage storageRef = FirebaseStorage.getInstance();
            StorageReference photoRef = storageRef.getReferenceFromUrl("gs://" + getString(R.string.google_storage_bucket));


            Long timestamp = System.currentTimeMillis();
            final StorageReference fullSizeRef = photoRef.child(FirebaseUtil.getCurrentUserId()).child("full").child(timestamp.toString()).child(fileName + ".jpg");
            final StorageReference thumbnailRef = photoRef.child(FirebaseUtil.getCurrentUserId()).child("thumb").child(timestamp.toString()).child(fileName + ".jpg");
            Log.d(TAG, fullSizeRef.toString());
            Log.d(TAG, thumbnailRef.toString());

            ByteArrayOutputStream fullSizeStream = new ByteArrayOutputStream();
            fullSize.compress(Bitmap.CompressFormat.JPEG, 90, fullSizeStream);
            byte[] bytes = fullSizeStream.toByteArray();
            fullSizeRef.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri fullSizeUrl = taskSnapshot.getDownloadUrl();
                  if(null!=fullSizeUrl.toString())
                   mCallbacks.onPostVideoUpload(fullSizeUrl.toString());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    mCallbacks.onPostUploaded(mApplicationContext.getString(
                            R.string.error_upload_task_create));
                }
            });
            // TODO: Refactor these insanely nested callbacks.
            return null;
        }
    }

    public Bitmap decodeSampledBitmapFromUri(Uri fileUri, int reqWidth, int reqHeight)
            throws IOException {
        InputStream stream = new BufferedInputStream(
                mApplicationContext.getContentResolver().openInputStream(fileUri));
        stream.mark(stream.available());
        BitmapFactory.Options options = new BitmapFactory.Options();
        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, null, options);
        stream.reset();
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        BitmapFactory.decodeStream(stream, null, options);
        // Decode bitmap with inSampleSize set
        stream.reset();
        return BitmapFactory.decodeStream(stream, null, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void uploadPost(final String imageUrl, String type, String inFileName, final String inPostText, String category, String typedata, final String videoID, final String tittle) {

        final DatabaseReference ref = FirebaseUtil.getBaseRef();
        DatabaseReference postsRef = FirebaseUtil.getPostsRef();
        final String newPostKey = postsRef.push().getKey();

        DatabaseReference mRefernce = FirebaseUtil.getCurrentUserRef();
        mRefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                People mPeople=dataSnapshot.getValue(People.class);
                EventPost newPost=null;

                newPost = new EventPost(mPeople, tittle, inPostText,
                        imageUrl,videoID,"13 sep 2018","Chennai", ServerValue.TIMESTAMP);


                Map<String, Object> updatedUserData = new HashMap<>();
                updatedUserData.put(FirebaseUtil.getPeoplePath() + FirebaseUtil.getCurrentUserId() + "/posts/"
                        + newPostKey, true);
                updatedUserData.put(FirebaseUtil.getPostsPath() + newPostKey,
                        new ObjectMapper().convertValue(newPost, Map.class));
                ref.updateChildren(updatedUserData, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference databaseReference) {
                        if (firebaseError == null) {

                            mCallbacks.onPostUploaded(null);
                        } else {
                            Log.e(TAG, "Unable to create new post: " + firebaseError.getMessage());

                            mCallbacks.onPostUploaded(mApplicationContext.getString(
                                    R.string.error_upload_task_create));
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



}
