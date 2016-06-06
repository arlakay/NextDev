package com.erd.nextdev2016;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.erd.nextdev2016.app.MyApplication;

/**
 * Created by ILM on 5/6/2016.
 */
public class DetailBeritaActivity extends BaseActivity {

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_berita);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail_news);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String title = i.getStringExtra("judul");
        String snipp = i.getStringExtra("snippet");
        String isi = i.getStringExtra("isi");
        String pic = i.getStringExtra("gambar");

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbar.setTitle(title);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.toolbar_text);
        collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        NetworkImageView imgNews = (NetworkImageView) findViewById(R.id.img_berita);
        TextView txtJudul = (TextView) findViewById(R.id.txt_judul_berita);
        TextView txtIsi = (TextView) findViewById(R.id.txt_isi_berita);

        imgNews.setImageUrl(pic, imageLoader);
        txtJudul.setText(title);
        txtIsi.setText(isi);
    }
}
