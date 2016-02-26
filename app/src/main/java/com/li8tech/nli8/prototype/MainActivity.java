package com.li8tech.nli8.prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.li8tech.nli8.prototype.adapter.NoticeAdapter;
import com.li8tech.nli8.prototype.pojo.LoggedInUser;
import com.li8tech.nli8.prototype.adapter.NoticeAdapter;
import com.li8tech.nli8.prototype.pojo.Notice;

import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private SwipeRefreshLayout swipeContainer;
    private GoogleApiClient mGoogleApiClient;
    private String noticeUrl =  "http://pilock.pythonanywhere.com/api/notice/";
    private TextView mTextView ;
    private RecyclerView recyclerView;
    private NoticeAdapter adapter;
    public Notice[] publicNotices;
    public SharedPreferences preferences;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading Notices !!! please wait...");
        if(getIntent().hasExtra("id")){
            noticeUrl = noticeUrl + getIntent().getExtras().getString("id","");
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if(getIntent().hasExtra("LoggedInUserDetails")){
            //LoggedInUser user=(LoggedInUser)(getIntent().getExtras().getString("LoggedInUserDetails"));
            LoggedInUser user;
            user = (LoggedInUser) getIntent().getParcelableExtra("LoggedInUserDetails");
            preferences =  MyApplication.getAppContext().getSharedPreferences("LogggedInUserName",Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("UserName",user.FullName);
                editor.commit();
                CharSequence welcomeMessage = "Welcome "+user.FullName;
                Toast toast = Toast.makeText(MyApplication.getAppContext(),welcomeMessage,Toast.LENGTH_SHORT);
                toast.show();

        }
        CheckLoggedInSession();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextView = (TextView)findViewById(R.id.mainTextView);
        recyclerView =  (RecyclerView)findViewById(R.id.noticesList);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Pull to refresh
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                GsonRequest<Notice[]> gsonRequest = new GsonRequest<Notice[]>(noticeUrl,Notice[].class,new HashMap<String,String>(),createNewAdapter(),handleException(), Request.Method.GET);
                requestQueue.add(gsonRequest);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        dialog.show();


        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();

        GsonRequest<Notice[]> gsonRequest = new GsonRequest<Notice[]>(noticeUrl,Notice[].class,new HashMap<String,String>(),createNewAdapter(),handleException(), Request.Method.GET);

        requestQueue.add(gsonRequest);
    }

    private Response.Listener<Notice[]> createNewAdapter() {
        return new Response.Listener<Notice[]> () {
            @Override
            public void onResponse(Notice[] response) {
                mTextView.setText("");
                adapter = new NoticeAdapter(response);
                if(response.length == 0){
                    Toast.makeText(MyApplication.getAppContext(), R.string.no_data_found, Toast.LENGTH_LONG).show();
                }
                publicNotices = response;
                adapter = new NoticeAdapter(response);

                // Attach the adapter to the recyclerview to populate items
                recyclerView.setAdapter(adapter);
                // Set layout manager to position the items
                recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getAppContext()));

                // Add separator
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST));
                // That's all!

                // Remember to CLEAR OUT old items before appending in the new ones
                adapter.clear();
                // ...the data has come back, add new items to your adapter...
                adapter.addAll(Arrays.asList(response));
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
                dialog.dismiss();
            }


        };
    }

    private Response.ErrorListener handleException() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                adapter = new NoticeAdapter(new Notice[0]);

                mTextView.setText("No internet found!!! try again");
                System.out.print(error.getStackTrace());
                if(adapter != null){
                    // Remember to CLEAR OUT old items before appending in the new ones
                    adapter.clear();
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                }

                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
                dialog.dismiss();

            }
        };
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
// click for logout
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        SharedPreferences preferences = getSharedPreferences("LogggedInUserName", 0);
                        preferences.edit().remove("UserName").commit();

                        Intent intent = new Intent(MainActivity.this, SingInActivity.class);
                        startActivity(intent);
                        finish();
                        // [END_EXCLUDE]
                    }
                });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage2) {

            Intent intent = new Intent(MainActivity.this, MedicalCenterActivity.class);
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.nav_mess_menu) {
            Intent intent = new Intent(MainActivity.this, MessMenuActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_raf) {
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mess_menu) {
            Intent intent = new Intent(MainActivity.this, MessMenuActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra("id","?category=1");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_send) {

        } else if(id == R.id.nav_timings){
            Intent intent = new Intent(MainActivity.this,PlacesInfoActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        //Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
    public void onClick(View v) {

    }
    public  void CheckLoggedInSession()
    {
        String strPref;
        SharedPreferences shf = getSharedPreferences("LogggedInUserName", Context.MODE_PRIVATE);
        if(shf!=null) {
            strPref = shf.getString("UserName", null);

            if (strPref != null) {
                // do some thing
                //Redirect to mainactivity
                //Intent intent = new Intent(Splash.this, MainActivity.class);
               // startActivity(intent);
               // finish();
            }
            else
            {
                Intent i = new Intent(MainActivity.this, SingInActivity.class);
                startActivity(i);
            }
        }
        else
        {
            Intent i = new Intent(MainActivity.this, SingInActivity.class);
            startActivity(i);
        }

    }

}
