package com.flaiker.sc2profiler.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.flaiker.sc2profiler.R;

public class ProfileDetailActivity extends AppCompatActivity {
    public static final String EXTRA_PROFILE_ID = ProfileDetailFragment.EXTRA_PROFILE_ID;
    public static final String EXTRA_PROFILE_NAME = ProfileDetailFragment.EXTRA_PROFILE_NAME;
    public static final String EXTRA_REALM = ProfileDetailFragment.EXTRA_REALM;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle("TEST");
        }

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();

            bundle.putInt(EXTRA_PROFILE_ID, getIntent().getExtras().getInt(EXTRA_PROFILE_ID));
            bundle.putString(EXTRA_PROFILE_NAME,
                    getIntent().getExtras().getString(EXTRA_PROFILE_NAME));
            bundle.putInt(EXTRA_REALM,
                    getIntent().getExtras().getInt(EXTRA_REALM));
            bundle.putParcelable("", Uri.EMPTY);
            ProfileDetailFragment fragment = new ProfileDetailFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.profile_detail_container, fragment)
                    .commit();
        }
    }
}
