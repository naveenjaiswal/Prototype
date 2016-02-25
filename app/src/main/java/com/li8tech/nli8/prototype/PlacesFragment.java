package com.li8tech.nli8.prototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.li8tech.nli8.prototype.adapter.MovieAdapter;
import com.li8tech.nli8.prototype.adapter.PlaceAdapter;
import com.li8tech.nli8.prototype.pojo.Pojo;
import com.li8tech.nli8.prototype.pojo.Pojo.Place;

import java.util.HashMap;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlacesFragment extends Fragment {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private String placesUrl = Pojo.API_BASE_URL + "places/";
    RecyclerView rvPlaces;

    public PlacesFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();

        GsonRequest<Place[]> gsonRequest = new GsonRequest<Place[]>(placesUrl,Place[].class,new HashMap<String,String>(),createNewAdapter(),handleException(), Request.Method.GET);

        requestQueue.add(gsonRequest);
        /*JsonArrayRequest request = new JsonArrayRequest(medcUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                parseJSONResponce(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }


        );

        requestQueue.add(request);*/
    }

    private Response.Listener<Place[]> createNewAdapter() {
        return new Response.Listener<Place[]> () {
            @Override
            public void onResponse(Place[] response) {
                if(response.length == 0){
                    Toast.makeText(getContext(), R.string.no_data_found, Toast.LENGTH_LONG).show();
                }
                rvPlaces = (RecyclerView) getView().findViewById(R.id.rvPlaces);

                // Create adapter passing in the sample user data
                PlaceAdapter adapter = new PlaceAdapter(response);
                // Attach the adapter to the recyclerview to populate items
                rvPlaces.setAdapter(adapter);
                // Set layout manager to position the items
                rvPlaces.setLayoutManager(new LinearLayoutManager(getContext()));

                // Add separator
                rvPlaces.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
                // That's all!
                rvPlaces.setAdapter(adapter);

                /*for (int i = 0; i < response.length; i++) {
                    Toast.makeText(MyApplication.getAppContext(),
                            "Movie : " + response[i].name,
                            Toast.LENGTH_SHORT).show();
                }*/
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
        return inflater.inflate(R.layout.fragment_place, container, false);
    }
}
