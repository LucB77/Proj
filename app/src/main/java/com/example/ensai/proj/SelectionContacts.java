package com.example.ensai.proj;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.ensai.proj.Bdd.DAO;
import com.example.ensai.proj.Bdd.ListeContactsTel;
import com.example.ensai.proj.Elements.ContactTel;
import com.example.ensai.proj.Metier.MyAdapter;

import java.util.ArrayList;

public class SelectionContacts extends AppCompatActivity {

    Button bouton;
    ListView liste;
    Long idEvenement;
    MyAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_contacts);
        Intent intent = getIntent();
        idEvenement = intent.getLongExtra("idEvenement", -1);
        bouton = (Button) findViewById(R.id.boutonValid2);
        liste = (ListView) findViewById(R.id.listeContact);
        liste.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        liste.setClickable(true);
        adapter = new MyAdapter(this, R.layout.list_perso, ListeContactsTel.listeContactsTel);
        liste.setAdapter(adapter);


        for (int i =0; i<adapter.getCount();i++){
            for (int j = 0; j<ListeContactsTel.listeEvenementActif.size(); j++){
                if(adapter.getItem(i).getIdTel().equals( ListeContactsTel.listeEvenementActif.get(j).getIdTelephone())){
                  ListeContactsTel.listeContactsTel.get(j).setSelected(true);
                    adapter.notifyDataSetChanged();


                }
            }


        }

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(liste.isItemChecked(position)){
                    ListeContactsTel.listeContactsTel.get(position).setSelected(false);
                    adapter.notifyDataSetChanged();
                }else{
                    ListeContactsTel.listeContactsTel.get(position).setSelected(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }

    public void confirmerContacts(View v){
        ArrayList<ContactTel> newListe = new ArrayList<ContactTel>();
        DAO.renouvellerContacts(this,idEvenement);

        for (int i = 0; i<adapter.getCount();i++){
            if(liste.isItemChecked(i)){
                DAO.insertContact(this,idEvenement,adapter.getItem(i).getIdTel());
            }
        }
        ListeContactsTel.chargerContactEvenement(this,idEvenement);

        Intent intentSortie = new Intent(this, EvenementGestion.class);
        intentSortie.putExtra("idEvenement", DAO.compteur);
        startActivity(intentSortie);
        finish();
    }

}
