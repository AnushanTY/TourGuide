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

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Anushan on 5/9/2018.
 */

public class ViewAllUser  extends Fragment{
    private  View view;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_all_user,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycleUser);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().getRoot();

        Query query = ref.child("User");
        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<User,ViewUser>(options) {
            @Override
            public ViewUser onCreateViewHolder(ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_user, parent, false);
                return new ViewUser(view1);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewUser holder, int position, @NonNull User model) {
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color1 = generator.getRandomColor();
                TextDrawable drawable1 = TextDrawable.builder()
                        .buildRound(String.valueOf(model.getUsername().charAt(0)).toUpperCase(), color1); // radius in px
                holder.imageView.setImageDrawable(drawable1);
                holder.name.setText(model.getUsername());
                holder.email.setText(model.getEmail());
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

    public  static class ViewUser extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView name,email;
        View view;
        public ViewUser(View itemView) {
            super(itemView);
            view=itemView;
            imageView=(ImageView)view.findViewById(R.id.imageView3);
            name=(TextView)view.findViewById(R.id.hisNmae2);
            email=(TextView)view.findViewById(R.id.hisLocation2);
        }
    }
}
