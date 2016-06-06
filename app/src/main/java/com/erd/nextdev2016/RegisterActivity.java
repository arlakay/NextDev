package com.erd.nextdev2016;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.erd.nextdev2016.app.MyApplication;
import com.erd.nextdev2016.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ILM on 5/4/2016.
 */
public class RegisterActivity extends BaseActivity {
    private static final String url = Constants.BASE_URL + "/users/register";
    private String tname, fname, email, phone, pass, pass2;
    private Button btnRegister, btnLinkToLogin;
    private TextInputLayout inputLayoutFullName, inputLayoutNamaTim, inputLayoutEmail, inputLayoutNoTelp, inputLayoutPassword, inputLayoutPassword2;
    private EditText inputFullName,	inputNamaTim, inputEmail, inputNoTelp, inputPassword, inputPassword2;
    private ProgressDialog pDialog;
    AlertDialog.Builder alertDialogBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this, R.style.AppCompatAlertDialogStyle);
        alertDialogBuilder.setCancelable(false);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputLayoutFullName = (TextInputLayout) findViewById(R.id.input_layout_nama_lengkap);
        inputLayoutNamaTim = (TextInputLayout) findViewById(R.id.input_layout_nama_tim);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutNoTelp = (TextInputLayout) findViewById(R.id.input_layout_no_telp);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutPassword2 = (TextInputLayout) findViewById(R.id.input_layout_confirm_password);

        inputFullName = (EditText) findViewById(R.id.nama_lengkap);
        inputNamaTim = (EditText) findViewById(R.id.nama_tim);
        inputEmail = (EditText) findViewById(R.id.email);
        inputNoTelp = (EditText) findViewById(R.id.no_telp);
        inputPassword = (EditText) findViewById(R.id.password);
        inputPassword2 = (EditText) findViewById(R.id.confirm_password);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        inputFullName.addTextChangedListener(new MyTextWatcher(inputFullName));
        inputNamaTim.addTextChangedListener(new MyTextWatcher(inputNamaTim));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputNoTelp.addTextChangedListener(new MyTextWatcher(inputNoTelp));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputPassword2.addTextChangedListener(new MyTextWatcher(inputPassword2));


        Button linkLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Button regiter = (Button) findViewById(R.id.btnRegister);
        assert regiter != null;
        regiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = inputFullName.getText().toString();
                email = inputEmail.getText().toString();
                phone = inputNoTelp.getText().toString();
                pass = inputPassword.getText().toString();
                pass2 = inputPassword2.getText().toString();

                if (pass.equalsIgnoreCase(pass2) || pass2.equalsIgnoreCase(pass)) {
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    // Check connection internet
                    if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                        registerUser(fname, email, phone, pass);
                    }else {
                        Toast.makeText(getApplicationContext(),
                                "No Internet Access Available!", Toast.LENGTH_LONG)
                                .show();
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_register, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_login:
                // Intent ke Login
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateTeamName() {
        if (inputNamaTim.getText().toString().trim().isEmpty()) {
            inputLayoutNamaTim.setError(getString(R.string.err_msg_name));
            requestFocus(inputNamaTim);
            return false;
        } else {
            inputLayoutNamaTim.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateFullName() {
        if (inputFullName.getText().toString().trim().isEmpty()) {
            inputLayoutFullName.setError(getString(R.string.err_msg_name));
            requestFocus(inputFullName);
            return false;
        } else {
            inputLayoutFullName.setErrorEnabled(false);
        }
        return true;
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

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword2() {
        if (inputPassword2.getText().toString().trim().isEmpty()) {
            inputLayoutPassword2.setError(getString(R.string.err_msg_password2));
            requestFocus(inputPassword2);
            return false;
        } else {
            inputLayoutPassword2.setErrorEnabled(false);
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

    private void registerUser(final String firstName, final String email,
                              final String phoneNumber, final String newPassword) {

         if (!validateFullName()) {
            return;
        } if (!validateEmail()) {
            return;
        } if (!validatePhone()) {
            return;
        } if (!validatePassword()) {
            return;
        } if (!validatePassword2()) {
            return;
        }

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("response");

                    // Check for code node in json
                    if (error.equals("00")){

                        JSONObject data = jObj.getJSONObject("status");
                        String reg_status = data.getString("register");

                        hideDialog();

                        alertDialogBuilder.setMessage("Registration Success");
                        alertDialogBuilder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                                //startActivity(i);
                                finish();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }else{

                        alertDialogBuilder.setMessage("Registration Success");
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

                alertDialogBuilder.setMessage("Registration Fail");
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("fullname", firstName);
                params.put("email", email);
                params.put("telp", phoneNumber);
                params.put("password", newPassword);
                return params;

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                // Removed this line if you dont need it or Use application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
                case R.id.nama_tim:
                    validateTeamName();
                    break;
                case R.id.nama_lengkap:
                    validateFullName();
                    break;
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.no_telp:
                    validatePhone();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
                case R.id.confirm_password:
                    validatePassword2();
                    break;
            }
        }
    }

}
