package com.erd.nextdev2016;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.erd.nextdev2016.app.MyApplication;
import com.erd.nextdev2016.helper.SessionManager;
import com.erd.nextdev2016.util.CircularImageView;
import com.erd.nextdev2016.util.Constants;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ILM on 5/17/2016.
 */
public class NewProfileActivity extends BaseActivity implements
        DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    public static final String DATA_URL_CITY= Constants.BASE_URL + "/Transaction/city";
    public static final String URL_GET_PROFILE = Constants.BASE_URL + "/users/user";
    public static final String URL_UPDATE_PROFILE = Constants.BASE_URL + "/users/profile";
    private String UPLOAD_URL ="http://192.168.43.246/api/NextDev/upload.php";
    private String KEY_IMAGE = "image";
    private String KEY_EMAIL = "email";
    private int PICK_IMAGE_REQUEST = 1;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.input_layout_name_profile) TextInputLayout lblName;
    @BindView(R.id.input_layout_dob_profile) TextInputLayout lblDob;
    @BindView(R.id.input_layout_address_profile) TextInputLayout lblAddress;
    @BindView(R.id.name_profile) EditText name;
    @BindView(R.id.dob_profile) EditText dob;
    @BindView(R.id.address_profile) EditText address;
    @BindView(R.id.spin_province) Spinner province;
    @BindView(R.id.spin_city) Spinner city;
    @BindView(R.id.radioGroup) RadioGroup radioGroup;
    @BindView(R.id.btnUpdate) Button update;
    @BindView(R.id.img_profile) CircularImageView fProfile;

    private Bitmap bitmap;
    private ArrayAdapter<String> adapter2;
    private ArrayAdapter<CharSequence> adapter;
    private ArrayList<String> cityAr;
    private AlertDialog.Builder alertDialogBuilder;
    private ProgressDialog pDialog;
    private SessionManager session;
    private RadioButton rb;
    private String selectedSort, valueCity, fullName, cid, dob2, addr2, prov2, city2, gender2, fp2;
    private String valueNama, valueDob, valueGender, valueProv, valueCityProf, valueAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newprofile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alertDialogBuilder = new AlertDialog.Builder(NewProfileActivity.this, R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setCancelable(false);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            Intent intent = new Intent(NewProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        cid = user.get(SessionManager.KEY_CID);

        cityAr = new ArrayList<String>();

        fProfile.setImageUrl("http://192.168.43.246/api/NextDev/assets/img/gallery/bootcamp1-10.jpg", imageLoader);

        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            NewProfileActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                }else {

                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                valueNama = name.getText().toString();
                valueDob = dob.getText().toString();
                valueGender = rb.getText().toString();
                valueProv = selectedSort.toString();
                valueCityProf = valueCity.toString();
                valueAddress = address.getText().toString();

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                // Check connection internet
                if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                    updateProfile(cid, valueNama, valueDob, valueGender, valueProv, valueCityProf, valueAddress);
                }else {
                    Toast.makeText(getApplicationContext(),
                            "No Internet Access Available!", Toast.LENGTH_LONG)
                            .show();
                }

                uploadImage(cid);

            }
        });

        fProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Image di klik", Toast.LENGTH_LONG).show();
                showFileChooser();
            }
        });

        setupSpinner();

        getProfileWhenLogin(cid);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                fProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupSpinner() {
        adapter = ArrayAdapter
                .createFromResource(this,
                        R.array.province,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(adapter);
        province.setOnItemSelectedListener(this);

        adapter2 = new ArrayAdapter<>(NewProfileActivity.this,
                android.R.layout.simple_spinner_dropdown_item, cityAr);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adapter2);
        city.setOnItemSelectedListener(this);

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

    private void getDataCity(final String province){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DATA_URL_CITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            JSONArray result = j.getJSONArray("city");

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("prov", province);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getCity(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);

                cityAr.add(json.getString("city_name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        city.setAdapter(adapter2);
    }

    private void getProfileWhenLogin(final String email) {
        cityAr.clear();
        adapter2.clear();
        String tag_string_req = "req_user";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_GET_PROFILE, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("TAG", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);

                    fullName = jObj.getString("nama_lengkap");
                    dob2 = jObj.getString("dob");
                    addr2 = jObj.getString("address");
                    prov2 = jObj.getString("province");
                    city2 = jObj.getString("city");
                    gender2 = jObj.getString("gender");
                    fp2 = jObj.getString("foto_profile");

                    if (gender2.equalsIgnoreCase("laki-laki")){
                        radioGroup.check(R.id.radioButton1);
                    } else {
                        radioGroup.check(R.id.radioButton2);
                    }

                    String compareValue = prov2;
                    if (!compareValue.equals(null)) {
                        int spinnerPosition = adapter.getPosition(compareValue);
                        province.setSelection(spinnerPosition);
                    }

                    String compareValue2 = city2;
                    adapter2.add(jObj.getString("city"));
                    city.setAdapter(adapter2);
                    if (!compareValue2.equals(null)) {
                        int spinnerPosition2 = adapter2.getPosition(compareValue2);
                        city.setSelection(spinnerPosition2);
                    }

                    name.setText(fullName);
                    dob.setText(dob2);
                    address.setText(addr2);

                    fProfile.setImageUrl(fp2, imageLoader);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    if (error.toString().equals("com.android.volley.ServerError")){
                        Toast.makeText(getApplicationContext(), "Email and Password did not match", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(getApplicationContext(), "Please try again later", Toast.LENGTH_LONG).show();
                    }
                }else{
//                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }}
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void updateProfile(final String email, final String nama, final String dob, final String gender, final String prov, final String city, final String address){
        pDialog.setMessage("Updating ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_UPDATE_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("response");

                    if (error.equals("00")){

                        JSONObject data = jObj.getJSONObject("status");
                        String reg_status = data.getString("profile");

                        hideDialog();

                        alertDialogBuilder.setMessage("Update Success");
                        alertDialogBuilder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                                //startActivity(i);
//                                finish();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }else{

                        alertDialogBuilder.setMessage("Update Success");
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                                //startActivity(i);
//                                finish();
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
                hideDialog();

                MyApplication.getInstance().cancelPendingRequests(this);

                alertDialogBuilder.setMessage("Update Failed");
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                        //startActivity(i);
//                        finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("nama", nama);
                params.put("dob", dob);
                params.put("gender", gender);
                params.put("prov", prov);
                params.put("city", city);
                params.put("address", address);
                return params;

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(final String email){
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        Toast.makeText(NewProfileActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Toast.makeText(NewProfileActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String image = getStringImage(bitmap);

                Map<String,String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, image);
                params.put(KEY_EMAIL, email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
        dob.setText(date);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spin_province:
                selectedSort = getSortByPosition(position);
                Log.d("TAGS", selectedSort);
                adapter2.clear();
                getDataCity(selectedSort);
                break;
            case R.id.spin_city:
                valueCity = city.getItemAtPosition(position).toString();
                Log.d("TAG", valueCity);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
