package com.etna.myguide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class SelectedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = getIntent().getStringExtra("nom");
        String fname = getIntent().getStringExtra("prenom");
        String town = getIntent().getStringExtra("ville");
        String description8 = getIntent().getStringExtra("description");
        String prix_prestation = getIntent().getStringExtra("prix_prestation");
        Toast.makeText(getApplicationContext() , name, Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_selected);
        TextView nom = (TextView) findViewById(R.id.nom);
        TextView prenom = (TextView) findViewById(R.id.prenom);
        TextView ville = (TextView) findViewById(R.id.ville);
        TextView description = (TextView) findViewById(R.id.description);
        TextView presta = (TextView) findViewById(R.id.prix_prestation);
        nom.setText(name);
        prenom.setText(fname);
        ville.setText(town);
        description.setText(description8);
        presta.setText(prix_prestation);
    }
}
