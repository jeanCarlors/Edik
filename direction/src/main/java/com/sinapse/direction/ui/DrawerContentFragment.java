package com.sinapse.direction.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.sinapse.direction.R;
import com.sinapse.direction.ui.helper.FreeContentAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerContentFragment extends Fragment {
    private Button btn;
    private RecyclerView freeContentRecyclerView;
    private FreeContentAdapter freeContentAdapter;
    private List<String> freeContentList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    StorageReference rootContentTopic = storage.getReference().child("/Edik Content");
    private String rootUrl = "gs://edik-6adf5.appspot.com";
    public DrawerContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_drawer_content, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Chargement de la page ...");
        progressDialog.setMessage("Si le chargement de page tarde, vérifier votre connexion d'internet.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        openContentTopic("/Free Content/NS I", rootView);

        final Intent intent = new Intent(getActivity(), EdikContentByClass.class);
        final Button btn = rootView.findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void openContentTopic(final String topic, final View view){
        rootContentTopic.child(topic).listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getPrefixes()) {
                            freeContentList.add(item.getName());
                        }
                        freeContentRecyclerView = view.findViewById(R.id.free_content_recycler_view);
                        freeContentAdapter = new FreeContentAdapter(view.getContext(), (ArrayList<String>) freeContentList);
                        freeContentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
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
