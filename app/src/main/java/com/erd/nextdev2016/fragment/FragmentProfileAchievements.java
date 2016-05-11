package com.erd.nextdev2016.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erd.nextdev2016.R;

/**
 * Created by ILM on 5/6/2016.
 */
public class FragmentProfileAchievements extends Fragment {

    public static FragmentProfileAchievements newInstance() {
        return new FragmentProfileAchievements();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_profile_achievements, container, false);

        return rootView;
    }

}
