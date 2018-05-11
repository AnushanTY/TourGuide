package com.example.anushan.tourguide;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

/**
 * Created by Anushan on 5/9/2018.
 */

public class ViewAllHistoricalSiteAdminView extends Fragment {

    private  View view;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.viewallhistoricalsitefor_admin,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycleAdmin);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().getRoot();

        Query query = ref.child("HistricalPlaces");
        FirebaseRecyclerOptions<Search_By_His_Name_one_item> options =
                new FirebaseRecyclerOptions.Builder<Search_By_His_Name_one_item>()
                        .setQuery(query, Search_By_His_Name_one_item.class)
                        .build();



        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Search_By_His_Name_one_item,AdminView>(options) {

            @Override
            public AdminView onCreateViewHolder(ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_historical_site_for_admin_view, parent, false);
                return new AdminView(view1);
            }

            @Override
            protected void onBindViewHolder(@NonNull AdminView holder, int position, @NonNull Search_By_His_Name_one_item model) {
                Picasso.with(getContext()).load(model.getImageUrl()).fit().into(holder.imageView);
                holder.name.setText(model.getName());
                holder.locationName.setText(model.getDistrict());
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

    public  static class AdminView extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView name,locationName;
        View view;
        public AdminView(View itemView) {
            super(itemView);
            view=itemView;
            imageView=(ImageView)view.findViewById(R.id.imageView4);
            name=(TextView)view.findViewById(R.id.hisNmae1);
            locationName=(TextView)view.findViewById(R.id.hisLocation1);
        }
    }
}
