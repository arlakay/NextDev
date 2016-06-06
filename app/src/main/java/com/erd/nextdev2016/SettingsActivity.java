package com.erd.nextdev2016;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.erd.nextdev2016.helper.SessionManager;

/**
 * Created by ILM on 5/3/2016.
 */
public class SettingsActivity extends BaseActivity{
    private SessionManager session;
    AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this, R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setCancelable(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnFeedback = (Button) findViewById(R.id.btn_feedback);
        Button btnAboutApp = (Button) findViewById(R.id.btn_about);
        Button btnLogout = (Button) findViewById(R.id.btn_setting_logout);

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] email = {"contact@thenextdev.id"};
                composeEmail(email , "NextDev Android Apps Feedback");
            }
        });

        btnAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.setTitle("About NextDev Apps");
                alertDialogBuilder.setMessage("Terima kasih kamu telah mengunduh aplikasi " +
                        "Telkomsel NextDev untuk Android! Gunakan aplikasi ini untuk " +
                        "mendaftarkan dirimu pada NextDev 2016, dapatkan informasi terkini " +
                        "seputar rangkaian roadshow serta tak ketinggalan dapatkan berbagai " +
                        "penawaran menarik dari Telkomsel.\n" +
                        "\n" + "Version 1.0\n");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

    }

    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}