package prajwal.lftassignment.com.leapfrogassignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import prajwal.lftassignment.com.leapfrogassignment.Adapters.FeedsAdapter;
import prajwal.lftassignment.com.leapfrogassignment.CustomViews.CircleImageView;
import prajwal.lftassignment.com.leapfrogassignment.Model.Feeds;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseReference;
    private List<Feeds> feedsList;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private CircleImageView ivProfileImage;
    private Uri imagePath;
    private RecyclerView rvFeedsLists;
    private FeedsAdapter feedsAdapter;
    private ProgressDialog progressDialog;
    private String EXTRA_MESSAGE = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        toolbar = (Toolbar) findViewById(R.id.toolbar_feeds);
        rvFeedsLists = (RecyclerView) findViewById(R.id.rv_feeds);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Feeds");
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference("feeds");
        ivProfileImage = (CircleImageView) findViewById(R.id.iv_profile_toolbar);
        progressDialog = new ProgressDialog(DashBoardActivity.this);
        feedsList = new ArrayList<Feeds>();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(DashBoardActivity.this, LoginActivity.class));
                    Toast.makeText(getApplicationContext(), "Session Expired. Please Login Again..", Toast.LENGTH_LONG).show();
                    finish();
                }else
                {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    imagePath = firebaseUser.getPhotoUrl();
                    Glide.with(getApplicationContext()).load(imagePath).asBitmap().placeholder(R.drawable.ic_person_grey).diskCacheStrategy(DiskCacheStrategy.RESULT).into(ivProfileImage);
                    fetchFeeds();
                }
            }
        };


        ivProfileImage.setOnClickListener(this);


    }

    private void fetchFeeds() {
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    progressDialog.dismiss();
                    for (DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                        Feeds feeds = childSnapShot.getValue(Feeds.class);
                        feedsList.add(feeds);
                    }
                    showFeeds();

                } catch (Exception e) {
                    Log.e("EXCEPTION", e.getLocalizedMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showFeeds() {
        if (feedsList.size() > 0) {
            rvFeedsLists.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            feedsAdapter = new FeedsAdapter(getApplicationContext(), feedsList);
            rvFeedsLists.setAdapter(feedsAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_profile_toolbar:
                Intent j = new Intent(DashBoardActivity.this, ProfileActivity.class);
                startActivity(j);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);

        }

    }


}



