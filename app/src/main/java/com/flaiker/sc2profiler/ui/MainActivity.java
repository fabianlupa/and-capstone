package com.flaiker.sc2profiler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.flaiker.sc2profiler.R;
import com.flaiker.sc2profiler.models.Profile;
import com.flaiker.sc2profiler.models.Ranking;
import com.flaiker.sc2profiler.sync.SyncHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements
        LadderFragment.OnListFragmentInteractionListener,
        ProfileFragment.OnListFragmentInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private AdView mAdView;
    private boolean mTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (findViewById(R.id.item_detail_container) != null) {
            mTablet = true;
        }

        SyncHelper.init(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onListFragmentInteraction(Ranking item) {
        showProfile(item.characterId, item.displayName, item.realm);
    }

    @Override
    public void onListFragmentInteraction(Profile profile) {
        showProfile(profile.id, profile.name, profile.realm);
    }

    private void showProfile(int profileId, String profileName, int realm) {
        if (mTablet) {
            Bundle args = new Bundle();
            args.putInt(ProfileDetailFragment.EXTRA_PROFILE_ID, profileId);
            args.putString(ProfileDetailFragment.EXTRA_PROFILE_NAME, profileName);
            args.putInt(ProfileDetailFragment.EXTRA_REALM, realm);
            ProfileDetailFragment fragment = new ProfileDetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            Intent detailIntent = new Intent(this, ProfileDetailActivity.class);
            detailIntent.putExtra(ProfileDetailActivity.EXTRA_PROFILE_ID, profileId);
            detailIntent.putExtra(ProfileDetailActivity.EXTRA_PROFILE_NAME, profileName);
            detailIntent.putExtra(ProfileDetailActivity.EXTRA_REALM, realm);
            startActivity(detailIntent);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LadderFragment.newInstance();
                case 1:
                    return ProfileFragment.newInstance();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.ladder_tab_label);
                case 1:
                    return getResources().getString(R.string.profile_tab_label);
            }
            return null;
        }
    }
}
