package com.example.rss_reader_android_kms.modules.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rss_reader_android_kms.R;
import com.example.rss_reader_android_kms.modules.rssreader.ListWebNewActivity;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText edtEmail, edtPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegister = findViewById(R.id.btnRegister);
        edtEmail = findViewById(R.id.edtEmailRegister);
        edtPassword = findViewById(R.id.edtPasswordRegister);
        firebaseAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(v -> {
            register();
        });
    }

    private void register() {
        String email, password;
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui long nhap Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            Toast.makeText(this, "Vui long nhap Password từ 6 kí tự trờ lên", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!LoginActivity.validate(email)) {
            Toast.makeText(this, "Vui long nhap lại Email", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ListWebNewActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}