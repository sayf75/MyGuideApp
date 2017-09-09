package com.etna.myguide;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    public ProfilFragment() {
        // Required empty public constructor
    }

    public static class apiWrapping
    {
        static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(main.getAPI_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        static GetAPI apiService = retrofit.create(GetAPI.class);
        public interface GetAPI {
            @GET("/users/{id}")
            Call<userInfo> getUserData(
                    @Path("id") String id
            );
        }
    }

    public static class userInfo{
        public final String type;
        public final String nom;
        public final String prenom;
        public final String pays;
        public final String ville;
        public final String langue;
        public final String description;
        public final String photo;
        public final String date_naissance;
        public final String bourse;

        public userInfo(String _type, String _nom, String _prenom, String _langue, String _description,
                        String _photo, String _date_naissance, String _bourse, String _pays, String _ville) {
            this.type = _type;
            this.nom = _nom;
            this.prenom = _prenom;
            this.langue = _langue;
            this.description = _description;
            this.photo = _photo;
            this.pays = _pays;
            this.ville = _ville;
            this.date_naissance = _date_naissance;
            this.bourse = _bourse;
        }
    }

    public void getProfileData(View view, String id)
    {
        final TextView type = (TextView)view.findViewById(R.id.type);
        final TextView nom = (TextView)view.findViewById(R.id.nom);
        final TextView prenom = (TextView)view.findViewById(R.id.prenom);
        final TextView pays = (TextView)view.findViewById(R.id.pays);
        final TextView ville = (TextView)view.findViewById(R.id.ville);
        final TextView langue = (TextView)view.findViewById(R.id.langue);
        final TextView age = (TextView)view.findViewById(R.id.age);
        final TextView bourse = (TextView)view.findViewById(R.id.bourse);
        final ImageView photo = (ImageView) view.findViewById(R.id.photo);
        final TextView description = (TextView)view.findViewById(R.id.description);

        Call<userInfo> call = apiWrapping.apiService.getUserData(id);
        call.enqueue(new Callback<userInfo>() {
            @Override
            public void onResponse(Call<userInfo> call, Response<userInfo> response) {
                if (response.isSuccessful()) {
                    userInfo user = response.body();
                    langue.setText(user.langue);
                    type.setText(user.type);
                    nom.setText(user.nom);
                    prenom.setText(user.prenom);
                    age.setText(user.date_naissance);
                    pays.setText(user.pays);
                    ville.setText(user.ville);
                    bourse.setText(user.bourse);
                    description.setText(user.description);
                    // photo imageview
                }
            }
            @Override
            public void onFailure(Call<userInfo> call, Throwable t) {}
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profil, container, false);

        final String id = this.getArguments().getString("id");
        getProfileData(view, id);
        Button editButton = (Button) view.findViewById(R.id.edit);

        String[] locales = Locale.getISOCountries();

        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            Log.d("", "Country Name = " + obj.getDisplayCountry());

        }

        return view;
    }

}
