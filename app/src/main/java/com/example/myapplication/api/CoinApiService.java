package com.example.myapplication.api;

import com.example.myapplication.Coin;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CoinApiService {
    @GET("coins/markets?vs_currency=try&ids=bitcoin,ethereum,ripple,cardano,solana,dogecoin,polkadot,litecoin,chainlink,binancecoin")
    Call<List<Coin>> getCoins();

}
