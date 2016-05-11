package com.erd.nextdev2016;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.erd.nextdev2016.app.MyApplication;
import com.erd.nextdev2016.fragment.FragmentProfileUser;
import com.erd.nextdev2016.helper.SQLiteHandler;
import com.erd.nextdev2016.helper.SessionManager;
import com.erd.nextdev2016.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {
	private TextInputLayout inputLayoutEmail, inputLayoutPassword;
	private EditText inputEmail, inputPassword;
	private SessionManager session;
	private SQLiteHandler db;
	private Intent intent;
	private ProgressDialog pDialog;
	String URL_LOGIN = Constants.BASE_URL + "/users/login";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		db = new SQLiteHandler(getApplicationContext());
		session = new SessionManager(getApplicationContext());
		if (session.isLoggedIn()) {
			intent = new Intent(LoginActivity.this, ScrollableTabsActivity.class);
			startActivity(intent);
			finish();
		}

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//		TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//		toolbarTitle.setText("Login");
//		toolbarTitle.setTextColor(Color.WHITE);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setDisplayShowTitleEnabled(false);

		inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
		inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);

		inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
		inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

		Button linkRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
		linkRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});

		Button login = (Button) findViewById(R.id.btnLogin);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();

				checkLogin(email, password);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_login, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.action_register:
				// Intent ke Login
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void checkLogin(final String username, final String password) {
		if (!validateEmail()) {
			return;
		} if (!validatePassword()) {
			return;
		}

		String tag_string_req = "req_login";
		pDialog.setMessage("Logging in ...");
		showDialog();

		StringRequest strReq = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>(){
			@Override
			public void onResponse(String response) {
				hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					String pid = jObj.getString("participant_id");
					String cid = jObj.getString("category_id");
					String teamName = jObj.getString("team_name");
					String fullName = jObj.getString("full_name");
					String email = jObj.getString("email");
					String phoneNo = jObj.getString("phone_number");
					String pass = jObj.getString("password");
					//String web = jObj.getString("web");

					session.setLogin(true);
					session.createLoginSession(username, password);

					Bundle bundle = new Bundle();
					bundle.putString("pid", pid);
					bundle.putString("tname", teamName);
					bundle.putString("fname", fullName);

					// set Fragmentclass Arguments
					FragmentProfileUser fragobj = new FragmentProfileUser();
					fragobj.setArguments(bundle);

					// Launch main activity
					Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
					intent.putExtra("pid", pid);
					intent.putExtra("cid", cid);
					intent.putExtra("tName", teamName);
					intent.putExtra("fName", fullName);
					intent.putExtra("email", email);
					intent.putExtra("phone", phoneNo);
					intent.putExtra("pass", pass);
					//intent.putExtra("web", web);

					startActivity(intent);
					finish();

				} catch (JSONException e) {
					// JSON error
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (error != null) {
					if (error.toString().equals("com.android.volley.ServerError")){
						Toast.makeText(getApplicationContext(), "Phone Number and Password did not match", Toast.LENGTH_LONG).show();
					} else{
						Toast.makeText(getApplicationContext(), "Please try again later", Toast.LENGTH_LONG).show();
					}
					hideDialog();
				}else{
					Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
					hideDialog();}}
		}) {
			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("cid", username);
				params.put("pin", password);
				return params;
			}
		};
		MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	private boolean validateEmail() {
		String email = inputEmail.getText().toString().trim();

		if (email.isEmpty() || !isValidEmail(email)) {
			inputLayoutEmail.setError(getString(R.string.login_err_msg_email));
			requestFocus(inputEmail);
			return false;
		} else {
			inputLayoutEmail.setErrorEnabled(false);
		}
		return true;
	}

	private boolean validatePassword() {
		if (inputPassword.getText().toString().trim().isEmpty()) {
			inputLayoutPassword.setError(getString(R.string.login_err_msg_password));
			requestFocus(inputPassword);
			return false;
		} else {
			inputLayoutPassword.setErrorEnabled(false);
		}
		return true;
	}


	private static boolean isValidEmail(String email) {
		return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	private void requestFocus(View view) {
		if (view.requestFocus()) {
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
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
				case R.id.email:
					validateEmail();
					break;
				case R.id.password:
					validatePassword();
					break;
			}
		}
	}

}
