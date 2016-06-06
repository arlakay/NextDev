package com.erd.nextdev2016;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.erd.nextdev2016.fragment.OneFragment;
import com.erd.nextdev2016.fragment.TwoFragment;
import com.erd.nextdev2016.helper.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScrollableTabsActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private SessionManager session;
    private int mCurrentSelectedPosition;
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollable_tabs);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .show(OneFragment.newInstance())
                    //.replace(R.id.viewpager, OneFragment.newInstance())
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        final String cid = user.get(SessionManager.KEY_CID);
        final String fname = user.get(SessionManager.KEY_FNAME);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_apply_idea:
                        //Snackbar.make(mContentFrame, "", Snackbar.LENGTH_SHORT).show();
                        Intent intent = new Intent(ScrollableTabsActivity.this, SubmitIdeStepOne.class);
                        startActivity(intent);
                        mCurrentSelectedPosition = 0;
                        return true;
                    case R.id.navigation_item_article:
                        Intent intent2 = new Intent(ScrollableTabsActivity.this, ScrollableTabsActivity.class);
                        startActivity(intent2);
                        mCurrentSelectedPosition = 1;
                        return true;
                    case R.id.navigation_item_gallery:
                        Intent intent8 = new Intent(ScrollableTabsActivity.this, GalleryActivity.class);
                        startActivity(intent8);
                        mCurrentSelectedPosition = 2;
                        return true;
                    case R.id.navigation_item_polling:
                        Intent intent3 = new Intent(ScrollableTabsActivity.this, PollingActivity.class);
                        startActivity(intent3);
                        mCurrentSelectedPosition = 3;
                        return true;
                    case R.id.navigation_item_telkomsel_feed:
                        Intent intent4 = new Intent(ScrollableTabsActivity.this, TelkomselPromoActivity.class);
                        startActivity(intent4);
                        mCurrentSelectedPosition = 4;
                        return true;
                    case R.id.navigation_item_booking:
                        Intent intent5 = new Intent(ScrollableTabsActivity.this, JoinTheRoadshowActivity.class);
                        startActivity(intent5);
                        mCurrentSelectedPosition = 5;
                        return true;
                    case R.id.navigation_item_faq:
                        Intent intent6 = new Intent(ScrollableTabsActivity.this, FAQActivity.class);
                        startActivity(intent6);
                        mCurrentSelectedPosition = 6;
                        return true;
                    case R.id.navigation_item_settings:
                        Intent intent7 = new Intent(ScrollableTabsActivity.this, SettingsActivity.class);
                        startActivity(intent7);
                        mCurrentSelectedPosition = 7;
                        return true;
                    default:
                        return true;
                }
            }
        });
        View header=navigationView.getHeaderView(0);

        TextView txtActiveUserName = (TextView)header.findViewById(R.id.name);
        CircleImageView imgUser = (CircleImageView) header.findViewById(R.id.circleView);
        txtActiveUserName.setText(cid);

        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            txtActiveUserName.setVisibility(View.GONE);
            imgUser.setVisibility(View.GONE);
        }else{
            txtActiveUserName.setVisibility(View.GONE);
            imgUser.setVisibility(View.GONE);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
        Menu menu = navigationView.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);
        recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_user:
                // Intent ke Login
                Intent intent = new Intent(ScrollableTabsActivity.this, NewProfileActivity.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TwoFragment(), "NEXTDEV 2016");
        adapter.addFrag(new OneFragment(), "NEWS");
        //adapter.addFrag(new ThreeFragment(), "GALLERY");
        //adapter.addFrag(new FourFragment(), "FAQ");
        //adapter.addFrag(new FiveFragment(), "OTHER");
        //adapter.addFrag(new SixFragment(), "SIX");
        //adapter.addFrag(new SevenFragment(), "SEVEN");
        //adapter.addFrag(new EightFragment(), "EIGHT");
        //adapter.addFrag(new NineFragment(), "NINE");
        //adapter.addFrag(new TenFragment(), "TEN");
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
