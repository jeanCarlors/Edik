package com.sinapse.professeur.course;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sinapse.libmodule.beans.Course;
import com.sinapse.libmodule.beans.Horaire;
import com.sinapse.professeur.R;
import com.sinapse.professeur.databinding.FragmentCourseDetailsBinding;
import com.sinapse.professeur.viewholders.ClassViewHolder;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseDetailsFragment extends Fragment {

    FragmentCourseDetailsBinding binding;
    private static final String ARG_PARAM1 = "COURSE_PATH";
    private String course_path;
    FirestoreRecyclerAdapter<Horaire, ClassViewHolder> adapter;

    boolean coursDetails = false;
    boolean horaireList = false;

    public CourseDetailsFragment() {
        // Required empty public constructor
    }


    public static CourseDetailsFragment newInstance(String param1) {
        CourseDetailsFragment fragment = new CourseDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            course_path = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCourseDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FirebaseFirestore.getInstance()
                .document(course_path)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        binding.className.setText(documentSnapshot.getString("classe"));
                        binding.coursName.setText(documentSnapshot.getString("name"));
                        coursDetails = true;
                        updateProgressBar();
                    }
                });

        Query query = FirebaseFirestore.getInstance()
                .document(course_path)
                .collection("horaire");

        FirestoreRecyclerOptions<Horaire> horaires = new FirestoreRecyclerOptions.Builder<Horaire>()
                .setQuery(query, new SnapshotParser<Horaire>() {
                    @NonNull
                    @Override
                    public Horaire parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Horaire horaire = new Horaire();
                        horaire.setDocPath(snapshot.getReference().getPath());
                        horaire.setUid(snapshot.getId());
                        horaire.setJour(snapshot.getString("jour"));
                        horaire.setHeure(snapshot.getString("heure"));
                        horaire.setDocPath(snapshot.getString("duree"));
                        return horaire;
                    }
                }).build();

        adapter = new FirestoreRecyclerAdapter<Horaire, ClassViewHolder>(horaires) {
            @Override
            protected void onBindViewHolder(@NonNull ClassViewHolder holder, int position, @NonNull Horaire model) {
                holder.title.setText(model.getJour());
                holder.subTitle.setText(String.format(
                        Locale.FRANCE,
                        "Heure: %s | Durée: %s",
                        model.getHeure(), model.getDuree()
                ));
                horaireList = true;
                updateProgressBar();
            }

            @NonNull
            @Override
            public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_2, parent, false);
                return new ClassViewHolder(view);
            }
        };

        binding.recycleViewHoraires.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleViewHoraires.setAdapter(adapter);
    }

    private void updateProgressBar(){
        binding.progressCircular.setVisibility(
                (coursDetails && horaireList)
                ? View.GONE : View.VISIBLE
        );
    }



    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}