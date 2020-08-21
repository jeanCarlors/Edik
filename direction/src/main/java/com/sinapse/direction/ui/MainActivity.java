package com.sinapse.direction.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.sinapse.direction.R;
import com.sinapse.direction.databinding.ActivityMainBinding;
import com.sinapse.direction.ui.helper.FreeContentAdapter;
import com.sinapse.direction.ui.helper.TopicAdapter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView freeContentRecyclerView;
    private FreeContentAdapter freeContentAdapter;
    private List<String> freeContentList = new ArrayList<>();
    private ProgressDialog progressDialog;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    StorageReference rootContentTopic = storage.getReference().child("/Edik Content");
    private String rootUrl = "gs://edik-6adf5.appspot.com";

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); //R.layout.activity_main
        setSupportActionBar(binding.toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Chargement de la page ...");
        progressDialog.setMessage("Si le chargement de page tarde, vérifier votre connexion d'internet.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        openContentTopic("/Free Content");
        test();
    }

    public void onLoginBtnClicked(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void openContentTopic(final String topic){
        rootContentTopic.child(topic).listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getPrefixes()) {
                            freeContentList.add(item.getName());
                        }
                        freeContentRecyclerView = findViewById(R.id.free_content_recycler_view);
                        freeContentAdapter = new FreeContentAdapter(getApplicationContext(), (ArrayList<String>) freeContentList);
                        freeContentRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        progressDialog.dismiss();
                        freeContentRecyclerView.setAdapter(freeContentAdapter);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Root content subject", "Connexion failed"); // Uh-oh, an error occurred!
                    }
                });
    }

    private void test(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> profil = new HashMap<>();
        profil.put("nom", "College Mixte");
        profil.put("adresse", "Petionville");
        profil.put("telephone", 12341815);

        Map<String, Object> descriptif = new HashMap<>();
        Map<String, Object> classe = new HashMap<>();
        Map<String, Object> cours = new HashMap<>();
        List eleves = new ArrayList();
        eleves.add("Jean");
        eleves.add("Paul");
        classe.put("eleves", eleves);

        cours.put("date_debut", Timestamp.now());
        cours.put("date_fin", Timestamp.now());

        descriptif.put("descriptif","Ce champs contient toutes les informations pour l'annee academique 2019-2020.");
        db.collection("/00000002").document("profil").set(profil);
        db.collection("/00000002").document("ac_2019_2020").set(descriptif);
        db.collection("/00000002").document("ac_2019_2020").collection("classes")
                .document("7eme A").collection("cours").document("svt_01-08-2020_11-00")
                .set(cours);
    }

}
