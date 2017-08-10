package com.etna.myguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.lang.String;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static com.etna.myguide.R.id.button;
import static com.etna.myguide.R.id.search_button;

public class ConnexionActivity extends AppCompatActivity {

    public static class apiWrapping
    {
        static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(main.getAPI_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        static GetAPI apiService = retrofit.create(GetAPI.class);
        public interface GetAPI {
            @GET("/users/{username}/{password}")
            Call<Users> getUsers(
                    @Path("username") String username,
                    @Path("password") String password
            );
        }
    }

    public static class Users{
        public final String username;
        public final String id;
        public final String password;
        public final String type;

        public Users(String _username, String _password, String _id, String _type) {
            this.username = _username;
            this.password = _password;
            this.id = _id;
            this.type = _type;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        final EditText username = (EditText)findViewById(R.id.editText);
        final EditText password = (EditText)findViewById(R.id.editText2);
        Button clickButton = (Button) findViewById(R.id.button);
        Button inscriptionButton = (Button) findViewById(R.id.button3);

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent homeIntent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
                startActivity(homeIntent);
            }
        });
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String u = username.getText().toString();
                final String p = password.getText().toString();
                Call<Users> call = apiWrapping.apiService.getUsers(u, p);
                call.enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        if (response.isSuccessful()) {
                            Users user = response.body();
                            Intent I = new Intent(ConnexionActivity.this, HomeActivity.class);
                            I.putExtra("id", user.id);
                            I.putExtra("type", user.type);
                            startActivity(I);
                        }
                    }
                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {}
                });
            }
        });

    }
}
