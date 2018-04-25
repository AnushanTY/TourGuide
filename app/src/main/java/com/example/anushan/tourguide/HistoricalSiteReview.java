package com.example.anushan.tourguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class HistoricalSiteReview extends Fragment {
    private View view;
    private ImageView ReviewImage;
    private ImageButton ReviewSend;
    private TextView reviewname;
    private EditText reviewuser;
    private RecyclerView reviewRecyler;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Bundle bundle;
    private String userName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        view= inflater.inflate(R.layout.fragment_historical_site_review, container, false);

        ReviewImage=(ImageView)view.findViewById(R.id.reviewImage);
        ReviewSend=(ImageButton)view.findViewById(R.id.reviewSend);
        reviewname=(TextView)view.findViewById(R.id.Reviewname);
        reviewuser=(EditText)view.findViewById(R.id.reviewUser);
        reviewRecyler=(RecyclerView)view.findViewById(R.id.reviewRecycle);
        bundle=this.getArguments();
        Picasso.with(getContext()).load(bundle.getString("image")).fit().into(ReviewImage);
        reviewname.setText(bundle.getString("name"));
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference().getRoot();
        databaseReference1.child("User").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userName= (String) dataSnapshot.child("username").getValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
             

            }
        });

        ReviewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(reviewuser.getText().toString())) {
                    Toast.makeText(getContext(), "Enter comment!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Review review= new Review(reviewuser.getText().toString(),userName);
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Review").child(bundle.getString("name"))
                        .child(firebaseUser.getUid()).setValue(review, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {

                            Toast.makeText(getContext(), "Data Insert Error!", Toast.LENGTH_SHORT).show();
                            reviewuser.setText("");

                        } else {
                            Toast.makeText(getContext(), "Data Insert success!", Toast.LENGTH_SHORT).show();
                            reviewuser.setText("");

                        }
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().getRoot();
        Query query=databaseReference.child("Review").child(bundle.getString("name"));

        FirebaseRecyclerOptions<Review> options =
                new FirebaseRecyclerOptions.Builder<Review>()
                        .setQuery(query, Review.class)
                        .build();

        adapter= new FirebaseRecyclerAdapter<Review, reviewHolder>(options) {
            @Override
            public reviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_review, parent, false);
                return new reviewHolder(view1);
            }

            @Override
            protected void onBindViewHolder(@NonNull reviewHolder holder, int position, @NonNull Review model) {
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color1 = generator.getRandomColor();
                TextDrawable drawable1 = TextDrawable.builder()
                        .buildRound(String.valueOf(model.getUsername().charAt(0)).toUpperCase(), color1); // radius in px
            holder.letterimage.setImageDrawable(drawable1);
            holder.user.setText(model.getUsername());
            holder.usercomment.setText(model.getComment());
            }
        };

        reviewRecyler.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewRecyler.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
        reviewRecyler.setAdapter(adapter);

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

    public static class reviewHolder extends RecyclerView.ViewHolder{
        View view;
        ImageView letterimage;
        TextView user,usercomment;
        public reviewHolder(View itemView) {
            super(itemView);
            view=itemView;
            letterimage=(ImageView)view.findViewById(R.id.letterimage);
            user=(TextView)view.findViewById(R.id.reviewuserID);
            usercomment=(TextView)view.findViewById(R.id.reviewabouthistorical);
        }
    }
}
