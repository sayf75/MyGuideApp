package com.etna.myguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class PaymentActivity extends AppCompatActivity {

    private String bourse_client = "0";
    private String bourse_guide = "0";

    public static class apiWrapping
    {
        static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(main.getAPI_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        static apiWrapping.API apiService = retrofit.create(apiWrapping.API.class);

        public interface API {
            @GET("/users/{id}")
            Call<Payment> getBourse(
                    @Path("id") String id
            );

            @PUT("/bourse/{id}/{amount}")
            Call<Payment> putAmount(
                    @Path("id") String id,
                    @Path("amount") int amount
            );
        }
    }

    public static class Payment{
        String bourse;
        String status;
        public Payment(String _bourse, String _status)
        {
            this.bourse = _bourse;
            this.status = _status;
        }
    }

    public static boolean CheckLuhn(String ccNumber)
    {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--)
        {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate)
            {
                n *= 2;
                if (n > 9)
                {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        final String id_client = "5";
        final String id_guide = "4";
        final int amount = 20;

        final EditText nbCarte   = (EditText)findViewById(R.id.nbCarte);
        final Button confirm = (Button)findViewById(R.id.confirm);
        final EditText date   = (EditText)findViewById(R.id.date);
        final EditText crypto   = (EditText)findViewById(R.id.crypto);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nb = nbCarte.getText().toString();
                final String d = date.getText().toString();
                final String c = crypto.getText().toString();

                if ("".equals(nb) || "".equals(d) || "".equals(c) ||
                        !"16".equals(nb.length()+"") || !"4".equals(d.length()+"") || !"3".equals(c.length()+"")) {
                    Toast.makeText(PaymentActivity.this, "Coordonnée(s) manquant(s)!",
                            Toast.LENGTH_SHORT).show();
                } else if ((CheckLuhn(nb)+"").equals("true")) {
                    Call<Payment> callClient = apiWrapping.apiService.getBourse(id_client);
                    callClient.enqueue(new Callback<Payment>() {
                        @Override
                        public void onResponse(Call<Payment> call, Response<Payment> response) {
                            if (response.isSuccessful()) {
                                Payment self = response.body();
                                bourse_client = self.bourse;

                                Call<Payment> callGuide = apiWrapping.apiService.getBourse(id_guide);
                                callGuide.enqueue(new Callback<Payment>() {
                                    @Override
                                    public void onResponse(Call<Payment> call, Response<Payment> response) {
                                        if (response.isSuccessful()) {
                                            Payment self = response.body();
                                            bourse_guide = self.bourse;
                                            int amountClient = Integer.parseInt(bourse_client) - amount;
                                            int amountGuide = Integer.parseInt(bourse_guide) + amount;

                                            Call<Payment> callAmountClient = apiWrapping.apiService.putAmount(id_client, amountClient);
                                            Call<Payment> callAmountGuide = apiWrapping.apiService.putAmount(id_guide, amountGuide);
                                            callAmountClient.enqueue(new Callback<Payment>() {
                                                @Override
                                                public void onResponse(Call<Payment> call, Response<Payment> response) {}
                                                @Override
                                                public void onFailure(Call<Payment> call, Throwable throwable) {}
                                            });
                                            callAmountGuide.enqueue(new Callback<Payment>() {
                                                @Override
                                                public void onResponse(Call<Payment> call, Response<Payment> response) {
                                                    Toast.makeText(PaymentActivity.this, "Paiement accepté!",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                @Override
                                                public void onFailure(Call<Payment> call, Throwable throwable) {}
                                            });
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Payment> call, Throwable t) {
                                        Log.d("", t.toString());
                                    }
                                });
                            }
                        }
                        @Override
                        public void onFailure(Call<Payment> call, Throwable t) {
                            Log.d("", t.toString());
                        }
                    });
                }
            }
        });
    }
}
