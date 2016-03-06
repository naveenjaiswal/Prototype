package com.li8tech.nli8.prototype;

import android.app.ProgressDialog;
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
import com.li8tech.nli8.prototype.pojo.Pojo;
import com.li8tech.nli8.prototype.pojo.Pojo.Movie;

import java.util.HashMap;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private String movieUrl = Pojo.API_BASE_URL + "movies/";
    RecyclerView rvMovies;
    private ProgressDialog dialog;

    public MovieFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getContext());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading Upcoming Movie !!! please wait...");
        dialog.show();

        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();

        GsonRequest<Movie[]> gsonRequest = new GsonRequest<Movie[]>(movieUrl,Movie[].class,new HashMap<String,String>(),createNewAdapter(),handleException(), Request.Method.GET,true);
        requestQueue.add(gsonRequest);
    }

    private Response.Listener<Movie[]> createNewAdapter() {
        return new Response.Listener<Movie[]> () {
            @Override
            public void onResponse(Movie[] response) {
                if(response.length == 0){
                    Toast.makeText(getContext(), R.string.no_data_found, Toast.LENGTH_LONG).show();
                }
                rvMovies = (RecyclerView) getView().findViewById(R.id.rvMovies);

                // Create adapter passing in the sample user data
                MovieAdapter adapter = new MovieAdapter(response);
                // Attach the adapter to the recyclerview to populate items
                rvMovies.setAdapter(adapter);
                // Set layout manager to position the items
                rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));

                // Add separator
                rvMovies.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
                // That's all!
                dialog.dismiss();
            }


        };
    }

    private Response.ErrorListener handleException() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print(error.getStackTrace());
                dialog.dismiss();
            }
        };
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }
}
