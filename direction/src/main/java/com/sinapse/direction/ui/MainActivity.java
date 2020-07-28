package com.sinapse.direction.ui;

import androidx.annotation.NonNull;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.sinapse.direction.R;
import com.sinapse.direction.ui.helper.FreeContentAdapter;
import com.sinapse.direction.ui.helper.TopicAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Chargement de la page ...");
        progressDialog.setMessage("Si le chargement de page tarde, vérifier votre connexion d'internet.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        openContentTopic("/Free Content/NS I");
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
}
