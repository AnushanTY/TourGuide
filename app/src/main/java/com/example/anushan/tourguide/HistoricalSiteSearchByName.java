package com.example.anushan.tourguide;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class HistoricalSiteSearchByName extends Fragment {
    private  View view;
    private EditText editText;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private  RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.activity_historical_site_search_by_name, container, false);
        editText=(EditText)view.findViewById(R.id.serach);
        recyclerView=(RecyclerView)view.findViewById(R.id.serachbyHisName);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference().getRoot();

                Query query = ref.child("HistricalPlaces").orderByChild("Name").startAt(str).endAt(str + "\uf8ff");
                FirebaseRecyclerOptions<Search_By_His_Name_one_item> options =
                        new FirebaseRecyclerOptions.Builder<Search_By_His_Name_one_item>()
                                .setQuery(query, Search_By_His_Name_one_item.class)
                                .build();



                firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Search_By_His_Name_one_item,SearchViewHolder>(options) {
                    @Override
                    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_by_name_item, parent, false);
                        return new SearchViewHolder(view1);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull Search_By_His_Name_one_item model) {
                        Picasso.with(getContext()).load(model.getImageUrl()).fit().into(holder.imageView);
                        holder.name.setText(model.getName());
                        holder.locationName.setText(model.getDistrict());
                    }
                };
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(firebaseRecyclerAdapter);
                firebaseRecyclerAdapter.startListening();


            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public  static class SearchViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView name,locationName;
        View view;
        public SearchViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            imageView=(ImageView)view.findViewById(R.id.imageView1);
            name=(TextView)view.findViewById(R.id.hisNmae);
            locationName=(TextView)view.findViewById(R.id.hisLocation);
        }
    }
}
