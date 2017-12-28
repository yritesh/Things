package com.example.ritesh.things;

import android.content.Context;
import android.icu.text.TimeZoneFormat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Ritesh on 18-12-2017.
 */

public class ProductListRV extends RecyclerView.Adapter<ProductListRV.ProductListViewHolder> {
    private String personId, listId;
    private ArrayList<Object> productList;
    private Context context;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://things-d6475.firebaseio.com/products");

    ProductListRV(ArrayList<Object> productList, String personId, String listId, Context context){
        this.personId = personId;
        this.listId = listId;
        this.context = context;
        this.productList = productList;
    }


    @Override
    public ProductListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_list_item_layout, parent, false);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductListViewHolder holder, int position) {

            final DataSnapshot dataSnapshot = (DataSnapshot) productList.get(position);


            rootRef.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnap) {
                    holder.txtTitleTest.setText(dataSnap.child("name").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        final String val = dataSnapshot.getValue().toString();
            holder.txtValTest.setText(val);
            holder.addSupply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int valInt = Integer.parseInt(val);
                    valInt++;
                    Object obj = valInt;
                    FirebaseDatabase.getInstance().getReferenceFromUrl("https://things-d6475.firebaseio.com/active_list/" + listId + "/products/" + dataSnapshot.getKey()).getRef().setValue(obj);
                }
            });
            holder.removeSupply.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int valInt = Integer.parseInt(val);
                    if(valInt !=  0){
                        valInt--;
                        Object obj = valInt;
                        FirebaseDatabase.getInstance().getReferenceFromUrl("https://things-d6475.firebaseio.com/active_list/" + listId + "/products/" + dataSnapshot.getKey()).getRef().setValue(obj);
                    }else {
                        Toast.makeText(context,"This product finished bro!",Toast.LENGTH_SHORT).show();
                    }

                }
            });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductListViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitleTest, txtValTest;
        Button addSupply, removeSupply;
        public ProductListViewHolder(View itemView) {

            super(itemView);
            txtTitleTest = (TextView) itemView.findViewById(R.id.txtTitleTest);
            txtValTest = (TextView) itemView.findViewById(R.id.txtValTest);
            addSupply = (Button) itemView.findViewById(R.id.addSupply);
            removeSupply = (Button) itemView.findViewById(R.id.removeSupply);
        }
    }
}
