package com.erd.nextdev2016.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.erd.nextdev2016.DetailBeritaActivity;
import com.erd.nextdev2016.R;
import com.erd.nextdev2016.app.MyApplication;
import com.erd.nextdev2016.helper.Restaurant;
import com.erd.nextdev2016.helper.SimpleRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OneFragment extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    private SimpleRecyclerAdapter adapter;
    private List<Restaurant> restaurantList;
    private JSONArray data;
    private String jsonResponse;
    private static String url = "http://octolink.co.id/api/NextDev/index.php/api/Transaction/artikel";

    @BindView(R.id.dummyfrag_scrollableview)  RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)  SwipeRefreshLayout swipeRefreshLayout;

    public static OneFragment newInstance() {
        return new OneFragment();
    }

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
        View rootView =  inflater.inflate(R.layout.fragment_one, container, false);
        ButterKnife.bind(this, rootView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        restaurantList = new ArrayList<>();
        //for (int i = 0; i < VersionModel.data.length; i++) {
        //list.add(VersionModel.data[i]);
        //}

        adapter = new SimpleRecyclerAdapter(restaurantList, new SimpleRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Restaurant model) {
                String title = model.getJudul();
                String snipp = model.getSnippet();
                String isi = model.getIsi_berita();
                String gambar = model.getGambar();

                Intent intent = new Intent(getActivity(), DetailBeritaActivity.class);

                intent.putExtra("judul", title);
                intent.putExtra("snippet", snipp);
                intent.putExtra("isi", isi);
                intent.putExtra("gambar", gambar);

                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        // Showing Swipe Refresh animation on activity create
        // As animation won't start on onCreate, post runnable is used
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getArtikelFromAPI();
            }
        });

        return rootView;
    }

    //This method is called when swipe refresh is pulled down
    @Override
    public void onRefresh() {
        restaurantList.clear();
        swipeRefreshLayout.setRefreshing(true);
        getArtikelFromAPI();
    }

    private void getArtikelFromAPI() {
        swipeRefreshLayout.setRefreshing(true);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String rs = response.toString();
                //Log.d(TAG, response.toString());
                try {
                    jsonResponse = "";
                    JSONObject job = new JSONObject(rs);
                    data = job.getJSONArray("artikel");

                    for (int i = 0; i < data.length(); i++) {

                        JSONObject person = data.getJSONObject(i);

                        int rank = person.getInt("id_berita");
                        String snippet = person.getString("snippet");
                        String title = person.getString("judul");
                        String isi = person.getString("isi_berita");
                        String gambar = person.getString("gambar");

                        Restaurant m = new Restaurant(rank, title, snippet, isi, gambar);
                        restaurantList.add(m);

                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipeRefreshLayout.setRefreshing(false);
//                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        final List<Restaurant> filteredModelList = filter(restaurantList, newText);
        adapter.animateTo(filteredModelList);
        recyclerView.scrollToPosition(0);
        return true;
    }

    private List<Restaurant> filter(List<Restaurant> models, String query) {
        query = query.toLowerCase();

        final List<Restaurant> filteredModelList = new ArrayList<>();
        //restaurantList = new ArrayList<>();

        for (Restaurant model : models) {
            final String text = model.getJudul().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SimpleRecyclerAdapter(filteredModelList, new SimpleRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Restaurant model) {
                //Toast.makeText(getContext(), "Item Clicked" + model.getId() , Toast.LENGTH_LONG).show();

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
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();  // data set changed
        return filteredModelList;
    }

}
