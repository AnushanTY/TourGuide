package com.example.anushan.tourguide;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

/**
 * Created by Anushan on 3/8/2018.
 */

public class Historical_site_whole_View extends Fragment {
    private  View view;
    private  RecyclerView recyclerView;
    private  FirebaseRecyclerAdapter adapter;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private  int nolike;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.historical_site_whole_view,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycle);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().getRoot();
        Query query=databaseReference.child("HistricalPlaces");
        progressDialog= new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        FirebaseCrash.report(new Exception("My first Android non-fatal error"));

        FirebaseRecyclerOptions<Historical_Site> options =
                new FirebaseRecyclerOptions.Builder<Historical_Site>()
                        .setQuery(query, Historical_Site.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Historical_Site, HistoricalSiteHolder>(options) {

            @Override
            public HistoricalSiteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_historical_site, parent, false);
                return new HistoricalSiteHolder(view1);
            }

            @Override
            protected void onBindViewHolder(@NonNull HistoricalSiteHolder holder, int position, @NonNull final Historical_Site model) {

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();

                }

                int noLikes= findlike(model.getName());
                Log.w("ppppppp", String.valueOf((noLikes)));
                holder.NoLike.setText(String.valueOf(noLikes));
                Picasso.with(getContext()).load(model.getImageUrl()).fit().into(holder.imageView);
                holder.name.setText(model.getName());
                holder.locationName.setText(model.getDistrict());
                holder.likeButton.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        likeButton.setUnlikeDrawableRes(R.drawable.ic_favorite_black_24dp);
                        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference();
                        databaseReference1.child("Historical_site_like").child(model.getName()).child("UserID")
                                .setValue(firebaseUser.getUid());

                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        likeButton.setUnlikeDrawableRes(R.drawable.ic_favorite_border_black_24dp);
                        DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference();
                        databaseReference2.child("Historical_site_like").child(model.getName()).child("UserID")
                                .removeValue();

                    }
                });

                holder.location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putDouble("Log",model.getLongitude());
                        bundle.putDouble("Lat",model.getLatitude());
                        Fragment fragment=new OneHistoricalSiteOnMapView();
                        fragment.setArguments(bundle);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });

                holder.rate.setText(String.valueOf(model.getRate()));
                holder.ratingBar.setRating((float) model.getRate());
                holder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle= new Bundle();
                        bundle.putString("image",model.getImageUrl());
                        bundle.putString("name",model.getName());
                        Fragment fragment= new HistoricalSiteReview();
                        fragment.setArguments(bundle);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, fragment)
                                .addToBackStack(null)
                                .commit();

                    }
                });

                holder.OverView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle= new Bundle();
                        bundle.putString("image",model.getImageUrl());
                        bundle.putString("name",model.getName());
                        bundle.putString("information",model.getInformation());
                        Fragment fragment= new HistoricalSiteOverview();
                        fragment.setArguments(bundle);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, fragment)
                                .addToBackStack(null)
                                .commit();


                    }
                });
            }
        };
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public int findlike(String name){
        nolike=0;
        DatabaseReference databaseReference0=FirebaseDatabase.getInstance().getReference();
        databaseReference0.child("Historical_site_like").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Log.w("pppppppppp", String.valueOf(dataSnapshot1));
                    nolike++;
                }
                Log.w("ooooo", String.valueOf(nolike));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return  nolike;
    }


    public  static class HistoricalSiteHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name,locationName,rate,NoLike,NoComment,OverView;
        LikeButton likeButton;
        ImageButton location,comment;
        RatingBar ratingBar;
        View view;
        public HistoricalSiteHolder(View itemView) {
            super(itemView);
            view=itemView;
            imageView=(ImageView)view.findViewById(R.id.image);
            name=(TextView)view.findViewById(R.id.name);
            locationName=(TextView)view.findViewById(R.id.locationName);
            likeButton=(LikeButton)view.findViewById(R.id.like_button);
            location=(ImageButton)view.findViewById(R.id.location);
            ratingBar=(RatingBar)view.findViewById(R.id.ratingbar);
            rate=(TextView)view.findViewById(R.id.rate);
            NoLike=(TextView)view.findViewById(R.id.nolike);
            NoComment=(TextView)view.findViewById(R.id.nocomment);
            OverView=(TextView)view.findViewById(R.id.overview);
            comment=(ImageButton)view.findViewById(R.id.comment);
        }
    }

}
