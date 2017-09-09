package com.etna.myguide;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import layout.ParametreFragment;
import layout.RechercheFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout DrawerL;
    private ActionBarDrawerToggle ABDT;

    FragmentTransaction FT;
    NavigationView Naviews;

    public static class apiWrapping
    {
        static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(main.getAPI_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        static GetAPI apiService = retrofit.create(GetAPI.class);
        public interface GetAPI {
            @PUT("/disponibilite/{id}/{disponibilite}")
            Call<Dispo> putDispo(
                    @Path("id") String id,
                    @Path("disponibilite") int disponibilite
            );

            @GET("/users/{id}")
            Call<Dispo> getDispo(
                    @Path("id") String id
            );
        }
    }

    public static class Dispo{
        String disponibilite;
        public Dispo(String _disponibilite) {
            this.disponibilite = _disponibilite;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final String id = getIntent().getStringExtra("id");
        final String type = getIntent().getStringExtra("type");
        DrawerL = (DrawerLayout) findViewById(R.id.drawerLayout);
        ABDT = new ActionBarDrawerToggle(this, DrawerL, R.string.open, R.string.close);
        DrawerL.addDrawerListener(ABDT);
        ABDT.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FT = getSupportFragmentManager().beginTransaction();
        FT.add(R.id.main_container, new RechercheFragment());
        FT.commit();

        final Switch dispo = (Switch)findViewById(R.id.dispo);

        if (type.equals("client")) {
            getSupportActionBar().setTitle("Recherche");
            dispo.setVisibility(View.GONE);
        }
        else {
            getSupportActionBar().setTitle("Disponibilité");
            Call<Dispo> call = apiWrapping.apiService.getDispo(id);
            call.enqueue(new Callback<Dispo>() {
                @Override
                public void onResponse(Call<Dispo> call, Response<Dispo> response) {
                    if (response.isSuccessful()) {
                        Dispo d = response.body();
                        if (d.disponibilite.equals("1"))
                            dispo.setChecked(true);
                        else
                            dispo.setChecked(false);
                    }
                }
                @Override
                public void onFailure(Call<Dispo> call, Throwable throwable) {}
            });
        }
        Naviews = (NavigationView)findViewById(R.id.navigation_view);
        Naviews.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_account:
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id );
                        ProfilFragment fragInfo = new ProfilFragment();
                        fragInfo.setArguments(bundle);
                        FT = getSupportFragmentManager().beginTransaction();
                        FT.replace(R.id.main_container, fragInfo);
                        FT.commit();
                        getSupportActionBar().setTitle("Mon compte");
                        item.setChecked(true);
                        DrawerL.closeDrawers();
                        break;
                    case R.id.nav_settings:
                        FT = getSupportFragmentManager().beginTransaction();
                        FT.replace(R.id.main_container,new ParametreFragment());
                        FT.commit();
                        getSupportActionBar().setTitle("Paramètres");
                        item.setChecked(true);
                        DrawerL.closeDrawers();
                        break;
                    case R.id.nav_recherche:
                        FT = getSupportFragmentManager().beginTransaction();
                        FT.replace(R.id.main_container,new RechercheFragment());
                        FT.commit();
                        getSupportActionBar().setTitle("Guide");
                        item.setChecked(true);
                        DrawerL.closeDrawers();
                        break;
                }

                return true;
            }
        });

        dispo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    Call<Dispo> call = apiWrapping.apiService.putDispo(id, 1);
                    call.enqueue(new Callback<Dispo>() {
                        @Override
                        public void onResponse(Call<Dispo> call, Response<Dispo> response) {
                            if (response.isSuccessful())
                            Toast.makeText(HomeActivity.this, "Vous êtes disponible pour guider.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(Call<Dispo> call, Throwable throwable) {}
                    });
                } else {
                    Call<Dispo> call = apiWrapping.apiService.putDispo(id, 0);
                    call.enqueue(new Callback<Dispo>() {
                        @Override
                        public void onResponse(Call<Dispo> call, Response<Dispo> response) {
                            if (response.isSuccessful())
                                Toast.makeText(HomeActivity.this, "Vous n'êtes plus disponible pour guider.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(Call<Dispo> call, Throwable throwable) {}
                    });
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ABDT.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
