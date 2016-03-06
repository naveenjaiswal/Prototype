package com.li8tech.nli8.prototype;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.li8tech.nli8.prototype.adapter.DoctorAdapter;
import com.li8tech.nli8.prototype.pojo.Pojo;
import com.li8tech.nli8.prototype.pojo.Pojo.Doctor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * A placeholder fragment containing a simple view.
 */
public class MedicalCenterFragment extends Fragment {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private String medcUrl = Pojo.API_BASE_URL + "doctors/";
    RecyclerView rvDoctors;
    private ProgressDialog dialog;

    public MedicalCenterFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(getContext());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading Medical Center data !!! please wait...");
        dialog.show();

        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();

        GsonRequest<Doctor[]> gsonRequest = new GsonRequest<Doctor[]>(medcUrl,Doctor[].class,new HashMap<String,String>(),createNewAdapter(),handleException(), Request.Method.GET,true);

        requestQueue.add(gsonRequest);

    }

    private Response.Listener<Doctor[]> createNewAdapter() {
        return new Response.Listener<Doctor[]> () {
            @Override
            public void onResponse(Doctor[] response) {
                if(response.length == 0){
                    Toast.makeText(getContext(), R.string.no_data_found, Toast.LENGTH_LONG).show();
                }
                rvDoctors = (RecyclerView) getView().findViewById(R.id.rvDoctors);

                // Create adapter passing in the sample user data
                DoctorAdapter adapter = new DoctorAdapter(response);
                // Attach the adapter to the recyclerview to populate items
                rvDoctors.setAdapter(adapter);
                // Set layout manager to position the items
                rvDoctors.setLayoutManager(new LinearLayoutManager(getContext()));

                // Add separator
                rvDoctors.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
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
        return inflater.inflate(R.layout.fragment_medical_center, container, false);
    }
}
