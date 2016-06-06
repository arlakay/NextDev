package com.erd.nextdev2016;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.erd.nextdev2016.util.Constants;

/**
 * Created by ILM on 5/8/2016.
 */
public class HasilSubmitIdeActivity extends Activity {
    private static final String url = Constants.BASE_URL + "/Transaction/submitide";
    private ProgressDialog pDialog;
    AlertDialog.Builder alertDialogBuilder;
    String namaTim_NextDev, namaOne_NextDev, dobOne_NextDev, jkOne_NextDev, provinsiOne_NextDev, kotaOne_NextDev, alamatOne_NextDev,
        namaTwo_NextDev, dobTwo_NextDev, jkTwo_NextDev, telpTwo_NextDev, emailTwo_NextDev, onProfileTwo_NextDev,
        namaThree_NextDev, dobThree_NextDev, jkThree_NextDev, telpThree_NextDev, emailThree_NextDev, onProfileThree_NextDev,
        namaApp_NextDev, urlApp_NextDev, urlVideo_NextDev, platformApp_NextDev, deskripsiApp_NextDev;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hasil);

        // Alert Dialog
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        namaTim_NextDev = i.getStringExtra("namatim");
        namaOne_NextDev = i.getStringExtra("namasatu");
        dobOne_NextDev = i.getStringExtra("dobsatu");
        jkOne_NextDev = i.getStringExtra("jksatu");
        provinsiOne_NextDev = i.getStringExtra("provinsi");
        kotaOne_NextDev = i.getStringExtra("kota");
        alamatOne_NextDev = i.getStringExtra("alamat");

        namaTwo_NextDev = i.getStringExtra("namatwo");
        dobTwo_NextDev = i.getStringExtra("dobtwo");
        jkTwo_NextDev = i.getStringExtra("jktwo");
        telpTwo_NextDev = i.getStringExtra("telptwo");
        emailTwo_NextDev = i.getStringExtra("emailtwo");
        onProfileTwo_NextDev = i.getStringExtra("onprofiletwo");

        namaThree_NextDev = i.getStringExtra("namathree");
        dobThree_NextDev = i.getStringExtra("dobthree");
        jkThree_NextDev = i.getStringExtra("jkthree");
        telpThree_NextDev = i.getStringExtra("telpthree");
        emailThree_NextDev = i.getStringExtra("emailthree");
        onProfileThree_NextDev = i.getStringExtra("onprofilethree");

        namaApp_NextDev = i.getStringExtra("appname");
        urlApp_NextDev = i.getStringExtra("appurl");
        urlVideo_NextDev = i.getStringExtra("slideurl");
        platformApp_NextDev = i.getStringExtra("platform");
        deskripsiApp_NextDev = i.getStringExtra("appdeskripsi");

        Button btnHome = (Button) findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HasilSubmitIdeActivity.this, ScrollableTabsActivity.class);
                startActivity(i);
            }
        });

    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
