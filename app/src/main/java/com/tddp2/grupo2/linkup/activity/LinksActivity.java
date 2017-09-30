package com.tddp2.grupo2.linkup.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;

public class LinksActivity extends AppCompatActivity {

    private static final String TAG = "LinksActivity";
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private String currentFragment;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Notification n = getIntent().getParcelableExtra("notification");

        setContentView(R.layout.activity_main);

        fragment = null;

        resolveFragment(n);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.drawer_search_links);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                return selectFragment(menuItem);
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void resolveFragment(Notification n) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();

        if (n.fbid.isEmpty()) {
            Log.i(TAG, "savedInstanceState null");
            fragment = new LinksFragment();
        }else {
            Log.i(TAG, "savedInstanceState NOT null");
            fragment = new MyLinksFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("notification", n);
            fragment.setArguments(bundle);
        }
        tx.replace(R.id.frame, fragment);
        tx.commit();

    }

    private boolean selectFragment(MenuItem menuItem) {
        return changeFragment(menuItem.getItemId());
    }

    public boolean changeFragment(int fragmentId) {
        FragmentTransaction fragmentTransaction;

        switch (fragmentId) {
            case R.id.drawer_search_links:
                fragment = new LinksFragment();
                currentFragment = getResources().getString(R.string.app_name);
                break;
            case R.id.drawer_my_links:
                fragment = new MyLinksFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("notification", new Notification("","",""));
                fragment.setArguments(bundle);
                currentFragment = getResources().getString(R.string.item_my_links);
                break;
            case R.id.drawer_settings:
                fragment = new SettingsFragment();
                currentFragment = getResources().getString(R.string.item_settings);
                break;
            case R.id.drawer_profile:
                fragment = new ProfileFragment();
                currentFragment = getResources().getString(R.string.item_perfil);
                break;
            case R.id.drawer_logout:
                logout();
            default:
                fragment = null;
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(currentFragment);
            navigationView.setCheckedItem(fragmentId);
            return true;
        }
        return false;
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogExit);
        builder.setMessage("¿Desea salir de la aplicación?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        LinksActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (currentFragment.equals(getResources().getString(R.string.item_perfil))) {
            fragment.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (currentFragment.equals(getResources().getString(R.string.item_perfil))) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
