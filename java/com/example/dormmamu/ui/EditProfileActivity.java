package com.example.dormmamu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.dormmamu.HomeActivity;
import com.example.dormmamu.LoginActivity;
import com.example.dormmamu.R;
import com.example.dormmamu.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etDescription, etContact, etAddress;
    private Button btnSave, btnCancel;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etDescription = findViewById(R.id.etDescription);
        etContact = findViewById(R.id.etContact);
        etAddress = findViewById(R.id.etAddress);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);


        sessionManager = new SessionManager(this);

        etDescription.setText(sessionManager.getDescription());
        etContact.setText(sessionManager.getContact());
        etAddress.setText(sessionManager.getAddress());


        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String desc = etDescription.getText().toString().trim();
            String contact = etContact.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (desc.isEmpty() || contact.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            sessionManager.saveProfileInfo(desc, contact, address);
            Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
            finish();
        });

    }
}
