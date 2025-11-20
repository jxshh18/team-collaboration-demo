package com.example.dormmamu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dormmamu.database.AppDatabase;
import com.example.dormmamu.model.User;
import com.example.dormmamu.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginButton;
    TextView registerText;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.registerText);

        AppDatabase db = AppDatabase.getInstance(this);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = db.userDao().login(email, password);

            if (user != null) {
                sessionManager.saveUserSession(user.getUsername(), user.getEmail());
                Toast.makeText(this, "Welcome, " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        String text = "Don't have an account? Register here.";
        SpannableString spannable = new SpannableString(text);

        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 23, text.length(), 0);
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(android.view.View widget) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        }, 23, text.length(), 0);

        registerText.setText(spannable);
        registerText.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
