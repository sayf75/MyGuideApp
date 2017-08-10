package com.etna.myguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class InscriptionActivity extends AppCompatActivity {

    public static class apiWrapping
    {
        static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(main.getAPI_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        static PostAPI apiService = retrofit.create(PostAPI.class);

        public interface PostAPI {
            @POST("/users/{username}/{password}/{email}")
            Call<User> postUser(
                    @Path("username") String username,
                    @Path("password") String password,
                    @Path("email") String email
            );
        }
    }

    public static class User{
        String status;
        public User(String _status){
            this.status = _status;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        final EditText user = (EditText)findViewById(R.id.editText3);
        final EditText pass = (EditText)findViewById(R.id.editText4);
        final EditText mail = (EditText)findViewById(R.id.editText5);

        findViewById(R.id.button4).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = user.getText().toString();
                String p = pass.getText().toString();
                String m = mail.getText().toString();
                Call<User> call = apiWrapping.apiService.postUser(u, p, m);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body().status.equals("200")) {
                            Toast.makeText(InscriptionActivity.this, "Inscription réussie!",
                                    Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(InscriptionActivity.this, ConnexionActivity.class);
                            startActivity(homeIntent);
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(InscriptionActivity.this, "Echec de l'inscription! Username ou Email déjà utlisé",
                                    Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
