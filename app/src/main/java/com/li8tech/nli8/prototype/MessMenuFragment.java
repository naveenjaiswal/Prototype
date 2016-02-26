package com.li8tech.nli8.prototype;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.li8tech.nli8.prototype.adapter.MessMenuAdapter;
import com.li8tech.nli8.prototype.pojo.Pojo;
import com.li8tech.nli8.prototype.pojo.Pojo.MessMenu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * A placeholder fragment containing a simple view.
 */
public class MessMenuFragment extends Fragment {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String messUrl = Pojo.API_BASE_URL + "messmenu/";
    RecyclerView rvMessMenu;

    public MessMenuFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        messUrl = messUrl + "?date="+today;
        GsonRequest<MessMenu[]> gsonRequest = new GsonRequest<MessMenu[]>(messUrl,MessMenu[].class,new HashMap<String,String>(),createNewAdapter(),handleException(), Request.Method.GET);
        requestQueue.add(gsonRequest);
    }

    private Response.Listener<MessMenu[]> createNewAdapter() {
        return new Response.Listener<MessMenu[]> () {
            @Override
            public void onResponse(MessMenu[] response) {
                if(response.length == 0){
                    Toast.makeText(getContext(), R.string.no_data_found, Toast.LENGTH_LONG).show();

                }
                rvMessMenu = (RecyclerView) getView().findViewById(R.id.rvMessMenu);

                // Create adapter passing in the sample user data
                MessMenuAdapter adapter = new MessMenuAdapter(response);
                // Attach the adapter to the recyclerview to populate items
                rvMessMenu.setAdapter(adapter);
                // Set layout manager to position the items
                rvMessMenu.setLayoutManager(new LinearLayoutManager(getContext()));

                // Add separator
                rvMessMenu.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
                // That's all!
            }


        };
    }

    private Response.ErrorListener handleException() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print(error.getStackTrace());
            }
        };
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_mess_menu, container, false);

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        TextView date = (TextView) returnView.findViewById(R.id.date);
        date.setText("Menu for " + today);

        return returnView;
    }
}
