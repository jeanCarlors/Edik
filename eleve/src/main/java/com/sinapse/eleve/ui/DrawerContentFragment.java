package com.sinapse.eleve.ui;

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
import com.sinapse.eleve.R;
import com.sinapse.eleve.ui.helper.FreeContentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DrawerContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawerContentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btn;
    private RecyclerView freeContentRecyclerView;
    private FreeContentAdapter freeContentAdapter;
    private List<String> freeContentList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private StorageReference rootContentTopic = storage.getReference().child("/Edik Content");
    private String rootUrl = "gs://edik-6adf5.appspot.com";

    public DrawerContentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrawerContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrawerContentFragment newInstance(String param1, String param2) {
        DrawerContentFragment fragment = new DrawerContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drawer_content, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Chargement de la page ...");
        progressDialog.setMessage("Si le chargement de page tarde, vérifier votre connexion d'internet.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        openContentTopic("/Free Content", rootView);

        final Intent intent = new Intent(getActivity(), ContentList.class);
        intent.putExtra("grade", getActivity().getIntent().getExtras().getString("grade"));
        final Button btn = rootView.findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        return rootView;
    }


    public void openContentTopic(final String topic, final View view){
        final String grade = getActivity().getIntent().getExtras().getString("grade").substring(1) + " ";
        rootContentTopic.child(topic).listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getPrefixes()) {
                            Log.d("grade", item.getName()+","+grade);
                            if(item.getName().contains(grade))
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
