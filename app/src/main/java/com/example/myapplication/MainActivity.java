package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button btnLogin, btnRegister;
    EditText etEmail, etPassword;
    TextView tvForgot;

    private void showToast(String mesaj) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_icon_kaldirma, null);
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(mesaj);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        etEmail = findViewById(R.id.et_username_login);
        etPassword = findViewById(R.id.et_password_login);
        tvForgot = findViewById(R.id.tv_forgot);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailGirisi = etEmail.getText().toString().trim();
                String sifreGirisi = etPassword.getText().toString().trim();

                if(emailGirisi.isEmpty() || sifreGirisi.isEmpty()){
                    showToast("Email ve şifre gerekli");
                    return;
                }

                mAuth.signInWithEmailAndPassword(emailGirisi, sifreGirisi)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                String kullaniciIsim = mAuth.getCurrentUser().getDisplayName();
                                if (kullaniciIsim == null) kullaniciIsim = ""; // boşsa kontrol
                                showToast("Giriş Başarılı, Yönlendiriliyorsunuz...");
                                Intent git = new Intent(MainActivity.this, HomeActivity.class);
                                git.putExtra("KULLANICI_ISIM", kullaniciIsim);
                                startActivity(git);
                                finish();
                            }
                            else {
                                String hataMesaji = task.getException().getMessage();
                                if(hataMesaji.contains("no user record") || hataMesaji.contains("There is no user")) {
                                    showToast("Kullanıcı bulunamadı");
                                } else if(hataMesaji.contains("password is invalid")) {
                                    showToast("Şifre hatalı");
                                } else {
                                    showToast("Hata: " + hataMesaji);
                                }
                            }
                        });
            }
        });

        btnRegister.setOnClickListener(v -> {
            Intent git = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(git);
        });

        tvForgot.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if(email.isEmpty()){
                showToast("Lütfen e-posta girin");
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            showToast("Şifre sıfırlama maili gönderildi");
                        } else {
                            showToast("Hata: " + task.getException().getMessage());
                        }
                    });
        });
    }
}
