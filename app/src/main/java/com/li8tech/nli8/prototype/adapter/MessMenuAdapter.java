package com.li8tech.nli8.prototype.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.li8tech.nli8.prototype.MainActivity;
import com.li8tech.nli8.prototype.MyApplication;
import com.li8tech.nli8.prototype.R;
import com.li8tech.nli8.prototype.VolleySingleton;
import com.li8tech.nli8.prototype.pojo.Contact;
import com.li8tech.nli8.prototype.pojo.Notice;
import com.li8tech.nli8.prototype.pojo.Pojo;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
        public TextView time;
        public FlowLayout fooditems;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            fooditems = (FlowLayout) itemView.findViewById(R.id.fooditems);

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

        if(Pojo.timeTypeMap.containsKey(messMenu.time)){
            viewHolder.time.setText(Pojo.timeTypeMap.get(messMenu.time));
        }else{
            viewHolder.time.setText("Default");
        }

        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);

        Iterator<Pojo.Item> iterFoodItem = messMenu.items.iterator();
        while(iterFoodItem.hasNext())
        {
            Pojo.Item item = iterFoodItem.next();
            TextView t = new TextView(MyApplication.getAppContext());
            t.setLayoutParams(params);
            t.setPadding(5, 5, 5, 5);
            t.setText(item.name);
            t.setTextColor(Color.WHITE);
            t.setBackgroundColor(Color.GRAY);
            viewHolder.fooditems.addView(t);
        }
    }
    // Return the total count of items
    @Override
    public int getItemCount() {
        return messMenus.size();
    }
}