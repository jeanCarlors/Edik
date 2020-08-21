package com.sinapse.direction.ui.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.sinapse.direction.R;
import com.sinapse.direction.ui.Classroom;
import com.sinapse.direction.ui.DrawerClassFragment;

import java.util.ArrayList;
import java.util.List;

public class GradeListForDetailsAdapter extends RecyclerView.Adapter<GradeListForDetailsAdapter.GradeListViewHolder>   {
    private List<String> gradeList;
    private LayoutInflater gradeInflater;

    public GradeListForDetailsAdapter(Context context, ArrayList<String> subjectList){
        gradeInflater = LayoutInflater.from(context);
        this.gradeList = subjectList;
    }

    @NonNull
    @Override
    public GradeListForDetailsAdapter.GradeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View gItemView = gradeInflater.inflate(R.layout.classroom_choice_layout, parent, false);
        return new GradeListForDetailsAdapter.GradeListViewHolder(gItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeListForDetailsAdapter.GradeListViewHolder holder, int position) {
        String sCurrent = gradeList.get(position);
        holder.gradeNameTextItemView.setText(sCurrent);
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    public class GradeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView gradeNameTextItemView;
        public final Button gradeDetailsBtn;
        final GradeListForDetailsAdapter gradeListForDetailsAdapter;

        public GradeListViewHolder(@NonNull View itemView, GradeListForDetailsAdapter gradeListForDetailsAdapter) {
            super(itemView);
            gradeNameTextItemView = (TextView) itemView.findViewById(R.id.grade_name);
            //gradeNameTextItemView.setMovementMethod(new ScrollingMovementMethod());
            gradeDetailsBtn = (Button) itemView.findViewById(R.id.grade_details_btn);
            this.gradeListForDetailsAdapter = gradeListForDetailsAdapter;

            gradeDetailsBtn.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Create fragment and give it an argument specifying the article it should show
            DrawerClassFragment newFragment = new DrawerClassFragment();
            Bundle args = new Bundle();
            //args.putString("classroom", gradeNameTextItemView.getText().toString());
            Intent intent = new Intent(v.getContext(), Classroom.class);
            intent.putExtra("classroom", gradeNameTextItemView.getText().toString());
            v.getContext().startActivity(intent);

            /*newFragment.setArguments(args);

            FragmentTransaction transaction = v.getContext().getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

// Commit the transaction
            transaction.commit();*/
        }
    }
}
