package com.erd.nextdev2016;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.erd.nextdev2016.fragment.FragmentProfileAchievements;
import com.erd.nextdev2016.fragment.FragmentProfileUser;
import com.erd.nextdev2016.helper.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ILM on 5/5/2016.
 */
public class ProfileActivity extends BaseActivity {
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            Intent intent = new Intent(ProfileActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

        Intent i = getIntent();
        String id = i.getStringExtra("pid");
        String namaTim = i.getStringExtra("tName");
        String namaLengkap = i.getStringExtra("fName");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnLogout = (Button)findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentProfileAchievements(), "ACHIEVEMENTS");
        adapter.addFrag(new FragmentProfileUser(), "USER INFO");
//        adapter.addFrag(new ThreeFragment(), "GALLERY");
//        adapter.addFrag(new FourFragment(), "FAQ");
//        adapter.addFrag(new FiveFragment(), "OTHER");
//        adapter.addFrag(new SixFragment(), "SIX");
//        adapter.addFrag(new SevenFragment(), "SEVEN");
//        adapter.addFrag(new EightFragment(), "EIGHT");
//        adapter.addFrag(new NineFragment(), "NINE");
//        adapter.addFrag(new TenFragment(), "TEN");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
