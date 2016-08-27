package com.ncr.medicalconferencetracker.Acitivities.AdminActivities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ncr.medicalconferencetracker.Acitivities.StartupActivity;
import com.ncr.medicalconferencetracker.Fragments.AdminFragments.ConferenceFragment;
import com.ncr.medicalconferencetracker.Fragments.AdminFragments.TopicFragment;
import com.ncr.medicalconferencetracker.R;

public class AdminMasterActivity extends AppCompatActivity {

    private final int ACTOR_TYPE = 0;

    private FrameLayout fragmentHolder;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout drawerList;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_master);


        initUIElements();

        if(fragmentHolder != null)
        {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            ConferenceFragment conferenceFragment = ConferenceFragment.newInstance(this);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.adminmaster_fragmentholder, conferenceFragment).commit();
        }
    }

    private void initUIElements()
    {
        toolbar = (Toolbar) findViewById(R.id.adminmaster_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu, null));

        toolbarTitle = (TextView) findViewById(R.id.adminmaster_toolbar_title);

        fragmentHolder = (FrameLayout) findViewById(R.id.adminmaster_fragmentholder);
        drawerLayout = (DrawerLayout) findViewById(R.id.adminmaster_drawerlayout);
        drawerList = (RelativeLayout) findViewById(R.id.adminmaster_drawerlist);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void showConferenceFragment(View v)
    {
        ConferenceFragment conferenceFragment = ConferenceFragment.newInstance(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.adminmaster_fragmentholder, conferenceFragment);

        // Commit the transaction
        transaction.commit();
        toolbarTitle.setText(R.string.adminmaster_act_drawer_conference_title);
        drawerLayout.closeDrawer(drawerList);
    }

    public void showTopicFragment(View v)
    {
        TopicFragment topicFragment = TopicFragment.newInstance(this, ACTOR_TYPE, null);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.adminmaster_fragmentholder, topicFragment);

        // Commit the transaction
        transaction.commit();
        toolbarTitle.setText(R.string.adminmaster_act_drawer_topic_title);
        drawerLayout.closeDrawer(drawerList);


    }

    public void logOutAction(View v)
    {
        Intent logoutIntent = new Intent(this, StartupActivity.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(logoutIntent);
    }

}
