package com.li8tech.nli8.prototype;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.li8tech.nli8.prototype.pojo.Category;
import com.li8tech.nli8.prototype.pojo.Contact;
import com.li8tech.nli8.prototype.pojo.Notice;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    Intent intent ;
    int loc;
    Notice notice;
    TextView titleTV,bodyTV,deptTV,eventDateTV,venueTV,contactTV,deadlineTV;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent =  getActivity().getIntent();
        notice =  (Notice)intent.getParcelableExtra("notice");

    }



    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflatedView =  inflater.inflate(R.layout.fragment_details, container, false);

        titleTV = (TextView)myInflatedView.findViewById(R.id.title_content);
        titleTV.setText(notice.title);
        // Set Title
        getActivity().setTitle(notice.title);

        bodyTV = (TextView)myInflatedView.findViewById(R.id.body_content);
        bodyTV.setText(notice.body);
        deptTV = (TextView)myInflatedView.findViewById(R.id.department_content);

        StringBuilder category = new StringBuilder();

        for(Category c:notice.category){
            category.append(c.name);
            category.append(" ");
        }

        StringBuilder con = new StringBuilder();

        for(Contact c:notice.contact){
            con.append(c.firstName);
            con.append(" ");
            con.append(c.lastName);
            con.append(" : ");
            con.append(c.email);

        }
        deptTV.setText(category);
        eventDateTV = (TextView)myInflatedView.findViewById(R.id.eventdate_content);
        eventDateTV.setText(notice.eventDate);
        venueTV = (TextView)myInflatedView.findViewById(R.id.venu_content);
        venueTV.setText(notice.venue);
        contactTV = (TextView)myInflatedView.findViewById(R.id.contact_content);
        contactTV.setText(con);
        deadlineTV = (TextView)myInflatedView.findViewById(R.id.deadline_content);
        deadlineTV.setText(notice.deadline);


        return myInflatedView;

    }
}
