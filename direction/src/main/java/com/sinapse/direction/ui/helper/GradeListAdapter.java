package com.sinapse.direction.ui.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinapse.direction.R;
import java.util.ArrayList;
import java.util.List;


public class GradeListAdapter extends RecyclerView.Adapter<GradeListAdapter.GradeListViewHolder>   {
    private List<String> gradeList;
    private LayoutInflater gradeInflater;

    public GradeListAdapter(Context context, ArrayList<String> gradeList){
        gradeInflater = LayoutInflater.from(context);
        this.gradeList = gradeList;
    }

    @NonNull
    @Override
    public GradeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View gItemView = gradeInflater.inflate(R.layout.grade_list_item_layout, parent, false);
        return new GradeListAdapter.GradeListViewHolder(gItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeListViewHolder holder, int position) {
        String sCurrent = gradeList.get(position);
        holder.gradeNameTextItemView.setText(sCurrent);
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    public class GradeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView gradeNameTextItemView;
        public final Button gradeValidationBtn;
        final GradeListAdapter gradeListAdapter;

        public GradeListViewHolder(@NonNull View itemView, GradeListAdapter gradeListAdapter) {
            super(itemView);
            gradeNameTextItemView = (TextView) itemView.findViewById(R.id.grade_name);
            gradeValidationBtn = (Button) itemView.findViewById(R.id.grade_validation_btn);
            this.gradeListAdapter = gradeListAdapter;

            gradeValidationBtn.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
          Log.d("gradeList", "Bien");
        }
    }
}
