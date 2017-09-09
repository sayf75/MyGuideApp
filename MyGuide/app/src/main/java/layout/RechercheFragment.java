package layout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.etna.myguide.R;
import com.etna.myguide.SelectedActivity;
import com.etna.myguide.main;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * A simple {@link Fragment} subclass.
 */
public class RechercheFragment extends Fragment {

    private SwipeRefreshLayout swipeContainer;
    private RadioButton listviewb;
    private RadioButton mapbutton;
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
            Call<List<Users>> getGuide ();
        }
    }

    public static class Users{
        public final String id;
        public final String nom;
        public final String prenom;
        public final String type;
        public final String pays;
        public final String ville;
        public final String description;
        public final String prix_prestation;
        public final String photo;
        public final int disponibilite;

        public Users(String _nom, String _prenom, String _id, String _type, String _pays,
                     String _ville, String _description, String _prix_prestation, int _disponibilite, String _photo) {
            this.id = _id;
            this.nom = _nom;
            this.prenom = _prenom;
            this.type = _type;
            this.pays = _pays;
            this.ville = _ville;
            this.description = _description;
            this.prix_prestation = _prix_prestation;
            this.disponibilite = _disponibilite;
            this.photo = _photo;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View tamere = inflater;
        final View view = inflater.inflate(R.layout.fragment_recherche, container, false);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        v = (ListView) view.findViewById(R.id.list);
        listviewb = (RadioButton) view.findViewById(R.id.listbutton);
        mapbutton = (RadioButton) view.findViewById(R.id.mapbutton);
        Call<List<Users>> call = apiWrapping.apiService.getGuide();
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, final Response<List<Users>> response) {
                List<String> items = new ArrayList<String>();
                if (response.isSuccessful()) {
                        final List<Users> user = response.body();
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
                    v.setClickable(true);
                    v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if  (position == 0) {
                                Toast.makeText(getActivity(), "Selection guide", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), SelectedActivity.class);
                                intent.putExtra("nom", user.get(position).nom);
                                intent.putExtra("prenom", user.get(position).prenom);
                                intent.putExtra("description", user.get(position).description);
                                intent.putExtra("ville", user.get(position).ville);
                                intent.putExtra("prix_prestation", user.get(position).prix_prestation);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getActivity(), "Selection guide", Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(getContext(), SelectedActivity.class);
                                intent2.putExtra("nom", user.get(position).nom);
                                intent2.putExtra("prenom", user.get(position).prenom);
                                intent2.putExtra("description", user.get(position).description);
                                intent2.putExtra("ville", user.get(position).ville);
                                intent2.putExtra("prix_prestation", user.get(position).prix_prestation);
                                startActivity(intent2);
                            }
                        }
                    });
                    if (listviewb.isChecked()) {
                        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
                    }
                    else if (mapbutton.isChecked()){
                        Toast.makeText(getContext(), "MapButton", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable throwable) {

            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }


            void refreshItems() {
                Call<List<Users>> call = apiWrapping.apiService.getGuide();
                call.enqueue(new Callback<List<Users>>() {
                    @Override
                    public void onResponse(Call<List<Users>> call, final Response<List<Users>> response) {
                        List<String> items = new ArrayList<String>();
                        if (response.isSuccessful()) {
                            final List<Users> user = response.body();
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
                            v.setClickable(true);
                            v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 0) {
                                        Toast.makeText(getActivity(), "Selection guide", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(), SelectedActivity.class);
                                        intent.putExtra("nom", user.get(position).nom);
                                        intent.putExtra("prenom", user.get(position).prenom);
                                        intent.putExtra("description", user.get(position).description);
                                        intent.putExtra("ville", user.get(position).ville);
                                        intent.putExtra("prix_prestation", user.get(position).prix_prestation);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getActivity(), "Selection guide", Toast.LENGTH_SHORT).show();
                                        Intent intent2 = new Intent(getContext(), SelectedActivity.class);
                                        intent2.putExtra("nom", user.get(position).nom);
                                        intent2.putExtra("prenom", user.get(position).prenom);
                                        intent2.putExtra("description", user.get(position).description);
                                        intent2.putExtra("ville", user.get(position).ville);
                                        intent2.putExtra("prix_prestation", user.get(position).prix_prestation);
                                        startActivity(intent2);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Users>> call, Throwable throwable) {

                    }
                });
                onItemsLoadComplete();
            }
                void onItemsLoadComplete() {
                // Update the adapter and notify data set changed
                // ...

                // Stop refresh animation
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

}
