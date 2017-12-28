package com.example.ritesh.things;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListPageActivity extends AppCompatActivity {

    private String personId, listId;
    private RecyclerView recyclerView;
    private ArrayList<Object> productList = new ArrayList<>();
    private Button addProd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);

        final Intent intent = getIntent();
        personId = intent.getStringExtra("personId");
        listId = intent.getStringExtra("listId");
        addProd = (Button) findViewById(R.id.addProductButton);
        recyclerView = (RecyclerView) findViewById(R.id.products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListPageActivity.this, AddProduct.class);
                i.putExtra("personId", personId);
                i.putExtra("listId", listId);
                startActivity(i);
            }
        });

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://things-d6475.firebaseio.com/active_list/" + listId + "/products");

        rootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Taggg", dataSnapshot.toString());
                productList.add(dataSnapshot);
                Log.d("Logged", productList.toString());
                recyclerView.setAdapter(new ProductListRV(productList, personId, listId, ListPageActivity.this));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int index = 0;
                for(Object o: productList){
                    if(o.toString().contains(dataSnapshot.getKey()+",")){

                        break;
                    }
                    else{
                    index++;
                    }
                }
                Log.d("Tagggl", productList.toString());
                productList.set(index,dataSnapshot);
                recyclerView.getAdapter().notifyDataSetChanged();

              /*  productList.add(dataSnapshot);
                Log.d("Logged", productList.toString()); */

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
