package com.sinapse.direction.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sinapse.direction.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerContentFragment extends Fragment {
    private Button btn;
    public DrawerContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_drawer_content, container, false);

        final Intent intent = new Intent(getActivity(), EdikContentByClass.class);
        final Button btn = rootView.findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        return rootView;
    }
}
