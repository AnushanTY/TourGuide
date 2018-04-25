package com.example.anushan.tourguide;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class HistoricalSiteOverview extends Fragment {
    private View view;
    private ImageView ReviewImage;
    private TextView reviewname,information;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Bundle bundle;
    private String userName;


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
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference().getRoot();
        return view;
    }




}
