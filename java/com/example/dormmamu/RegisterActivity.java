package com.example.dormmamu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dormmamu.database.AppDatabase;
import com.example.dormmamu.model.User;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameInput, emailInput, passwordInput;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);

        TextView haveAccountText = findViewById(R.id.haveAccountText);
        SpannableString ss = new SpannableString("Already have an account? Login here.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        };
        ss.setSpan(clickableSpan, 25, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        haveAccountText.setText(ss);
        haveAccountText.setMovementMethod(LinkMovementMethod.getInstance());
        haveAccountText.setHighlightColor(Color.TRANSPARENT);
        haveAccountText.setLinkTextColor(Color.BLUE);

        AppDatabase db = AppDatabase.getInstance(this);

        registerButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.endsWith("@lpunetwork.edu.ph")) {
                Toast.makeText(this, "Use your LPUNetwork email only!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6 || !password.matches(".*\\d.*")) {
                Toast.makeText(this, "Password must be 6+ chars and contain a number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.userDao().getUserByEmail(email) != null) {
                Toast.makeText(this, "Email already registered!", Toast.LENGTH_SHORT).show();
                return;
            }

            db.userDao().insert(new User(username, email, password));
            Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
