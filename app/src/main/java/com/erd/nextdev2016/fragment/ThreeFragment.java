package com.erd.nextdev2016.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.erd.nextdev2016.R;
import com.erd.nextdev2016.app.MyApplication;
import com.erd.nextdev2016.helper.GalleryAdapter;
import com.erd.nextdev2016.helper.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ThreeFragment extends Fragment implements SearchView.OnQueryTextListener {

    public static ThreeFragment newInstance() {
        return new ThreeFragment();
    }

    private RecyclerView recyclerView;
    private String TAG = ThreeFragment.class.getSimpleName();
    private static final String endpoint = "http://api.androidhive.info/json/glide.json";
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //adapter = new SwipeListAdapter(getActivity(), restaurantList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_three, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.dummyfrag_scrollableview);

        pDialog = new ProgressDialog(getActivity());
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getActivity(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
//                newFragment.setArguments(bundle);
//                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fetchImages();

        return rootView;

    }

    private void fetchImages() {

        pDialog.setMessage("Downloading json...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d(TAG, response.toString());
                        pDialog.hide();

                        images.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Image image = new Image();
                                image.setName(object.getString("name"));

                                JSONObject url = object.getJSONObject("url");
                                image.setSmall(url.getString("small"));
                                image.setMedium(url.getString("medium"));
                                image.setLarge(url.getString("large"));
                                image.setTimestamp(object.getString("timestamp"));

                                images.add(image);

                            } catch (JSONException e) {
                                //Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
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

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_main, menu);
        //super.onCreateOptionsMenu(menu, inflater);

//        MenuItem item = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_maps:
//                Intent intent = new Intent(getActivity(), MapsResActivity.class);
//                startActivity(intent);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        final List<Restaurant> filteredModelList = filter(restaurantList, newText);
//        adapter.animateTo(filteredModelList);
//        recyclerView.scrollToPosition(0);
        return true;
    }

//    private List<Restaurant> filter(List<Restaurant> models, String query) {
//        query = query.toLowerCase();
//
//        final List<Restaurant> filteredModelList = new ArrayList<>();
//        //restaurantList = new ArrayList<>();
//
//        for (Restaurant model : models) {
//            final String text = model.getTitle().toLowerCase();
//            if (text.contains(query)) {
//                filteredModelList.add(model);
//            }
//        }
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        adapter = new SimpleRecyclerAdapter(filteredModelList, new SimpleRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Restaurant model) {
//                //Toast.makeText(getContext(), "Item Clicked" + model.getId() , Toast.LENGTH_LONG).show();
//
//                String namaRes = model.getTitle();
//                String alamatRes = model.getAlamat();
//                String detail = model.getDetail();
//                String lat = String.valueOf(model.getLati());
//                String lng = String.valueOf(model.getLongi());
//                String kategori = model.getKategori();
//                String urlPicRes = model.getUrlpic();
//                String menu = model.getMenu();
//
//                Intent intent = new Intent(getActivity(), DetailRestaurantActivity.class);
//
//                intent.putExtra("nama", namaRes);
//                intent.putExtra("alamat", alamatRes);
//                intent.putExtra("detail", detail);
//                intent.putExtra("url", urlPicRes);
//                intent.putExtra("kat", kategori);
//                intent.putExtra("menu", menu);
//                intent.putExtra("lat", lat);
//                intent.putExtra("lng", lng);
//
//                startActivity(intent);
//            }
//        });
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();  // data set changed
//        return filteredModelList;
//    }

}
