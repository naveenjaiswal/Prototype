package com.li8tech.nli8.prototype;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by FDUSER on 24-Feb-16.
 */
public class VolleySingleton {

    private  static  VolleySingleton  sInstance = null;
    public static final String BASE_API_URL = "http://pilock.pythonanywhere.com/api/";
    public static final String NOTICE_URL_SEGMENT = "notice/";
    private RequestQueue mRequestQueue ;
    private ImageLoader imageLoader ;

    public VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            private LruCache<String,Bitmap> cache = new LruCache<>((int)((Runtime.getRuntime().maxMemory()/1024)/8));
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });
    }

    public static VolleySingleton getInstance(){

        if(sInstance == null)
            sInstance = new VolleySingleton();

        return sInstance;

    }


    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
