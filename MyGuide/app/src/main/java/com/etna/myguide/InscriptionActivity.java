package com.etna.myguide;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class InscriptionActivity extends AppCompatActivity {

    private static final String TAG ="IncriptionActivity";
    private TextView DisplayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;



    public static class apiWrapping
    {
        static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(main.getAPI_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        static PostAPI apiService = retrofit.create(PostAPI.class);

        public interface PostAPI {
            @POST("/users/{email}/{password}/{nom}/{prenom}/{date_naissance}")
            Call<User> postUser(
                    @Path("email") String email,
                    @Path("password") String password,
                    @Path("nom") String nom,
                    @Path("prenom") String prenom,
                    @Path("date_naissance") String date_naissance
            );
        }
    }

    public static class User{
        public final String status;
        public User(String _status){
            this.status = _status;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        final EditText nom = (EditText)findViewById(R.id.editText1);
        final EditText prenom = (EditText)findViewById(R.id.editText2);
        final TextView date = (TextView)findViewById(R.id.tvDate);
        final EditText pass = (EditText)findViewById(R.id.editText4);
        final EditText mail = (EditText)findViewById(R.id.editText5);

        findViewById(R.id.button4).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String p = pass.getText().toString();
            String m = mail.getText().toString();
            String no = nom.getText().toString();
            String pnom = prenom.getText().toString();
            String d = date.getText().toString();
            Call<User> call = apiWrapping.apiService.postUser(m, p, no, pnom, d);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();

                    if (user.status.equals("200"))
                    {
                        Toast.makeText(InscriptionActivity.this, "Inscription réussi!",
                                Toast.LENGTH_SHORT).show();
                        Intent coIntent = new Intent(InscriptionActivity.this, ConnexionActivity.class);
                        startActivity(coIntent);
                    }
                    else {
                        Toast.makeText(InscriptionActivity.this, "Inscription échoué!",
                                Toast.LENGTH_SHORT).show();
                        Toast.makeText(InscriptionActivity.this, "Identifiants manquant ou email déjà utilisé!",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(InscriptionActivity.this, "Inscription échoué!",
                            Toast.LENGTH_SHORT).show();
                    Toast.makeText(InscriptionActivity.this, "Identifiants manquant ou email déjà utilisé!",
                            Toast.LENGTH_LONG).show();
                }
            });
            }
        });

        DisplayDate = (TextView) findViewById(R.id.tvDate);

        DisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int day =  cal.get(Calendar.DAY_OF_MONTH);
                int month =  cal.get(Calendar.MONTH);
                int year =  cal.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(InscriptionActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, DateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        DateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: YY/MM/DD: " + year + "/" + month + "/" + day);

                String date =  month + "/" + day + "/" + year;
                DisplayDate.setText(date);
            }
        };
    }
}
