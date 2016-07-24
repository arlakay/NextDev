package com.erd.nextdev2016;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.erd.nextdev2016.app.MyApplication;
import com.erd.nextdev2016.fragment.SlideshowDialogFragment;
import com.erd.nextdev2016.helper.GalleryAdapter;
import com.erd.nextdev2016.helper.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ILM on 5/7/2016.
 */
public class GalleryActivity extends BaseActivity {

    private String TAG = GalleryActivity.class.getSimpleName();
//    private static final String endpoint = "https://api.instagram.com/v1/tags/nextdev2016/media/recent?access_token=3257218348.1677ed0.d56e3c7f90bb44cbab514b2dc7dc3507";
    private static final String endpoint = "http://octolink.co.id/api/NextDev/index.php/api/Transaction/gallery";
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    private JSONArray data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fetchImages();
    }

    private void fetchImages() {

        pDialog.setMessage("Loading Image");
        pDialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String rs = response.toString();
                //Log.d(TAG, response.toString());
                pDialog.hide();

                images.clear();

                try {
                    JSONObject job = new JSONObject(rs);
                    data = job.getJSONArray("gallery");

                    for (int i = 0; i < data.length(); i++) {

                        JSONObject url = data.getJSONObject(i);

                        Image image = new Image();

                        image.setSmall(url.getString("img_url"));
                        image.setMedium(url.getString("img_url"));
                        image.setLarge(url.getString("img_url"));

                        images.add(image);

                    }
                } catch (JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });
        MyApplication.getInstance().addToRequestQueue(req);
    }

    private void fetchImagesInstagram() {
//        pDialog.setMessage("Loading...");
//        pDialog.show();
//
//        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
//                endpoint, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                        String rs = response.toString();
//                        pDialog.hide();
//                        images.clear();
//
//                        try {
//                            JSONObject job = new JSONObject(rs);
//                            JSONArray data = job.getJSONArray("data");
//
//                            for (int i = 0; i < data.length(); i++) {
//
//                                JSONObject person = data.getJSONObject(i);
//
//                                JSONObject caption = person.getJSONObject("caption");
//                                String text = caption.getString("text");
//                                JSONObject from = caption.getJSONObject("from");
//                                String fullname = from.getString("full_name");
//
//                                JSONObject url = person.getJSONObject("images");
//
//                                JSONObject tumb = url.getJSONObject("thumbnail");
//                                String link = tumb.getString("url");
//
//                                JSONObject tumb2 = url.getJSONObject("standard_resolution");
//                                String link2 = tumb2.getString("url");
//
//                                JSONObject tumb3 = url.getJSONObject("low_resolution");
//                                String link3 = tumb3.getString("url");
//
//                                Image image = new Image();
//                                image.setName(fullname);
//                                image.setSmall(link);
//                                image.setMedium(link3);
//                                image.setLarge(link2);
//                                image.setTimestamp(text);
//
//                                images.add(image);
//                            }
//                            mAdapter.notifyDataSetChanged();
//                        } catch (JSONException e) {
//                            //Log.e(TAG, "Json parsing error: " + e.getMessage());
//                        }
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //Log.e(TAG, "Error: " + error.getMessage());
//                pDialog.hide();
//            }
//        });
//        MyApplication.getInstance().addToRequestQueue(req);
    }

}
