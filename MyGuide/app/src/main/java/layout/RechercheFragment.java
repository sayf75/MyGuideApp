package layout;


import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.etna.myguide.ConnexionActivity;
import com.etna.myguide.R;
import com.etna.myguide.main;
import com.huxq17.swipecardsview.SwipeCardsView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class RechercheFragment extends Fragment {

    private static String nom;
    private static String prenom;
    private static String email;
    private static String pays;
    private static String ville;
    private static String adresse;
    private static String postal;

    private ListView v;
    private ArrayAdapter<String> adapter;

    public RechercheFragment() {

    }

    public static class apiWrapping
    {
        static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(main.getAPI_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        static GetAPI apiService = retrofit.create(GetAPI.class);
        public interface GetAPI {
                @GET("/guides")
                Call <List<Users>> getGuide ();

        }
    }

    public static class Users{
        public final String id;
        public final String nom;
        public final String prenom;
        public final String type;
        public final String pays;
        public final String ville;
        public final String adresse;
        public final String postal;
        public final int disponibilite;
        public final String bourse;

        public Users(String _nom, String _prenom, String _id, String _type, String _pays, String _ville, String _adresse, String _postal, int _disponibilite, String _bourse) {
            this.id = _id;
            this.nom = _nom;
            this.prenom = _prenom;
            this.type = _type;
            this.pays = _pays;
            this.ville = _ville;
            this.adresse = _adresse;
            this.postal = _postal;
            this.disponibilite = _disponibilite;
            this.bourse = _bourse;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        final View view = inflater.inflate(R.layout.fragment_recherche, container, false);
        v = (ListView) view.findViewById(R.id.list);
        Call<List<Users>> call = apiWrapping.apiService.getGuide();
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                List<String> items = new ArrayList<String>();
                if (response.isSuccessful()) {
                    List<Users> user = response.body();

                    if (user.size() > 0) {
                       for (int i = 0; i != user.size(); i++) {
                           if (user.get(i).disponibilite == 1) {
                               items.add(user.get(i).prenom + " " + user.get(i).nom);
                           }

                       }
                    }
                    adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1, items);
                    v.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable throwable) {

            }
        });
        return view;
    }



}
