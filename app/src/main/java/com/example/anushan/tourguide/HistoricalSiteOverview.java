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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class HistoricalSiteOverview extends Fragment {
    private View view;
    private ImageView ReviewImage;
    private TextView reviewname,information;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Bundle bundle;
    private String userName;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private ImageView mSelectImage;

    private Button mSubmitBtn,startAR;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;

    private Uri mImageUri = null;


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
        recyclerView=(RecyclerView)view.findViewById(R.id.userHisPhoto);
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
//                startActivityForResult(intent,
////                        CAMERA_REQUEST_CODE);
//                startActivityForResult(intent,
//                        GALLERY_REQUEST);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//

                startActivityForResult(Intent.createChooser(intent, "Select Picture"),CAMERA_REQUEST_CODE);



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
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mSelectImage.setImageBitmap(bitmap);

            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().getRoot();
        Query query= databaseReference.child("UserUploadedImages").child(bundle.getString("name"));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("ggggg", String.valueOf(dataSnapshot));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerOptions<UsersHistoricalPhoto> options =
                new FirebaseRecyclerOptions.Builder<UsersHistoricalPhoto>()
                        .setQuery(query, UsersHistoricalPhoto.class)
                        .build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<UsersHistoricalPhoto,UserImageHolder>(options) {
            @Override
            public UserImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_historical_site_photo, parent, false);
                return new UserImageHolder(view1);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserImageHolder holder, int position, @NonNull UsersHistoricalPhoto model) {
                Picasso.with(getContext()).load(model.getImage()).fit().into(holder.imageView);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    public static  class UserImageHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private View view;
        public UserImageHolder(View itemView) {
            super(itemView);
            view=itemView;
            imageView=(ImageView)view.findViewById(R.id.imageView2);
        }
    }
}


