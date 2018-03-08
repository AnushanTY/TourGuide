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
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by Anushan on 3/8/2018.
 */

public class Historical_site_whole_View extends Fragment {
    private  View view;
    private  RecyclerView recyclerView;
    private  FirebaseRecyclerAdapter adapter;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.historical_site_whole_view,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycle);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().getRoot();
        Query query=databaseReference.child("HistricalPlaces");
        progressDialog= new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.show();


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
            protected void onBindViewHolder(@NonNull HistoricalSiteHolder holder, int position, @NonNull Historical_Site model) {

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();

                }
                Picasso.with(getContext()).load(model.getImageUrl()).fit().into(holder.imageView);
                holder.name.setText(model.getName());
                holder.locationName.setText(model.getDistrict());
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


    public  static class HistoricalSiteHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name,locationName;
        View view;
        public HistoricalSiteHolder(View itemView) {
            super(itemView);
            view=itemView;
            imageView=(ImageView)view.findViewById(R.id.image);
            name=(TextView)view.findViewById(R.id.name);
            locationName=(TextView)view.findViewById(R.id.locationName);
        }
    }

}
