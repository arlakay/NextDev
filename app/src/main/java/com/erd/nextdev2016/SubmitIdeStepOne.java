package com.erd.nextdev2016;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.erd.nextdev2016.app.MyApplication;
import com.erd.nextdev2016.helper.SessionManager;
import com.erd.nextdev2016.util.Constants;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ILM on 5/8/2016.
 */
public class SubmitIdeStepOne extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{

    private static final String url = Constants.BASE_URL + "/transaction/submitideone";
    public static final String DATA_URL_PROVINCE = Constants.BASE_URL + "/Transaction/province";
    public static final String DATA_URL_CITY= Constants.BASE_URL + "/Transaction/city";
    public String team, nama, dob, gender, provinsi, kota, alamat;
    public String selectedSort, valueCity;
    private ProgressDialog pDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private EditText etTeam, etName, etDob, etGender, etProv, etCity, etAddr;
    private RadioGroup radioGroup;
    private RadioButton rb;
    private Spinner spinner, spinner2;
    private SessionManager session;
    private ArrayList<String> cityAr;
    ArrayAdapter<String> adapter2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_idea_step_one);

        cityAr = new ArrayList<String>();

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            Intent intent = new Intent(SubmitIdeStepOne.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

        alertDialogBuilder = new AlertDialog.Builder(SubmitIdeStepOne.this, R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setCancelable(false);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etTeam = (EditText)findViewById(R.id.nama_tim);
        etName = (EditText)findViewById(R.id.nama_lengkap);
        etDob = (EditText)findViewById(R.id.dob);
        etGender = (EditText)findViewById(R.id.gender);
        etProv = (EditText)findViewById(R.id.province);
        etCity = (EditText)findViewById(R.id.city);
        etAddr = (EditText)findViewById(R.id.address);
        spinner = (Spinner) findViewById(R.id.spin_province);
        spinner2 = (Spinner) findViewById(R.id.spin_city);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        etDob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            SubmitIdeStepOne.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                }else {

                }
            }
        });

        radioGroup.check(R.id.radioButton1);
        valueCity = "DKI Jakarta";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                team = etTeam.getText().toString();
                nama= etName.getText().toString();
                dob = etDob.getText().toString();
                gender = rb.getText().toString();
                provinsi = selectedSort.toString();
                kota = valueCity.toString();
                alamat = etAddr.getText().toString();

                if (team.trim().length() > 0 && nama.trim().length() > 0 && dob.trim().length() > 0 && provinsi.trim().length() > 0 && kota.trim().length() > 0 && alamat.trim().length() > 0 ) {

                    storeMemberOne(team, nama, dob, gender, provinsi, kota, alamat);
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
                        R.array.province,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        adapter2 = new ArrayAdapter<>(SubmitIdeStepOne.this,
                android.R.layout.simple_spinner_dropdown_item, cityAr);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

    }

    private String getSortByPosition(int position) {
        switch (position) {
            case 0: return "Nanggroe Aceh Darussalam (NAD)";
            case 1: return "Sumatera Utara";
            case 2: return "Sumatera Barat";
            case 3: return "Riau";
            case 4: return "Kepulauan Riau";
            case 5: return "Jambi";
            case 6: return "Bengkulu";
            case 7: return "Sumatera Selatan";
            case 8: return "Kepulauan Bangka Belitung";
            case 9: return "Lampung";
            case 10: return "Banten";
            case 11: return "Jawa Barat";
            case 12: return "DKI Jakarta";
            case 13: return "Jawa Tengah";
            case 14: return "Daerah Istimewa Yogyakarta";
            case 15: return "Jawa Timur";
            case 16: return "Bali";
            case 17: return "Nusa Tenggara Barat";
            case 18: return "Nusa Tenggara Timur";
            case 19: return "Kalimantan Barat";
            case 20: return "Kalimantan Selatan";
            case 21: return "Kalimantan Tengah";
            case 22: return "Kalimantan Timur";
            case 23: return "Kalimantan Utara";
            case 24: return "Gorontalo";
            case 25: return "Sulawesi Utara";
            case 26: return "Sulawesi Tengah";
            case 27: return "Sulawesi Tenggara";
            case 28: return "Sulawesi Selatan";
            case 29: return "Sulawesi Barat";
            case 30: return "Maluku";
            case 31: return "Maluku Utara";
            case 32: return "Papua";
            case 33: return "Papua Barat";
            default: return "DKI Jakarta";
        }
    }

    private void storeMemberOne(final String team, final String nama, final String dob, final String gender,
                                final String provinsi, final String kota, final String alamat) {

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

                        alertDialogBuilder.setMessage("Step 1 DONE");
                        alertDialogBuilder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getApplicationContext(), SubmitIdeStepTwo.class);
                                i.putExtra("team", team);
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
                                finish();
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
                        Intent i = new Intent(getApplicationContext(), SubmitIdeStepTwo.class);
                        startActivity(i);
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
                params.put("namasatu", nama);
                params.put("dobsatu", dob);
                params.put("jksatu", gender);
                params.put("provinsi", provinsi);
                params.put("kota", kota);
                params.put("alamat", alamat);

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
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        etDob.setText(date);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spin_province:
                selectedSort = getSortByPosition(position);
                adapter2.clear();
                getDataCity(selectedSort);
                break;
            case R.id.spin_city:
                valueCity = spinner2.getItemAtPosition(position).toString();
                Log.d("TAG", valueCity);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getDataCity(final String province){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DATA_URL_CITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            JSONArray result = j.getJSONArray("city");

                            //Calling method getStudents to get the students from the JSON Array
                            getCity(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("prov", province);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getCity(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                cityAr.add(json.getString("city_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter2.notifyDataSetChanged();
        //Setting adapter to show the items in the spinner
        spinner2.setAdapter(adapter2);
    }

}
