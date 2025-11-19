package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {

    private final List<Coin> coinList;
    private final Context context;
    private final SharedPreferences prefs;

    public CoinAdapter(Context context, List<Coin> coinList) {
        this.context = context;
        this.coinList = coinList;
        prefs = context.getSharedPreferences("FAVORILER", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coin, parent, false);
        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder holder, int position) {

        Coin coin = coinList.get(position);

        holder.tvName.setText(coin.getName());
        holder.tvSymbol.setText(coin.getSymbol().toUpperCase());
        holder.tvPrice.setText(String.format("₺%,.2f", coin.getPrice()));

        double degisim = coin.getChange();
        holder.tvChange.setText(String.format("%.2f%%", degisim));

        int renk = (degisim >= 0)
                ? ContextCompat.getColor(context, android.R.color.holo_green_light)
                : ContextCompat.getColor(context, android.R.color.holo_red_light);

        holder.tvChange.setTextColor(renk);

        Glide.with(context)
                .load(coin.getLogoUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivLogo);

        String anahtar = coin.getSymbol().toLowerCase();

        Set<String> favoriler = prefs.getStringSet("favoriListesi", new HashSet<>());
        boolean favMi = favoriler.contains(anahtar);
        coin.setFavorite(favMi);

        holder.ivFavorite.setImageResource(
                favMi ? R.drawable.yildiz_sari : R.drawable.yildiz_seffaf
        );

        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Set<String> set = new HashSet<>(prefs.getStringSet("favoriListesi", new HashSet<>()));

                if (coin.isFavorite()) {
                    set.remove(anahtar);
                    coin.setFavorite(false);
                    holder.ivFavorite.setImageResource(R.drawable.yildiz_seffaf);
                } else {
                    set.add(anahtar);
                    coin.setFavorite(true);
                    holder.ivFavorite.setImageResource(R.drawable.yildiz_sari);
                }

                prefs.edit().putStringSet("favoriListesi", set).apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }

    public static class CoinViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLogo, ivFavorite;
        TextView tvName, tvSymbol, tvPrice, tvChange;

        public CoinViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLogo = itemView.findViewById(R.id.ivCoinLogo);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            tvName = itemView.findViewById(R.id.tvCoinName);
            tvSymbol = itemView.findViewById(R.id.tvCoinSymbol);
            tvPrice = itemView.findViewById(R.id.tvCoinPrice);
            tvChange = itemView.findViewById(R.id.tvCoinChange);
        }
    }

    public void updateCoinList(List<Coin> yeniListe) {
        coinList.clear();
        coinList.addAll(yeniListe);
        notifyDataSetChanged();
    }
}
