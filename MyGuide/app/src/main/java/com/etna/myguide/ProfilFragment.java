package com.etna.myguide;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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

    private static String nom;
    private static String prenom;
    private static String email;
    private static String pays;
    private static String ville;
    private static String adresse;
    private static String postal;

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

            @PUT("/users/{id}/{nom}/{prenom}/{email}/{pays}/{ville}/{adresse}/{postal}")
            Call<userInfo> setUserData(
                    @Path("id") String id,
                    @Path("nom") String nom,
                    @Path("prenom") String prenom,
                    @Path("email") String email,
                    @Path("pays") String pays,
                    @Path("ville") String ville,
                    @Path("adresse") String adresse,
                    @Path("postal") String postal
            );
        }
    }

    public static class userInfo{
        public final String type;
        public final String nom;
        public final String prenom;
        public final String username;
        public final String password;
        public final String email;
        public final String pays;
        public final String ville;
        public final String adresse;
        public final String postal;

        public userInfo(String _type, String _nom, String _prenom, String _username, String _password, String _email,
                        String _pays, String _ville, String _adresse, String _postal) {
            this.type = _type;
            this.nom = _nom;
            this.prenom = _prenom;
            this.username = _username;
            this.password = _password;
            this.email = _email;
            this.pays = _pays;
            this.ville = _ville;
            this.adresse = _adresse;
            this.postal = _postal;
        }
    }

    public void getProfileData(View view, String id)
    {
        final TextView username = (TextView)view.findViewById(R.id.username);
        final TextView type = (TextView)view.findViewById(R.id.type);
        final EditText nom = (EditText)view.findViewById(R.id.nom);
        final EditText prenom = (EditText)view.findViewById(R.id.prenom);
        final EditText email = (EditText)view.findViewById(R.id.email);
        final EditText pays = (EditText)view.findViewById(R.id.pays);
        final EditText ville = (EditText)view.findViewById(R.id.ville);
        final EditText adresse = (EditText)view.findViewById(R.id.adresse);
        final EditText postal = (EditText)view.findViewById(R.id.postal);

        Call<userInfo> call = apiWrapping.apiService.getUserData(id);
        call.enqueue(new Callback<userInfo>() {
            @Override
            public void onResponse(Call<userInfo> call, Response<userInfo> response) {
                if (response.isSuccessful()) {
                    userInfo user = response.body();
                    username.setText(user.username);
                    type.setText(user.type);
                    nom.setText(user.nom, TextView.BufferType.EDITABLE);
                    prenom.setText(user.prenom, TextView.BufferType.EDITABLE);
                    email.setText(user.email, TextView.BufferType.EDITABLE);
                    pays.setText(user.pays, TextView.BufferType.EDITABLE);
                    ville.setText(user.ville, TextView.BufferType.EDITABLE);
                    adresse.setText(user.adresse, TextView.BufferType.EDITABLE);
                    postal.setText(user.postal, TextView.BufferType.EDITABLE);
                    ProfilFragment.nom = user.nom;
                    ProfilFragment.prenom = user.prenom;
                    ProfilFragment.email = user.email;
                    ProfilFragment.pays = user.pays;
                    ProfilFragment.ville = user.ville;
                    ProfilFragment.adresse = user.adresse;
                    ProfilFragment.postal = user.postal;
                }
            }
            @Override
            public void onFailure(Call<userInfo> call, Throwable t) {}
        });
    }

    public void setProfileData(View view, String id)
    {
        final EditText nom = (EditText)view.findViewById(R.id.nom);
        final EditText prenom = (EditText)view.findViewById(R.id.prenom);
        final EditText email = (EditText)view.findViewById(R.id.email);
        final EditText pays = (EditText)view.findViewById(R.id.pays);
        final EditText ville = (EditText)view.findViewById(R.id.ville);
        final EditText adresse = (EditText)view.findViewById(R.id.adresse);
        final EditText postal = (EditText)view.findViewById(R.id.postal);

        /*if ("".equals(nom.getText().toString()) || "".equals(prenom.getText().toString())
                || "".equals(password.getText().toString()) || "".equals(email.getText().toString())
                || )
            Toast.makeText(this.getContext(), "Manque un parametre", Toast.LENGTH_SHORT).show();
        else {*/
            Call<userInfo> call = apiWrapping.apiService.setUserData(id,
                    "".equals(nom.getText().toString()) ? ProfilFragment.nom : nom.getText().toString(),
                    "".equals(prenom.getText().toString()) ? ProfilFragment.prenom : prenom.getText().toString(),
                    "".equals(email.getText().toString()) ? ProfilFragment.email : email.getText().toString(),
                    "".equals(pays.getText().toString()) ? ProfilFragment.pays : pays.getText().toString(),
                    "".equals(ville.getText().toString()) ? ProfilFragment.ville : ville.getText().toString(),
                    "".equals(adresse.getText().toString()) ? ProfilFragment.adresse : adresse.getText().toString(),
                    "".equals(postal.getText().toString()) ? ProfilFragment.postal : postal.getText().toString());
            call.enqueue(new Callback<userInfo>() {
                @Override
                public void onResponse(Call<userInfo> call, Response<userInfo> response) {}
                @Override
                public void onFailure(Call<userInfo> call, Throwable t) {}
            });
        //}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profil, container, false);

        final String id = this.getArguments().getString("id");
        getProfileData(view, id);
        Button modButton = (Button) view.findViewById(R.id.modify);
        modButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfileData(view ,id);
            }
        });
        return view;
    }

}
