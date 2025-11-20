package com.example.dormmamu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormmamu.FavoritesAdapter;
import com.example.dormmamu.HomeActivity;
import com.example.dormmamu.LoginActivity;
import com.example.dormmamu.R;
import com.example.dormmamu.model.Dorm;
import com.example.dormmamu.utils.FavoriteManager;
import com.example.dormmamu.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView rvFavorites;
    private FavoritesAdapter adapter;
    private EditText searchBar;
    private LinearLayout emptyLayout;
    private ImageView menuButton;
    private SessionManager sessionManager;
    private ArrayList<Dorm> favoriteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        sessionManager = new SessionManager(this);
        drawerLayout = findViewById(R.id.drawerLayoutFav);
        navigationView = findViewById(R.id.navigationView);
        rvFavorites = findViewById(R.id.rvFavorites);
        searchBar = findViewById(R.id.searchBarFav);
        emptyLayout = findViewById(R.id.emptyLayout);
        menuButton = findViewById(R.id.menuButtonFav);

        setupDrawer();
        loadFavorites();
        setupSearch();
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open, R.string.close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home)
                startActivity(new Intent(this, HomeActivity.class));
            else if (id == R.id.nav_profile)
                startActivity(new Intent(this, ProfileActivity.class));
            else if (id == R.id.activity_edit_profile)
                startActivity(new Intent(this, EditProfileActivity.class));
            else if (id == R.id.nav_favorites)
                Toast.makeText(this, "You're already in Favorites", Toast.LENGTH_SHORT).show();
            else if (id == R.id.nav_help_center)
                startActivity(new Intent(this, HelpCenterActivity.class));
            else if (id == R.id.nav_privacy_policy)
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
            else if (id == R.id.nav_terms_conditions)
                startActivity(new Intent(this, TermsConditionsActivity.class));
            else if (id == R.id.nav_contact_support)
                startActivity(new Intent(this, ContactSupportActivity.class));
            else if (id == R.id.nav_delete_account)
                Toast.makeText(this, "Account deletion coming soon.", Toast.LENGTH_SHORT).show();
            else if (id == R.id.nav_logout) {
                sessionManager.clearSession();
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
            }

            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    private void loadFavorites() {
        favoriteList = FavoriteManager.getFavorites(this);

        if (favoriteList.isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            rvFavorites.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            rvFavorites.setVisibility(View.VISIBLE);
        }

        adapter = new FavoritesAdapter(
                favoriteList,
                dorm -> {
                    Intent i = new Intent(FavoritesActivity.this, DormDetailActivity.class);
                    i.putExtra("dorm", dorm);
                    startActivity(i);
                },
                dorm -> {
                    FavoriteManager.removeFavorite(FavoritesActivity.this, dorm);
                    favoriteList.remove(dorm);
                    adapter.notifyDataSetChanged();

                    if (favoriteList.isEmpty()) {
                        emptyLayout.setVisibility(View.VISIBLE);
                        rvFavorites.setVisibility(View.GONE);
                    }

                    Toast.makeText(FavoritesActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                }
        );

        rvFavorites.setLayoutManager(new LinearLayoutManager(this));
        rvFavorites.setAdapter(adapter);
    }

    private void setupSearch() {
        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFavorites(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void filterFavorites(String text) {
        ArrayList<Dorm> filtered = new ArrayList<>();
        for (Dorm dorm : favoriteList) {
            if (dorm.getName().toLowerCase().contains(text.toLowerCase())) {
                filtered.add(dorm);
            }
        }

        adapter = new FavoritesAdapter(
                filtered,
                dorm -> {
                    Intent i = new Intent(this, DormDetailActivity.class);
                    i.putExtra("dorm", dorm);
                    startActivity(i);
                },
                dorm -> {
                    FavoriteManager.removeFavorite(this, dorm);
                    favoriteList.remove(dorm);
                    filterFavorites(text); // refresh filter
                }
        );

        rvFavorites.setAdapter(adapter);
    }
}
