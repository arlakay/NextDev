package com.erd.nextdev2016.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.erd.nextdev2016.Profile;
import com.erd.nextdev2016.ProfileActivity;
import com.erd.nextdev2016.R;
import com.erd.nextdev2016.app.MyApplication;
import com.erd.nextdev2016.helper.SessionManager;
import com.erd.nextdev2016.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ILM on 5/6/2016.
 */
public class FragmentProfileUser extends Fragment implements Profile {
    public static final String url = Constants.BASE_URL + "/users/user";
    private static final String TAG = FragmentProfileUser.class.getSimpleName();
    private SessionManager session;

    @BindView(R.id.nama) EditText name;
    @BindView(R.id.dob)  EditText dob;
    @BindView(R.id.gender)  EditText gender;
    @BindView(R.id.province)  EditText province;
    @BindView(R.id.city)  EditText city;
    @BindView(R.id.address)  EditText address;

    public String fullName, email, cid, cadangan, strtext;

    public static FragmentProfileUser newInstance(){
        return new FragmentProfileUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_userinfo, container, false);
        ButterKnife.bind(this, rootView);

        ProfileActivity activity = (ProfileActivity) getActivity();
        String email = activity.getMyData();

        getProfileWhenLogin(email);

        return rootView;
    }

    private void getProfileWhenLogin(final String email) {
        String tag_string_req = "req_user";
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    String pid = jObj.getString("participant_id");
                    String cid = jObj.getString("category_id");
                    String teamName = jObj.getString("team_name");
                    fullName = jObj.getString("full_name");
                    String email = jObj.getString("email");
                    String phoneNo = jObj.getString("phone_number");
                    String pass = jObj.getString("password");
                    //String web = jObj.getString("web");

                    name.setText(fullName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    if (error.toString().equals("com.android.volley.ServerError")){
                        Toast.makeText(getActivity(), "Email and Password did not match", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(getActivity(), "Please try again later", Toast.LENGTH_LONG).show();
                    }
                }else{
//                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                }}
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void updateUserProfile(){

    }

    @Override
    public void saveEmail(String email) {
        this.email = email;
    }
}
