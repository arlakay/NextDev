package com.erd.nextdev2016;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.erd.nextdev2016.app.MyApplication;
import com.erd.nextdev2016.helper.SessionManager;
import com.erd.nextdev2016.util.Constants;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ILM on 5/8/2016.
 */
public class SubmitIdeStepTwo extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener{

    private static final String url = Constants.BASE_URL + "/transaction/submitidetwo";
    public String team, namaTwo, dobTwo, jkTwo, telpTwo, emailTwo, onProfileTwo;
    private ProgressDialog pDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private EditText etNameTwo, etDobTwo, etGenderTwo, etPhoneTwo, etEmailTwo, etOnProfileTwo;
    private SessionManager session;
    private RadioGroup radioGroup;
    private RadioButton rb;
    private TextInputLayout inputLayoutEmail, inputLayoutNoTelp;
    private EditText inputEmail, inputNoTelp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_idea_step_two);

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            Intent intent = new Intent(SubmitIdeStepTwo.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

        alertDialogBuilder = new AlertDialog.Builder(SubmitIdeStepTwo.this, R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setCancelable(false);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Intent i = getIntent();
        team = i.getStringExtra("team");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNameTwo = (EditText)findViewById(R.id.nama_member_two);
        etDobTwo = (EditText)findViewById(R.id.dob_member_two);
        etGenderTwo = (EditText)findViewById(R.id.gender_member_two);
        etPhoneTwo = (EditText)findViewById(R.id.phone_member_two);
        etEmailTwo = (EditText)findViewById(R.id.email_member_two);
        etOnProfileTwo = (EditText)findViewById(R.id.online_profile_member_two);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutNoTelp = (TextInputLayout) findViewById(R.id.input_layout_phone);

        inputEmail = (EditText) findViewById(R.id.email_member_two);
        inputNoTelp = (EditText) findViewById(R.id.phone_member_two);

        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputNoTelp.addTextChangedListener(new MyTextWatcher(inputNoTelp));

        etDobTwo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            SubmitIdeStepTwo.this,
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                //team = etTeam.getText().toString();
                namaTwo= etNameTwo.getText().toString();
                dobTwo = etDobTwo.getText().toString();
                jkTwo = rb.getText().toString();
                telpTwo = etPhoneTwo.getText().toString();
                emailTwo = etEmailTwo.getText().toString();
                onProfileTwo = etOnProfileTwo.getText().toString();

                if (namaTwo.trim().length() > 0 && dobTwo.trim().length() > 0 && jkTwo.trim().length() > 0 && telpTwo.trim().length() > 0
                        && emailTwo.trim().length() > 0 && onProfileTwo.trim().length() > 0 ) {

                    storeMemberTwo(team, namaTwo, dobTwo, jkTwo, telpTwo, emailTwo, onProfileTwo);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter Credentials", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }


    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhone() {
        String phoneNumber = inputNoTelp.getText().toString().trim();

        if (phoneNumber.isEmpty() || !isPhoneNumberValid(phoneNumber)) {
            inputLayoutNoTelp.setError(getString(R.string.err_msg_phone_number));
            inputNoTelp.setError(getString(R.string.err_msg_phone_number));
            requestFocus(inputNoTelp);
            return false;
        } else {
            inputLayoutNoTelp.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        int intIndex = phoneNumber.indexOf("08");
        return intIndex == 0 && phoneNumber.trim().length() > 9;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void storeMemberTwo(final String team, final String nama, final String dob, final String jk,
                                final String telp, final String email, final String profile) {

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

                        alertDialogBuilder.setMessage("Step 2 DONE");
                        alertDialogBuilder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getApplicationContext(), SubmitIdeStepThree.class);
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
                params.put("namadua", nama);
                params.put("dobdua", dob);
                params.put("jkdua", jk);
                params.put("telpdua", telp);
                params.put("emaildua", email);
                params.put("onprofiledua", profile);

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
        etDobTwo.setText(date);
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.email_member_two:
                    validateEmail();
                    break;
                case R.id.phone_member_two:
                    validatePhone();
                    break;

            }
        }
    }

}
