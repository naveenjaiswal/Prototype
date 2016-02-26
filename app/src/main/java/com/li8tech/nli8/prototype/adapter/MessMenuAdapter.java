package com.li8tech.nli8.prototype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.li8tech.nli8.prototype.R;
import com.li8tech.nli8.prototype.VolleySingleton;
import com.li8tech.nli8.prototype.pojo.Notice;
import com.li8tech.nli8.prototype.pojo.Pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hduser on 25/2/16.
 */
public class MessMenuAdapter extends RecyclerView.Adapter<MessMenuAdapter.ViewHolder> {


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView date;
        public TextView time;
        public TextView fooditems;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            fooditems = (TextView) itemView.findViewById(R.id.fooditems);

        }
    }

    // Store a member variable for the contacts
    private List<Pojo.MessMenu> messMenus;

    // Pass in the contact array into the constructor
    public MessMenuAdapter(Pojo.MessMenu[] messMenus) {

        this.messMenus = new ArrayList<Pojo.MessMenu>();
        this.messMenus.addAll(Arrays.asList(messMenus));

    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public MessMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.mess_menu_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MessMenuAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Pojo.MessMenu messMenu = messMenus.get(position);

        // Set item views based on the data model
        viewHolder.date.setText(messMenu.date);


        if(Pojo.timeTypeMap.containsKey(messMenu.time)){
            viewHolder.time.setText(Pojo.timeTypeMap.get(messMenu.time));
        }else{
            viewHolder.time.setText("Default");
        }


        StringBuilder itemsString = new StringBuilder();
        for(Pojo.Item item:messMenu.items){
            itemsString.append(item.name);
            itemsString.append(" ");
        }
        viewHolder.fooditems.setText(itemsString);
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return messMenus.size();
    }
}