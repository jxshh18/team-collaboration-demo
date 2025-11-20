package com.example.dormmamu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.dormmamu.HomeActivity;
import com.example.dormmamu.LoginActivity;
import com.example.dormmamu.R;
import com.example.dormmamu.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvDescription, tvContact, tvAddress;
    private SessionManager sessionManager;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView btnMenu, btnBack;
    private Button btnEditProfile, btnLogout, btnMyFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvDescription = findViewById(R.id.tvDescription);
        tvContact = findViewById(R.id.tvContact);
        tvAddress = findViewById(R.id.tvAddress);

        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawer_layout_profile);
        btnMenu = findViewById(R.id.btnMenu);
        btnBack = findViewById(R.id.btnBack);

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        btnMyFavorites = findViewById(R.id.btnMyFavorites);


        populateProfile();


        btnBack.setOnClickListener(v -> finish());


        btnEditProfile.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class))
        );


        btnMyFavorites.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, FavoritesActivity.class))
        );


        btnLogout.setOnClickListener(v -> {
            sessionManager.clearSession();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });


        btnMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) startActivity(new Intent(this, HomeActivity.class));
            else if (id == R.id.nav_profile) startActivity(new Intent(this, ProfileActivity.class));
            else if (id == R.id.activity_edit_profile) startActivity(new Intent(this, EditProfileActivity.class));
            else if (id == R.id.nav_favorites) startActivity(new Intent(this, MapActivity.class));
            else if (id == R.id.nav_help_center) startActivity(new Intent(this, HelpCenterActivity.class));
            else if (id == R.id.nav_privacy_policy) startActivity(new Intent(this, PrivacyPolicyActivity.class));
            else if (id == R.id.nav_terms_conditions) startActivity(new Intent(this, TermsConditionsActivity.class));
            else if (id == R.id.nav_contact_support) startActivity(new Intent(this, ContactSupportActivity.class));
            else if (id == R.id.nav_delete_account) Toast.makeText(this, "Account deletion coming soon.", Toast.LENGTH_SHORT).show();
            else if (id == R.id.nav_logout) {
                sessionManager.clearSession();
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        populateProfile();
    }

    private void populateProfile() {
        tvName.setText(sessionManager.getUsername());
        tvEmail.setText(sessionManager.getEmail());
        tvDescription.setText(sessionManager.getDescription());
        tvContact.setText(sessionManager.getContact());
        tvAddress.setText(sessionManager.getAddress());
    }
}
