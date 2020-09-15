package com.sinapse.eleve.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.sinapse.eleve.R;
import com.sinapse.eleve.databinding.ActivityMainBinding;
import com.sinapse.eleve.ui.Home;
import com.sinapse.eleve.ui.helper.FreeContentAdapter;
import com.sinapse.libmodule.beans.Session;
import com.sinapse.libmodule.beans.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView freeContentRecyclerView;
    private FreeContentAdapter freeContentAdapter;
    private List<String> freeContentList = new ArrayList<>();
    private ProgressDialog progressDialog;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    StorageReference rootContentTopic = storage.getReference().child("/Edik Content");
    private String rootUrl = "gs://edik-6adf5.appspot.com";

    private static int LOGIN_ACTIVITY = 123;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); //R.layout.activity_main
        setSupportActionBar(binding.toolbar);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            binding.fab.setText("Entrer");
            binding.fab.setVisibility(View.GONE);

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            Session.currentUser = new User();
                            Session.currentUser.setName(documentSnapshot.getString("name"));
                            Session.currentUser.setPhoto(documentSnapshot.getString("photo"));
                            Session.currentUser.setStatus(documentSnapshot.getString("status"));
                            Session.currentUser.setType(documentSnapshot.getString("type"));
                            Session.currentUser.setSchool(documentSnapshot.getString("school"));
                            Session.currentUser.setUid(user.getUid());

                            try {
                                Session.currentUser.setVerified(documentSnapshot.getBoolean("verified"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            binding.fab.setVisibility(View.VISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseAuth.getInstance().signOut();
                            binding.fab.setText("Se connecter");
                            binding.fab.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    });
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Chargement de la page ...");
        progressDialog.setMessage("Si le chargement de page tarde, vérifier votre connexion d'internet.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        openContentTopic("/Free Content");
    }



    public void onLoginBtnClicked(View view) {
        if(Session.currentUser == null) {
            Intent intent = new Intent(this, Login.class);
            startActivityForResult(intent, LOGIN_ACTIVITY);
        } else {
            goToDrawer();
        }
    }

    private void goToDrawer() {
        Log.d("LOGIN", Session.currentUser.getUid());
        startActivity(new Intent(this, DrawerHome.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_ACTIVITY && resultCode == RESULT_OK) {
            if(Session.currentUser != null)
                binding.fab.setText("Entrer");
            goToDrawer();
        }
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
}
