package com.etna.myguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.lang.String;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.Arrays;
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
            @GET("/users/{email}/{password}")
            Call<Users> getUsers(
                    @Path("email") String email,
                    @Path("password") String password
            );
        }
    }

    public static class Users{
        public final String id;
        public final String type;

        public Users(String _id, String _type) {
            this.id = _id;
            this.type = _type;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        final EditText email = (EditText)findViewById(R.id.editText);
        final EditText password = (EditText)findViewById(R.id.editText2);
        final Button clickButton = (Button) findViewById(R.id.button);
        final Button signupButton = (Button) findViewById(R.id.signUp);

        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m = email.getText().toString();
                String p = password.getText().toString();
                Call<Users> call = apiWrapping.apiService.getUsers(m, p);
                call.enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        if (response.isSuccessful()) {
                            Users user = response.body();
                            Toast.makeText(ConnexionActivity.this, "Connexion réussie!",
                                    Toast.LENGTH_SHORT).show();
                            Intent I = new Intent(ConnexionActivity.this, HomeActivity.class);
                            I.putExtra("id", user.id);
                            I.putExtra("type", user.type);
                            startActivity(I);
                        }
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {
                        Toast.makeText(ConnexionActivity.this, "Connexion échoué!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent J = new Intent(ConnexionActivity.this, InscriptionActivity.class);
                startActivity(J);
            }
        });

    }
}
