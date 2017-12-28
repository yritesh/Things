package com.example.ritesh.things;

import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private TextView usernameHomeActivity;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usernameHomeActivity = (TextView) findViewById(R.id.usernameHomeActivity);
        recyclerView = (RecyclerView) findViewById(R.id.listsRV);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        getUserData();

    }

    private void getUserData(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            final String personId = acct.getId();
            Log.d("id:", personId);


            String helloName = "Hi, " + personName + "!";
            usernameHomeActivity.setText(helloName);

            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://things-d6475.firebaseio.com/members");


            Query twoRef = rootRef.child(personId).orderByKey();



            twoRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_SHORT).show();
                    String value = dataSnapshot.getKey();
                    //arrayList.add("new");
                    arrayList.add(value);
                    recyclerView.setAdapter(new listsRV(arrayList, personId, HomeActivity.this));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
