package com.etna.myguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SelectedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = getIntent().getStringExtra("nom");
        String fname = getIntent().getStringExtra("prenom");
        String town = getIntent().getStringExtra("ville");
        String adress = getIntent().getStringExtra("adresse");
        String post = getIntent().getStringExtra("postal");
        setContentView(R.layout.activity_selected);
        TextView nom = (TextView) findViewById(R.id.nom);
        TextView prenom = (TextView) findViewById(R.id.prenom);
        TextView ville = (TextView) findViewById(R.id.ville);
        TextView adresse = (TextView) findViewById(R.id.adresse);
        TextView postal = (TextView) findViewById(R.id.postal);
        nom.setText(name);
        prenom.setText(fname);
        ville.setText(town);
        adresse.setText(adress);
        postal.setText(post);
    }
}
