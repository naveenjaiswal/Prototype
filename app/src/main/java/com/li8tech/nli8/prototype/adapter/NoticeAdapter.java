package com.li8tech.nli8.prototype.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.li8tech.nli8.prototype.DetailsActivity;
import com.li8tech.nli8.prototype.R;
import com.li8tech.nli8.prototype.pojo.Notice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by FDUSER on 25-Feb-16.
 */
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {

    // Store a member variable for the contacts
    private List<Notice> notices;
    private Context context;
    private LayoutInflater inflater;

    // Pass in the contact array into the constructor
    public NoticeAdapter(Notice[] notices) {
        this.notices = new ArrayList<Notice>();
        this.notices.addAll(Arrays.asList(notices));
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.custom_row_notice, parent, false);

// Return a new holder instance
       MyViewHolder viewHolder = new MyViewHolder(contactView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // Get the data model based on position
        Notice notice= notices.get(position);

        // Set item views based on the data model
        TextView textViewTitle = holder.noticeTitleTv;
        textViewTitle.setText(notice.title);

        TextView textViewDept = holder.noticeDeptTv;
        textViewDept.setText(notice.venue);

        TextView textViewDate = holder.noticeDeadlineTv;
        textViewDate.setText(notice.eventDate);

    }




    @Override
    public int getItemCount() {
        return notices.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public TextView noticeTitleTv;
        public TextView noticeDeptTv;
        public TextView noticeDeadlineTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            noticeTitleTv= (TextView) itemView.findViewById(R.id.noticeTitle);
            noticeDeptTv = (TextView) itemView.findViewById(R.id.noticeSub);
            noticeDeadlineTv = (TextView) itemView.findViewById(R.id.noticeDeadline);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new  Intent(context, DetailsActivity.class);

            intent.putExtra("notice",notices.get(getLayoutPosition()));
            context.startActivity(intent);

        }
    }

    // Clean all elements of the recycler
    public void clear() {
        this.notices.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Notice> notices) {
        this.notices.addAll(notices);
        notifyDataSetChanged();
    }


}
