package ru.startandroid1.taximanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ru.startandroid1.taximanager.R;
import ru.startandroid1.taximanager.adapter.TabsPagerFragmentAdapterCustomer;

public class UsersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPagerUser;
    TextView tVMainActivityLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_user);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRequest();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user);

        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();

        tVMainActivityLogin = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tVMainActivityLogin);
        TextView tVMainActivityName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tVMainActivityName);

        tVMainActivityLogin.setText(intent.getStringExtra("LALogin"));
        tVMainActivityName.setText(intent.getStringExtra("LAName"));

        initTabs();
    }

    private void initTabs() {
        viewPagerUser = (ViewPager)findViewById(R.id.viewPagerUser);
        TabsPagerFragmentAdapterCustomer adapter = new TabsPagerFragmentAdapterCustomer(getSupportFragmentManager());
        viewPagerUser.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayoutUser);
        tabLayout.setupWithViewPager(viewPagerUser);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_user) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_manage_user) {

        } else if (id == R.id.nav_new_request) {
            addRequest();
        } else if (id == R.id.nav_current_request) {
            viewPagerUser.setCurrentItem(0);
        } else if (id == R.id.nav_all_requests) {
            viewPagerUser.setCurrentItem(1);
        } else if (id == R.id.nav_share_user) {
            viewPagerUser.setCurrentItem(2);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addRequest(){
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        intent.putExtra("username", tVMainActivityLogin.getText().toString());
        startActivity(intent);
    }
}
