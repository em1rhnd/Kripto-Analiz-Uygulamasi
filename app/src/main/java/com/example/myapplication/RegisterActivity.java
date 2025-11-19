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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText etIsimSoyisim, etEmail, etSifre, etDogumYili;
    Button btnRegisterUser, btnVazgec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        etIsimSoyisim = findViewById(R.id.et_isimsoyisim);
        etEmail = findViewById(R.id.et_email);
        etSifre = findViewById(R.id.et_sifre);
        etDogumYili = findViewById(R.id.et_dogumyili);
        btnRegisterUser = findViewById(R.id.btn_registerUser);
        btnVazgec = findViewById(R.id.btn_vazgec);

        btnRegisterUser.setOnClickListener(v -> {
            String isimSoyisim = etIsimSoyisim.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String sifre = etSifre.getText().toString().trim();
            String dogumYiliStr = etDogumYili.getText().toString().trim();

            if (isimSoyisim.isEmpty() || email.isEmpty() || sifre.isEmpty() || dogumYiliStr.isEmpty()) {
                showCustomToast("Lütfen tüm alanları doldurun");
                return;
            }

            int dogumYili = Integer.parseInt(dogumYiliStr);
            int mevcutYil = Calendar.getInstance().get(Calendar.YEAR);
            int yas = mevcutYil - dogumYili;

            if (yas < 18) {
                showCustomToast("Kayıt için en az 18 yaşında olmalısınız");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, sifre)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            showCustomToast("Kayıt başarılı: " + isimSoyisim);

                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates =
                                        new UserProfileChangeRequest.Builder()
                                                .setDisplayName(isimSoyisim)
                                                .build();
                                user.updateProfile(profileUpdates);
                            }

                            finish();
                        } else {
                            showCustomToast("Hata: " + task.getException().getMessage());
                        }
                    });

        });

        btnVazgec.setOnClickListener(view -> finish());
    }

    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_icon_kaldirma, null);
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
