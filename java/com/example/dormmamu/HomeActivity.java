package com.example.dormmamu;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dormmamu.adapter.DormAdapter;
import com.example.dormmamu.database.AppDatabase;
import com.example.dormmamu.database.DormDao;
import com.example.dormmamu.model.Dorm;
import com.example.dormmamu.ui.ContactSupportActivity;
import com.example.dormmamu.ui.EditProfileActivity;
import com.example.dormmamu.ui.FilterDialog;
import com.example.dormmamu.ui.HelpCenterActivity;
import com.example.dormmamu.ui.MapActivity;
import com.example.dormmamu.ui.PrivacyPolicyActivity;
import com.example.dormmamu.ui.ProfileActivity;
import com.example.dormmamu.ui.TermsConditionsActivity;
import com.example.dormmamu.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView dormRecyclerView;
    private DormAdapter dormAdapter;
    private SessionManager sessionManager;
    private List<Dorm> dormList = new ArrayList<>();
    private ImageView menuButton, btnFilters;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        dormRecyclerView = findViewById(R.id.dormRecyclerView);
        menuButton = findViewById(R.id.menuButton);
        btnFilters = findViewById(R.id.btnFilters);
        searchBar = findViewById(R.id.searchBar);

        menuButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        setupDormList();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                dormAdapter.getFilter().filter(s.toString());
            }
        });
        ImageView profileImage = findViewById(R.id.profileImage);

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        btnFilters.setOnClickListener(v -> {
            FilterDialog.show(HomeActivity.this, (min, max, bath, cities, sortBy) -> {
                dormAdapter.applyFilters(
                        "", bath, new HashSet<>(cities),
                        sortBy.equals("Price (Low → High)") ? 1 :
                                sortBy.equals("Price (High → Low)") ? 2 :
                                        sortBy.equals("Rating (High → Low)") ? 3 :
                                                sortBy.equals("Rating (Low → High)") ? 4 : 0,
                        min, max
                );
                Toast.makeText(HomeActivity.this, "Filters Applied", Toast.LENGTH_SHORT).show();
            });
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

    private void setupDormList() {
        AppDatabase db = AppDatabase.getInstance(this);
        DormDao dormDao = db.dormDao();

        dormDao.deleteAllDorms();  // Clear table first to avoid duplicates
        dormList.clear();

        dormDao.insertDorm(new Dorm(
                "Governor's Hills Dormitory",
                "Governor’s Hills, Manggahan — General Trias",
                "₱3500", "₱2000 deposit", "Private Bathroom",
                "Near Governor’s Hills Clubhouse", "09123456701",
                new int[]{R.drawable.dorm1, R.drawable.dorm2, R.drawable.dorm3},
                14.32262, 120.90990, 4.5f, 3500
        ));

        dormDao.insertDorm(new Dorm(
                "Tsarina Student Place",
                "Tsarina Grand Villas, Manggahan — General Trias",
                "₱3000", "₱1500 deposit", "Shared Bathroom",
                "Near Tsarina Phase 3 Entrance", "09123456702",
                new int[]{R.drawable.dorm4, R.drawable.dorm5, R.drawable.dorm6},
                14.32347, 120.91688, 4.2f, 3000
        ));

        dormDao.insertDorm(new Dorm(
                "Florida Sun Estates Dorm",
                "Florida Sun Estates, Manggahan — General Trias",
                "₱4200", "₱2500 deposit", "Private Ensuite",
                "Walking distance to Florida Sun Market", "09123456703",
                new int[]{R.drawable.dorm7, R.drawable.dorm8, R.drawable.dorm9},
                14.32213, 120.91739, 4.6f, 4200
        ));

        dormDao.insertDorm(new Dorm(
                "LPU Student Dormitory",
                "Riverside, Zone 2 — Near LPU Cavite",
                "₱3800", "₱2000 deposit", "Shared Bathroom",
                "5 minutes walk to LPU Cavite back gate", "09123456704",
                new int[]{R.drawable.dorm10, R.drawable.dorm11, R.drawable.dorm12},
                14.32580, 120.93820, 4.3f, 3800
        ));

        dormDao.insertDorm(new Dorm(
                "Southpoint LPU Residence",
                "Southpoint Subdivision — General Trias (Near LPU Cavite)",
                "₱4500", "₱2500 deposit", "Private Bathroom",
                "Beside Southpoint Terminal, walking distance to LPU Cavite", "09123456705",
                new int[]{R.drawable.dorm13, R.drawable.dorm14, R.drawable.dorm15},
                14.32610, 120.93750, 4.7f, 4500
        ));


        dormList.addAll(dormDao.getAllDorms());

        dormRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dormAdapter = new DormAdapter(this, dormList);
        dormRecyclerView.setAdapter(dormAdapter);
    }
}
