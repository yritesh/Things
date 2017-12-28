package com.example.ritesh.things;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ritesh on 14-12-2017.
 */

public class listsRV extends RecyclerView.Adapter<listsRV.ProgrammingViewHolder> {
    private String personId;
    private ArrayList<String> data;
    private Context context;
    public listsRV(ArrayList<String> data, String personId, Context context){
        this.data = data;
        this.personId = personId;
        this.context = context;
    }
    @Override
    public ProgrammingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProgrammingViewHolder holder, int position) {


        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://things-d6475.firebaseio.com/members/" + personId + "/" + data.get(position));
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child("nameList");
                DataSnapshot ds2 = dataSnapshot.child("typeList");
                String value = (String) ds.getValue();
                String type = (String) ds2.getValue();
                Log.d("log", value);
                holder.txtTitile.setText(value);
                switch (type){
                    case "office":{
                        holder.imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.home_icon));
                    }
                    break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ListPageActivity.class);
                i.putExtra("personId", personId);
                i.putExtra("listId", data.get(holder.getAdapterPosition()));
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ProgrammingViewHolder extends RecyclerView.ViewHolder{
        ImageView imgIcon;
        TextView txtTitile;
        public ProgrammingViewHolder(View itemView) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            txtTitile = (TextView) itemView.findViewById(R.id.txtTitle);
        }
    }
}
