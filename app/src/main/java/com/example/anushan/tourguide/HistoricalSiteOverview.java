package com.example.anushan.tourguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.edmodo.cropper.CropImageView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;


public class HistoricalSiteOverview extends Fragment {
    private View view;
    private ImageView ReviewImage;
    private TextView reviewname,information;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Bundle bundle;
    private String userName;

    private ImageView mSelectImage;

    private Button mSubmitBtn,startAR;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;

    private Uri mImageUri = null;

    private static final  int GALLERY_REQUEST =1;

    private static final int CAMERA_REQUEST_CODE=1;

    private StorageReference mStorage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_historical_site_overview, container, false);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        ReviewImage=(ImageView)view.findViewById(R.id.overviewImage);
        reviewname=(TextView)view.findViewById(R.id.overviewname);
        information=(TextView)view.findViewById(R.id.information);
        bundle=this.getArguments();
        Picasso.with(getContext()).load(bundle.getString("image")).fit().into(ReviewImage);
        reviewname.setText(bundle.getString("name"));
        information.setText(bundle.getString("information"));

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("UserUploadedImages");

        mSelectImage = (ImageView)view. findViewById(R.id.galery);

        mSubmitBtn = (Button)view. findViewById(R.id.gallerySubmit);

        mProgress = new ProgressDialog(getContext());
        startAR=(Button)view.findViewById(R.id.startAR);

        startAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.anushan.AR");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAMERA_REQUEST_CODE);


            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
                mSelectImage.setBackgroundResource(R.drawable.ic_menu_camera);
                mSelectImage.setImageDrawable(null);
            }
        });

        return view;
    }


    private void startPosting(){

        mProgress.setMessage("Posting to photo...");


        if( mImageUri != null){

            mProgress.show();
            StorageReference filepath = mStorage.child("UserUploadedImages").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl =taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost = mDatabase.child(bundle.getString("name")).push();
                    newPost.child("image").setValue(downloadUrl.toString());

                    mProgress.dismiss();

                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mImageUri = data.getData();
                mSelectImage.setImageURI(mImageUri);
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // convert byte array to Bitmap

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);

                mSelectImage.setImageBitmap(bitmap);

            }
        }
    }
}


