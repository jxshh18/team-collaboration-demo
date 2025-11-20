// C:/Users/pc/AndroidStudioProjects/Dormmamu4/app/src/main/java/com/example/dormmamu/ui/DormDetailActivity.java
package com.example.dormmamu.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.dormmamu.HomeActivity;
import com.example.dormmamu.LoginActivity;
import com.example.dormmamu.R;
import com.example.dormmamu.model.Dorm;
import com.example.dormmamu.utils.FavoriteManager;
import com.example.dormmamu.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class DormDetailActivity extends AppCompatActivity {

    private Dorm selectedDorm;
    private GestureDetector gestureDetector;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView btnBack, btnMenu, imgMap, ivTogglePricing, imgProfile, dormMainImage;
    private Button btnFavorite;
    private TextView tvDormName, tvPricing, tvDeposit, tvBathroom, tvMapAddress, tvContact, tvAddress;
    private LinearLayout layoutPricingDetails, layoutPricingHeader;

    private ArrayList<Dorm> dormList;
    private int selectedPosition;
    private boolean isFavorite = false;

    private ImageView imgDorm, btnPrev, btnNext;
    private int[] images;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dorm_details);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        imgMap = findViewById(R.id.imgMap);
        btnBack = findViewById(R.id.btnBack);
        btnMenu = findViewById(R.id.btnMenu);
        btnFavorite = findViewById(R.id.btnFavorite);
        ivTogglePricing = findViewById(R.id.ivTogglePricing);
        imgProfile = findViewById(R.id.imgProfile);
        dormMainImage = findViewById(R.id.imgDorm);

        tvDormName = findViewById(R.id.tvDormName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPricing = findViewById(R.id.tvPricing);
        tvDeposit = findViewById(R.id.tvDeposit);
        tvBathroom = findViewById(R.id.tvBathroom);
        tvMapAddress = findViewById(R.id.tvMapAddress);
        tvContact = findViewById(R.id.tvContact);

        layoutPricingDetails = findViewById(R.id.layoutPricingDetails);
        layoutPricingHeader = findViewById(R.id.layoutPricingHeader);

        imgProfile.setOnClickListener(v -> {
            Intent intent = new Intent(DormDetailActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 120;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null) return false;
                float dx = e2.getX() - e1.getX();
                float dy = e2.getY() - e1.getY();

                if (Math.abs(dx) > Math.abs(dy) &&
                        Math.abs(dx) > SWIPE_THRESHOLD &&
                        Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {

                    if (dx > 0) swipeRight();
                    else swipeLeft();
                    return true;
                }
                return false;
            }
        });

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout,
                        R.string.app_name, R.string.app_name);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        btnBack.setOnClickListener(v -> onBackPressed());

        btnMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(navigationView))
                drawerLayout.closeDrawer(navigationView);
            else
                drawerLayout.openDrawer(navigationView);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            handleMenuClick(item);
            drawerLayout.closeDrawer(navigationView);
            return true;
        });

        // --------------------------------------------------------------
        // ✅ FIXED EXPAND/COLLAPSE LOGIC (arrow down first → arrow up)
        // --------------------------------------------------------------
        layoutPricingDetails.setVisibility(View.GONE); // hidden at start
        ivTogglePricing.setRotation(0f); // arrow DOWN

        View.OnClickListener togglePricing = v -> {
            boolean visible = layoutPricingDetails.getVisibility() == View.VISIBLE;

            if (visible) {
                layoutPricingDetails.setVisibility(View.GONE);
                ivTogglePricing.animate().rotation(0f).setDuration(200); // arrow down
            } else {
                layoutPricingDetails.setVisibility(View.VISIBLE);
                ivTogglePricing.animate().rotation(180f).setDuration(200); // arrow up
            }
        };

        layoutPricingHeader.setOnClickListener(togglePricing);
        ivTogglePricing.setOnClickListener(togglePricing);
        // --------------------------------------------------------------

        dormList = (ArrayList<Dorm>) getIntent().getSerializableExtra("dorm_list");
        selectedPosition = getIntent().getIntExtra("selected_position", 0);

        if (dormList == null || dormList.isEmpty()) {
            Toast.makeText(this, "No dorm data found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        updateDormDetails(selectedPosition);

        isFavorite = FavoriteManager.isFavorite(this, selectedDorm.getName());
        updateFavoriteUI();

        imgMap.setOnClickListener(v -> {
            if (selectedDorm == null) {
                Toast.makeText(this, "Dorm data not available", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(DormDetailActivity.this, MapActivity.class);
            intent.putExtra("selected_dorm", selectedDorm);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        tvContact.setOnClickListener(v -> {
            String contact = selectedDorm.getContactNumber();
            if (contact == null || contact.trim().isEmpty()) {
                Toast.makeText(this, "No contact available", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + contact));
            startActivity(intent);
        });

        btnFavorite.setOnClickListener(v -> {
            bounce(btnFavorite);
            isFavorite = !isFavorite;
            FavoriteManager.saveFavorite(DormDetailActivity.this, selectedDorm, isFavorite);
            Snackbar.make(v, isFavorite ? "Added to Favorites" : "Removed from Favorites", Snackbar.LENGTH_SHORT).show();
            updateFavoriteUI();
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    private void swipeLeft() {
        if (selectedPosition < dormList.size() - 1) {
            selectedPosition++;
            updateDormDetails(selectedPosition);
            refreshFavoriteStatus();
        } else Toast.makeText(this, "No next dorm", Toast.LENGTH_SHORT).show();
    }

    private void swipeRight() {
        if (selectedPosition > 0) {
            selectedPosition--;
            updateDormDetails(selectedPosition);
            refreshFavoriteStatus();
        } else Toast.makeText(this, "No previous dorm", Toast.LENGTH_SHORT).show();
    }

    private void refreshFavoriteStatus() {
        isFavorite = FavoriteManager.isFavorite(this, selectedDorm.getName());
        updateFavoriteUI();
    }

    private void updateFavoriteUI() {
        if (isFavorite) {
            btnFavorite.setText("Favorite");
            btnFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_filled, 0, 0, 0);
        } else {
            btnFavorite.setText("Favorite");
            btnFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_outline, 0, 0, 0);
        }
        btnFavorite.setCompoundDrawablePadding(12);
    }

    private void updateDormDetails(int position) {
        selectedDorm = dormList.get(position);

        tvDormName.setText(selectedDorm.getName());
        tvAddress.setText(selectedDorm.getLocation());
        tvPricing.setText(selectedDorm.getFormattedPrice());
        tvDeposit.setText("Deposit: " + selectedDorm.getDeposit());
        tvBathroom.setText("Bathroom: " + selectedDorm.getBathroomType());
        tvMapAddress.setText(selectedDorm.getAddressDetail());

        int[] gallery = selectedDorm.getImageGallery();
        if (gallery != null && gallery.length > 0) {
            dormMainImage.setImageResource(gallery[0]);
        } else {
            dormMainImage.setImageResource(R.drawable.dorm1);
        }

        currentIndex = 0;
        setupImageSlider();

        String contact = selectedDorm.getContactNumber();
        if (contact == null || contact.trim().isEmpty()) contact = "No contact available";
        tvContact.setText(contact);
    }

    private void bounce(View v) {
        v.animate().scaleX(1.12f).scaleY(1.12f).setDuration(110)
                .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(110))
                .start();
    }

    private void setupImageSlider() {
        images = selectedDorm.getImageGallery();
        if (images == null || images.length == 0) images = new int[]{R.drawable.dorm1};

        imgDorm = findViewById(R.id.imgDorm);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        if (currentIndex < 0) currentIndex = 0;
        if (currentIndex >= images.length) currentIndex = images.length - 1;

        imgDorm.setImageResource(images[currentIndex]);
        updateButtons();

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                imgDorm.setImageResource(images[currentIndex]);
                updateButtons();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < images.length - 1) {
                currentIndex++;
                imgDorm.setImageResource(images[currentIndex]);
                updateButtons();
            }
        });
    }

    private void updateButtons() {
        btnPrev.setAlpha(currentIndex == 0 ? 0.3f : 1f);
        btnPrev.setEnabled(currentIndex != 0);

        btnNext.setAlpha(currentIndex == images.length - 1 ? 0.3f : 1f);
        btnNext.setEnabled(currentIndex != images.length - 1);
    }

    private void handleMenuClick(@NonNull MenuItem item) {
        int id = item.getItemId();
        SessionManager sessionManager = new SessionManager(this);

        if (id == R.id.nav_home) startActivity(new Intent(this, HomeActivity.class));
        else if (id == R.id.nav_profile) startActivity(new Intent(this, ProfileActivity.class));
        else if (id == R.id.activity_edit_profile)
            startActivity(new Intent(this, EditProfileActivity.class));
        else if (id == R.id.nav_favorites) startActivity(new Intent(this, MapActivity.class));
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
        } else Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();

        drawerLayout.closeDrawer(GravityCompat.END);
    }
}
