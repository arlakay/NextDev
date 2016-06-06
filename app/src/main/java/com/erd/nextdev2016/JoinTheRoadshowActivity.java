package com.erd.nextdev2016;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ILM on 5/7/2016.
 */
public class JoinTheRoadshowActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_the_roadshow);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView jogja = (TextView) findViewById(R.id.register_jogja);
        TextView semarang = (TextView) findViewById(R.id.register_semarang);
        TextView surabaya = (TextView) findViewById(R.id.register_surabaya);
        TextView malang = (TextView) findViewById(R.id.register_malang);
        TextView banyuwangi = (TextView) findViewById(R.id.register_banyuwangi);
        TextView denpasar = (TextView) findViewById(R.id.register_denpasar);
        TextView bandung = (TextView) findViewById(R.id.register_bandung);
        TextView bogor = (TextView) findViewById(R.id.register_bogor);
        TextView jakarta = (TextView) findViewById(R.id.register_jakarta);
        TextView aceh = (TextView) findViewById(R.id.register_aceh);
        TextView medan = (TextView) findViewById(R.id.register_medan);
        TextView pekanbaru = (TextView) findViewById(R.id.register_pekanbaru);
        TextView palembang = (TextView) findViewById(R.id.register_palembang);
        TextView manado = (TextView) findViewById(R.id.register_manado);
        TextView ambon = (TextView) findViewById(R.id.register_ambon);
        TextView lampung = (TextView) findViewById(R.id.register_lampung);
        TextView makassar = (TextView) findViewById(R.id.register_makassar);
        TextView banjarmasin = (TextView) findViewById(R.id.register_banjarmasin);
        TextView balikpapan = (TextView) findViewById(R.id.register_balikpapan);
        TextView pontianak = (TextView) findViewById(R.id.register_pontianak);

        jogja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1PDchj5kspnRE3vdlimMunIPv6eq5uDXIWJ3NMNO3brU/viewform"));
                startActivity(browserIntent);
            }
        });

        semarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1kvC2OkJI57OXYFyHmsS_xQDYrncNxRZt22iHB1QbYmg/viewform"));
                startActivity(browserIntent);
            }
        });

        surabaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/10ScZSbxLqANnUj5sJHOWzlZv5tS-aRTy5N9KS-U4ITk/viewform"));
                startActivity(browserIntent);
            }
        });

        malang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1irluhX_J5A1Og5RO_jmnpxY1PoUSeTwnRtZ30kSk4wY/viewform"));
                startActivity(browserIntent);
            }
        });

        banyuwangi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1bvcMN5q4IBvNanZCEsfbIDNSKi3sLBIiUflTjLLIvCY/viewform"));
                startActivity(browserIntent);
            }
        });

        denpasar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1Z9q37WyE0gNuT50_beZH8-B-ZKKOYxSDY-Hwfd-FkJ4/viewform"));
                startActivity(browserIntent);
            }
        });

        bandung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1Hvtp0-FPznuMpwBvefND1Oii4Uq8n8HHO4nSFi1H_7s/viewform"));
                startActivity(browserIntent);
            }
        });

        bogor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1-PZyYGuu1SXoFdICo4APSf1Sn9o_iHn3W-cPx6iYmNc/viewform"));
                startActivity(browserIntent);
            }
        });

        jakarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1vwTtO3vOA2MSdFYBi0joV1XsHMyLjPvmdoTzG9J96uw/viewform"));
                startActivity(browserIntent);
            }
        });

        aceh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1y8bq-vvLeJkhXC_837aZrJTDV2QkpSH5WV_0gEmI3GQ/viewform"));
                startActivity(browserIntent);
            }
        });

        medan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1XDa8PNlomVGXntMbuAy4jbsPmKwjKfhpV0dTZ05p2ig/viewform"));
                startActivity(browserIntent);
            }
        });

        pekanbaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/10Rkv7UQzdPmdLAJ19_YF7UG8_PepaimuaGZiADbQkTE/viewform"));
                startActivity(browserIntent);
            }
        });

        palembang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1gf5p00I-6f7QWJcd6tqNj1ZFcrBr9XBUq1Y56_aVvyI/viewform"));
                startActivity(browserIntent);
            }
        });

        manado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1-LJDk0bldkscx56MawZwe2WFA3T7m5GGo8cDoAG36tU/viewform"));
                startActivity(browserIntent);
            }
        });

        ambon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1NNOiqGK7JFPROJSXUTGUTrab1g1IdecnPgIheAVJEZQ/viewform"));
                startActivity(browserIntent);
            }
        });

        lampung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1cERUyZ6EBLm3hgqkUBpHjokd208FjPK-cIpeczfYIOs/viewform"));
                startActivity(browserIntent);
            }
        });

        makassar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/170-pMBpj3L8nkP6CYLuNhiJFwyyvDL2Pw_anpuDybwI/viewform"));
                startActivity(browserIntent);
            }
        });

        banjarmasin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/18cq6mfqurkGrVNZOtc-lslERiVh6kfK6jaV4ZDoiki8/viewform"));
                startActivity(browserIntent);
            }
        });

        balikpapan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1_U-pjohbL37qtoana0KZ3x3RY16a_DwpgxwWDgFWAoU/viewform"));
                startActivity(browserIntent);
            }
        });

        pontianak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/11LNzGqvBUfHUJ3ZfKBs9gNg3BihBQOS5aHwuPBh-3sE/viewform"));
                startActivity(browserIntent);
            }
        });


    }

}
