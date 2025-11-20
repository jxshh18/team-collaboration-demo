package com.example.dormmamu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.dormmamu.HomeActivity;
import com.example.dormmamu.LoginActivity;
import com.example.dormmamu.R;
import com.example.dormmamu.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class TermsConditionsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView btnBack, menuButton, imgProfile;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition); // make sure XML file matches this name

        sessionManager = new SessionManager(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        btnBack = findViewById(R.id.btnBack);
        menuButton = findViewById(R.id.menuButton);
        imgProfile = findViewById(R.id.imgProfile);

        btnBack.setOnClickListener(v -> onBackPressed());

        menuButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(navigationView)) drawerLayout.closeDrawer(navigationView);
            else drawerLayout.openDrawer(navigationView);
        });

        imgProfile.setOnClickListener(v -> {
            startActivity(new Intent(TermsConditionsActivity.this, ProfileActivity.class));
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            handleMenuClick(item);
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    private void handleMenuClick(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) startActivity(new Intent(this, HomeActivity.class));
        else if (id == R.id.nav_profile) startActivity(new Intent(this, ProfileActivity.class));
        else if (id == R.id.activity_edit_profile) startActivity(new Intent(this, EditProfileActivity.class));
        else if (id == R.id.nav_favorites) startActivity(new Intent(this, MapActivity.class));
        else if (id == R.id.nav_help_center) startActivity(new Intent(this, HelpCenterActivity.class));
        else if (id == R.id.nav_privacy_policy) startActivity(new Intent(this, PrivacyPolicyActivity.class));
        else if (id == R.id.nav_terms_conditions) startActivity(new Intent(this, TermsConditionsActivity.class));
        else if (id == R.id.nav_contact_support) startActivity(new Intent(this, ContactSupportActivity.class));
        else if (id == R.id.nav_delete_account)
            Toast.makeText(this, "Account deletion coming soon.", Toast.LENGTH_SHORT).show();
        else if (id == R.id.nav_logout) {
            sessionManager.clearSession();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
    }
}
