package com.erd.nextdev2016;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.erd.nextdev2016.app.MyApplication;
import com.erd.nextdev2016.helper.SessionManager;
import com.erd.nextdev2016.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ILM on 5/8/2016.
 */
public class SubmitIdeStepFour extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String url = Constants.BASE_URL + "/transaction/submitidefour";
    public String team, appName, appUrl, slideUrl, platform, appDescription;
    public String selectedSort;
    private Spinner spinner;
    private ProgressDialog pDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private EditText etAppName, etAppUrl, etSlideUrl, etPlatform, etAppDescription;
    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_idea_step_four);

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            Intent intent = new Intent(SubmitIdeStepFour.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

        alertDialogBuilder = new AlertDialog.Builder(SubmitIdeStepFour.this, R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setCancelable(false);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Intent i = getIntent();
        team = i.getStringExtra("team");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etAppName = (EditText)findViewById(R.id.app_name);
        etAppUrl = (EditText)findViewById(R.id.web_app_url);
        etSlideUrl = (EditText)findViewById(R.id.video_presentation_url);
        etPlatform = (EditText)findViewById(R.id.platform);
        etAppDescription = (EditText)findViewById(R.id.web_app_description);

        spinner = (Spinner)findViewById(R.id.spin_platform);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appName= etAppName.getText().toString();
                appUrl = etAppUrl.getText().toString();
                slideUrl = etSlideUrl.getText().toString();
                platform = selectedSort.toString();
                appDescription = etAppDescription.getText().toString();

                if (appName.trim().length() > 0 && appUrl.trim().length() > 0 && slideUrl.trim().length() > 0
                        && appDescription.trim().length() > 0 ) {

                    storeMemberFour(team, appName, appUrl, slideUrl, platform, appDescription);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter Credentials", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this,
                        R.array.platform,
                        android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private String getSortByPosition(int position) {
        switch (position) {
            case 0: return "Android";
            case 1: return "IOS";
            case 2: return "Web";
            case 3: return "WebApp";
            default: return "Web";
        }
    }

    private void storeMemberFour(final String team, final String appname, final String appurl,
                                 final String slide, final String platform, final String deskripsi) {

        pDialog.setMessage("Processing ...");
        showDialog();

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("response");

                    if (error.equals("00")){

                        JSONObject data = jObj.getJSONObject("status");
                        String reg_status = data.getString("submitide");

                        hideDialog();

                        alertDialogBuilder.setMessage("ALL DONE");
                        alertDialogBuilder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getApplicationContext(), HasilSubmitIdeActivity.class);
                                startActivity(i);
                                //finish();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }else{

                        alertDialogBuilder.setMessage("Registration Failed");
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                                //startActivity(i);
                                //finish();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }

                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(),
                //        "Registration Failed", Toast.LENGTH_LONG).show();
                hideDialog();

                MyApplication.getInstance().cancelPendingRequests(this);

                alertDialogBuilder.setMessage("Registration Error: " + error.getMessage());
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Intent i = new Intent(getApplicationContext(), SubmitIdeStepTwo.class);
                        //startActivity(i);
                        //finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("namatim", team);
                params.put("appname", appname);
                params.put("appurl", appurl);
                params.put("slideurl", slide);
                params.put("platform", platform);
                params.put("appdeskripsi", deskripsi);

                return params;
            }
        };
        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedSort = getSortByPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
