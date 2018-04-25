package com.example.anushan.tourguide;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserMainDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;
    private TextView username1,usernameEmail;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String us1,usEmail1;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fragment= new Historical_site_whole_View();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, fragment).addToBackStack(null);
        ft.commit();
        setContentView(R.layout.activity_user_main_dashboard);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        username1 = (TextView)header.findViewById(R.id.username1);
        usernameEmail= (TextView)header.findViewById(R.id.username2);
        imageView= (ImageView)header.findViewById(R.id.img1);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().getRoot();
        databaseReference.child("User").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                us1= (String) dataSnapshot.child("username").getValue();
                usEmail1= (String) dataSnapshot.child("email").getValue();
                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color1 = generator.getRandomColor();
                TextDrawable drawable1 = TextDrawable.builder()
                        .buildRound(String.valueOf(us1.charAt(0)).toUpperCase(), color1); // radius in px

                username1.setText(us1);
                usernameEmail.setText(usEmail1);
                imageView.setImageDrawable(drawable1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_main_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment=new Historical_site_whole_View();

        } else if (id == R.id.nav_hostorical_site_on_Map) {
            fragment=new HistoricalSitesOnMapView();

        } else if (id == R.id.nav_search_by_location) {
            fragment=new HistoricalSitesByLocation();

        } else if (id == R.id.nav_search_by_name) {
            fragment=new HistoricalSiteSearchByName();

        } else if (id == R.id.nav_account) {
            fragment=new UserAccout();
        } else if (id == R.id.nav_share) {

        }else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent= new Intent(UserMainDashboard.this,LoginActivity.class);
            startActivity(intent);
            finish();

        }if(fragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment).addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
