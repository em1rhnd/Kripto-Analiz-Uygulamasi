package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.myapplication.api.CoinApiService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView rvFavoriler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView btnGeri = findViewById(R.id.btnGeri);
        TextView tvIsim = findViewById(R.id.tvProfilIsim);
        rvFavoriler = findViewById(R.id.rvFavoriler);

        rvFavoriler.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
            tvIsim.setText(user.getDisplayName());
        } else {
            tvIsim.setText("Misafir");
        }

        btnGeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences prefs = getSharedPreferences("FAVORILER", MODE_PRIVATE);
        Set<String> favoriIds = prefs.getStringSet("favoriListesi", new HashSet<String>());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coingecko.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CoinApiService api = retrofit.create(CoinApiService.class);

        api.getCoins().enqueue(new Callback<List<Coin>>() {
            @Override
            public void onResponse(Call<List<Coin>> call, Response<List<Coin>> response) {
                List<Coin> favoriListe = new ArrayList<Coin>();

                if (response.body() != null) {
                    for (Coin c : response.body()) {
                        String anahtar = c.getSymbol().toLowerCase();
                        if (favoriIds.contains(anahtar)) {
                            favoriListe.add(c);
                        }
                    }
                }

                rvFavoriler.setAdapter(new CoinAdapter(ProfileActivity.this, favoriListe));
            }

            @Override
            public void onFailure(Call<List<Coin>> call, Throwable t) {
            }
        });
    }
}
