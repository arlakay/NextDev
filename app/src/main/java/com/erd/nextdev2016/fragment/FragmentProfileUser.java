package com.erd.nextdev2016.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.erd.nextdev2016.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ILM on 5/6/2016.
 */
public class FragmentProfileUser extends Fragment {
    @BindView(R.id.nama) EditText name;
    @BindView(R.id.dob)  EditText dob;
    @BindView(R.id.gender)  EditText gender;
    @BindView(R.id.province)  EditText province;
    @BindView(R.id.city)  EditText city;
    @BindView(R.id.address)  EditText address;

    public static FragmentProfileUser newInstance() {
        return new FragmentProfileUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_userinfo, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    private void getProfileWhenLogin() {

    }
}
