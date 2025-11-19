package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.api.CoinApiService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCoins;
    private CoinAdapter coinAdapter;
    private List<Coin> coinList;
    private TextView textGreeting;

    private SharedPreferences preferences;
    private static final String PREF_NAME = "app_prefs";
    private static final String THEME_KEY = "theme_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int savedMode = preferences.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(savedMode);

        setContentView(R.layout.activity_home);


        ImageView darkToggle = findViewById(R.id.btnDarkMode);
        darkToggle.setOnClickListener(v -> {
            int currentMode = AppCompatDelegate.getDefaultNightMode();
            int newMode = (currentMode == AppCompatDelegate.MODE_NIGHT_YES)
                    ? AppCompatDelegate.MODE_NIGHT_NO
                    : AppCompatDelegate.MODE_NIGHT_YES;

            preferences.edit().putInt(THEME_KEY, newMode).apply();
            AppCompatDelegate.setDefaultNightMode(newMode);
            recreate();
        });


        textGreeting = findViewById(R.id.textGreeting);
        String ad = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        textGreeting.setText("Merhaba " + ad.split(" ")[0] + " 👋");



        ViewPager2 viewPagerBanner = findViewById(R.id.viewPagerBanner);
        List<BannerItem> banners = new ArrayList<>();
        banners.add(new BannerItem("Bitcoin Günlüğü", R.drawable.banner_btc));
        banners.add(new BannerItem("Ethereum Günlüğü", R.drawable.banner_ether));
        banners.add(new BannerItem("Litecoin Günlüğü", R.drawable.banner_ltc));

        BannerAdapter bannerAdapter = new BannerAdapter(banners);
        viewPagerBanner.setAdapter(bannerAdapter);


        bannerAdapter.setOnItemClickListener(banner -> {
            String url = "";
            if (banner.getTitle().equals("Bitcoin Günlüğü")) {
                url = "https://tr.investing.com/crypto/bitcoin/news";
            } else if (banner.getTitle().equals("Ethereum Günlüğü")) {
                url = "https://tr.investing.com/crypto/ethereum/news";
            } else if (banner.getTitle().equals("Litecoin Günlüğü")) {
                url = "https://www.coinkolik.com/altcoin-haberleri/litecoin/";
            }
            if (!url.isEmpty()) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });


        recyclerViewCoins = findViewById(R.id.recyclerViewCoins);
        recyclerViewCoins.setLayoutManager(new LinearLayoutManager(this));

        coinList = new ArrayList<>();
        coinAdapter = new CoinAdapter(HomeActivity.this, coinList);
        recyclerViewCoins.setAdapter(coinAdapter);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coingecko.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CoinApiService apiService = retrofit.create(CoinApiService.class);

        apiService.getCoins().enqueue(new Callback<List<Coin>>() {

            @Override
            public void onResponse(Call<List<Coin>> call, Response<List<Coin>> response) {
                if (response.isSuccessful() && response.body() != null) {


                    SharedPreferences prefs = getSharedPreferences("FAVORILER", MODE_PRIVATE);
                    Set<String> favoriler = prefs.getStringSet("favoriListesi", new HashSet<>());


                    for (Coin coin : response.body()) {
                        String key = coin.getSymbol().toLowerCase();
                        coin.setFavorite(favoriler.contains(key));
                    }

                    coinAdapter.updateCoinList(response.body());
                }
            }


            @Override
            public void onFailure(Call<List<Coin>> call, Throwable t) {}
        });


        ImageView btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent git = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(git);
            }
        });



    }
    @Override
    protected void onResume() {
        super.onResume();


        if (coinAdapter != null) {
            coinAdapter.notifyDataSetChanged();
        }
    }

}



