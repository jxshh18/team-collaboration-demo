package com.example.dormmamu.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.dormmamu.R;
import com.example.dormmamu.model.Dorm;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView mapView;
    private ImageView backButton;
    private MyLocationNewOverlay locationOverlay;
    private GeoPoint dormPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.map);
        backButton = findViewById(R.id.backButton);

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        IMapController mapController = mapView.getController();
        mapController.setZoom(17.0);

        Dorm dorm = (Dorm) getIntent().getSerializableExtra("selected_dorm");
        if (dorm == null) {
            Toast.makeText(this, "No dorm data.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        double lat = dorm.getLatitude();
        double lon = dorm.getLongitude();
        dormPoint = new GeoPoint(lat, lon);
        mapController.setCenter(dormPoint);

        Marker marker = new Marker(mapView);
        marker.setPosition(dormPoint);
        marker.setTitle(dorm.getName());
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);

        requestLocationPerms();

        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void setupMyLocation() {
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        locationOverlay.enableMyLocation();
        locationOverlay.enableFollowLocation();
        mapView.getOverlays().add(locationOverlay);

        locationOverlay.runOnFirstFix(() -> runOnUiThread(() -> {
            GeoPoint user = locationOverlay.getMyLocation();
            if (user != null) drawRoute(user, dormPoint);
        }));
    }

    private void drawRoute(GeoPoint start, GeoPoint end) {
        RoadManager rm = new OSRMRoadManager(this, getPackageName());
        ArrayList<GeoPoint> points = new ArrayList<>();
        points.add(start);
        points.add(end);

        new Thread(() -> {
            Road road = rm.getRoad(points);
            if (road.mStatus != Road.STATUS_OK) return;

            Polyline overlay = RoadManager.buildRoadOverlay(road);
            runOnUiThread(() -> {
                mapView.getOverlays().add(overlay);
                mapView.invalidate();
                // zoom to start to make route visible
                mapView.getController().animateTo(start);
                mapView.getController().setZoom(16.5);
            });
        }).start();
    }

    private void requestLocationPerms() {
        String[] perms = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ArrayList<String> needed = new ArrayList<>();
        for (String perm : perms)
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED)
                needed.add(perm);

        if (!needed.isEmpty()) {
            ActivityCompat.requestPermissions(this, needed.toArray(new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE);
        } else setupMyLocation();
    }

    @Override
    public void onRequestPermissionsResult(int code, String[] perms, int[] results) {
        super.onRequestPermissionsResult(code, perms, results);
        setupMyLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
