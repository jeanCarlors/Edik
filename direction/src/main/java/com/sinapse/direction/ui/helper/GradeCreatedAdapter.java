package com.sinapse.direction.ui.helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinapse.direction.R;

import java.util.ArrayList;
import java.util.List;

public class GradeCreatedAdapter extends RecyclerView.Adapter<GradeCreatedAdapter.GradeCreatedViewHolder> {
    private List<String> gradeList;
    private LayoutInflater gradeInflater;

    public GradeCreatedAdapter(Context context, ArrayList<String> subjectList){
        gradeInflater = LayoutInflater.from(context);
        this.gradeList = subjectList;
    }


    @NonNull
    @Override
    public GradeCreatedAdapter.GradeCreatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View gItemView = gradeInflater.inflate(R.layout.grade_created_item_layout, parent, false);
        return new GradeCreatedAdapter.GradeCreatedViewHolder(gItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeCreatedViewHolder holder, int position) {
        String sCurrent = gradeList.get(position);
        holder.gradeNameTextItemView.setText(sCurrent);
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    public class GradeCreatedViewHolder extends RecyclerView.ViewHolder {
        public final TextView gradeNameTextItemView;
        final GradeCreatedAdapter gradeCreatedAdapter;

        public GradeCreatedViewHolder(@NonNull View itemView, GradeCreatedAdapter gradeCreatedAdapter) {
            super(itemView);
            gradeNameTextItemView = (TextView) itemView.findViewById(R.id.grade_name);
            this.gradeCreatedAdapter = gradeCreatedAdapter;
        }
    }
}
